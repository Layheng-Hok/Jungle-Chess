package model.piece.animal;

import model.board.Move;
import model.piece.Piece;
import model.piece.SpecialPiece;
import model.player.PlayerColor;

public class Lion extends SpecialPiece {
    public Lion(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.LION, pieceCoordinate, pieceColor, Animal.LION.ordinal());
    }

    @Override
    public String toString() {
        return Animal.LION.toString();
    }

    @Override
    public Lion movePiece(Move move) {
        return new Lion(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String getPieceRank() {
        return Animal.LION.getPieceRank();
    }
}
