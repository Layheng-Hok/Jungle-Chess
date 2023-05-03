package model.piece.animal;

import model.board.Move;
import model.piece.CommonPiece;
import model.piece.Piece;
import model.player.PlayerColor;

public class Wolf extends CommonPiece {
    public Wolf(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.WOLF, pieceCoordinate, pieceColor, Animal.WOLF.ordinal());
    }

    @Override
    public String toString() {
        return Animal.WOLF.toString();
    }

    @Override
    public Wolf movePiece(Move move) {
        return new Wolf(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String getPieceRank() {
        return Animal.WOLF.getPieceRank();
    }
}
