

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

public class showStat extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the MIME type for the response message
        response.setContentType("text/html");


        File fileOut = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/morris.html");
        fileOut.getParentFile().mkdirs();

        File fileBase = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/morris_start.html");
        Path from = fileBase.toPath(); //convert from File to Path
        Path to = fileOut.toPath(); //convert from String to Path
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOut, true /* append = true */), StandardCharsets.UTF_8));

        //PrintWriter out = new PrintWriter(fileOut);
        // Get a output writer to write the response message into the network socket
        //PrintWriter out = response.getWriter();

        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        try {
            // Step 1: Allocate a database Connection object
            Class.forName("com.mysql.jdbc.Driver");  // Needed for JDK9/Tomcat9
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quiz?useSSL=false", "Onlab", "onlab"); // <== Check!
            Global.setNumOfUSersFromDB(conn);
            Global.syncUserColumns(conn);
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            out.append("    <script>$(function() {\n" +
                    "\n" +
                    "    Morris.Bar({\n" +
                    "        element: 'morris-bar-chart',\n" +
                    "        data: [");

            Global.setNumOfQuestionsFromDB(conn);

            ArrayList<Integer> percentList = new ArrayList<>();


            for(int i=1; i<(Global.getNumOfUsers() + 1); i++){

                String numOfCorAns = "SELECT COUNT(*) AS total FROM quiz.questions WHERE user" + i + "=1 ORDER BY id;";

                stmt2 = conn.createStatement();
                ResultSet columnCountSet = stmt2.executeQuery(numOfCorAns);
                double percent = 0;

                if(columnCountSet.next()){
                    double corAnsNum = columnCountSet.getInt("total");
                    double allQNum = Global.getNumOfQuestions();
                    percent = (corAnsNum/allQNum) * 100;
                    percentList.add((int)percent);
                    out.append("\t\t{\n" +
                            "            y: 'user"+ i +"',\n" +
                            "            a: "+ (int)percent +"\n" +
                            "        }");
                    if(i!=Global.getNumOfUsers()){
                        out.append(",");
                    }
                }
                Global.setPercentList(percentList);
            }
            out.append("],\n" +
                    "        xkey: 'y',\n" +
                    "        ykeys: ['a', 'b'],\n" +
                    "        labels: ['Series A', 'Series B'],\n" +
                    "        hideHover: 'auto',\n" +
                    "        resize: true\n" +
                    "    });\n" +
                    "    \n" +
                    "});\n" +
                    "</script>\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>");
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
            response.sendRedirect("http://localhost:8080/webpages/login/pages/morris.html");
        }

    }
}
