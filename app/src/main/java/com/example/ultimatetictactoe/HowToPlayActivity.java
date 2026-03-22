package com.example.ultimatetictactoe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class HowToPlayActivity extends AppCompatActivity {

    ScrollView scrollView;
    Spinner spinnerSection;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        scrollView = findViewById(R.id.scrollView);
        spinnerSection = findViewById(R.id.spinnerSection);
        btnBack = findViewById(R.id.btnBack);

        // ── SECTION JUMP SPINNER ──
        String[] sections = {"Basic Rules", "The Send Rule", "Winning", "Strategy Tips"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sections);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSection.setAdapter(adapter);

        // ── SCROLL TO SECTION ON SPINNER SELECT ──
        spinnerSection.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                int[] sectionIds = {
                        R.id.sectionBasic,
                        R.id.sectionSendRule,
                        R.id.sectionWinning,
                        R.id.sectionStrategy
                };

                findViewById(sectionIds[position]).post(() -> {
                    int scrollTo = findViewById(sectionIds[position]).getTop();
                    scrollView.smoothScrollTo(0, scrollTo);
                });
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // ── BACK BUTTON ──
        btnBack.setOnClickListener(v -> finish());
    }
}