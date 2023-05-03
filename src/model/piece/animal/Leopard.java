package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Leopard extends CommonPiece {
    public Leopard(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.LEOPARD, pieceCoordinate, pieceColor, Animal.LEOPARD.ordinal());
    }

    @Override
    public String toString() {
        return Animal.LEOPARD.toString();
    }

    @Override
    public String getPieceRank() {
        return Animal.LEOPARD.getPieceRank();
    }
}
