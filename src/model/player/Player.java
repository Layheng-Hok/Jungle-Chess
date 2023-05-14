package model.player;

import model.board.*;
import model.piece.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final Collection<Move> validMoves;

    Player(Board board, Collection<Move> validMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.validMoves = validMoves;
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract PlayerColor getAllyColor();

    public abstract Player getEnemyPlayer();

    public boolean isMoveValid (final Move move) {
        return this.validMoves.contains(move);
    }

    public abstract boolean isDenPenetrated();

    public MoveTransition makeMove (final Move move) {
        if (!isMoveValid(move)) {
            return new MoveTransition(this.board, move, MoveStatus.INVALID_MOVE);
        }
        final Board transitionBoard = move.execute();
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public Collection<Move> getValidMoves() {
        return this.validMoves;
    }
}
