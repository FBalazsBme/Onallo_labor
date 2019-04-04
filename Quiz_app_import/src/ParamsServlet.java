// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class ParamsServlet extends HttpServlet {  // JDK 1.6 and above only

    // The doGet() runs once per HTTP GET request to this servlet.
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int numberOfAns = Global.getNumOfAnswers();
        request.setAttribute("src", numberOfAns);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(numberOfAns);
        writer.flush();
    }
}