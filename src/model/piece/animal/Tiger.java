package model.piece.animal;

import model.board.Move;
import model.piece.SpecialPiece;
import model.player.PlayerColor;

public class Tiger extends SpecialPiece {
    public Tiger(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.TIGER, pieceCoordinate, pieceColor, Animal.TIGER.ordinal(), true);
    }

    public Tiger(final int pieceCoordinate, final PlayerColor pieceColor, final boolean isFirstMove) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal(), isFirstMove);
    }

    @Override
    public String toString() {
        return Animal.TIGER.toString();
    }

    @Override
    public Tiger movePiece(Move move) {
        return new Tiger(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }
}
