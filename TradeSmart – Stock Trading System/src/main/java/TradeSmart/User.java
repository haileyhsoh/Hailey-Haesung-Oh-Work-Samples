package haileyoh_CSCI201_Assignment4;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {
	
	
	@SerializedName("username")
	@Expose
    private String username;
	
	
	@SerializedName("password")
	@Expose
    private String password;
	
	
	@SerializedName("email")
	@Expose
    private String email;
	
	
	@SerializedName("balance")
	@Expose
    private double balance;

    public User() {
    }
    public User(String u, String p, String e, double b) {
    	this.username = u;
    	this.password = p;
    	this.email = e;
    	this.balance = b;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
