package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Cat extends CommonPiece {
    Cat(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.CAT.ordinal());
    }
}
