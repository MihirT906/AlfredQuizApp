package com.example.dthome.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

//import com.google.firebase.auth.FirebaseAuth;

public class QuizHomeActivity extends AppCompatActivity implements View.OnClickListener{
   // FirebaseAuth mAuth
    TextView tvScore,tvDescription,tvInfo;
    int highScore = 0;
    String difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);

        tvScore = (TextView) findViewById(R.id.tvFinalScore);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        findViewById(R.id.imageViewStart).setOnClickListener(this);
        findViewById(R.id.btnEasy).setOnClickListener(this);
        findViewById(R.id.btnMedium).setOnClickListener(this);
        findViewById(R.id.btnHard).setOnClickListener(this);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        //mAuth = FirebaseAuth.getInstance();
        highScore = getIntent().getIntExtra("finalscore",0);
        tvScore.setText(String.valueOf(highScore));
        difficulty = "";
        tvDescription.setText(difficulty);

    }

    public void logoutUser(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    public void startQuiz(){
        if(difficulty.isEmpty()){
            tvDescription.setText("Please choose a difficulty");
        }else {
            Intent intent = new Intent(QuizHomeActivity.this, QuestionsActivity.class);
            intent.putExtra("difficulty",difficulty);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewStart:
                startQuiz();
                break;
            case R.id.btnEasy:
                difficulty = "easy";
                tvDescription.setText("Each question is worth 10 points");
                tvInfo.setText("Press the start Button");
                break;
            case R.id.btnMedium:
                difficulty = "medium";
                tvDescription.setText("Each question is worth 15 points");
                tvInfo.setText("Press the start Button");
                break;
            case R.id.btnHard:
                difficulty = "hard";
                tvDescription.setText("Each question is worth 20 points");
                tvInfo.setText("Press the start Button");
                break;
        }

    }
}
