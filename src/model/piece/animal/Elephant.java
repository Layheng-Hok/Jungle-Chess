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

public class Elephant extends Piece {
    private final int[] POTENTIAL_MOVE_COORDINATES = {-7, -1, 1, 7};

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
    public int positionDevelopmentScore() {
        return this.pieceColor.elephantDevelopmentScore(this.pieceCoordinate);
    }

    @Override
    public Collection<Move> determineValidMoves(final Board board) {
        final List<Move> validMoves = new ArrayList<>();
        for (final int currentPotentialOffset : POTENTIAL_MOVE_COORDINATES) {
            int potentialDestinationCoordinate = this.pieceCoordinate + currentPotentialOffset;
            if (BoardUtils.isInBoundary(potentialDestinationCoordinate) && !BoardUtils.isRiverOrDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                final Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new Move.StandardMove(board, this, potentialDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceAtDestinationColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceAtDestinationColor && pieceAtDestination.getPieceDefenseRank() != Animal.RAT.ordinal()) {
                        validMoves.add(new Move.CaptureMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
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
