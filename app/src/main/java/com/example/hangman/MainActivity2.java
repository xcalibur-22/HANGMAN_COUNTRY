package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity2 extends AppCompatActivity {
    public static String select="";
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    final String[] diff = new String[1];
    Button button;

    void change(String s){
        diff[0] = s;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button=findViewById(R.id.button);
        textInputLayout=findViewById(R.id.drop_menu);
        autoCompleteTextView=findViewById(R.id.drop_items);
        String[] items={"EASY","MEDIUM","HARD"};

        diff[0] ="EASY";
        ArrayAdapter<String> itemAdapter=new ArrayAdapter<>(MainActivity2.this,R.layout.items_list,items);
        autoCompleteTextView.setAdapter(itemAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                diff[0] =(String)adapterView.getItemAtPosition(i);
                Log.d("choice1", diff[0]);

            }
        });
        Log.d("choice2", diff[0]);
    }
    public void onClick(View view) {
        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra(select, diff[0]);
        startActivity(intent);

    }
}