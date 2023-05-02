package model.piece.animal;

import model.piece.SpecialPiece;
import model.player.PlayerColor;

public class Lion extends SpecialPiece {
    public Lion(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.LION.ordinal());
    }
}
