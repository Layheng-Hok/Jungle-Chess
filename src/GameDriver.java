import model.board.Board;
import view.GameFrame;

public class GameDriver {
    public static void main(String[] args) {
        Board board = Board.constructStandardBoard();
        System.out.println(board);
        GameFrame.get().show();
    }
}
