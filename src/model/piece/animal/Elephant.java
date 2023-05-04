package model.piece.animal;

import model.board.Move;
import model.piece.CommonPiece;
import model.piece.Piece;
import model.player.PlayerColor;

public class Elephant extends CommonPiece {
    public Elephant(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.ELEPHANT, pieceCoordinate, pieceColor, Animal.ELEPHANT.ordinal(), true);
    }

    public Elephant(final int pieceCoordinate, final PlayerColor pieceColor, final boolean isFirstMove) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal(), isFirstMove);
    }

    @Override
    public String toString() {
        return Animal.ELEPHANT.toString();
    }

    @Override
    public Elephant movePiece(Move move) {
        return new Elephant(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String getPieceRank() {
        return Animal.ELEPHANT.getPieceRank();
    }
}
