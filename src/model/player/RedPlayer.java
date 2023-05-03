package model.player;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;

import java.util.Collection;

public class RedPlayer extends Player {
    public RedPlayer(final Board board, final Collection<Move> blueStandardValidMoves, final Collection<Move> redStandardValidMoves) {
        super(board, redStandardValidMoves, blueStandardValidMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getRedPieces();
    }

    @Override
    public PlayerColor getAllyColor() {
        return PlayerColor.RED;
    }

    @Override
    public Player getEnemyPlayer() {
        return this.board.bluePlayer();
    }
}
