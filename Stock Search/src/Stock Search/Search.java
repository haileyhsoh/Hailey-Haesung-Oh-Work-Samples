
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.text.WordUtils;

public class Search {
	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in = new Scanner(System.in);
		System.out.println("What is the name of the company file?");
		String filename = in.nextLine();
		File inputFile = new File(filename);
		
// Check for file name
		
		try {
			if (!inputFile.exists()) {
				throw new FileNotFoundException("The file " + filename + " could not be found.");
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("The file " + filename + " could not be found.");
			return;
		}
				
		Scanner sc = new Scanner(inputFile);
		String temp = "";
		while (sc.hasNext()) {
			temp += sc.nextLine();
		}
		sc.close();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();;
		Stock stock = gson.fromJson(temp, Stock.class);
		
// Check for file format
		
		for (int i=0; i < stock.getListSize(); i++) {
			
			Datum curr = stock.getDatabyIndex(i);
			
			boolean name = false;
			boolean ticker = false;
			boolean description = false;
			boolean date = false;
			boolean exchange = false;
			
			if (curr.getName() != "" && curr.getName() != null) {
				name = true;
			}
			if (curr.getTicker() != "" && curr.getTicker() != null) {
				ticker = true;
			}
			if (curr.getDescription() != "" && curr.getDescription() != null) {
				description = true;
			}
			if (curr.getStartDate() != "" && curr.getStartDate() != null) {
				
				String s = curr.getStartDate();
							
				if (s.length() == 10) {	
					boolean validDate = true;
					
					for (int j=0; j < 10; j++) {
						
						if (j == 4 || j == 7) {
							if (s.charAt(j) != '-') {
								validDate = false;
								break;
							}
						}
						else {
							if (!Character.isDigit(s.charAt(j))) {
								validDate = false;
								break;
							}
						}
					}
					
					if (validDate) {
						date = true;
					}
					
				}
				
			}
			
			if (!curr.getExchangeCode().equals("") && curr.getExchangeCode() != null) {
				
				if (curr.getExchangeCode().equals("NASDAQ") || curr.getExchangeCode().equals("NYSE")) {
					exchange = true;
				}

			}
			
			if (!name || !ticker || !description || !date || !exchange) {
				System.out.println("The file " + filename + " is not formatted properly.");
				return;
			}
			
		}

// end check for file format	
						
		System.out.println("The file has been properly read.");
		System.out.println("");
		
		while (true) {
			
			System.out.println("		1) Display all public companies");
			System.out.println("		2) Search for a stock (by ticker)");
			System.out.println("		3) Search for all stocks on an exchange");
			System.out.println("		4) Add a new company/stocks");
			System.out.println("		5) Remove a company");
			System.out.println("		6) Sort companies");
			System.out.println("		7) Exit");
			
			boolean valid = false;
			int command = 1;
			
			while (valid == false) {
			
				System.out.println("What would you like to do?");
				
				String input = in.nextLine();
				
				if (input.length() != 1) {
					System.out.println("That is not a valid option.");
				}
				else {
					command = Character.getNumericValue(input.charAt(0));
					
					if (command > 7 || command <= 0) {
						System.out.println("That is not a valid option.");
					}
					else {
						valid = true;
					}
				}
								
			}
			
			if (command == 1) { // 1) Display all public companies
				
				for (int i=0; i < stock.getListSize(); i++) {
					
					Datum curr = stock.getDatabyIndex(i);
					
					System.out.println(curr.getName() + ", symbol " + curr.getTicker() + ", started on " + curr.getStartDate() + ", listed on " + curr.getExchangeCode() + ", ");
					System.out.println("	" + WordUtils.wrap(curr.getDescription(), 100));
					System.out.println("");
					
				}
				
				
			}
			else if (command == 2) { // 2) Search for a stock (by ticker)
				boolean exists = false;
				
				while (exists == false) {
					System.out.println("What is the ticker of the company you would like to search for?");
					
					String t = in.nextLine().toUpperCase();
					
					int index = 0;
					
					for (int i=0; i < stock.getListSize(); i++) {
						if (t.equals(stock.getDatabyIndex(i).getTicker())) {
							exists = true;
							index = i;
							break;
							
						}
					}
					
					if (!exists) {
						System.out.println(t + " could not be found.");
					}
					else {
						Datum tmp = stock.getDatabyIndex(index);
						System.out.println(tmp.getName() + ", symbol " + tmp.getTicker() + ", started on " + tmp.getStartDate() + ", listed on " + tmp.getExchangeCode());
						System.out.println("");
					}
				}
				
			}
			else if (command == 3) { // 3) Search for all stocks on an exchange
				
				boolean exists = false;
				
				while(exists == false) {
				
					System.out.println("What Stock Exchange would you like to search for?");
					
					String se = in.nextLine().toUpperCase();
					ArrayList<String> array = new ArrayList<String>();
					
					for (int i=0; i < stock.getListSize(); i++) {
						if (se.equals(stock.getDatabyIndex(i).getExchangeCode())) {
							array.add(stock.getDatabyIndex(i).getTicker());
			
						}
					}
					
					if (array.size() > 0) {
						exists = true;
						for (int i=0; i < array.size(); i++) {
							if (i <= array.size() - 3) {
								System.out.print(array.get(i) + ", ");
							}
							else if (i == array.size() - 2) {
								System.out.print(array.get(i) + " ");
							}
							else {
								System.out.print("and " + array.get(i));
							}
						}
						System.out.println(" found on the " + se + " exchange.");
						System.out.println("");
					}
					else {
						System.out.println("No exchange named " + se + " found.");
					}
				
				}
				
			}
			else if (command == 4) { // 4) Add a new company/stocks
				
				boolean check = false;
				
				while (check == false) {
				
					System.out.println("What is the name of the company you would like to add?");
					
					String c = in.nextLine();
					boolean exists = false;
					
					for (int i=0; i < stock.getListSize(); i++) {
						if (c.toUpperCase().equals(stock.getDatabyIndex(i).getName().toUpperCase())) {
							exists = true;
							break;
						}
					}
					if (exists) {
						System.out.println("There is already an entry for " + c);				
					}
					else {
						
						check = true;
						
						Datum entry = new Datum();
						entry.setName(c);
						
						System.out.println("What is the stock symbol of " + c + "?");
						
						String sym = in.nextLine().toUpperCase();
						entry.setTicker(sym);
						
						System.out.println("What is the start date of " + c + " ?");
						
						String date = in.nextLine();
						entry.setStartDate(date);
						
						System.out.println("What is the exchange where " + c + " is listed?");
						
						String ex = in.nextLine().toUpperCase();
						entry.setExchangeCode(ex);
						
						System.out.println("What is the description of " + c + "?");
						
						String des = in.nextLine();
						entry.setDescription(des);
						
						System.out.println("There is now a new entry for:");
						
						System.out.println(entry.getName() + ", symbol " + entry.getTicker() + ", started on " + entry.getStartDate() + ", listed on " + entry.getExchangeCode() + ", ");
						System.out.println("	" + WordUtils.wrap(entry.getDescription(), 100));
						System.out.println("");
						
						stock.appendData(entry);
					}
				}
			}
			else if (command == 5) { // 5) Remove a company
				
				for (int i=1; i < stock.getListSize()+1; i++) {
					System.out.println("	" + i + ") " + stock.getDatabyIndex(i-1).getName());
				}
				
				System.out.println("Which company would you like to remove?");
				
				
				String n = in.nextLine();
				
				int num = Character.getNumericValue(n.charAt(0));
				
				if (num < 1 || num > stock.getListSize()+1) {
					return;
				}
				
				
				System.out.println(stock.getDatabyIndex(num-1).getName() + " is now removed.");
				System.out.println("");
				
				stock.removeData(stock.getDatabyIndex(num-1));
				
			}
			else if (command == 6) { // 6) Sort companies
				
				System.out.println("	1) A to Z");
				System.out.println("	2) Z to A");
				System.out.println("");
				System.out.println("How would you like to sort by?");
				
				String n = in.nextLine();
				int option = Character.getNumericValue(n.charAt(0));
												
				if (option == 1) {
					// ChatGPT's code to sort companies A to Z based on names. (1 line) - ChatGPT, <chat.openai.com/chat>
					Collections.sort(stock.getData(), Comparator.comparing(a -> a.getName()));										
				}
				else if (option == 2) {					
					// ChatGPT's code to sort companies Z to A based on names. (1 line) - ChatGPT, <chat.openai.com/chat>
					Collections.sort(stock.getData(), Comparator.comparing(Datum::getName).reversed());						
				}
				else {
					System.out.println("That is not a valid option. 6");
					return;
				}
				
				System.out.println("Your companies are now sorted from in alphabetical order (A-Z).");
				System.out.println("");
								
			}
			else if (command == 7) { // 7) Exit
				System.out.println("	1) Yes");
				System.out.println("	2) No");
				System.out.println("Would you like to save your edits?");
				
				String n = in.nextLine();
				int option = Character.getNumericValue(n.charAt(0));
				
				if (option == 1) {
					// ChatGPT's code to save edits. (6 lines) - ChatGPT, <chat.openai.com/chat>
					try (FileWriter fileWriter = new FileWriter(filename)) {
						gson.toJson(stock, fileWriter);
						System.out.println("Your edits have been saved to " + filename);
					} catch (IOException e) {
			            e.printStackTrace();
			        }
				}
					
				System.out.println("Thank you for using my program!");
				
				in.close();
		
				System.exit(0);
			}
		
		} // end while
		
	}
	
}
