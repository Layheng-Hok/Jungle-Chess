import model.board.Board;
import view.GameFrame;

public class ChessGame {
    public static void main(String[] args) {
        Board board = Board.constructStandardBoard();
        System.out.println(board);
        GameFrame table = new GameFrame();
    }
}