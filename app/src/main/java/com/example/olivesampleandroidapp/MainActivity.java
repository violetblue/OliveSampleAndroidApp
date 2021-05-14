package com.example.olivesampleandroidapp;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sarnava.textwriter.TextWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextWriter textWriter;

        textWriter = findViewById(R.id.textWriter);

        textWriter
                .setWidth(12)
                .setDelay(30)
                .setColor(Color.GREEN)
                .setConfig(TextWriter.Configuration.INTERMEDIATE)
                .setSizeFactor(30f)
                .setLetterSpacing(25f)
                .setText("HELLO OLIVE")
                .setListener(new TextWriter.Listener() {
                    @Override
                    public void WritingFinished() {

                        //do stuff after animation is finished
                    }
                })
                .startAnimation();
    }
}
