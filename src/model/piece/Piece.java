package model.piece;

import model.board.Board;
import model.board.Move;
import model.player.PlayerColor;

import java.util.Collection;

public abstract class Piece {
    protected final int pieceCoordinate;
    protected final PlayerColor pieceColor;

    protected Piece(final int pieceCoordinate, final PlayerColor pieceColor) {
        this.pieceCoordinate = pieceCoordinate;
        this.pieceColor = pieceColor;
    }

    public abstract Collection<Move> determineValidMoves(final Board board);

    public PlayerColor getPieceColor() {
        return this.pieceColor;
    }
}
