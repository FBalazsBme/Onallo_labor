

import com.mysql.cj.protocol.Resultset;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SubmitQuiz extends HttpServlet {

    Connection conn = null;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Class.forName("com.mysql.jdbc.Driver");  // Needed for JDK9/Tomcat9
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz?useSSL=false", "Onlab", "onlab");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Global.globalUsers.loadFromDB(conn);

            String email = request.getParameter("email");
            String pass = request.getParameter("password");
            if(Global.getNumOfQuestions() == Global.numOfAnsByQ.size())
            {
                Global.actualUser = Global.globalUsers.new User(0,email, pass);
                response.sendRedirect("http://localhost:8080/query1");
            }
            else
            {
                out.println("Database out of sync!");
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
