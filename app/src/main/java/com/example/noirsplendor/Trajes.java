package com.example.noirsplendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Trajes extends AppCompatActivity {

    Button next, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajes);

        next = (Button) findViewById(R.id.buttonNext);
        back = (Button) findViewById(R.id.buttonBack);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = new Intent(Trajes.this, Zapatos.class);
                startActivity(miIntent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = new Intent(Trajes.this, Corbatas.class);
                startActivity(miIntent);
            }
        });
    }
}