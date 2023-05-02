package model.piece;

import model.board.Board;
import model.board.Move;
import model.player.PlayerColor;

import java.util.Collection;

public abstract class Piece {
    protected final int pieceCoordinate;
    protected final PlayerColor pieceColor;
    protected final int attackPieceRank;
    protected int defensePieceRank;

    protected Piece(final int pieceCoordinate, final PlayerColor pieceColor, final int attackPieceRank) {
        this.pieceCoordinate = pieceCoordinate;
        this.pieceColor = pieceColor;
        this.attackPieceRank = attackPieceRank;
        this.defensePieceRank = attackPieceRank;
    }

    public abstract Collection<Move> determineValidMoves(final Board board);

    public PlayerColor getPieceColor() {
        return this.pieceColor;
    }

    public int getDefensePieceRank() {
        return this.defensePieceRank;
    }
}
