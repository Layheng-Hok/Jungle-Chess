package model.piece;

import model.board.Board;
import model.board.Move;
import model.piece.animal.Animal;
import model.player.PlayerColor;

import java.util.Collection;

public abstract class Piece {
    protected final Animal pieceType;
    protected final int pieceCoordinate;
    protected final PlayerColor pieceColor;
    protected final int pieceAttackRank;
    protected int pieceDefenseRank;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    protected Piece(final Animal pieceType, final int pieceCoordinate, final PlayerColor pieceColor, final int attackPieceRank, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.pieceCoordinate = pieceCoordinate;
        this.pieceColor = pieceColor;
        this.pieceAttackRank = attackPieceRank;
        this.pieceDefenseRank = attackPieceRank;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return this.pieceType == otherPiece.getPieceType()
                && this.pieceColor == otherPiece.getPieceColor()
                && this.pieceCoordinate == otherPiece.getPieceCoordinate()
                && this.isFirstMove == otherPiece.isFirstMove();
    }

    public boolean equalsTypeTwo(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return this.pieceType == otherPiece.getPieceType()
                && this.pieceColor == otherPiece.getPieceColor();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    private int computeHashCode() {
        int result = this.pieceType.hashCode();
        result = 31 * result + this.pieceColor.hashCode();
        result = 31 * result + this.pieceCoordinate;
        result = 31 * result + (this.isFirstMove ? 1 : 0);
        return result;
    }

    public abstract Collection<Move> determineValidMoves(final Board board);

    public abstract Piece movePiece(final Move move);

    public abstract int positionDevelopmentScore();

    public Animal getPieceType() {
        return this.pieceType;
    }

    public int getPieceCoordinate() {
        return this.pieceCoordinate;
    }

    public PlayerColor getPieceColor() {
        return this.pieceColor;
    }

    public int getPieceAttackRank() {
        return this.pieceAttackRank;
    }

    public int getPieceDefenseRank() {
        return this.pieceDefenseRank;
    }

    public void setPieceDefenseRank(int pieceDefenseRank) {
        this.pieceDefenseRank = pieceDefenseRank;
    }

    public int getPiecePower() {
        return this.pieceType.getPiecePower();
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }
}
