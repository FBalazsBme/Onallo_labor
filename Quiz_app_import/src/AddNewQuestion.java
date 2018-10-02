import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
public class AddNewQuestion extends HttpServlet {

    Connection connection = null;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Global.globalQuestions = new Questions();
        try {
            Class.forName("com.mysql.jdbc.Driver");  // Needed for JDK9/Tomcat9
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz?useSSL=false", "Onlab", "onlab");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Global.globalQuestions.loadFromDB(connection);


            String question = request.getParameter("question");
            String answer1 = request.getParameter("answer1");
            String answer2 = request.getParameter("answer2");
            String answer3 = request.getParameter("answer3");
            String answer4 = request.getParameter("answer4");
            int cLoc = Integer.parseInt(request.getParameter("correctLoc"));//throws NumberFormatException

            if(Global.globalQuestions.containsQuestion(Global.globalQuestions.new Question(question, answer1,answer2,answer3,
                    answer4,cLoc)))
            {
                out.println("Question not added, it already exists!");
            }
            else
            {
                Global.globalQuestions.addQuestion(Global.globalQuestions.new Question(question, answer1,answer2,answer3,
                        answer4,cLoc));
                Global.globalQuestions.syncDB(connection);
                out.println("Question added");
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
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
