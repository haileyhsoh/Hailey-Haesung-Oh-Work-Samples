import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import haileyoh_CSCI201_Assignment4.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Gson gson = new Gson();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String error = "Username or password is missing";
            pw.write(gson.toJson(error));
            pw.flush();
            return; 
        }
        else {
        	JDBCConnector connect = new JDBCConnector();
        	User loginUser = connect.findUser(username, password);
        	
        	if (loginUser == null) {
        		pw.write(gson.toJson("Username or password is invalid."));
        	} else {
        		String currentUser = gson.toJson(loginUser);
    	        pw.write(currentUser);
        	}
        }
        
        pw.flush();

    }
}
