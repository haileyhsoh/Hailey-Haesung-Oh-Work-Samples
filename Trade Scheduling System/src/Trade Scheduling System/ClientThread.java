
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class ClientThread extends Thread {

	private BufferedReader br;
	private PrintWriter pw;
	private Double balance;
	private Boolean available = true;
	private Double profit = 0.0;
	private int numClientsLeft;
	private static long startTime;

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double b) {
		balance = b;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean a) {
		available = a;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double p) {
		profit = p;
	}

	public ClientThread(String hostname, int port) {
		try {

			System.out.println("Welcome to JoesStocks v2.0!");
			System.out.println("Enter the server hostname:");
			Scanner scan = new Scanner(System.in);
			String input;
			input = scan.nextLine();
			System.out.println("Enter the server port:");
			input = scan.nextLine();

			Socket s = new Socket(hostname, port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());

			numClientsLeft = Integer.parseInt(br.readLine());
			if (numClientsLeft == 1) {
				System.out.println("1 more trader is needed before the service can begin.\nWaiting...");
			} else if (numClientsLeft > 1) {
				System.out
						.println(numClientsLeft + " more traders are needed before the service can begin.\nWaiting...");
			}

			if (br.readLine().equals("Start")) {
				System.out.println("All traders have arrived!");
				System.out.println("Starting service.");
			}

			this.start();

		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient constructor: " + ioe.getMessage());
		}
	}

	public void run() {
		try {

			while (true) {
				String line = br.readLine();
				
				DateFormat simple = new SimpleDateFormat("H:mm:ss:SSS");
				simple.setTimeZone(TimeZone.getTimeZone("UTC"));

				String[] data = line.split(" ");
				String buyOrSell = data[0];
				String gainOrCost = "";
				
				if (!(buyOrSell.equals("sale") || buyOrSell.equals("purchase"))) {
					System.out.println(line);					
					continue;
				}
				if (buyOrSell.equals("sale")) {
					gainOrCost = "gain";
				} else {
					gainOrCost = "cost";
				}
				String quantity = data[1];
				String ticker = data[2];
				String price = data[3];
				String priceXquantity = data[4];
				String start = data[5];
				
				Long startTime = Long.parseLong(start);

				Date result = new Date(System.currentTimeMillis() - startTime);
				System.out.println("[" + simple.format(result) + "] " + "Assigned " + buyOrSell + " of " + quantity
						+ " stock(s)" + " of " + ticker + ". " + "Total " + gainOrCost + " estimate = " + price + " * " + quantity + " = " + priceXquantity + ".");

				result = new Date(System.currentTimeMillis() - startTime);
				System.out.println("[" + simple.format(result) + "] " + "Starting " + buyOrSell + " of " + quantity
						+ " stock(s)" + " of " + ticker + ". " + "Total " + gainOrCost + " = " + price + " * " + quantity + " = " + priceXquantity + ".");

				pw.println("started");
				pw.flush();
				

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				synchronized(ClientThread.class) {
					result = new Date(System.currentTimeMillis() - startTime);
					System.out.println("[" + simple.format(result) + "] " + "Finished " + buyOrSell + " of " + quantity
							+ " stock(s)" + " of " + ticker + ". ");

					pw.println("finished");
					pw.flush();
				}
				
				

			}

		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient.run(): " + ioe.getMessage());
		}
	}

	public static void main(String[] args) {
		ClientThread ct = new ClientThread("localhost", 3456);
	}

}
