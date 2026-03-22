package com.example.ultimatetictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    AutoCompleteTextView etPlayer1, etPlayer2;
    RadioGroup rgFirstPlayer;
    Spinner spinnerSymbol;
    Button btnStartGame, btnHowToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // ── BIND VIEWS ──
        etPlayer1 = findViewById(R.id.etPlayer1);
        etPlayer2 = findViewById(R.id.etPlayer2);
        rgFirstPlayer = findViewById(R.id.rgFirstPlayer);
        spinnerSymbol = findViewById(R.id.spinnerSymbol);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnHowToPlay = findViewById(R.id.btnHowToPlay);

        // ── AUTOCOMPLETE FOR NAMES ──
        String[] nameSuggestions = {"Player 1", "Player 2", "Guest"};
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, nameSuggestions);
        etPlayer1.setAdapter(nameAdapter);
        etPlayer2.setAdapter(nameAdapter);

        // ── SYMBOL SET SPINNER ──
        String[] symbols = {"X & O  (Classic)", "★ & ●  (Stars)", "▲ & ■  (Shapes)"};
        ArrayAdapter<String> symbolAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, symbols);
        symbolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSymbol.setAdapter(symbolAdapter);

        // ── START GAME BUTTON ──
        btnStartGame.setOnClickListener(v -> {

            String player1 = etPlayer1.getText().toString().trim();
            String player2 = etPlayer2.getText().toString().trim();

            // Validation
            if (player1.isEmpty()) {
                etPlayer1.setError("Enter Player 1 name");
                return;
            }
            if (player2.isEmpty()) {
                etPlayer2.setError("Enter Player 2 name");
                return;
            }
            if (player1.equalsIgnoreCase(player2)) {
                etPlayer2.setError("Names must be different");
                return;
            }

            // Who goes first
            int selectedId = rgFirstPlayer.getCheckedRadioButtonId();
            String firstPlayer;
            if (selectedId == R.id.rbPlayer1First) {
                firstPlayer = player1;
            } else if (selectedId == R.id.rbPlayer2First) {
                firstPlayer = player2;
            } else {
                // Coin flip
                firstPlayer = (Math.random() < 0.5) ? player1 : player2;
                Toast.makeText(this, "🪙 " + firstPlayer + " goes first!", Toast.LENGTH_SHORT).show();
            }

            // Symbol set
            int symbolIndex = spinnerSymbol.getSelectedItemPosition();
            String p1Symbol, p2Symbol;
            if (symbolIndex == 1) {
                p1Symbol = "★"; p2Symbol = "●";
            } else if (symbolIndex == 2) {
                p1Symbol = "▲"; p2Symbol = "■";
            } else {
                p1Symbol = "X"; p2Symbol = "O";
            }

            // Pass data to GameActivity
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("player1", player1);
            intent.putExtra("player2", player2);
            intent.putExtra("firstPlayer", firstPlayer);
            intent.putExtra("p1Symbol", p1Symbol);
            intent.putExtra("p2Symbol", p2Symbol);
            startActivity(intent);
        });

        // ── HOW TO PLAY BUTTON ──
        btnHowToPlay.setOnClickListener(v -> {
            Intent intent = new Intent(this, HowToPlayActivity.class);
            startActivity(intent);
        });
    }
}