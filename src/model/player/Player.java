package model.player;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final Collection<Move> validMoves;

    protected Player(Board board, Collection<Move> validMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.validMoves = validMoves;
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract PlayerColor getAlly();

    public abstract Player getEnemy();

    public boolean isMoveValid (final Move move) {
        return this.validMoves.contains(move);
    }

    public boolean isDenPenetrated() {
        return false;
    }

    public MoveTransition makeMove (final Move move) {
        return null;
    }
}
