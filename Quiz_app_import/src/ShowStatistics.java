

import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowStatistics extends HttpServlet {


    Connection conn = null;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the MIME type for the response message
        response.setContentType("text/html");


        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> isAnsCorList = new ArrayList<>();

        Statement stmt = null;
        Statement stmt2 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");  // Needed for JDK9/Tomcat9
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz?useSSL=false", "Onlab", "onlab"); // <== Check!
            Global.setNumOfUSersFromDB(conn);
            Global.syncUserColumns(conn);
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();

            Global.setNumOfQuestionsFromDB(conn);
            int qNum = 1;
            int correctAns = 0;
            boolean isActualAnsTrue = false;
            Global.globalQuestions.loadFromDB(conn);
            int size = Global.globalQuestions.getQuestionsFromDB()
                    .get((3)).getAnswers().size();
            int size3 = Global.globalQuestions.getQuestionsFromDB()
                    .get((4)).getAnswers().size();

            for(Integer getAns:Global.numOfAnsByQ){
                ArrayList<Boolean> answersTrue = new ArrayList<>();
                int size1 = Global.globalQuestions.getQuestionsFromDB()
                        .get((3)).getAnswers().size();
                for(int i = 1; i < getAns; i++){
                    int count = (qNum*10) + i;
                    String parameterName = "radAns" + count;
                    if(request.getParameter(parameterName)== null){
                        answersTrue.add(false);
                    } else {
                        answersTrue.add(true);
                    }
                }
                int size2 = Global.globalQuestions.getQuestionsFromDB()
                        .get((3)).getAnswers().size();
                Questions.Question q = Global.globalQuestions.getQuestionsFromDB()
                        .get((qNum-1));
                if(Global.globalQuestions.getQuestionsFromDB()
                        .get((qNum-1)).doAnswersMatch(answersTrue)){
                    correctAns++;
                    isActualAnsTrue = true;
                } else {
                    isActualAnsTrue = false;
                }
                int id = Global.globalQuestions.getQuestionsFromDB()
                        .get((qNum-1)).getId();
                Global.actualUser.setAnsInDB(conn, convertBooleanToInt(isActualAnsTrue),id);
                qNum++;
            }


            for(int i=1; i<(Global.getNumOfUsers() + 1); i++){

                String numOfCorAns = "SELECT COUNT(*) AS total FROM quiz.questions WHERE user" + i + "=1 ORDER BY id;";

                stmt2 = conn.createStatement();
                ResultSet columnCountSet = stmt2.executeQuery(numOfCorAns);
                double percent = 0;

                if(columnCountSet.next()){
                    double corAnsNum = columnCountSet.getInt("total");
                    double allQNum = Global.getNumOfQuestions();
                    percent = (corAnsNum/allQNum) * 100;
                    list.add((int)percent);
                }
                Global.setPercentList(list);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                // Step 5: Close the resources
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        request.setAttribute("list", list);

        RequestDispatcher rd = request.getRequestDispatcher("/webpages/login/pages/morris.jsp");
        rd.forward(request, response);

    }

    private int convertBooleanToInt(boolean b){
        if(b == true){
            return 1;
        }
        else {
            return 0;
        }
    }
}
