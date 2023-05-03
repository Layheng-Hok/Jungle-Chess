package model.piece.animal;

import model.piece.SpecialPiece;
import model.player.PlayerColor;

public class Tiger extends SpecialPiece {
    public Tiger(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.TIGER, pieceCoordinate, pieceColor, Animal.TIGER.ordinal());
    }

    @Override
    public String toString() {
        return Animal.TIGER.toString();
    }

    @Override
    public String getPieceRank() {
        return Animal.TIGER.getPieceRank();
    }
}
