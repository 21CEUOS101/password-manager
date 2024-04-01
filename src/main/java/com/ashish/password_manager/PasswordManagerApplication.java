package com.ashish.password_manager;

import java.util.Scanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordManagerApplication {

	public static void main(String[] args) {
		PasswordManagerImpl passwordManager = new PasswordManagerImpl();

		try {
			if (!passwordManager.dbManager.connect()) {
				System.out.println("Could not connect to the database. Exiting...");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Enter the master password: ");

		Scanner scanner = new Scanner(System.in);

		String masterPassword = scanner.next();

		// passwordManager.addMasterPassword(masterPassword);

		if (!passwordManager.checkMasterPassword(masterPassword)) {
			System.out.println("Incorrect master password. Exiting...");
			System.exit(1);
		}

		System.out.println("Welcome to the Password Manager! \n");
		System.out.println("Developed by : Ashish Prajapati \n");

		while (true) {
			System.out.println("Please select an option: \n");
			System.out.println("1. Add a new password");
			System.out.println("2. Remove a password");
			System.out.println("3. Update a password");
			System.out.println("4. View all passwords");
			System.out.println("5. View a password");
			System.out.println("6. Exit");

			int choice = scanner.nextInt();
			String username = new String();
			switch (choice) {
				case 1:
					System.out.println("Enter the website: ");
					String website = scanner.next();
					System.out.println("Enter the username: ");
					username = scanner.next();
					System.out.println("Enter the password: ");
					String password = scanner.next();
					Password passwordObj = new Password(website, username, password);
					passwordManager.addPassword(passwordObj);
					break;
				case 2:
					System.out.println("Enter the username of the password you want to remove: ");
					username = scanner.next();
					passwordManager.removePassword(username);
					break;
				case 3:
					System.out.println("Enter the username of the password you want to update: ");
					username = scanner.next();
					System.out.println("Enter the new password: ");
					String newPassword = scanner.next();
					passwordManager.updatePassword(username, newPassword);
					break;
				case 4:
					passwordManager.viewAllPasswords();
					break;
				case 5:
					System.out.println("Enter the website or username of the password you want to view: ");
					String query = scanner.next();
					passwordManager.viewPassword(query);
					break;
				case 6:
					passwordManager.close();
					System.exit(0);
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					break;
			}

			// ask user if they want to continue
			System.out.println("Do you want to continue? (yes/no)");
			String continueChoice = scanner.next();
			if (continueChoice.equals("no")) {
				passwordManager.close();
				System.exit(0);
			}
		}
	}

}
