package model.piece.animal;

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
    public String getPieceRank() {
        return Animal.LION.getPieceRank();
    }
}
