package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Leopard extends CommonPiece {
    public Leopard(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.LEOPARD.ordinal());
    }

    @Override
    public String toString() {
        return AnimalRank.LEOPARD.toString();
    }

    @Override
    public String getPieceRank() {
        return AnimalRank.LEOPARD.getPieceRank();
    }
}
