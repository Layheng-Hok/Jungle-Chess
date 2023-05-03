package model.player;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;

import java.util.Collection;

public class BluePlayer extends Player{
    public BluePlayer(Board board, Collection<Move> blueStandardValidMoves, Collection<Move> redStandardValidMoves) {
        super(board, blueStandardValidMoves, redStandardValidMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBluePieces();
    }

    @Override
    public PlayerColor getAlly() {
        return PlayerColor.BLUE;
    }

    @Override
    public Player getEnemy() {
        return this.board.redPlayer();
    }
}
