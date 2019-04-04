
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AddNewAnsSlot extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{
            if(Global.incrementNumOfAnswers()){
                File fileOut = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/addquestion_answers.html");
                PrintWriter out = new PrintWriter(new FileOutputStream(fileOut, false /* append = false */));
                out.printf(" ");
                out.close();
                File destination = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/addquestion.html");
                out = new PrintWriter(new FileOutputStream(destination, false /* append = false */));
                out.printf(" ");
                out.close();
                out = new PrintWriter(new FileOutputStream(fileOut, true /* append = true */));
                for(int i = 1; i < (Global.getNumOfAnswers() + 1); ++i){
                    out.append("<input class=\"form-control\" placeholder=\"answer " + i +"\" name=\"answer" + i + "\"  type=\"answer" + i +"\" value=\"\">\n" +
                            "<input type=\"file\" class=\"filestyle\" data-classButton=\"btn btn-primary\" data-input=\"false\" data-text=\"Add picture\" data-classIcon=\"icon-plus\" data-buttonText=\"Your label here.\"\n" +
                            "name=\"picA" + i +"\">");
                }
                out.close();
                File firstPart = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/addquestion_first.html");
                File lastPart = new File("C:/Users/Onlab/Downloads/Apache_Software_Foundation/tomcat/webapps/Quiz_app_import/web/webpages/login/pages/addquestion_last.html");
                File [] files = {firstPart,fileOut, lastPart};
                MergeContent.joinFiles(destination, files);
                response.sendRedirect("http://localhost:8080/webpages/login/pages/addquestion.html");
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }



    }

}
