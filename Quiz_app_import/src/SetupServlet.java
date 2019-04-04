

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

public class SetupServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String maxAns = request.getParameter("max");
            int maxAnsNum = Integer.parseInt(maxAns);
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz?useSSL=false", "Onlab", "onlab");
            Statement st;
            if((maxAns != null) && (maxAnsNum > Global.getNumOfAnswers()))
            {
                st=connection.createStatement();
                for(int i = (Global.getNumOfAnswers()+1); i<(maxAnsNum+1); ++i){
                    int i1 = st.executeUpdate("ALTER TABLE quiz.questions ADD answer"+ i +" varchar(50)DEFAULT \"NA\";");
                    int i2 = st.executeUpdate("ALTER TABLE quiz.questions ADD ans"+ i +"Pic varchar(200) DEFAULT \"NA\";");
                    int i3 = st.executeUpdate("ALTER TABLE quiz.questions ADD ans"+ i +"True boolean DEFAULT false;");
                }
                Global.setNumOfAnswers(maxAnsNum);
                response.sendRedirect("http://localhost:8080/query1");
            }else if ((maxAns != null) && (maxAnsNum > 3)){
                response.sendRedirect("http://localhost:8080/query1");
            }
            else{
                out.println("Something went wrong.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
