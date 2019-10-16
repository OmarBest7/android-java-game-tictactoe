package com.ceibalabs.connect3;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //ArrayList<ImageView> tokens = new ArrayList<ImageView>(9);
    boolean isTicTacToe; //true gato, false conecta4
    boolean gameIsActive;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2}; //2-unplayed, 0-yellow, 1-red
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    int activePlayer = 0; //0-yellow, 1-red
    LinearLayout playAgainLayout;
    TextView winnerMessage;
    TableLayout boardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.playAgainLayout = (LinearLayout) this.findViewById(R.id.playAgainLayout);
        this.winnerMessage = (TextView) this.findViewById(R.id.textViewGamestate);
        this.boardLayout = (TableLayout) this.findViewById(R.id.boardLayout);

        this.gameIsActive = true;
    }

    public void move(View view){
        ImageView token = (ImageView) view;
        //token.setTranslationY(-1000f);
        token.animate().translationYBy(1000f).rotation(360).setDuration(300);
    }

    public void DropIn(View view){
        ImageView counter = (ImageView) view; //to check which image has been selected
        int selectedSlot = Integer.parseInt(counter.getTag().toString());

        if(this.isSlotEmpty(selectedSlot)){
            if( !this.isGameOver() ){
                counter.setTranslationY(-1000f);
                this.gameState[selectedSlot] = this.activePlayer; //save in memory player that played that slot

                if(this.activePlayer == 0){
                    counter.setImageResource(R.drawable.yellow);
                    this.activePlayer = 1;
                } else {
                    counter.setImageResource(R.drawable.red);
                    this.activePlayer = 0;
                }
                counter.animate().translationYBy(1000f).rotation(360).setDuration(300);
                //this.printState();
                this.checkForWinner();
            }
        } else {
            this.sendToast("That slot has already been played");
        }

    }

    public void sendToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void checkForWinner(){
        boolean haveAWinner = false;
        String winner = "";

        for(int[] winningPosition : this.winningPositions){
            if(this.gameState[winningPosition[0]] == this.gameState[winningPosition[1]] &&
                    this.gameState[winningPosition[1]] == this.gameState[winningPosition[2]] &&
                    this.gameState[winningPosition[0]] != 2){

                this.gameIsActive = false;
                //check who won
                haveAWinner = true;
                winner = "Red";
                if (this.gameState[winningPosition[0]] == 0) {
                    winner = "Yellow";
                }
            }
        }

        if(haveAWinner){
            this.sendToast(winner + " has won.");
            this.showGameStatus("Game is over!");
        } else if (isGameOver()) {
            this.sendToast("It´s a draw");
            this.showGameStatus("Game is over!");
        }

    }

    public void showGameStatus(String message){
        this.playAgainLayout.setVisibility(View.VISIBLE);
        this.winnerMessage.setText(message);
    }

    public boolean isGameOver(){
        if(this.gameIsActive){
            for(int slot : this.gameState){
                if(slot == 2){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSlotEmpty(int i){
        if (this.gameState[i] == 2){
            return true;
        } else {
            return false;
        }
    }

    public void resetGame(View view){
        this.playAgainLayout.setVisibility(View.INVISIBLE);
        this.activePlayer = 0;
        for(int i=0; i < this.gameState.length; i++){
            this.gameState[i] = 2;
        }
        for(int i=0; i < this.boardLayout.getChildCount(); i++){
            TableRow tableRow = (TableRow) this.boardLayout.getChildAt(i);
            for(int j=0; j < tableRow.getChildCount(); j++){
                ImageView boardPlace = (ImageView) tableRow.getChildAt(j);
                boardPlace.setImageResource(0);
            }
        }
        this.gameIsActive = true;
    }

    public void printState(){
        System.out.println("Game STATE");
        for(int i=0; i<this.gameState.length; i++){
            System.out.print("["+ String.valueOf(this.gameState[i])+"] ");
            if((i+1)%3 == 0){
                System.out.print("\n");
            }
        }
        System.out.println("----");
    }
}
