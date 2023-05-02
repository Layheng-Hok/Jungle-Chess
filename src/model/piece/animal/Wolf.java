package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Wolf extends CommonPiece {
    public Wolf(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.WOLF.ordinal());
    }
}
