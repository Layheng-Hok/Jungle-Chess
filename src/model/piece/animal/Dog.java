package model.piece.animal;

import model.board.Move;
import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Dog extends CommonPiece {
    public Dog(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.DOG, pieceCoordinate, pieceColor, Animal.DOG.ordinal(), true);
    }

    public Dog(final int pieceCoordinate, final PlayerColor pieceColor, final boolean isFirstMove) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal(), isFirstMove);
    }

    @Override
    public String toString() {
        return Animal.DOG.toString();
    }

    @Override
    public Dog movePiece(Move move) {
        return new Dog(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String getPieceRank() {
        return Animal.DOG.getPieceRank();
    }
}
