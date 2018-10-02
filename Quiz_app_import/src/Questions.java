import java.sql.*;
import java.util.ArrayList;

public class Questions {

    private ArrayList<Question> questionList;
    private ArrayList<Question> questionsFromDB;

    public Questions(){
        questionList = new ArrayList<Question>();
        questionsFromDB = new ArrayList<Question>();

    }

    public Questions(ArrayList<Question> userList) {
        this.questionList = userList;
        questionsFromDB = new ArrayList<Question>();
    }

    public boolean addQuestion(Question u){
        if(!questionList.contains(u)){
            questionList.add(u);
            return true;
        }
        else{
            return false;
        }
    }

    public void syncDB(Connection conn) throws SQLException {
        for(Question q:questionList){
            if(!(questionsFromDB.contains(q))){
                q.addToDB(conn);
            }
        }
        loadFromDB(conn);
    }

    public void removeQuestion(Question q){
        questionList.remove(q);
    }

    public boolean containsQuestion(Question q){
        if(questionList.contains(q)){
            return true;
        }
        else{
            return false;
        }
    }

    public void loadFromDB(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        String getQuestionsFromDB = "select * from quiz.questions;";
        ResultSet rset = st.executeQuery(getQuestionsFromDB);
        Question actualQuestion = new Question();
        while(rset.next()){
            actualQuestion = new Question(rset.getString("question"),rset.getString("answer1"),
                    rset.getString("answer2"), rset.getString("answer3"), rset.getString("answer4"), rset.getInt("correctLoc"));
            questionList.add(actualQuestion);
            questionsFromDB.add(actualQuestion);
        }
    }

    public class Question{
        private String question;
        private String answer1;
        private String answer2;
        private String answer3;
        private String answer4;
        private int correctLoc;


        public Question(){
            this.question = null;
            this.answer1 = null;
            this.answer2 = null;
            this.answer3 = null;
            this.answer4 = null;
            this.correctLoc = 0;
        }


        public Question(String question, String answer1, String answer2, String answer3, String answer4, int correctLoc) throws IllegalArgumentException {
            this.question = question;
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.answer4 = answer4;
            if(correctLoc > 0 && correctLoc < 5){
                this.correctLoc = correctLoc;
            }
            else{
                throw new IllegalArgumentException();
            }
        }




        public void addToDB(Connection conn) throws SQLException {

            String insertQuestionintoDB = "insert into quiz.questions (question, answer1, answer2, answer3, answer4, correctLoc)" +
                    " values (?,?,?,?,?,?);";
            PreparedStatement pst = conn.prepareStatement(insertQuestionintoDB);

            pst.setString(1, this.question);
            pst.setString(2, this.answer1);
            pst.setString(3, this.answer2);
            pst.setString(4, this.answer3);
            pst.setString(5, this.answer4);
            pst.setInt(6, this.correctLoc);

            pst.execute();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!Question.class.isAssignableFrom(obj.getClass())) {
                return false;
            }

            final Question other = (Question) obj;
            if ((this.question == null) ? (other.question != null) : !this.question.equals(other.question)) {
                return false;
            }

            if ((this.answer1 == null) ? (other.answer1 != null) : !this.answer1.equals(other.answer1)) {
                return false;
            }

            if ((this.answer2 == null) ? (other.answer2 != null) : !this.answer2.equals(other.answer2)) {
                return false;
            }

            if ((this.answer3 == null) ? (other.answer3 != null) : !this.answer3.equals(other.answer3)) {
                return false;
            }

            if ((this.answer4 == null) ? (other.answer4 != null) : !this.answer4.equals(other.answer4)) {
                return false;
            }


            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + (this.question != null ? this.question.hashCode() : 0);
            hash = 53 * hash + (this.answer1 != null ? this.answer1.hashCode() : 0);
            hash = 53 * hash + (this.answer2 != null ? this.answer2.hashCode() : 0);
            hash = 53 * hash + (this.answer3 != null ? this.answer3.hashCode() : 0);
            hash = 53 * hash + (this.answer4 != null ? this.answer4.hashCode() : 0);
            return hash;
        }

    }

}
