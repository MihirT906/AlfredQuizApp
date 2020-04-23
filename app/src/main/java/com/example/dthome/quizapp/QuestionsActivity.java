package com.example.dthome.quizapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {
    TextView tvQuestion,tvScore,tvNoOfQ,tvCorrectAnswer;
    ProgressBar progressBar;
    Button op1,op2,op3,op4,btnConfirm;
    ImageView ivGoBack;
    String level;
    int optionPressed = -1;
    int counter = 0;
    int noOfCorrect = 0;
    int score = 0;
    String questionString, correctAnswerString, option2String, option3String, option4String, optionClickedString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvScore = (TextView) findViewById(R.id.tvFinalScore);
        tvCorrectAnswer = (TextView) findViewById(R.id.tvCorrectAnswer);
        tvNoOfQ = (TextView) findViewById(R.id.tvNoOfQ);
        ivGoBack = (ImageView) findViewById(R.id.ivGoBack);
        progressBar = (ProgressBar) findViewById(R.id.progressBar) ;
        op1 = (Button) findViewById(R.id.option1);
        op2 = (Button) findViewById(R.id.option2);
        op3 = (Button) findViewById(R.id.option3);
        op4 = (Button) findViewById(R.id.option4);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        level = getIntent().getStringExtra("difficulty");

        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestionsActivity.this, QuizHomeActivity.class));
            }
        });
        op1.setEnabled(false);
        op2.setEnabled(false);
        op3.setEnabled(false);
        op4.setEnabled(false);
        btnConfirm.setEnabled(false);
        DownloadTask task = new DownloadTask();
        task.execute("https://opentdb.com/api.php?amount=30&difficulty=" + level + "&type=multiple");

    }

    public void resetButtons(){
        op1.setBackgroundResource(R.drawable.etbackground);
        op2.setBackgroundResource(R.drawable.etbackground);
        op3.setBackgroundResource(R.drawable.etbackground);
        op4.setBackgroundResource(R.drawable.etbackground);

    }


    public void confirmAnswer(View view)
    {
        if(optionPressed==-1)
        {
            Toast.makeText(getApplicationContext(),"Choose one option",Toast.LENGTH_SHORT).show();
        }else {
            resetButtons();
            DownloadTask task = new DownloadTask();
            task.execute("https://opentdb.com/api.php?amount=30&difficulty=" + level + "&type=multiple");
        }
    }

    public void checkIfCorrect(String correctAnswerString){
        op1.setEnabled(false);
        op2.setEnabled(false);
        op3.setEnabled(false);
        op4.setEnabled(false);
        btnConfirm.setEnabled(false);
        switch (optionPressed) {
            case 1:
                optionClickedString = op1.getText().toString();
                break;
            case 2:
                optionClickedString = op2.getText().toString();
                break;
            case 3:
                optionClickedString = op3.getText().toString();
                break;
            case 4:
                optionClickedString = op4.getText().toString();
                break;
            default:
                optionClickedString = "";
                break;
        }
        if (!optionClickedString.isEmpty()) {
            if (optionClickedString == correctAnswerString) {
                Toast.makeText(getApplicationContext(), "Correct!!", Toast.LENGTH_SHORT).show();
                Log.i("correct", "correct");
                noOfCorrect++;
                tvScore.setText(String.valueOf(noOfCorrect));
                /*if(level.equals("easy"))
                    tvScore.setText(String.valueOf(score*10));
                else if(level.equals("medium"))
                    tvScore.setText(String.valueOf(score*15));
                else
                    tvScore.setText(String.valueOf(score*20));
*/

            } else {
                tvCorrectAnswer.setText(correctAnswerString);
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            }

        }
        resetButtons();
        //tvScore.setText(score);
        if(counter>9)
        {
            Toast.makeText(getApplicationContext(),"Game Over",Toast.LENGTH_SHORT);
            op1.setEnabled(false);
            op2.setEnabled(false);
            op3.setEnabled(false);
            op4.setEnabled(false);
            btnConfirm.setEnabled(true);
            btnConfirm.setText("Next");
            btnConfirm.setBackgroundColor(R.drawable.chosenanswerbg);
            tvScore.setBackgroundColor(R.drawable.finalscorebg);
            if(level.equals("easy"))
                score = noOfCorrect*10;
            else if(level.equals("medium"))
                score = noOfCorrect*15;
            else
                score = noOfCorrect*20;
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(QuestionsActivity.this,FinalScreenActivity.class);
                    intent.putExtra("score",score);
                    intent.putExtra("noCorrect",noOfCorrect);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });


        }else {
            DownloadTask task = new DownloadTask();
            task.execute("https://opentdb.com/api.php?amount=30&difficulty=" + level + "&type=multiple");
        }
    }



    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            progressBar.setProgress(10);
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            Log.i("button pressed", String.valueOf(optionPressed));
            try{
                progressBar.setProgress(30);
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                progressBar.setProgress(70);
                while (data!=-1)
                {
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }

                return result;

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Sorry! an error occured in first block",Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                resetButtons();
                counter++;
                tvNoOfQ.setText(String.valueOf(counter));
                JSONObject jsonObject = new JSONObject(s);
                String resultString = jsonObject.getString("results");
                JSONArray arr = new JSONArray(resultString);
                JSONObject jsonPart = arr.getJSONObject(counter);
                //questionString = URLEncoder.encode(jsonPart.getString("question"),);
                questionString = jsonPart.getString("question");
                questionString = questionString.replace("&quot;","\"");
                questionString = questionString.replace("&#039;","\'");
                questionString = questionString.replace("&rdquo;", "\"");
                questionString = questionString.replace("&rsquo;", "\"");

                correctAnswerString = jsonPart.getString("correct_answer");

                String incorrectString = jsonPart.getString("incorrect_answers");
                JSONArray arrIncorrect = new JSONArray(incorrectString);

                option2String = (String) arrIncorrect.get(0);
                option3String = (String) arrIncorrect.get(1);
                option4String = (String) arrIncorrect.get(2);
                ArrayList<String> answers = new ArrayList<String>();
                if(!(questionString.equals("") || correctAnswerString.equals("") || option2String.equals("") || option3String.equals("") || option4String.equals("") )) {
                    tvQuestion.setText(questionString);
                    Random rand = new Random();
                    //int locationOfCorrectAnswer = rand.nextInt(4);

                    answers.add(correctAnswerString);
                    answers.add(option2String);
                    answers.add(option3String);
                    answers.add(option4String);
                    Collections.shuffle(answers);
                    //Log.i("correct",correctAnswerString);
                    progressBar.setProgress(100);
                    tvCorrectAnswer.setText("");
                    op1.setText(answers.get(0));
                    op2.setText(answers.get(1));
                    op3.setText(answers.get(2));
                    op4.setText(answers.get(3));
                    Log.i("entered", "options");
                    Log.i("correct Answer", correctAnswerString);
                    op1.setEnabled(true);
                    op2.setEnabled(true);
                    op3.setEnabled(true);
                    op4.setEnabled(true);
                    btnConfirm.setEnabled(true);
                    progressBar.setProgress(0);
                    op1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resetButtons();
                            optionPressed = 1;
                            op1.setBackgroundColor(R.drawable.chosenanswerbg);
                        }
                    });
                    op2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resetButtons();
                            optionPressed = 2;
                            op2.setBackgroundColor(R.drawable.chosenanswerbg);
                        }
                    });
                    op3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resetButtons();
                            optionPressed = 3;
                            op3.setBackgroundColor(R.drawable.chosenanswerbg);
                        }
                    });
                    op4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resetButtons();
                            optionPressed = 4;
                            op4.setBackgroundColor(R.drawable.chosenanswerbg);
                        }
                    });
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(optionPressed!=-1){
                                checkIfCorrect(correctAnswerString);
                            }else{
                                Toast.makeText(getApplicationContext(),"Choose one option",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //Log.i("option pressed", String.valueOf(optionPressed));
                   // Log.i("option clicked", optionClickedString);
                    optionPressed = -1;

                }

            }catch (Exception e){
               // Log.i("error",e.getMessage());
                //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


}
