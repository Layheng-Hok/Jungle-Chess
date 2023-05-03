package model.piece.animal;

import model.board.Move;
import model.piece.Piece;
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
    public Tiger movePiece(Move move) {
        return new Tiger(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String getPieceRank() {
        return Animal.TIGER.getPieceRank();
    }
}
