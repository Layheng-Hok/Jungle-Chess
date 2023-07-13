package model.piece.animal;

import model.board.Move;
import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Cat extends CommonPiece {
    public Cat(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal(), true);
    }

    public Cat(final int pieceCoordinate, final PlayerColor pieceColor, final boolean isFirstMove) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal(), isFirstMove);
    }

    @Override
    public String toString() {
        return Animal.CAT.toString();
    }

    @Override
    public Cat movePiece(Move move) {
        return new Cat(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }
}
