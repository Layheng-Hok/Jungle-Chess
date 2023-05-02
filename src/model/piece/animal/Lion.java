package model.piece.animal;

import model.piece.SpecialPiece;
import model.player.PlayerColor;

public class Lion extends SpecialPiece {
    public Lion(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.LION.ordinal());
    }

    @Override
    public String toString() {
        return AnimalRank.LION.toString();
    }

    @Override
    public String getPieceRank() {
        return AnimalRank.LION.getPieceRank();
    }
}
