<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>First JSP</title>
</head>

<body>
<nav class="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
    <div class="container">
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="http://localhost:8080/query1">Home</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<%
    double num = (double)request.getAttribute("integer");
    ArrayList<ArrayList<Integer>> objectList = (ArrayList<ArrayList<Integer>>)request.getAttribute("list");
    for(ArrayList<Integer> j:objectList){
        int iterate = 0;
        for(Integer k:j){
            if(iterate == 0){
%>
<p>(<jsp:expression>k</jsp:expression>)</p>
<%
            }
            iterate++;
            }
    }
    if (num > 0.05) {
%>
<h2>You'll have a lucky day!</h2><p>(<jsp:expression>num</jsp:expression>)</p>
<%
} else {
%>
<h2>Well, life goes on ... </h2><p>(<%= num %>)</p>
<%
    }
%>
<a href="<%= request.getRequestURI() %>"><h3>Try Again</h3></a>
</body>
</html>