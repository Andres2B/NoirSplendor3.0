package com.example.noirsplendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Zapatos extends AppCompatActivity {

    Button next, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zapatos);

        back = (Button) findViewById(R.id.buttonBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = new Intent(Zapatos.this, Trajes.class);
                startActivity(miIntent);
            }
        });
    }
}