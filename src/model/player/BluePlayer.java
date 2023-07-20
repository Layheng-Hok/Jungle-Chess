package model.player;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;

import java.util.Collection;

public class BluePlayer extends Player {
    public BluePlayer(final Board board, final Collection<Move> blueStandardValidMoves, final Collection<Move> redStandardValidMoves) {
        super(board, blueStandardValidMoves);
    }
    @Override
    public String toString() {
        return "Blue";
    }

    @Override
    public boolean isDenPenetrated() {
        boolean isDenPenetrated = false;
        for (Piece piece : this.getEnemyPlayer().getActivePieces()) {
            if (piece.getPieceCoordinate() == 59) {
                isDenPenetrated = true;
                break;
            }
        }
        return isDenPenetrated;
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBluePieces();
    }

    @Override
    public PlayerColor getAllyColor() {
        return PlayerColor.BLUE;
    }

    @Override
    public Player getEnemyPlayer() {
        return this.board.redPlayer();
    }
}
