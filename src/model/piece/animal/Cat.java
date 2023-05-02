package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Cat extends CommonPiece {
    public Cat(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.CAT.ordinal());
    }

    @Override
    public String toString() {
        return AnimalRank.CAT.toString();
    }

    @Override
    public String getPieceRank() {
        return AnimalRank.CAT.getPieceRank();
    }
}
