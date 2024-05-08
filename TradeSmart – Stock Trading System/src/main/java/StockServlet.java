import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/StockServlet")
public class StockServlet extends HttpServlet {
	private static final String API_KEY = "co011d9r01qmeb8ukqjgco011d9r01qmeb8ukqk0";
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");

		PrintWriter out = response.getWriter();
		String ticker = request.getParameter("ticker");
		String apiUrl = "https://finnhub.io/api/v1/stock/profile2?symbol=" + ticker + "&token=" + API_KEY;
		URL url = new URL(apiUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String result = in.readLine();
		JsonObject jsonObj = JsonParser.parseString(result).getAsJsonObject();
   
		String apiUrl2 = "https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=" + API_KEY;
		URL url2 = new URL(apiUrl2);
		HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
		conn2.setRequestMethod("GET");
		BufferedReader in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
		String result2 = in2.readLine();
		JsonObject jsonObj2 = JsonParser.parseString(result2).getAsJsonObject();
		
		JsonObject jsonObj3 = new JsonObject();
        jsonObj3.add("profile", jsonObj); 
        jsonObj3.add("quote", jsonObj2); 

        out.print(jsonObj3.toString());
        out.flush();
        out.close();
		

	}

}

