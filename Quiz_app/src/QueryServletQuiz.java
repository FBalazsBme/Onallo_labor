// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".



import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

public class QueryServletQuiz extends HttpServlet {  // JDK 1.6 and above only

    private ArrayList<Integer> numOfAns = new ArrayList<Integer>();

    private Questions allQuestionsFromDB = new Questions();

    private String question = "";

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
            Global.setNumOfQuestionsFromDB(conn);
            // database-URL(hostname, port, default database), username, password

            // Step 2: Allocate a Statement object within the Connection
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();

            // Step 3: Execute a SQL SELECT query
            String sqlStr = "select * from quiz.questions ORDER BY id;";
            String sqlNumOfColumns = "SELECT count(*) AS total\n" +
                    "FROM information_schema.columns\n" +
                    "WHERE table_name = 'questions';";

            // Print an HTML page as the output of the query
            ResultSet rset = stmt.executeQuery(sqlStr);// Send the query to the server
            ResultSet columnCountSet = stmt2.executeQuery(sqlNumOfColumns);
            int i = 1;
            out.append("<form method=\"post\" action=\"../../../evalans\">");
            String fileName = "NA";

            if(columnCountSet.next()){
                int ansNum = (columnCountSet.getInt("total") - 3 - Global.getNumOfUsers())/3;
                Global.setNumOfAnswers(ansNum);
            }

            Global.globalQuestions.loadFromDB(conn);


            // Step 4: Process the query result set
            while (rset.next()) {
                // Print a paragraph <p>...</p> for each record
                question = rset.getString("question");
                out.append("<div class=\"post-preview\">\n" + "<a href=\"../../checkans\">\n"
                        + "<h2 class=\"post-title\">"
                        + question
                        + "</h2>"
                        + "</a>");
                //addPics(fileName, rset, out);
                for(int l = 1; l < (Global.getNumOfAnswers()+1); ++l) {
                    String ansCheck = "ans" + l + "True";
                    if ((rset.getInt(ansCheck) == 0) || (rset.getInt(ansCheck) == 1)) {
                        String appendString = rset.getString("answer" + l);
                        int count = (i*10)+l;
                        //ez lényegében egy kódolás, hogy mennyi kérdés és válasz van, és l csak max 8 lehet
                        //lényeg hogy l eggyel több mint a valós érték
                        if (!appendString.equals("NA")){
                            out.append("<h3 class=\"post-subtitle\">"
                                    + rset.getString("answer" + l)
                                    + "</h3>"
                                    + "<input type = \"checkbox\"\n" +
                                    "name = \"radAns" + count + "\"\n" +
                                    "id = \"sizeSmall\"\n" +
                                    "value = \"small\"\n />");
                        }
                        else {
                            numOfAns.add(l);
                            break;
                        }
                    } else {
                        break;
                    }
                }

                out.append("</a></div><hr>");
                ++i;
            }
            Global.setNumOfAnsByQ(numOfAns);
            out.append("<input type=\"submit\" value=\"Submit answers\" class=\"btn btn-lg btn-success btn-block\" >");
            out.append("</form>");
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

    public void resetAddQHtml() throws FileNotFoundException {
        File resetAnsNum = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/addquestion_answers.html");
        PrintWriter out = new PrintWriter(new FileOutputStream(resetAnsNum, false /* append = false */));
        out.printf(" ");
        out.close();
        File destination = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/addquestion.html");

    }

    public void addPics(String fileName, ResultSet rset, PrintWriter out) throws SQLException {
        fileName = rset.getString("qPic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans1Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans2Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans3Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans4Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans5Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans6Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans7Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

        fileName = rset.getString("ans8Pic");
        if(!fileName.equals("NA") && fileName!=null && !rset.wasNull()){
            out.append("      <img src=\"http://localhost:8080/webpages/questions/uploads/" +
                    fileName +
                    "\" alt=\"question_image\" height=\"500\" width=\"500\"/>\n");
        }

    }
}