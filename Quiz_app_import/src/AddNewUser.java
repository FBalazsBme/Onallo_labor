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

public class AddNewUser extends HttpServlet {

    Connection connection = null;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Global.globalUsers = new Users();
        try {
            Class.forName("com.mysql.jdbc.Driver");  // Needed for JDK9/Tomcat9
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz?useSSL=false", "Onlab", "onlab");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Global.globalUsers.loadFromDB(connection);

            String email = request.getParameter("email");
            String pass = request.getParameter("password");
            if(Global.globalUsers.containsUser(Global.globalUsers.new User(email, pass)))
            {
                response.sendRedirect("http://localhost:8080/webpages/login/pages/usernotadded.html");
            }
            else
            {
                Global.globalUsers.addUser(Global.globalUsers.new User(email, pass));
                Global.globalUsers.syncDB(connection);
                response.sendRedirect("http://localhost:8080/webpages/login/pages/useradded.html");

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
