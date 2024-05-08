
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

public class CSVStock extends Thread {
	private static int balance;
	private static long startTime;
	private Semaphore semaphore;
	private int stockPrice;
	private int numStocks;
	private String ticker;
	private boolean available = true;

	public CSVStock(int num, String name) {
		this.ticker = name;
		this.semaphore = new Semaphore(num);
	}

	public static synchronized int getBalance() {
		return balance;
	}

	public static void setBalance(int b) {
		CSVStock.balance = b;
	}

	public static void setStartTime(long s) {
		startTime = s;
	}

	public void setStockPrice(int sp) {
		this.stockPrice = sp;
	}

	public void setNumStocks(int ns) {
		this.numStocks = ns;
	}

	public String getTicker() {
		return ticker;
	}

	public boolean getAvailable() {
		return available;
	}

	public void run() {
		try {
			semaphore.acquire();
			if (semaphore.availablePermits() == 0) {
				available = false;
			} else {
				available = true;
			}

			DateFormat simple = new SimpleDateFormat("H:mm:ss:SSS");
			simple.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date result = new Date(System.currentTimeMillis() - startTime);

			if (numStocks > 0) { // BUY, takes 2 seconds
				System.out.println("[" + simple.format(result) + "] " + "Starting purchase of " + numStocks
						+ " stocks of " + ticker);
				Thread.sleep(2000);
				Thread.yield();

				synchronized (CSVStock.class) {
					if (balance - (stockPrice * numStocks) >= 0) {
						Date d = new Date(System.currentTimeMillis() - startTime);
						System.out.println("[" + simple.format(d) + "] " + "Finished purchase of " + numStocks
								+ " stocks of " + ticker + "\nCurrent Balance after trade: "
								+ (balance -= stockPrice * numStocks));

					} else {
						System.out.println("Transaction failed due to insufficient balance. Unsuccessful purchase of "
								+ numStocks + " stocks of " + ticker);
					}
				}
			} else { // SELL, takes 3 seconds
				System.out.println("[" + simple.format(result) + "] " + "Starting sale of " + numStocks * (-1)
						+ " stocks of " + ticker);
				Thread.sleep(3000);
				Thread.yield();
				synchronized (CSVStock.class) {
					Date d = new Date(System.currentTimeMillis() - startTime);
					System.out.println(
							"[" + simple.format(d) + "] " + "Finished sale of " + numStocks * (-1) + " stocks of "
									+ ticker + "\nCurrent Balance after trade: " + (balance -= stockPrice * numStocks));
				}

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}
}
