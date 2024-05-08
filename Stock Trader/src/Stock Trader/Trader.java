
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;

public class Trader {
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {

		Scanner in = new Scanner(System.in);
		System.out.println("What is the name of the file containing the company information?");
		String filename = in.nextLine();
		File inputFile = new File(filename);

		try {
			if (!inputFile.exists()) {
				throw new FileNotFoundException("The company file " + filename + " could not be found.");
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("The company file " + filename + " could not be found.");
			return;
		}

		Scanner sc = new Scanner(inputFile);
		String temp = "";
		while (sc.hasNext()) {
			temp += sc.nextLine();
		}
		sc.close();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		;
		Stock2 stock = gson.fromJson(temp, Stock2.class);

// Check for file format
		for (int i = 0; i < stock.getListSize(); i++) {

			Datum2 curr = stock.getDatabyIndex(i);

			boolean name = false;
			boolean ticker = false;
			boolean description = false;
			boolean date = false;
			boolean stockbrokers = false;
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

					for (int j = 0; j < 10; j++) {

						if (j == 4 || j == 7) {
							if (s.charAt(j) != '-') {
								validDate = false;
								break;
							}
						} else {
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

			if (curr.getStockBrokers() > 0 && curr.getStockBrokers() != null) {
				stockbrokers = true;
			}

			if (!curr.getExchangeCode().equals("") && curr.getExchangeCode() != null) {

				if (curr.getExchangeCode().equals("NASDAQ") || curr.getExchangeCode().equals("NYSE")) {
					exchange = true;
				}

			}

			if (!name || !ticker || !description || !date || !exchange) {
				System.out.println("The company file " + filename + " is not formatted properly.");
				return;
			}

		}
		// end check for file format
		System.out.println("The company file has been properly read.");
		System.out.println("");

		List<String> tickers = new ArrayList<String>();

		for (int i = 0; i < stock.getListSize(); i++) {
			Datum2 d = stock.getDatabyIndex(i);
			tickers.add(d.getTicker().toUpperCase());
		}
		
// Read in CSV File			

		System.out.println("What is the name of the file containing the schedule information?");
		String filename2 = in.nextLine();

		try {
			if (!inputFile.exists()) {
				throw new FileNotFoundException("The company file " + filename + " could not be found.");
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("The schedule file " + filename + " could not be found.");
			return;
		}

		// How to Read CSV File in Java (13 lines) - Java T Point, <https://www.javatpoint.com/how-to-read-csv-file-in-java>
		String line = "";
		String splitBy = ",";
		List<String[]> csvList = new ArrayList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename2));
			while ((line = br.readLine()) != null) {
				String[] csvLine = line.split(splitBy);
				csvList.add(csvLine);
//				System.out.println(csvLine[0] + ", " + csvLine[1] + ", " + csvLine[2] + ", " + csvLine[3]);
			}
		} catch (IOException e) {
			System.out.println("The schedule file is not in vaild CSV format.");
			return;
		}

		for (int i = 0; i < csvList.size(); i++) {
			if (csvList.get(i).length != 4) {
				System.out.println("Invalid CSV Format: must contain exactly 4 values per line.");
				return;
			}

			int initTime;

			try {
				initTime = Integer.parseInt(csvList.get(i)[0]);
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid CSV File: first field must be an integer."); // initiated time
				return;
			}

			if (initTime < 0) {
				System.out.println("Invalid CSV File: first field must be a positive integer.");
				return;
			}

			String csvTicker = csvList.get(i)[1].toUpperCase();

			if (!tickers.contains(csvTicker)) {
				System.out.println("Invalid CSV File: " + csvTicker + " is not a valid ticker.");
				return;
			}

			try {
				Integer.parseInt(csvList.get(i)[2]);
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid CSV File: third field must be an integer."); // number of stocks
				return;
			}

			int stockPrice;

			try {
				stockPrice = Integer.parseInt(csvList.get(i)[3]);
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid CSV File: fourth field must be an integer.");
				return;
			}

			if (stockPrice < 0) {
				System.out.println("Invalid CSV File: fourth field must be a positive integer.");
				return;
			}

		}

		System.out.println("The schedule file has been properly read.");
		System.out.println("");
		System.out.println("What is the initial balance?");
		System.out.println("");

		int balance;

		try {
			balance = Integer.parseInt(in.nextLine());
		} catch (NumberFormatException nfe) {
			System.out.println("Error: Initial balance must be an integer.");
			return;
		}

		System.out.println("Starting execution of program...");
		System.out.println("");
		System.out.println("Initial Balance: " + balance);

		List<String> csvTickers = new ArrayList<String>();

		for (int i = 0; i < csvList.size(); i++) {
			String s = csvList.get(i)[1].toUpperCase();
			if (!csvTickers.contains(s)) {
				csvTickers.add(s);
			}
		}

		List<CSVStock> stocks = new ArrayList<CSVStock>();
		CSVStock.setBalance(balance);
		for (int i = 0; i < csvTickers.size(); i++) {
			int sb = 0;
			String name = "";
			for (int j = 0; j < stock.getListSize(); j++) {
				if (stock.getDatabyIndex(j).getTicker().toUpperCase().equals(csvTickers.get(i))) {
					sb = stock.getDatabyIndex(j).getStockBrokers();
					name = stock.getDatabyIndex(j).getTicker().toUpperCase();
				}
			}
			stocks.add(new CSVStock(sb, name));

		}
		long startTime = System.currentTimeMillis();
		CSVStock.setStartTime(startTime);

		Timer t = new java.util.Timer();

		Semaphore semaphore = new Semaphore(0);

		for (int i = 0; i < csvList.size(); i++) {
			String[] currentStock = csvList.get(i);
			String nextStockTicker = i < csvList.size() - 1 ? csvList.get(i + 1)[1] : "";
			int quantity = Integer.parseInt(currentStock[2]);
			int price = Integer.parseInt(currentStock[3]);
			int time = Integer.parseInt(currentStock[0]) * 1000;
			t.schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					String currentStockTicker = currentStock[1];
					for (int k = 0; k < stocks.size(); k++) {
						if (stocks.get(k).getTicker().equals(currentStockTicker)) {
							CSVStock currentPointingStock = stocks.get(k);
							currentPointingStock.setNumStocks(quantity);
							currentPointingStock.setStockPrice(price);

							Thread newThread = new Thread(() -> {
								currentPointingStock.run();
								semaphore.release();
							});
							newThread.start();
							if (nextStockTicker.equals(currentStockTicker)) {
								try {
									Thread.sleep(50);
									if (!currentPointingStock.getAvailable()) {
										newThread.join();
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

						}

					}
				}
			}, time);
		}

		try {
			semaphore.acquire(csvList.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("All trades completed!");

	}
}
