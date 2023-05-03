package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Elephant extends CommonPiece {
    public Elephant(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.ELEPHANT, pieceCoordinate, pieceColor, Animal.ELEPHANT.ordinal());
    }

    @Override
    public String toString() {
        return Animal.ELEPHANT.toString();
    }

    @Override
    public String getPieceRank() {
        return Animal.ELEPHANT.getPieceRank();
    }
}
