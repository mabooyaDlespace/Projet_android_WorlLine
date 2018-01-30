package worldline.ssm.rd.ux.wltwitter.model;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class QuestionBank {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionBank(List<Question> questionList) {
        mQuestionList = questionList;

        // Shuffle the question list
        Collections.shuffle(mQuestionList);

        mNextQuestionIndex = 0;
    }

    public Question getQuestion() {
        // Ensure we loop over the questions
        if (mNextQuestionIndex == mQuestionList.size()) {
            mNextQuestionIndex = 0;
        }

        // Please note the post-incrementation
        return mQuestionList.get(mNextQuestionIndex++);
    }
    public static QuestionBank generateQuestions(){
        List<Question> list =new ArrayList<>();
        list.add(new Question("Quelle est la meilleur matière?",
                Arrays.asList("Android","java2","FHS","ASN"),
                0));

        list.add(new Question("Quelle matière contient de la numérisation?",
                Arrays.asList("Android","java2","FHS","ASN"),
                3));

        list.add(new Question("Quelle matière contient des sciences humaines?",
                Arrays.asList("Android","java2","FHS","ASN"),
                2));

        list.add(new Question("Quelle matière est enseigné par mr Boulinguez?",
                Arrays.asList("Android","java2","FHS","ASN"),
                3));

        list.add(new Question("Quelle est la meilleur matière?",
                Arrays.asList("Android","java2","FHS","ASN"),
                0));
        return new QuestionBank(list);

    }
}