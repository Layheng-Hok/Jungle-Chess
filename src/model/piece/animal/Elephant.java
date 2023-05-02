package model.piece.animal;

import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Elephant extends CommonPiece {
    public Elephant(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.ELEPHANT.ordinal());
    }

    @Override
    public String toString() {
        return AnimalRank.ELEPHANT.toString();
    }

    @Override
    public String getPieceRank() {
        return AnimalRank.ELEPHANT.getPieceRank();
    }
}
