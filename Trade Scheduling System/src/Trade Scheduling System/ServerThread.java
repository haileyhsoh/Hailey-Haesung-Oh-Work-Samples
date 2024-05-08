
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
	
	private List<String> incompleteTrades;
	private long startTime;
	private Double profit = 0.0;
	private Boolean busy = false;
	private Boolean affordable = true;
	private Double balance = 0.0;
	private Double initBalance = 0.0;
	private PrintWriter pw;
	private BufferedReader br;
	private Assignment3 a3;
	private int numClientsLeft;
	public void setNumClientsLeft(int n) {
		numClientsLeft = n;
	}
	public int getNumClientsLeft() {
		return numClientsLeft;
	}
	public void print(String m) {
		pw.println(m);
		pw.flush();
	}
	
	public Boolean getBusy() {
		return busy;
	}
	public Boolean getAffordable() {
		return affordable;
	}
	public void setBusy(Boolean b) {
		busy = b;
	}
	
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double b) {
		balance = b;
	}
	public Double getInitBalance() {
		return initBalance;
	}

	public void setInitBalance(Double b) {
		initBalance = b;
	}
	
	public void setStartTime(long s) {
		startTime = s;
	}
	public void setProfit(Double p) {
		profit = p;
	}
	public Double getProfit() {
		return profit;
	}
	
	public void addIncompleteTrade(String s) {
		incompleteTrades.add(s);
	}
	public List<String> getIncompleteTrades() {
		return incompleteTrades;
	}

	
	public ServerThread(Socket s, Assignment3 a3) {
		try {
			this.a3 = a3;
			this.incompleteTrades = new ArrayList<>();
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));

			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}
	
	public void sendMessage(String message) {
		pw.println(message);
		pw.flush();
	}
	
	public void run() {
		try {
			while(true) {
				String line = br.readLine();
				
				if (line != null) {
					if (line.equals("started")) {
						busy = true;
					}
					
					else if (line.equals("finished")) {
						busy = false;
					}
				}
				
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		}
	}
	

}
