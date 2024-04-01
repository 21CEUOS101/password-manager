package com.ashish.password_manager;
import java.util.Scanner;

interface PasswordManager {

    void addMasterPassword(String masterPassword);

    boolean checkMasterPassword(String masterPassword);

    void addPassword(Password password);

    void removePassword(String username);

    void updatePassword(String username, String newPassword);

    void viewAllPasswords();

    boolean viewPassword(String query);

    void close();
}

public class PasswordManagerImpl implements PasswordManager {
    DBManager dbManager = new DBManager();

    public void addMasterPassword(String masterPassword) {
        dbManager.addMasterPassword(masterPassword);
    }

    public boolean checkMasterPassword(String masterPassword) {
        return dbManager.checkMasterPassword(masterPassword);
    }

    public void addPassword(Password password) {
        dbManager.addPassword(password.getWebsite(), password.getUsername(), password.getPassword());
    }

    public void removePassword(String username) {
        dbManager.removePassword(username);
    }

    public void updatePassword(String username, String newPassword) {
        dbManager.updatePassword(username, newPassword);
    }

    public void viewAllPasswords() {
        dbManager.viewAllPasswords();
    }

    public boolean viewPassword(String query) {
        return dbManager.viewPassword(query);
    }

    @Override
    public void close() {
        dbManager.close();
    }
}
