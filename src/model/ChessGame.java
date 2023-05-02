package model;

import model.board.Board;

public class ChessGame {
    public static void main(String[] args) {
        Board board = Board.constructStandardBoard();
        System.out.println(board);
    }
}
