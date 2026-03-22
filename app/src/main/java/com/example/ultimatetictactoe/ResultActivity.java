package com.example.ultimatetictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvWinnerName, tvWinnerSymbol, tvMatchSummary;
    RatingBar ratingMatch;
    CheckBox cbGoodGame, cbRematch, cbLucky;
    Button btnRematch, btnMainMenu;

    String winner, winnerSymbol, player1, player2, p1Symbol, p2Symbol;
    int p1Boards, p2Boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // ── GET INTENT DATA ──
        winner = getIntent().getStringExtra("winner");
        winnerSymbol = getIntent().getStringExtra("winnerSymbol");
        player1 = getIntent().getStringExtra("player1");
        player2 = getIntent().getStringExtra("player2");
        p1Symbol = getIntent().getStringExtra("p1Symbol");
        p2Symbol = getIntent().getStringExtra("p2Symbol");
        p1Boards = getIntent().getIntExtra("p1Boards", 0);
        p2Boards = getIntent().getIntExtra("p2Boards", 0);

        // ── BIND VIEWS ──
        tvWinnerName = findViewById(R.id.tvWinnerName);
        tvWinnerSymbol = findViewById(R.id.tvWinnerSymbol);
        tvMatchSummary = findViewById(R.id.tvMatchSummary);
        ratingMatch = findViewById(R.id.ratingMatch);
        cbGoodGame = findViewById(R.id.cbGoodGame);
        cbRematch = findViewById(R.id.cbRematch);
        cbLucky = findViewById(R.id.cbLucky);
        btnRematch = findViewById(R.id.btnRematch);
        btnMainMenu = findViewById(R.id.btnMainMenu);

        // ── SET WINNER INFO ──
        if (winner.equals("Draw")) {
            tvWinnerName.setText("It's a Draw!");
        } else {
            tvWinnerName.setText(winner + " Wins!");
        }
        tvWinnerSymbol.setText(winnerSymbol);

        // ── MATCH SUMMARY ──
        String summary = player1 + " (" + p1Symbol + ") won " + p1Boards + " board"
                + (p1Boards == 1 ? "" : "s") + ".\n"
                + player2 + " (" + p2Symbol + ") won " + p2Boards + " board"
                + (p2Boards == 1 ? "" : "s") + ".\n\n";

        if (winner.equals("Draw")) {
            summary += "Neither player completed 3 boards in a row. A hard-fought draw!";
        } else {
            summary += winner + " completed 3 mini boards in a row on the big board. Well played!";
        }
        tvMatchSummary.setText(summary);

        // ── RATING BAR ──
        ratingMatch.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (fromUser) {
                String[] reactions = {"", "Rough game 😬", "Could be better 😐",
                        "Decent match 👍", "Great game! 😄", "Epic match! 🔥"};
                int index = (int) rating;
                if (index > 0 && index < reactions.length) {
                    Toast.makeText(this, reactions[index], Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ── REMATCH BUTTON ──
        btnRematch.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("player1", player1);
            intent.putExtra("player2", player2);
            intent.putExtra("firstPlayer", player1);
            intent.putExtra("p1Symbol", p1Symbol);
            intent.putExtra("p2Symbol", p2Symbol);
            startActivity(intent);
            finish();
        });

        // ── MAIN MENU BUTTON ──
        btnMainMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, LaunchActivity.class));
            finish();
        });

        // ── POPUP MENU on Rematch button long press ──
        btnRematch.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add(0, 1, 0, "Share Result");
            popup.getMenu().add(0, 2, 1, "View Stats");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 1) {
                    // Share intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareText = winner.equals("Draw")
                            ? "We played Ultimate Tic-Tac-Toe and it ended in a draw! 🎮"
                            : winner + " won our Ultimate Tic-Tac-Toe match "
                            + p1Boards + "-" + p2Boards + "! 🏆";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    startActivity(Intent.createChooser(shareIntent, "Share Result"));
                } else if (item.getItemId() == 2) {
                    Toast.makeText(this,
                            player1 + ": " + p1Boards + " boards | "
                                    + player2 + ": " + p2Boards + " boards",
                            Toast.LENGTH_LONG).show();
                }
                return true;
            });
            popup.show();
            return true;
        });
    }
}