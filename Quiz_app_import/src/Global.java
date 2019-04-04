

import java.sql.*;
import java.util.ArrayList;

public class Global {
    public static Users globalUsers;
    public static Questions globalQuestions = new Questions();
    public static Users.User actualUser = null;
    public static ArrayList<Integer> numOfAnsByQ = null;
    private static ArrayList<Integer> percentList = new ArrayList<>();
    private static int numOfAnswers = 4;
    private static int numOfAddAnswers = 4;
    private static int numOfUsers = 0;
    private static int numOfQuestions = 0;

    public static Users getGlobalUsers() {
        return globalUsers;
    }

    public static void setGlobalUsers(Users globalUsers) {
        Global.globalUsers = globalUsers;
    }

    public static Questions getGlobalQuestions() {
        return globalQuestions;
    }

    public static void setGlobalQuestions(Questions globalQuestions) {
        Global.globalQuestions = globalQuestions;
    }

    public static int getNumOfAnswers() {
        return numOfAnswers;
    }

    public static boolean setNumOfAnswers(int numOfAnswers) {
        if(numOfAnswers < Global.numOfAnswers){
            return false;
        } else {
            Global.numOfAnswers = numOfAnswers;
            return true;
        }
    }

    public static int getNumOfQuestions() {
        return numOfQuestions;
    }

    public static void setNumOfQuestions(int numOfQuestions) {
        Global.numOfQuestions = numOfQuestions;
    }

    public static void setNumOfQuestionsFromDB(Connection conn){
        String sqlNumOfRows = "SELECT COUNT(*) AS total FROM quiz.questions;";
        Statement stmt2 = null;
        try {
            stmt2 = conn.createStatement();
            ResultSet columnCountSet = stmt2.executeQuery(sqlNumOfRows);

            if(columnCountSet.next()){
                int qNum = columnCountSet.getInt("total");
                Global.setNumOfQuestions(qNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean incrementNumOfAnswers(){
        if(numOfAnswers<8){
            numOfAnswers++;
            return true;
        }
        return false;
    }

    public static ArrayList<Integer> getPercentList() {
        return percentList;
    }

    public static void setPercentList(ArrayList<Integer> percentList) {
        Global.percentList = percentList;
    }

    public static int getNumOfAddAnswers() {
        return numOfAddAnswers;
    }

    public static void setNumOfAddAnswers(int numOfAddAnswers) {
        Global.numOfAddAnswers = numOfAddAnswers;
    }

    public static ArrayList<Integer> getNumOfAnsByQ() {
        return numOfAnsByQ;
    }

    public static void setNumOfAnsByQ(ArrayList<Integer> numOfAnsByQ) {
        Global.numOfAnsByQ = numOfAnsByQ;
    }

    public static int getNumOfUsers() {
        return numOfUsers;
    }

    public static void setNumOfUsers(int numOfUsers) {
        Global.numOfUsers = numOfUsers;
    }

    public static void setNumOfUSersFromDB(Connection conn) throws SQLException {
        String sqlNumOfRows = "SELECT MAX(id) AS total FROM quiz.users;";
        Statement stmt2 = conn.createStatement();
        ResultSet columnCountSet = stmt2.executeQuery(sqlNumOfRows);

        if(columnCountSet.next()){
            int ansNum = columnCountSet.getInt("total");
            Global.setNumOfUsers(ansNum);
        }
    }

    public static void syncUserColumns(Connection conn) {
        String sqlNumOfRows = " ";
        Statement stmt2 = null;
        try {
            stmt2 = conn.createStatement();
            for(int i=1; i<(Global.getNumOfUsers() + 1); i++){
                sqlNumOfRows = "ALTER TABLE quiz.questions " +
                        "ADD COLUMN user" + i + " int(11) DEFAULT 2;";
                try{
                    stmt2.execute(sqlNumOfRows);
                }
                catch(SQLSyntaxErrorException e){
                }
            }
        } catch (Exception e) {
        }
    }

    public Global() {
        globalUsers = null;
        globalQuestions = null;
        numOfAnswers = 4;
    }
}
