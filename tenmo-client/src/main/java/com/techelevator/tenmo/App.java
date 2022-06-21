package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.ListUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                getTransferById();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        //Call getbalance from the accountservice, print result
        AccountService accountService = new AccountService();
        System.out.println(accountService.getBalance(currentUser));
		
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		System.out.println("View Transfer History:");
        TransferService transferService = new TransferService();
        //Declare arraylist?
        Transfer[] transferList = transferService.getUserTransfers(currentUser);

        //System.out.println(transferList.size() + " transfers");

        for (Transfer transfer:transferList){
            //Exception in thread "main" java.lang.ClassCastException

            System.out.println("Transfer ID: " + transfer.getTransferId() + " | Transfer From: " + transfer.getFromUserName() + " | Transfer to Account: " + transfer.getToUserName() + " | Amount to Transfer: $" + transfer.getAmount());
        }

	}

	private void getTransferById(){
        //Pull a transfer based on its specific transfer ID
        Scanner inputObject = new Scanner(System.in);
        TransferService transferService = new TransferService();
        System.out.println("Please enter the ID of the Transfer you wish to view:");


       try {
           long transferId=inputObject.nextLong();
           Transfer transfer = transferService.getTransferByTransferId(currentUser, transferId);
           System.out.println("Transfer ID: " + transfer.getTransferId());
           System.out.println("Transfer From: " + transfer.getFromUserName() + " | Transfer to Account#: " + transfer.getToUserName() + " | Amount to Transfer: $" + transfer.getAmount());

       } catch (Exception e){
           System.out.println("No, it's broken. " + e);
       }
    }

    private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        TransferService transferService = new TransferService();
        ListUserService listUserService = new ListUserService();
        Scanner inputObject = new Scanner(System.in);

        ListUser[] listOfUsers = listUserService.getListUsers(currentUser);

        for (ListUser user : listOfUsers){
            System.out.println("Username: " + user.getUserName() + "    | Account ID: " + user.getAccountId());
        }



        System.out.println("Please enter an account to send to:");
        long toAccountId = inputObject.nextLong();
        System.out.println("Please enter an amount of money to send");
        BigDecimal amount = inputObject.nextBigDecimal();

        transferService.makeTransfer(currentUser,toAccountId,amount);


		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
