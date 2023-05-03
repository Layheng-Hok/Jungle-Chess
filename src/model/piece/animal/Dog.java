package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Dog extends CommonPiece {
    public Dog(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.DOG, pieceCoordinate, pieceColor, Animal.DOG.ordinal());
    }

    @Override
    public String toString() {
        return Animal.DOG.toString();
    }

    @Override
    public String getPieceRank() {
        return Animal.DOG.getPieceRank();
    }
}
