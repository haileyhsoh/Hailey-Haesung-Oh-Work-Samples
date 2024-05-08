
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Scheduler {

	private Vector<ServerThread> serverThreads;
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public Assignment3(int port) {

		Scanner in = new Scanner(System.in);
		Boolean validFilename = false;
		String filename = "";

		while (!validFilename) {
			System.out.println("What is the name of the schedule file?");
			filename = in.nextLine();
			File inputFile = new File(filename);
			try {
				if (!inputFile.exists()) {
					throw new FileNotFoundException("The company file " + filename + " could not be found.");
				}
			} catch (FileNotFoundException fnfe) {
				System.out.println("The schedule file " + filename + " could not be found.");
				continue;
			}
			validFilename = true;
		}

		// How to Read CSV File in Java (13 lines) - Java T Point,
		// <https://www.javatpoint.com/how-to-read-csv-file-in-java>
		String line = "";
		String splitBy = ",";
		List<String[]> csvList = new ArrayList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				String[] csvLine = line.split(splitBy);
				csvList.add(csvLine);
//						System.out.println(csvLine[0] + ", " + csvLine[1] + ", " + csvLine[2]);
			}
		} catch (IOException e) {
			System.out.println("The schedule file is not in vaild CSV format.");
			return;
		}

		for (int i = 0; i < csvList.size(); i++) {
			if (csvList.get(i).length != 3) {
				System.out.println("Invalid CSV Format: must contain exactly 3 values per line.");
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

			try {
				Integer.parseInt(csvList.get(i)[2]);
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid CSV File: third field must be an integer."); // number of stocks
				return;
			}

		}

		System.out.println("\nThe schedule file has been properly read.\n");

		// Get stock prices from FinnHub
		List<String> tickerList = new ArrayList<String>();
		for (int i = 0; i < csvList.size(); i++) {
			String ticker = csvList.get(i)[1];
			Boolean present = false;
			for (int j = 0; j < tickerList.size(); j++) {
				String savedTicker = tickerList.get(j);
				if (ticker.equals(savedTicker)) {
					present = true;
					break;
				}
			}
			if (!present) {
				tickerList.add(ticker);
			}
			present = false;
		}

		HashMap<String, Double> stockPrices = new HashMap<String, Double>();

		for (int i = 0; i < tickerList.size(); i++) {
			String ticker = tickerList.get(i);
			try {
				stockPrices.put(ticker, getStockPriceJson(ticker));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(ticker + " " + stockPrices.get(ticker));
		}

// Ask for traders.csv

		validFilename = false;
		filename = "";

		while (!validFilename) {
			System.out.println("What is the name of the traders file?");
			filename = in.nextLine();
			File inputFile = new File(filename);
			try {
				if (!inputFile.exists()) {
					throw new FileNotFoundException("The traders file " + filename + " could not be found.");
				}
			} catch (FileNotFoundException fnfe) {
				System.out.println("The traders file " + filename + " could not be found.");
				continue;
			}
			validFilename = true;
		}

		line = "";
		splitBy = ",";
		List<String[]> traders = new ArrayList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				String[] tradersLine = line.split(splitBy);
				traders.add(tradersLine);
//				System.out.println(tradersLine[0] + ", " + tradersLine[1]);
			}
		} catch (IOException e) {
			System.out.println("The schedule file is not in vaild CSV format.");
			return;
		}

		for (int i = 0; i < traders.size(); i++) {
			if (traders.get(i).length != 2) {
				System.out.println("Invalid CSV Format: must contain exactly 2 values per line.");
				return;
			}

			int serialNumber;

			try {
				serialNumber = Integer.parseInt(traders.get(i)[0]);
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid CSV File: first field must be an integer."); // initiated time
				return;
			}

			if (serialNumber < 0) {
				System.out.println("Invalid CSV File: first field must be a positive integer.");
				return;
			}

			int initBalance;

			try {
				initBalance = Integer.parseInt(traders.get(i)[1]);
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid CSV File: second field must be an integer."); // number of stocks
				return;
			}

			if (initBalance < 0) {
				System.out.println("Invalid CSV File: second field must be a positive integer.");
				return;
			}

		}

		System.out.println("The traders file has been properly read.");

		int numClients = traders.size();

		try {
			System.out.println("Binding to port " + 3456);
			ServerSocket ss = new ServerSocket(3456);
			System.out.println("Waiting for traders...");
			serverThreads = new Vector<ServerThread>();

			int numTradersLeft = numClients;
			while (numTradersLeft > 0) {
				Socket s = ss.accept(); // blocking
				numTradersLeft--;
				System.out.println("Connection from: " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this); // start a new serverThread for each connection
				serverThreads.add(st);
				if (numTradersLeft > 0) {
					System.out.println("Waiting for " + numTradersLeft + " more trader(s)...");
				}
				st.print(Integer.toString(numTradersLeft));
			}

			System.out.println("Starting service.");

			for (ServerThread threads : serverThreads) {
				threads.print("Start");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DateFormat simple = new SimpleDateFormat("H:mm:ss:SSS");
		simple.setTimeZone(TimeZone.getTimeZone("UTC"));

		long startTime = System.currentTimeMillis();
		int index = 0;
		for (ServerThread threads : serverThreads) {
			// Set start time
			threads.setStartTime(startTime);
			// Set balance
			String b = traders.get(index)[1];
			threads.setBalance(Double.parseDouble(b));
			threads.setInitBalance(Double.parseDouble(b));
			index++;
		}

		Timer t = new java.util.Timer();
		Semaphore semaphore = new Semaphore(0);

		int numTrades = 0;

		for (int i = 0; i < csvList.size(); i++) {
			String[] currentStock = csvList.get(i);
			int time = Integer.parseInt(currentStock[0]) * 1000;

			t.schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					String ticker = currentStock[1];
					int quantity = Integer.parseInt(currentStock[2]);
					Double price = stockPrices.get(ticker);
					ServerThread trader = serverThreads.get(0);
					String buyOrSell = "";
					String send;
					Date result;

					// Check each trader for availability and assign trade
					Boolean tradeComplete = false;

					for (int t = serverThreads.size()-1; t >= 0; t--) {
						ServerThread curr = serverThreads.get(t);
						// 1. Is trader busy?
						if (curr.getBusy() == true) {
							continue;
						}
						// 2. Can trader afford?
						else { // trader is NOT busy

							if (quantity > 0) { // BUY
								buyOrSell = "purchase";
								Double x = curr.getBalance() - price * quantity;
								if (x > 0) {
									tradeComplete = true;
									curr.setBalance(x);
									trader = curr;
								}
							} else { // SELL, quantity < 0
								buyOrSell = "sale";
								tradeComplete = true;
								curr.setProfit(curr.getProfit() + quantity * (-1) * price);
								trader = curr;
							}
						}
					}

					if (tradeComplete == false) {
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:s");
						String incomplete = "(" + (time / 1000) + ", " + ticker + ", " + quantity + ", "
								+ simpleformat.format(cal.getTime()) + ")";
						for (ServerThread threads : serverThreads) {
							threads.addIncompleteTrade(incomplete);
						}

					} else { // tradeComplete == true
						quantity = Math.abs(quantity);
						String m = buyOrSell + " " + quantity + " " + ticker + " " + price + " " + (price * quantity)
								+ " " + startTime;
						trader.print(m);

					}
					semaphore.release();
				}

			}, time);

			numTrades++;
		}

		try {
			semaphore.acquire(numTrades);
			System.out.println("Processing Complete.");

			for (ServerThread threads : serverThreads) {
				Date d = new Date(System.currentTimeMillis() - startTime);
				List<String> array = threads.getIncompleteTrades();
				String it = "[" + simple.format(d) + "] " + "Incomplete Trades: ";

				if (array.size() < 1) {
					it += "NONE";
				} else {
					for (int k = 0; k < array.size(); k++) {
						it += array.get(k) + " ";
					}
				}

				threads.print(it);

				String totalProfits = "Total Profit from Sales: $" + df.format(threads.getProfit()) + ".";

				threads.print(totalProfits);

				String totalCost = "Total Cost from Purchases: $"
						+ df.format((threads.getInitBalance() - threads.getBalance())) + ".";
				threads.print(totalCost);

				threads.print("\nProcessing Complete.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Scheduler a3 = new Scheduler(3456);
	}

	private static Double getStockPriceJson(String ticker) throws IOException {
		URL url = new URL(String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", ticker,
				"co011d9r01qmeb8ukqjgco011d9r01qmeb8ukqk0"));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String result = in.readLine();
		JsonObject jsonObj = JsonParser.parseString(result).getAsJsonObject();
		double c = jsonObj.get("c").getAsDouble();
		return c;
	}

}
