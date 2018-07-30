package com.sarel.minesweeper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.Random;


public class Controller {
    public GridPane board;
    int height = 9;
    int width =9;
    int mineNumber=10;
    private GamePiece [][] boardMatrix;
    public void initialize(){
        resetBoard();
    }
    class GamePiece extends Button{
        boolean isMine=false;
        boolean revealed=false;
        int row;
        int column;

        GamePiece(int column,int row){
            this.row=row;
            this.column=column;
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setRevealed();
                    if (isMine) {
                        gameEnd(false);
                    }else if(isGameEnd()){
                        gameEnd(true);
                    }
                }
            });
        }
        void setRevealed(){
            if(!isMine) {
                int numberOfMinesNeighbors =getNumberOfMinesNeighbors(column,row);
                setText(String.valueOf(numberOfMinesNeighbors));
                if(numberOfMinesNeighbors==0&&!revealed){
                    revealed=true;
                    revealedNeighbors();
                }else {
                    revealed=true;
                }
            }else {
                setStyle("-fx-background-color:red;");
                revealed=true;
            }
        }
        void revealedNeighbors(){
            for(int i=column-1;i<=column+1;i++){
                for(int j=row-1;j<=row+1;j++){
                    if(!(i==column&&j==row)){
                        GamePiece gamePiece=getNeighbor(i,j);
                        if(gamePiece!=null&&!gamePiece.revealed){
                            gamePiece.setRevealed();
                        }
                    }
                }
            }
        }

    }
    public void revealedAll() {
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
               if(!boardMatrix[i][j].revealed){
                   boardMatrix[i][j].setRevealed();
               }
            }
        }
    }
    public int getNumberOfMinesNeighbors(int column,int row){
        int minesNeighbors = 0;
        for(int i=column-1;i<=column+1;i++){
            for(int j=row-1;j<=row+1;j++){
                if(!(i==column&&j==row)){
                    GamePiece gamePiece=getNeighbor(i,j);
                    if(gamePiece!=null&&gamePiece.isMine){
                        minesNeighbors++;
                    }
                }
            }
        }
        return minesNeighbors;
    }
    public boolean isGameEnd(){
        boolean end=true;
        for (int i=0;i<width;i++){
            for (int j=0;j<height;j++){
                if(!boardMatrix[i][j].isMine&&!boardMatrix[i][j].revealed){
                    end=false;
                }
            }
        }
        return end;
    }
    public void gameEnd(boolean success){
        revealedAll();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GameOver");
        if(success){
            alert.setHeaderText("Success!");
            alert.setContentText("");
        }else {
            alert.setHeaderText("fail!");
            alert.setContentText("");
        }
        alert.showAndWait();
        resetBoard();
    }
    public void resetBoard(){
        boardMatrix = new GamePiece[width][height];
        board.getChildren().clear();
        for (int i=0;i<width;i++){
            for (int j=0;j<height;j++){
                GamePiece gamePiece = new GamePiece(i,j);
                gamePiece.setMaxWidth(25);
                gamePiece.setMaxHeight(25);
                gamePiece.setMinWidth(25);
                gamePiece.setMinHeight(25);
                boardMatrix[i][j]=gamePiece;
                board.add(gamePiece,i,j,1,1);
            }
        }
        int _mineNumber=mineNumber;
        while (_mineNumber>0){
            Random r = new Random();
            int minePlaceColumn = r.nextInt(width);
            int minePlaceRow    = r.nextInt(height);
            if(!boardMatrix[minePlaceColumn][minePlaceRow].isMine){
                boardMatrix[minePlaceColumn][minePlaceRow].isMine=true;
                _mineNumber--;
            }
        }
    }
    public GamePiece getNeighbor(int column,int row){
        GamePiece neighbor = null;
        if((row<0||column<0)||(row>=height||column>=width)){
            return null;
        }
        neighbor = boardMatrix[column][row];
        return neighbor;
    }
}
