

import java.sql.*;
import java.util.ArrayList;

public class Questions {

    private ArrayList<Question> questionList;
    private ArrayList<Question> questionsFromDB;

    public Questions(){
        questionList = new ArrayList<Question>();
        questionsFromDB = new ArrayList<Question>();

    }

    public int getNumOfQuestions(){
        return questionList.size();
    }

    public ArrayList<Question> getQuestionsFromDB() {
        return questionsFromDB;
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
        String getQuestionsFromDB = "select * from quiz.questions ORDER BY id;";
        ResultSet rset = st.executeQuery(getQuestionsFromDB);
        Question actualQuestion = new Question();
        while(rset.next()){

            ArrayList<String> answers = new ArrayList<>();
            ArrayList<Boolean> ansCorList = new ArrayList<>();

            for(int k = 1; k < (Global.getNumOfAnswers()+1); ++k) {
                String appendString = rset.getString("answer" + k);
                boolean b1 = convertIntToBoolean(rset.getInt("ans"+ k +"True"));
                if (!appendString.equals("NA")){
                    answers.add(appendString);
                    ansCorList.add(b1);
                }
                else {
                    break;
                }
            }

            int id = rset.getInt("id");
            String question = rset.getString("question");

            questionList.add(new Question(id, question, answers, ansCorList));
            questionsFromDB.add(new Question(id, question, answers, ansCorList));
            int size = answers.size();
            int o = 1;
        }
    }

    private Boolean convertIntToBoolean(int i) throws SQLException{
        switch (i){
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new SQLException();
        }
    }

    private int convertBooleanToInt (boolean b) {
        if(b==true){
            return 1;
        } else {
            return 0;
        }
    }

    public class Question{
        private int id;
        private String question;
        private ArrayList<Answer> answers;

        public Question(){
            this.question = null;
            this.answers = new ArrayList<Answer>();
        }

        public Question(int id,String question, ArrayList<String> answers, ArrayList<Boolean> ansCorrectList)
                throws IllegalArgumentException {
            this.id = id;
            this.question = question;
            this.answers = new ArrayList<Answer>();
            int i = 0;
            for(String a:answers){
                this.answers.add(new Answer(a, ansCorrectList.get(i)));
                ++i;
            }
        }

        public boolean doAnswersMatch(ArrayList<Boolean> checkedAns){
            int i = 0;
            for(Boolean b:checkedAns){
                if(b != this.answers.get(i).isAnsCorrect()){
                    return false;
                }
                i++;
            }
            return true;
        }

        public ArrayList<Answer> getAnswers() {
            return answers;
        }

        public int getId() {
            return id;
        }

        public void addToDB(Connection conn) throws SQLException {

            StringBuffer insertQintoDBBuffer = new StringBuffer();
            insertQintoDBBuffer.append("insert into quiz.questions (question");
            int i = 1;
            for(Answer a:answers) {
                insertQintoDBBuffer.append(" ,answer");
                insertQintoDBBuffer.append(i);
                insertQintoDBBuffer.append(" ,ans");
                insertQintoDBBuffer.append(i);
                insertQintoDBBuffer.append("True");
                ++i;
            }
            insertQintoDBBuffer.append(") values (?");
            for(Answer a:answers) {
                insertQintoDBBuffer.append(",?,?");
            }
            insertQintoDBBuffer.append(");");

            PreparedStatement pst = conn.prepareStatement(insertQintoDBBuffer.toString());

            pst.setString(1, this.question);
            i = 2;
            for(Answer a:answers){
                pst.setString(i++, a.getAnswer());
                pst.setInt(i++, convertBooleanToInt(a.isAnsCorrect()));
            }

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

            if ((this.answers.get(0).getAnswer() == null) ? (other.answers.get(0).getAnswer() != null) : !this.answers.get(0).getAnswer().equals(other.answers.get(0).getAnswer())) {
                return false;
            }

            if ((this.answers.get(1).getAnswer() == null) ? (other.answers.get(1).getAnswer() != null) : !this.answers.get(1).getAnswer().equals(other.answers.get(1).getAnswer())) {
                return false;
            }

            if ((this.answers.get(2).getAnswer() == null) ? (other.answers.get(2).getAnswer() != null) : !this.answers.get(2).getAnswer().equals(other.answers.get(2).getAnswer())) {
                return false;
            }

            if ((this.answers.get(3).getAnswer() == null) ? (other.answers.get(3).getAnswer() != null) : !this.answers.get(3).getAnswer().equals(other.answers.get(3).getAnswer())) {
                return false;
            }


            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + (this.question != null ? this.question.hashCode() : 0);
            hash = 53 * hash + (this.answers.get(0).getAnswer() != null ? this.answers.get(0).getAnswer().hashCode() : 0);
            hash = 53 * hash + (this.answers.get(1).getAnswer() != null ? this.answers.get(1).getAnswer().hashCode() : 0);
            hash = 53 * hash + (this.answers.get(2).getAnswer() != null ? this.answers.get(2).getAnswer().hashCode() : 0);
            hash = 53 * hash + (this.answers.get(3).getAnswer() != null ? this.answers.get(3).getAnswer().hashCode() : 0);
            return hash;
        }

    }

}
