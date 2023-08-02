package model.player;

import model.board.*;
import model.piece.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected Collection<Move> validMoves;

    Player(Board board, Collection<Move> validMoves) {
        this.board = board;
        this.validMoves = validMoves;
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract PlayerColor getAllyColor();

    public abstract Player getEnemyPlayer();

    public boolean isMoveValid(final Move move) {
        return this.validMoves.contains(move);
    }

    public abstract boolean isDenPenetrated();

    public MoveTransition makeMove(final Move move) {
        if (!isMoveValid(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.INVALID_MOVE);
        }
        final Board transitionBoard = move.execute();
        return new MoveTransition(this.board, transitionBoard, move, MoveStatus.DONE);
    }

    public MoveTransition unmakeMove (final Move move) {
        return new MoveTransition(this.board, move.undo(), move, MoveStatus.DONE);
    }

    public Collection<Move> getValidMoves() {
        return this.validMoves;
    }

    public void setValidMoves(Collection<Move> validMoves) {
        this.validMoves = validMoves;
    }
}
