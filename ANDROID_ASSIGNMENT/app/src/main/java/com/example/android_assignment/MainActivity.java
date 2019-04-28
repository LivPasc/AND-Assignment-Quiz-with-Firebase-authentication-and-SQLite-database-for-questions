package com.example.android_assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CODE_SIGN_IN = 1;
    private FirebaseAuth firebaseAuth;
    private static final int QUIZ_CODE = 2;
    private static final String PREF_SHARED = "prefShared";
    public static final String HIGH_SCORE = "highScore";
    private TextView tvHighScore;
    public int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Log.d("AUTH", firebaseAuth.getCurrentUser().getDisplayName());
        } else {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.GOOGLE_PROVIDER,
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.EMAIL_PROVIDER
                    ).build(), CODE_SIGN_IN);
        }
        tvHighScore = findViewById(R.id.tv_highscore);
        getHighScore();

        Button btnStartQuiz = (Button) findViewById(R.id.btn_start);
        btnStartQuiz.setOnClickListener(this);
        Button btnLogOut = (Button) findViewById(R.id.btn_logout);
        btnLogOut.setOnClickListener(this);
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("AUTH", "USER IS LOGGED OUT!");
                        finish();
                    }
                });
    }

    private void startQuiz() {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        startActivityForResult(intent, QUIZ_CODE);
    }

    public void getHighScore() {
        SharedPreferences preferences = getSharedPreferences(PREF_SHARED, MODE_PRIVATE);
        highScore = preferences.getInt(HIGH_SCORE, 0);
        tvHighScore.setText("Highscore is: " + highScore);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    Log.d("AUTH", firebaseAuth.getCurrentUser().getDisplayName());
                } else {
                    Log.d("AUTH", "Not signed in");
                }
                break;

            case QUIZ_CODE:
                if (resultCode == RESULT_OK) {
                    int score = data.getIntExtra(QuizActivity.SCORING, 0);
                    if (score > highScore) {
                        highScore = score;
                        tvHighScore.setText("Highscore is: " + highScore);
                        SharedPreferences preferences = getSharedPreferences(PREF_SHARED, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(HIGH_SCORE, highScore);
                        editor.apply();
                    }
                }
                break;
            default:
                break;
        }

//        if(requestCode == CODE_SIGN_IN){
//            if(resultCode == RESULT_OK){
//                Log.d("AUTH", firebaseAuth.getCurrentUser().getDisplayName());
//            } else {
//                Log.d("AUTH", "FAILED TO LOGIN");
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_logout:
                logOut();
                break;

            case R.id.btn_start:
                startQuiz();
                break;
            default:
                break;
        }
    }
}
