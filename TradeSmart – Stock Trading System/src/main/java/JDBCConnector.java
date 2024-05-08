import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import haileyoh_CSCI201_Assignment4.User;

public class JDBCConnector {
	
    private Connection c;

	
	public JDBCConnector() {
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/ass4?user=root&password=mystar03");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	public User findUser(String user, String pass) {
		
		ResultSet rs = null;
		PreparedStatement pst = null;
		
		
		try {
			String sql = "SELECT id, username, pass, email, balance FROM users WHERE username = ? AND password = ?";
			pst = c.prepareStatement(sql);
			pst.setString(1, user);
	        pst.setString(2, pass);
	        
	        rs = pst.executeQuery();
	        
	        if (rs.next()) {
	        	String u = rs.getString("username");
	        	String p = rs.getString("pass");
	        	String e = rs.getString("email");
	        	Double b = rs.getDouble("balance");
	        	
	        	User currentUser = new User(u,p,e,b);
	        	return currentUser;
	        }
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (c != null) c.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
		}
		
        
		return null;
		
	}
	

	public static int registerUser(String username, String password, String email) {
	
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	
	int userID = -1;
	
	try {
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ass4?user=root&password=mystar03");
		
		st = conn.createStatement();
		rs = st.executeQuery("SELECT * FROM users WHERE username='" + username + "'");
		
		if (!rs.next()) {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM users WHERE email='" + email + "'");
			if (!rs.next()) {
				// no user with that email either
				rs.close();
				st.execute("INSERT INTO users (username, pass, email, balance) VALUES ('" 
				+ username + "', '" + password + "', '" + email + "', 50000)");
				
				rs = st.executeQuery("SELECT LAST_INSERT_ID()");
				rs.next();
				userID = rs.getInt(1);
			} else {
				// this email is taken.
				userID = -2;
			}
		}
	} catch (SQLException sqle) {
		System.out.println("SQLException in registerUser. ");
		sqle.printStackTrace();
	} finally {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		}
	}
	return userID;

	}
	

	 
	 
}
