package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Dog extends CommonPiece {
    Dog(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.DOG.ordinal());
    }
}
