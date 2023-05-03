package model.piece.animal;

import model.board.Move;
import model.piece.CommonPiece;
import model.piece.Piece;
import model.player.PlayerColor;

public class Cat extends CommonPiece {
    public Cat(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal());
    }

    @Override
    public String toString() {
        return Animal.CAT.toString();
    }

    @Override
    public Cat movePiece(Move move) {
        return new Cat(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String getPieceRank() {
        return Animal.CAT.getPieceRank();
    }
}
