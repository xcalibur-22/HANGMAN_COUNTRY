package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    //declaring variables
    TextView textView;
    Button reset;
    Dialog dialog;
    TextView tv;
    ImageView imageView;
    TextView display;
    String answer;
    String displayedText;
    char[] displayedArray;
    EditText input;
    TextView txtLettersUsed;
    String lettersUsed;
    final String message_letters_Use="Tried Letters: ";
    TextView txtLives;
    String lives;
    int livesLeft;
    int score;
    int hints;

    ArrayList<String> listOfWords;
    final String win_message="YOU HAVE WON";
    final String loss_message="YOU HAVE LOST";
    int[] gameId = {R.drawable.game0,R.drawable.game1,R.drawable.game2,R.drawable.game3,R.drawable.game4,R.drawable.game5,R.drawable.game6,R.drawable.game7};


    void revealLetter(char letter){
        int indexOfLetter=answer.indexOf(letter);
        //while loop to replace the underscore with the letter
        while(indexOfLetter>=0){
            displayedArray[indexOfLetter]=answer.charAt(indexOfLetter);
            indexOfLetter=answer.indexOf(letter,indexOfLetter+1);

        }
    }

    void displayOnScreen(){
        displayedText=String.valueOf(displayedArray);
        display.setText(displayedText);

        Log.d("won",Boolean.toString(displayedText.contains("_")));


    }

    void gameInitialize() {
        //shuffling of words
        hints=2;
        Collections.shuffle(listOfWords);
        answer=listOfWords.get(0);
        answer=answer.substring(0,answer.length()-1);
        answer=answer.toLowerCase(Locale.ROOT);
        score=0;
        Log.d("tag",answer);

        listOfWords.remove(0);
        //initialize character array
        displayedArray=answer.toCharArray();
        //replace with underscores
        for (int i = 0; i < answer.length() ; i++) {
           if(displayedArray[i]!=' ')
               displayedArray[i]='_';
           else
               displayedArray[i]='/';
        }
        displayedText=String.valueOf(displayedArray);

        displayOnScreen();

        input.setText("");
        imageView.setImageResource(gameId[0]);
        lettersUsed=" ";
        txtLettersUsed.setText(message_letters_Use);
        Intent intent =getIntent();
        String diff2=intent.getStringExtra(MainActivity2.select);

        Log.d("choice",diff2);
        if(diff2.equalsIgnoreCase("MEDIUM")){
            lives="\u2764 \u2764 \u2764 \u2764 \u2764 "; //hearts in unicode
            livesLeft=5;
        }
        else if(diff2.equalsIgnoreCase("HARD")){
            lives="\u2764 \u2764 \u2764 "; //hearts in unicode
            livesLeft=3;
        }
        else{
        lives="\u2764 \u2764 \u2764 \u2764 \u2764 \u2764 \u2764 "; //hearts in unicode
        livesLeft=7;}
        txtLives.setText(lives);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView=findViewById(R.id.textView);
        display = findViewById(R.id.display);
        input = findViewById(R.id.input);
        txtLettersUsed = findViewById(R.id.lettersUsed);
        txtLives=findViewById(R.id.livesLeft);
        imageView=findViewById(R.id.imageView2);
        listOfWords=new ArrayList<String>();
        reset=findViewById(R.id.button3);
        dialog=new Dialog(this);

        InputStream inputStream = null;
        Scanner sc = null;
        String newWord = "";
        try {
            inputStream = getAssets().open("words.txt");
            sc = new Scanner(inputStream);
            sc.useDelimiter("\n");
            while (sc.hasNext()) {
                newWord = sc.next();
                listOfWords.add(newWord);
//                Toast.makeText(this, newWord, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getClass().getSimpleName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(sc!= null){
                sc.close();
        }
        try {
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,e.getClass().getSimpleName()+ ": " +e.getMessage(),Toast.LENGTH_SHORT).show();
        }}
        gameInitialize();
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && score==0 ) {

                    checkIfPresent(charSequence.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    void checkIfPresent(char letter){
        input.setText("");
        //if letter is present in answer
        letter=Character.toLowerCase(letter);
        if(answer.indexOf(letter) >= 0 ){
            //if the letter was not displayed yet
            if(displayedText.indexOf(letter)<0){
                revealLetter(letter);
                displayOnScreen();
            }
            else
                Toast.makeText(this, "LETTER ALREADY USED", Toast.LENGTH_SHORT).show();
        }
        //if letter was not found
        else{
            //check if letter was not used
            if(lettersUsed.indexOf(letter)<0 ){
            decreaseLives();}
            else
                Toast.makeText(this, "LETTER ALREADY USED", Toast.LENGTH_SHORT).show();

        }
        //display the letter already used
        if(lettersUsed.indexOf(letter)<0){
            lettersUsed+=letter+", ";
            String str=message_letters_Use+lettersUsed;
            txtLettersUsed.setText(str);
        }
        if(!displayedText.contains("_")){
//            txtLettersUsed.setText(win_message);
            dialog.setContentView(R.layout.you_win);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            Toast.makeText(this, win_message, Toast.LENGTH_SHORT).show();
            score=1;
        }
        if(lives.isEmpty()){
            display.setText(answer);
            score=1;
//            txtLettersUsed.setText(loss_message);
            dialog.setContentView(R.layout.you_loose);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            Toast.makeText(this, loss_message+"\n COUNTRY IS "+answer.toUpperCase(Locale.ROOT), Toast.LENGTH_SHORT).show();
        }

    }
    void decreaseLives(){
        livesLeft--;
        lives=lives.substring(0,lives.length()-2);
        imageView.setImageResource(gameId[7-livesLeft]);
        txtLives.setText(lives);
//        Toast.makeText(this, "INCORRECT LETTER", Toast.LENGTH_SHORT).show();

    }
    public void resetGame(View view){
        //to reset the game
        dialog.dismiss();
        gameInitialize();

    }
    public void Hint(View view){
//        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        if(hints>0){
        hints--;
        int i;
//        int rand=new Random().nextInt(answer.length());
//        while(displayedText.charAt(rand)!='_' || displayedText.charAt(rand)=='/');
//        rand=new Random().nextInt(answer.length());
            for ( i = 0; i <answer.length() ; i++) {
                if(displayedText.charAt(i)=='_')
                    break;
            }
        revealLetter(answer.charAt(i));
        displayOnScreen();
        }
        else
            Toast.makeText(this, "NO MORE HINTS", Toast.LENGTH_SHORT).show();
    }

}