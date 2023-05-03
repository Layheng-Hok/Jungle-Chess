package model.player;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;

import java.util.Collection;

public class RedPlayer extends Player{
    public RedPlayer(Board board, Collection<Move> blueStandardValidMoves, Collection<Move> redStandardValidMoves) {
        super(board, redStandardValidMoves, blueStandardValidMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getRedPieces();
    }

    @Override
    public PlayerColor getAlly() {
        return PlayerColor.RED;
    }

    @Override
    public Player getEnemy() {
        return this.board.bluePlayer();
    }
}
