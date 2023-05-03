package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Wolf extends CommonPiece {
    public Wolf(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.WOLF, pieceCoordinate, pieceColor, Animal.WOLF.ordinal());
    }

    @Override
    public String toString() {
        return Animal.WOLF.toString();
    }

    @Override
    public String getPieceRank() {
        return Animal.WOLF.getPieceRank();
    }
}
