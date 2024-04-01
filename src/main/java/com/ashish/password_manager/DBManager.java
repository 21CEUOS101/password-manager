package com.ashish.password_manager;
import java.sql.*;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/password_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "ashish2901";
    private Connection connection;
    private static final String AES_KEY = "0123456789abcdef";

    public boolean connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // check for master password with decryption
    public boolean checkMasterPassword(String masterPassword) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM master_password");
            if (resultSet.next()) {
                String encryptedMasterPassword = resultSet.getString("password");
                String decryptedMasterPassword = decryptPassword(encryptedMasterPassword);
                return masterPassword.equals(decryptedMasterPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // write function to add password to master_password table with encryption
    public void addMasterPassword(String masterPassword) {
        String encryptedMasterPassword = encryptPassword(masterPassword);

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO master_password (password) VALUES (?)")) {
            preparedStatement.setString(1, encryptedMasterPassword);
            preparedStatement.executeUpdate();
            System.out.println("Master password added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addPassword(String website, String username, String password) {
        String encryptedPassword = encryptPassword(password);

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO passwords (website, username, password) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, website);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, encryptedPassword);
            preparedStatement.executeUpdate();
            System.out.println("Password added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String encryptPassword(String password) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void viewAllPasswords() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM passwords");
            while (resultSet.next()) {
                String decryptedPassword = decryptPassword(resultSet.getString("password"));

                System.out.println("--------------------");
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Website: " + resultSet.getString("website"));
                System.out.println("Username: " + resultSet.getString("username"));
                System.out.println("Password: " + decryptedPassword);
                System.out.println();
            }
            System.out.println("All passwords displayed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String decryptPassword(String encryptedPassword) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removePassword(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM passwords WHERE username = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            System.out.println("Password removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(String username, String newPassword) {

        String encryptedPassword = newPassword; // For simplicity, storing password as-is (not recommended for production)

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE passwords SET password = ? WHERE username = ?")) {
            preparedStatement.setString(1, encryptedPassword);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            System.out.println("Password updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean viewPassword(String query) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM passwords WHERE website = ?")) {
            preparedStatement.setString(1, query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String decryptedPassword = decryptPassword(resultSet.getString("password"));

                System.out.println("--------------------");
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Website: " + resultSet.getString("website"));
                System.out.println("Username: " + resultSet.getString("username"));
                System.out.println("Password: " + decryptedPassword);
                System.out.println();
            }
            System.out.println("Password displayed successfully!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
