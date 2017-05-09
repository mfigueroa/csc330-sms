package csc330sms;

import java.util.ArrayList;
import java.math.*;
import java.text.NumberFormat;
import java.text.*;

import csc330sms.CommandFramework.CommandArgument;
import csc330sms.broker.StockBrokerAccount;
import csc330sms.exchange.Order;
import csc330sms.security.Security;

public class AccountCommand extends CommandFramework {

	public AccountCommand(StockBrokerAccount sba) {
		super(sba);
		this.addPositionalArgument("request", "Stock, options, cash, or summary.", ValidationType.STRING);
	}
	
	private void printAccountBalance() {
		String balance = NumberFormat.getCurrencyInstance().format(sba.getAccountBalance());
		System.out.printf("You have a total cash balance of %s\n", balance);
	}
	
	private void printEquityBalance() {
		String balance = NumberFormat.getCurrencyInstance().format(sba.getEquityValue());
		System.out.printf("You have a total equity balance of %s\n", balance);
	}
	
	private void printOrderHistory() {
		ArrayList<Order> history = sba.getOrderHistory();
		if (history == null) {
			System.out.println("You have no order history.");
		}
		else {
			System.out.printf("%-20s %-10s %-10s %-10s\n", "DATE", "SYMBOL", "PRICE", "QUANTITY");
			for (int i = 0; i < history.size(); i++) {
				Security s = history.get(i).getSecurity();
				System.out.printf("%-20s %-10s %-10s %-10s\n", 
						new SimpleDateFormat("MM/dd HH:mm").format(history.get(i).getTimestamp()),
						s.getSymbol(),
						NumberFormat.getCurrencyInstance().format(s.getPrice()),
						s.getQuantity()
				);
			}
		}
	}
	
	public boolean run(String arguments) throws InvalidArgument {
		// Return control of the program to ApplicationLoader
		if (!super.run(arguments)) return false;
		
		ArrayList<CommandArgument> args = this.getPositionalArguments();
		String request = args.get(0).getArgument().toString();
		
		if (request.contains("cash")) {
			printAccountBalance();
		}
		
		if (request.contains("summary")) {
			printAccountBalance();
			printEquityBalance();
			
		}
		
		if (request.equalsIgnoreCase("history")) {
			printOrderHistory();
		}
		return true;
	}
}
