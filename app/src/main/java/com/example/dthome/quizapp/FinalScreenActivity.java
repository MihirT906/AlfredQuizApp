package com.example.dthome.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class FinalScreenActivity extends AppCompatActivity {

    TextView tvScore,tvNoCorrect;
    int finalCorrect, finalScore;
    Button btnPlayAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);
        tvScore = (TextView) findViewById(R.id.tvFinalScore);
        btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
        tvNoCorrect = (TextView) findViewById(R.id.tvNoCorrect);
        finalScore = getIntent().getIntExtra("score",0);
        finalCorrect = getIntent().getIntExtra("noCorrect",0);
        tvScore.setText(String.valueOf(finalScore));
        tvNoCorrect.setText(String.valueOf(finalCorrect));

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalScreenActivity.this,QuizHomeActivity.class);
                intent.putExtra("finalscore",finalScore);
                startActivity(intent);
            }
        });
    }
    public void logoutUser(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
}
