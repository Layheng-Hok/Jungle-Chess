package model.piece.animal;

import model.piece.SpecialPiece;
import model.player.PlayerColor;

public class Tiger extends SpecialPiece {
    public Tiger(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.TIGER.ordinal());
    }
}
