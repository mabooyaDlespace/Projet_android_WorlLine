package worldline.ssm.rd.ux.wltwitter.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import worldline.ssm.rd.ux.wltwitter.R;
import worldline.ssm.rd.ux.wltwitter.model.Question;
import worldline.ssm.rd.ux.wltwitter.model.QuestionBank;

public class TopQuizActivity extends Activity implements View.OnClickListener {
    private TextView mQuestionTextView;
    private Button mAnswerButton0;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
    private int flag_bonne_ou_mauvaise_reponse = 1;
    private SharedPreferences mSharedPreferences;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    private int mScore = 0;
    private int mNumberOfQuestion = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_quiz);
        /* Shared prefs*/
        mSharedPreferences =getSharedPreferences("worldlinepref",MODE_PRIVATE);
/*On a initialiser le contexte mtnt on Bind */
        mQuestionTextView = (TextView) findViewById(R.id.activity_game_question_text);
        mAnswerButton0 = (Button) findViewById(R.id.activity_game_answer1_btn);
        mAnswerButton1 = (Button) findViewById(R.id.activity_game_answer2_btn);
        mAnswerButton2 = (Button) findViewById(R.id.activity_game_answer3_btn);
        mAnswerButton3 = (Button) findViewById(R.id.activity_game_answer4_btn);
/*On Bind les bouton à des tag */
        mAnswerButton0.setTag(0);
        mAnswerButton1.setTag(1);
        mAnswerButton2.setTag(2);
        mAnswerButton3.setTag(3);
/*On rajoute des Listener pour chaque bouton*/
        mAnswerButton0.setOnClickListener(this);
        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);

/* On initie la première quesion*/
        mQuestionBank = QuestionBank.generateQuestions();
        mCurrentQuestion = mQuestionBank.getQuestion();
        displayQuestion(mCurrentQuestion);


    }

    private void displayQuestion(final Question question) {
        mQuestionTextView.setText(question.getQuestion());
        mAnswerButton0.setText(question.getChoiceList().get(0));
        mAnswerButton1.setText(question.getChoiceList().get(1));
        mAnswerButton2.setText(question.getChoiceList().get(2));
        mAnswerButton3.setText(question.getChoiceList().get(3));

    }

    /**
     * Si la réponse est bonne alors on passe à une nouvelle réponse et on patient pendant 500 ms
     *
     * @param view
     */
    @Override
    public void onClick(View view) {// Question réponses
        int responseIndex = (int) view.getTag();
        if (responseIndex == mCurrentQuestion.getAnswerIndex()) { // Answer Index = id de la bonne réponse
            //bonne reponse
            view.setBackgroundColor(Color.GREEN);
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            if (flag_bonne_ou_mauvaise_reponse == 1) {
                mScore++;
            }
            new CountDownTimer(500, 500) { //40000 milli seconds is total time, 1000 milli seconds is time interval

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    mAnswerButton0.setBackgroundColor(Color.WHITE);
                    mAnswerButton1.setBackgroundColor(Color.WHITE);
                    mAnswerButton2.setBackgroundColor(Color.WHITE);
                    mAnswerButton3.setBackgroundColor(Color.WHITE);
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                    flag_bonne_ou_mauvaise_reponse=1;
                }
            }.start();
            if (--mNumberOfQuestion == 0) {
                endGame();
            }


        } else {
            //Mauvaise reponses
            flag_bonne_ou_mauvaise_reponse=0;
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            view.setBackgroundColor(Color.RED);

        }

    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);// Ca permet de récuperer le score
                        setResult(RESULT_OK, intent);// il faut enregistrer auprès d'android, result OK = ca sest bien passé
                        finish();//arrete l'activité courrente et revient à l'activité précédente
                    }
                }).create().show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu_action_logout) {
           mSharedPreferences.edit().clear().commit();
            //update message
            Intent intent = new Intent();
            //intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);// Ca permet de récuperer le score
            setResult(RESULT_OK, intent);// il faut enregistrer auprès d'android, result OK = ca sest bien passé
            Toast.makeText(TopQuizActivity.this, getResources().getString(R.string.action_logout), Toast.LENGTH_SHORT).show();

            finish();//arrete l'activité courrente et revient à l'activité précédente


        }

        return super.onOptionsItemSelected(item);
    }

/*End*/
}
