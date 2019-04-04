

public class Answer {

    private String answer;
    private boolean ansCorrect;

    public Answer(String answer, boolean ansCorrect) {
        this.answer = answer;
        this.ansCorrect = ansCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnsCorrect() {
        return ansCorrect;
    }

    public void setAnsCorrect(boolean ansCorrect) {
        this.ansCorrect = ansCorrect;
    }
}
