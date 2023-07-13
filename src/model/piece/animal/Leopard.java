package model.piece.animal;

import model.board.Move;
import model.piece.CommonPiece;
import model.player.PlayerColor;

public class Leopard extends CommonPiece {
    public Leopard(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.LEOPARD, pieceCoordinate, pieceColor, Animal.LEOPARD.ordinal(), true);
    }

    public Leopard(final int pieceCoordinate, final PlayerColor pieceColor, final boolean isFirstMove) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal(), isFirstMove);
    }

    @Override
    public String toString() {
        return Animal.LEOPARD.toString();
    }


    @Override
    public Leopard movePiece(Move move) {
        return new Leopard(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }
}
