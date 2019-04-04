

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

public class AnswerClicked extends HttpServlet {

    Connection conn = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("Answer Check opened");

            /*if(Global.globalUsers.containsUser(Global.globalUsers.new User(email, pass)))
            {
                response.sendRedirect("http://localhost:8080/query1");
            }
            else
            {
                out.println("Username or Password incorrect");
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
            }*/

    }

}
