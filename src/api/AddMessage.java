package api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import connect.ConnectionProvider;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;



/**
 * Servlet implementation class AddMessage
 */
@WebServlet("/AddMessage")
public class AddMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Get Method executed");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		MessageData obj = new Gson().fromJson(request.getReader(), MessageData.class);
		try{
			Connection con=ConnectionProvider.getConnection();
			con=ConnectionProvider.getConnection();
			
        	con.setAutoCommit(false); //for maintaining multiple transactions
        	//insert using prepared statements
			String query="INSERT INTO messages (name,message)"
			+"VALUES(?,?)";
			// create the Oracle insert preparedstatement
		      PreparedStatement preparedStmt = con.prepareStatement(query);
			  preparedStmt.setString(1, obj.getName());
		      preparedStmt.setString(2, obj.getMessage());
		      // execute the preparedstatement
		      preparedStmt.executeUpdate();
		      con.commit();// all transactions upto this point will be commited simultaneous
		      
		      con.close();
		      Map<String, Object> data = new HashMap<>();
		      data.put("name", obj.getName());
		      data.put("message", obj.getMessage());
		      data.put("databaseStatus", true);
		      String json = new Gson().toJson(data);
		      response.setContentType("application/json");
		      response.setCharacterEncoding("UTF-8");
		      response.getWriter().write(json);
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			Map<String, Object> data = new HashMap<>();
		      data.put("name", obj.getName());
		      data.put("message", obj.getMessage());
		      data.put("databaseStatus", false);
		      String json = new Gson().toJson(data);
		      response.setContentType("application/json");
		      response.setCharacterEncoding("UTF-8");
		      response.getWriter().write(json);
		}
		
	}

}
