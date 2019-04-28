package com.example.android_assignment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvLocation;
    private TextView tvCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button btnSubmit;
    private ColorStateList txtColorDefRb;
    private ColorStateList txtColorDefCd;
    private int qCounter;
    private int qCountT;
    private QuizQuestion currentQ;
    private ArrayList<QuizQuestion> quizQuestionList;
    private int score;
    private boolean answered;
    public static final String SCORING = "scoring";
    public static final long COUNTDOWN = 60000;
    public static final String KEY_FOR_SCORE = "keyForScore";
    public static final String KEY_FOR_LOCATION = "keyForLocation";
    public static final String KEY_FOR_TIMELEFT = "keyForTimeleft";
    public static final String KEY_FOR_ANSWERED = "keyForAnswered";
    public static final String KEY_FOR_QUIZLIST = "keyForQuestions";
    private CountDownTimer countDownTimer;
    private long timeLeft;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.d("AUTH", "QUIZ START");

        tvQuestion = findViewById(R.id.tv_question);
        tvScore = findViewById(R.id.tv_score);
        tvLocation = findViewById(R.id.tv_quizLocation);
        tvCountDown = findViewById(R.id.tv_countdown);
        rbGroup = findViewById(R.id.radio_gr);
        rb1 = findViewById(R.id.radio_btn1);
        rb2 = findViewById(R.id.radio_btn2);
        rb3 = findViewById(R.id.radio_btn3);
        rb4 = findViewById(R.id.radio_btn4);
        btnSubmit = findViewById(R.id.btn_submit);

        txtColorDefRb = rb1.getTextColors();
        txtColorDefCd = tvCountDown.getTextColors();

        if (savedInstanceState == null) {
            DB_Context helper = new DB_Context(this);
            quizQuestionList = helper.getAllQuestions();
            Log.d("AUTH", "DATABASE SUCCESS");
            qCountT = quizQuestionList.size();
            Log.isLoggable("AUTH", qCountT);
            Collections.shuffle(quizQuestionList);
            goToNextQ();
        } else {
            score = savedInstanceState.getInt(KEY_FOR_SCORE);
            Log.d("AUTH", "SCORE SUCCESS " + score);
            timeLeft = savedInstanceState.getLong(KEY_FOR_TIMELEFT);
            Log.d("AUTH", "TIMELEFT SUCCESS " + timeLeft);
            quizQuestionList = savedInstanceState.getParcelableArrayList(KEY_FOR_QUIZLIST);
            answered = savedInstanceState.getBoolean(KEY_FOR_ANSWERED);
            Log.d("AUTH", "ANSWERED SUCCESS " + answered);
            qCountT = quizQuestionList.size();
            Log.d("AUTH", "QUESTION LIST SUCCESS " + qCountT);
            qCounter = savedInstanceState.getInt(KEY_FOR_LOCATION);
            Log.d("AUTH", "LOCATION SUCCESS" + qCounter);
            currentQ = quizQuestionList.get(qCounter - 1);
            if (!answered) {
                timeLeft = savedInstanceState.getLong(KEY_FOR_TIMELEFT);
                countDownTimer = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished;
                        int min = (int) (timeLeft / 1000) / 60;
                        int sec = (int) (timeLeft / 1000) % 60;
                        String timer = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                        tvCountDown.setText(timer);
                        if (timeLeft < 15000) {
                            tvCountDown.setTextColor(Color.RED);
                        } else {
                            tvCountDown.setTextColor(txtColorDefCd);
                        }
                    }

                    @Override
                    public void onFinish() {
                        timeLeft = 0;
                        int min = (int) (timeLeft / 1000) / 60;
                        int sec = (int) (timeLeft / 1000) % 60;
                        String timer = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                        tvCountDown.setText(timer);
                        if (timeLeft < 15000) {
                            tvCountDown.setTextColor(Color.RED);
                        } else {
                            tvCountDown.setTextColor(txtColorDefCd);
                        }
                        verifyAnswer();
                    }
                }.start();
            } else {
                if (timeLeft < 15000) {
                    tvCountDown.setTextColor(Color.RED);
                } else {
                    tvCountDown.setTextColor(txtColorDefCd);
                }
                giveSolution();
            }
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        verifyAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Click on answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    goToNextQ();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_FOR_SCORE, score);
        outState.putInt(KEY_FOR_LOCATION, qCounter);
        outState.putBoolean(KEY_FOR_ANSWERED, answered);
        outState.putLong(KEY_FOR_TIMELEFT, timeLeft);
        outState.putParcelableArrayList(KEY_FOR_QUIZLIST, quizQuestionList);
    }

    private void verifyAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton answer = findViewById(rbGroup.getCheckedRadioButtonId());
        int ansNr = rbGroup.indexOfChild(answer) + 1;

        if (ansNr == currentQ.getAnsNr()) {
            score++;
            tvScore.setText("Score is: " + score);
        }

        giveSolution();
    }

    private void giveSolution() {
        rb4.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb1.setTextColor(Color.RED);
        switch (currentQ.getAnsNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                rb1.setText(":)");
                rb2.setText(":(");
                rb3.setText(":(");
                rb4.setText(":(");
                tvQuestion.setText("The correct answer is 1");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                rb2.setText(":)");
                rb1.setText(":(");
                rb3.setText(":(");
                rb4.setText(":(");
                tvQuestion.setText("The correct answer is 2");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                rb3.setText(":)");
                rb2.setText(":(");
                rb1.setText(":(");
                rb4.setText(":(");
                tvQuestion.setText("The correct answer is 3");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                rb4.setText(":)");
                rb2.setText(":(");
                rb3.setText(":(");
                rb1.setText(":(");
                tvQuestion.setText("The correct answer is 4");
                break;
        }

        if (qCounter < qCountT) {
            btnSubmit.setText("Next");
        } else {
            btnSubmit.setText("Finish");
        }
    }

    private void goToNextQ() {
        rb1.setTextColor(txtColorDefRb);
        rb2.setTextColor(txtColorDefRb);
        rb3.setTextColor(txtColorDefRb);
        rb4.setTextColor(txtColorDefRb);
        rbGroup.clearCheck();

        if (qCounter < qCountT) {
            currentQ = quizQuestionList.get(qCounter);
            timeLeft = COUNTDOWN;
            countDownTimer = new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                    int min = (int) (timeLeft / 1000) / 60;
                    int sec = (int) (timeLeft / 1000) % 60;
                    String timer = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                    tvCountDown.setText(timer);
                    if (timeLeft < 15000) {
                        tvCountDown.setTextColor(Color.RED);
                    } else {
                        tvCountDown.setTextColor(txtColorDefCd);
                    }
                }

                @Override
                public void onFinish() {
                    timeLeft = 0;
                    int min = (int) (timeLeft / 1000) / 60;
                    int sec = (int) (timeLeft / 1000) % 60;
                    String timer = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                    tvCountDown.setText(timer);
                    if (timeLeft < 15000) {
                        tvCountDown.setTextColor(Color.RED);
                    } else {
                        tvCountDown.setTextColor(txtColorDefCd);
                    }
                    verifyAnswer();
                }
            }.start();
            tvQuestion.setText(currentQ.getQ());
            rb1.setText(currentQ.getO1());
            rb2.setText(currentQ.getO2());
            rb3.setText(currentQ.getO3());
            rb4.setText(currentQ.getO4());

            qCounter++;
            tvLocation.setText("Qustion nr.: " + qCounter + " of " + qCountT);

            answered = false;
            btnSubmit.setText("Submit");
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        Intent scoreIntent = new Intent();
        scoreIntent.putExtra(SCORING, score);
        setResult(RESULT_OK, scoreIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (time + 1000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "To exit press 2 times", Toast.LENGTH_SHORT).show();
        }
        time = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
