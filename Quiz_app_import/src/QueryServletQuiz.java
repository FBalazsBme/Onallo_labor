// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class QueryServletQuiz extends HttpServlet {  // JDK 1.6 and above only
 
   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
	  
	  
	  
	  File fileOut = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/questions/index.html");
	  fileOut.getParentFile().mkdirs();

	  File fileBase = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/questions/index_base.html");
	  Path from = fileBase.toPath(); //convert from File to Path
      Path to = fileOut.toPath(); //convert from String to Path
      Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

	  PrintWriter out = new PrintWriter(new FileOutputStream(fileOut, true /* append = true */));

	  //PrintWriter out = new PrintWriter(fileOut);
      // Get a output writer to write the response message into the network socket
      //PrintWriter out = response.getWriter();
 
      Connection conn = null;
      Statement stmt = null;
      try {
         // Step 1: Allocate a database Connection object
         Class.forName("com.mysql.jdbc.Driver");  // Needed for JDK9/Tomcat9
         conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/quiz?useSSL=false", "Onlab", "onlab"); // <== Check!
            // database-URL(hostname, port, default database), username, password
 
         // Step 2: Allocate a Statement object within the Connection
         stmt = conn.createStatement();
 
         // Step 3: Execute a SQL SELECT query
         String sqlStr = "select * from quiz.questions;";
 
         // Print an HTML page as the output of the query
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
 
         // Step 4: Process the query result set
         while(rset.next()) {
            // Print a paragraph <p>...</p> for each record			
            out.append("<div class=\"post-preview\">\n" + "<a href=\"../../checkans\">\n"
				 + "<h2 class=\"post-title\">"
				 + rset.getString("question")
				 + "</h2>"
                 + "</a>"
				 + "<h3 class=\"post-subtitle\">"
				 + rset.getString("answer1")
				 + "</h3>"
				 + "<h3 class=\"post-subtitle\">"
				 + rset.getString("answer2")
				 + "</h3>"
				 + "<h3 class=\"post-subtitle\">"
				 + rset.getString("answer3")
				 + "</h3>"
				 + "<h3 class=\"post-subtitle\">"
				 + rset.getString("answer4")
				 + "</h3>"
				 + "</a></div><hr>");
         }
         out.append("</div></div></div><hr></body></html>");
     } catch (SQLException ex) {
		out.println("1111111");
        ex.printStackTrace();
     } catch (ClassNotFoundException ex) {
		out.println("222222");
        ex.printStackTrace();
     } finally {
        out.close();  // Close the output writer
        try {
           // Step 5: Close the resources
           if (stmt != null) stmt.close();
           if (conn != null) conn.close();
        } catch (SQLException ex) {
			out.println("333333");
           ex.printStackTrace();
        }
		response.sendRedirect("http://localhost:8080/webpages/questions/index.html");
     }
   }
}