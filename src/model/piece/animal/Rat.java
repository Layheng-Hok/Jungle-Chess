package model.piece.animal;

import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.board.Terrain;
import model.piece.Piece;
import model.player.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static model.board.Move.*;

public class Rat extends Piece {
    private final int[] POTENTIAL_MOVE_COORDINATES = {-7, -1, 1, 7};

    public Rat(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(Animal.RAT, pieceCoordinate, pieceColor, Animal.RAT.ordinal(), true);
    }

    public Rat(final int pieceCoordinate, final PlayerColor pieceColor, final boolean isFirstMove) {
        super(Animal.CAT, pieceCoordinate, pieceColor, Animal.CAT.ordinal(), isFirstMove);
    }

    @Override
    public String toString() {
        return Animal.RAT.toString();
    }


    @Override
    public Rat movePiece(Move move) {
        return new Rat(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public Collection<Move> determineValidMoves(final Board board) {
        final List<Move> validMoves = new ArrayList<>();
        for (final int currentPotentialOffset : POTENTIAL_MOVE_COORDINATES) {
            int potentialDestinationCoordinate = this.pieceCoordinate + currentPotentialOffset;
            if (BoardUtils.isInBoundary(potentialDestinationCoordinate) && !BoardUtils.isDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                final Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new StandardMove(board, this, potentialDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceAtDestinationColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceAtDestinationColor && (pieceAtDestination.getPieceDefenseRank() == Animal.ELEPHANT.ordinal() ||
                            pieceAtDestination.getPieceDefenseRank() == Animal.RAT.ordinal()) ||
                            pieceAtDestination.getPieceDefenseRank() == Animal.TRAPPED_ANIMAL.ordinal()) {
                        if (BoardUtils.isLand(this.pieceCoordinate) && !BoardUtils.isRiver(potentialDestinationCoordinate) ||
                                BoardUtils.isRiver(this.pieceCoordinate) && BoardUtils.isRiver(potentialDestinationCoordinate)) {
                            validMoves.add(new CaptureMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
                        }
                    }

                }
            }
        }
        return Collections.unmodifiableList(validMoves);
    }

    private static boolean isColumnZeroExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardUtils.COLUMN_ZERO[currentCoordinate] && (potentialOffset == -1));
    }

    private static boolean isColumnSixExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardUtils.COLUMN_SIX[currentCoordinate] && (potentialOffset == 1));
    }
}
