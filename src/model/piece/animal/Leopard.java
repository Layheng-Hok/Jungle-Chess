package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Leopard extends CommonPiece {
    Leopard(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.LEOPARD.ordinal());
    }
}
