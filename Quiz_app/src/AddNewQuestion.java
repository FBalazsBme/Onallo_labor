
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

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
            ArrayList<String> answers = new ArrayList<>();
            ArrayList<Boolean> ansCorList = new ArrayList<>();
            String parameterName = null;
            String answer = null;
            Integer isCorrectInt = null;
            int j = 1;
            for(int i = 1; i<(Global.getNumOfAnswers() + 1); i++){
                parameterName = "answer" + i;
                answer = request.getParameter(parameterName);
                j++;
                if(answer == null) break;
                answers.add(answer);
            }

            for(int i = 1; i<j; i++){
                parameterName = "ans" + i + "check";
                if(request.getParameter(parameterName) == null){
                    ansCorList.add(false);
                } else {
                    ansCorList.add(true);
                }
            }

            if(Global.globalQuestions.containsQuestion(Global.globalQuestions.new Question(-1,question, answers, ansCorList)))
            {
                response.sendRedirect("http://localhost:8080/webpages/login/pages/questionnotadded.html");
            }
            else
            {
                Global.globalQuestions.addQuestion(Global.globalQuestions.new Question(-1,question, answers, ansCorList));
                Global.globalQuestions.syncDB(connection);
                Global.setNumOfQuestions(Global.getNumOfQuestions()+1);
                out.println("Question added");
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
                response.sendRedirect("http://localhost:8080/webpages/login/pages/questionadded.html");
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

    private Boolean convertIntToBoolean(int i) throws SQLException{
        switch (i){
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new SQLException();
        }
    }

}
