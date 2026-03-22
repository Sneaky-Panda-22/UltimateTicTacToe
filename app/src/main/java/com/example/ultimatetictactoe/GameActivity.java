package com.example.ultimatetictactoe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    String player1, player2, currentPlayer, p1Symbol, p2Symbol;

    int[][] boardState = new int[9][9];
    int[] bigBoard = new int[9];
    int activeBoard = -1;

    View[] miniBoardViews = new View[9];
    TextView[][] cells = new TextView[9][9];
    TextView[] winOverlays = new TextView[9];

    TextView tvPlayer1Name, tvPlayer2Name, tvCurrentTurn, tvSendRule;
    TextView tvPlayer1Score, tvPlayer2Score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        player1 = getIntent().getStringExtra("player1");
        player2 = getIntent().getStringExtra("player2");
        currentPlayer = getIntent().getStringExtra("firstPlayer");
        p1Symbol = getIntent().getStringExtra("p1Symbol");
        p2Symbol = getIntent().getStringExtra("p2Symbol");

        tvPlayer1Name = findViewById(R.id.tvPlayer1Name);
        tvPlayer2Name = findViewById(R.id.tvPlayer2Name);
        tvPlayer1Score = findViewById(R.id.tvPlayer1Score);
        tvPlayer2Score = findViewById(R.id.tvPlayer2Score);
        tvCurrentTurn = findViewById(R.id.tvCurrentTurn);
        tvSendRule = findViewById(R.id.tvSendRule);

        tvPlayer1Name.setText(player1);
        tvPlayer2Name.setText(player2);
        tvPlayer1Score.setText("Boards: 0");
        tvPlayer2Score.setText("Boards: 0");

        updateTurnDisplay();

        int[] boardIds = {
                R.id.board_00, R.id.board_01, R.id.board_02,
                R.id.board_10, R.id.board_11, R.id.board_12,
                R.id.board_20, R.id.board_21, R.id.board_22
        };

        int[] cellIds = {
                R.id.cell_00, R.id.cell_01, R.id.cell_02,
                R.id.cell_10, R.id.cell_11, R.id.cell_12,
                R.id.cell_20, R.id.cell_21, R.id.cell_22
        };

        for (int b = 0; b < 9; b++) {
            miniBoardViews[b] = findViewById(boardIds[b]);
            winOverlays[b] = miniBoardViews[b].findViewById(R.id.tvWinOverlay);

            final int boardIndex = b;
            for (int c = 0; c < 9; c++) {
                final int cellIndex = c;
                cells[b][c] = (TextView) miniBoardViews[b].findViewById(cellIds[c]);
                cells[b][c].setOnClickListener(v -> handleCellClick(boardIndex, cellIndex));
            }
        }

        highlightActiveBoard();
    }

    void handleCellClick(int boardIndex, int cellIndex) {

        if (activeBoard != -1 && activeBoard != boardIndex) {
            Toast.makeText(this, "You must play in the highlighted board!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bigBoard[boardIndex] != 0) {
            Toast.makeText(this, "This board is already finished!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (boardState[boardIndex][cellIndex] != 0) {
            Toast.makeText(this, "Cell already taken!", Toast.LENGTH_SHORT).show();
            return;
        }

        int playerNum = currentPlayer.equals(player1) ? 1 : 2;
        String symbol = currentPlayer.equals(player1) ? p1Symbol : p2Symbol;

        boardState[boardIndex][cellIndex] = playerNum;
        cells[boardIndex][cellIndex].setText(symbol);
        cells[boardIndex][cellIndex].setClickable(false);

        int miniBoardResult = checkWinner(boardState[boardIndex]);
        if (miniBoardResult != 0) {
            bigBoard[boardIndex] = miniBoardResult;
            showMiniBoardWin(boardIndex, miniBoardResult);

            if (miniBoardResult == 1) {
                Toast.makeText(this, player1 + " claimed this board!", Toast.LENGTH_SHORT).show();
            } else if (miniBoardResult == 2) {
                Toast.makeText(this, player2 + " claimed this board!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "This board is a draw!", Toast.LENGTH_SHORT).show();
            }

            updateScores();

            int gameResult = checkWinner(bigBoard);
            if (gameResult != 0) {
                endGame(gameResult);
                return;
            }

            if (isBigBoardFull()) {
                endGame(3);
                return;
            }
        }

        if (bigBoard[cellIndex] != 0) {
            activeBoard = -1;
        } else {
            activeBoard = cellIndex;
        }

        currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
        updateTurnDisplay();
        highlightActiveBoard();
    }

    int checkWinner(int[] board) {
        int[][] lines = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        for (int[] line : lines) {
            if (board[line[0]] != 0 &&
                    board[line[0]] == board[line[1]] &&
                    board[line[1]] == board[line[2]]) {
                return board[line[0]];
            }
        }

        for (int cell : board) {
            if (cell == 0) return 0;
        }
        return 3;
    }

    void showMiniBoardWin(int boardIndex, int result) {
        winOverlays[boardIndex].setVisibility(View.VISIBLE);
        if (result == 1) {
            winOverlays[boardIndex].setText(p1Symbol);
            winOverlays[boardIndex].setBackgroundColor(0xCCDFF0D8);
        } else if (result == 2) {
            winOverlays[boardIndex].setText(p2Symbol);
            winOverlays[boardIndex].setBackgroundColor(0xCCF2DEDE);
        } else {
            winOverlays[boardIndex].setText("—");
            winOverlays[boardIndex].setBackgroundColor(0xCCEEEEEE);
        }

        for (int c = 0; c < 9; c++) {
            cells[boardIndex][c].setClickable(false);
        }
    }

    void updateScores() {
        int p1Score = 0, p2Score = 0;
        for (int b = 0; b < 9; b++) {
            if (bigBoard[b] == 1) p1Score++;
            else if (bigBoard[b] == 2) p2Score++;
        }
        tvPlayer1Score.setText("Boards: " + p1Score);
        tvPlayer2Score.setText("Boards: " + p2Score);
    }

    boolean isBigBoardFull() {
        for (int b = 0; b < 9; b++) {
            if (bigBoard[b] == 0) return false;
        }
        return true;
    }

    void highlightActiveBoard() {
        for (int b = 0; b < 9; b++) {
            if (bigBoard[b] != 0) continue;
            if (activeBoard == -1 || activeBoard == b) {
                miniBoardViews[b].setBackgroundResource(R.drawable.mini_board_active);
                for (int c = 0; c < 9; c++) {
                    if (boardState[b][c] == 0) {
                        cells[b][c].setClickable(true);
                    }
                }
            } else {
                miniBoardViews[b].setBackgroundResource(R.drawable.mini_board_background);
                for (int c = 0; c < 9; c++) {
                    cells[b][c].setClickable(false);
                }
            }
        }

        if (activeBoard == -1) {
            tvSendRule.setText("Play anywhere");
        } else {
            tvSendRule.setText("Play in the highlighted board");
        }
    }

    void updateTurnDisplay() {
        String symbol = currentPlayer.equals(player1) ? p1Symbol : p2Symbol;
        tvCurrentTurn.setText(symbol);
    }

    void endGame(int result) {
        String winner, winnerSymbol;

        if (result == 1) {
            winner = player1;
            winnerSymbol = p1Symbol;
        } else if (result == 2) {
            winner = player2;
            winnerSymbol = p2Symbol;
        } else {
            winner = "Draw";
            winnerSymbol = "—";
        }

        int p1Boards = 0, p2Boards = 0;
        for (int b = 0; b < 9; b++) {
            if (bigBoard[b] == 1) p1Boards++;
            else if (bigBoard[b] == 2) p2Boards++;
        }

        String message = result == 3
                ? "It's a draw! " + player1 + ": " + p1Boards + " boards, "
                + player2 + ": " + p2Boards + " boards."
                : winner + " wins! " + p1Boards + " vs " + p2Boards + " boards.";

        final String finalWinner = winner;
        final String finalWinnerSymbol = winnerSymbol;
        final int finalP1Boards = p1Boards;
        final int finalP2Boards = p2Boards;

        new AlertDialog.Builder(this)
                .setTitle(result == 3 ? "It's a Draw!" : "🏆 " + winner + " Wins!")
                .setMessage(message)
                .setPositiveButton("See Results", (dialog, which) -> {
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("winner", finalWinner);
                    intent.putExtra("winnerSymbol", finalWinnerSymbol);
                    intent.putExtra("player1", player1);
                    intent.putExtra("player2", player2);
                    intent.putExtra("p1Symbol", p1Symbol);
                    intent.putExtra("p2Symbol", p2Symbol);
                    intent.putExtra("p1Boards", finalP1Boards);
                    intent.putExtra("p2Boards", finalP2Boards);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Rematch", (dialog, which) -> restartGame())
                .setCancelable(false)
                .show();
    }

    void restartGame() {
        boardState = new int[9][9];
        bigBoard = new int[9];
        activeBoard = -1;
        currentPlayer = player1;

        for (int b = 0; b < 9; b++) {
            winOverlays[b].setVisibility(View.GONE);
            winOverlays[b].setText("");
            miniBoardViews[b].setBackgroundResource(R.drawable.mini_board_background);
            for (int c = 0; c < 9; c++) {
                cells[b][c].setText("");
                cells[b][c].setClickable(true);
                boardState[b][c] = 0;
            }
        }

        updateScores();
        updateTurnDisplay();
        tvSendRule.setText("Play anywhere");
        highlightActiveBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Restart");
        menu.add(0, 2, 1, "How to Play");
        menu.add(0, 3, 2, "Quit to Menu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            new AlertDialog.Builder(this)
                    .setTitle("Restart Game")
                    .setMessage("Are you sure you want to restart?")
                    .setPositiveButton("Restart", (dialog, which) -> restartGame())
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        } else if (id == 2) {
            startActivity(new Intent(this, HowToPlayActivity.class));
            return true;
        } else if (id == 3) {
            new AlertDialog.Builder(this)
                    .setTitle("Quit to Menu")
                    .setMessage("Are you sure? Current game will be lost.")
                    .setPositiveButton("Quit", (dialog, which) -> {
                        startActivity(new Intent(this, LaunchActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}