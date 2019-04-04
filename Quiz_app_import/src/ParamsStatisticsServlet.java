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

public class ParamsStatisticsServlet extends HttpServlet {  // JDK 1.6 and above only

    // The doGet() runs once per HTTP GET request to this servlet.
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Integer> questions = Global.getPercentList();
        String json = new Gson().toJson(questions);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
