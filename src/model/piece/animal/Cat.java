package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Cat extends CommonPiece {
    public Cat(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal());
    }

    @Override
    public String toString() {
        return Animal.CAT.toString();
    }

    @Override
    public String getPieceRank() {
        return Animal.CAT.getPieceRank();
    }
}
