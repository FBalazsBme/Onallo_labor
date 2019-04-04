

import java.sql.*;
import java.util.ArrayList;

public class QuestionStore {

    private ArrayList<Question> questionList;
    private ArrayList<Question> QuestionStoreFromDB;

    public QuestionStore(){
        questionList = new ArrayList<Question>();
        QuestionStoreFromDB = new ArrayList<Question>();

    }

    public QuestionStore(ArrayList<Question> userList) {
        this.questionList = userList;
        QuestionStoreFromDB = new ArrayList<Question>();
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
            if(!(QuestionStoreFromDB.contains(q))){
/*                q.addToDB(conn);*/
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
        String getQuestionStoreFromDB = "select * from quiz.QuestionStore;";
        ResultSet rset = st.executeQuery(getQuestionStoreFromDB);
        Question actualQuestion = new Question();
        while(rset.next()){
/*            actualQuestion = new Question(rset.getString("question"),rset.getString("answer1"),
                    rset.getString("answer2"), rset.getString("answer3"), rset.getString("answers"), rset.getInt("numOfAns"));*/
            questionList.add(actualQuestion);
            QuestionStoreFromDB.add(actualQuestion);
        }
    }

    public class Question{
        private String question;
        private ArrayList<String> answers;
        private ArrayList<Boolean> answersTrue;

        private int numOfAns;


        public Question(){
            this.question = null;
            this.answers = null;
            this.numOfAns = 0;
        }


        public Question(String question, ArrayList<String> answers, int numOfAns) throws IllegalArgumentException {
            this.question = question;
            this.answers = answers;
            if(numOfAns > 0 && numOfAns < 5){
                this.numOfAns = numOfAns;
            }
            else{
                throw new IllegalArgumentException();
            }
        }




/*        public void addToDB(Connection conn) throws SQLException {



 *//*           String insertQuestionintoDB = "insert into quiz.QuestionStore (question, answer1, answer2, answer3, answer4, correctLoc)" +
                    " values (?,?,?,?,?,?);";
            PreparedStatement pst = conn.prepareStatement(insertQuestionintoDB);

            pst.setString(1, this.question);
            pst.setString(5, this.answers);
            pst.setInt(6, this.numOfAns);*//*

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

            if ((this.answers == null) ? (other.answers != null) : !this.answers.equals(other.answers)) {
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
            hash = 53 * hash + (this.answers != null ? this.answers.hashCode() : 0);
            return hash;
        }*/

    }

}
