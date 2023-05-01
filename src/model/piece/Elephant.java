package model.piece;

import model.board.Board;
import model.board.BoardTools;
import model.board.Move;
import model.board.Terrain;
import model.player.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Elephant extends Piece {
    private final int[] POTENTIAL_MOVE_COORDINATES = {-7, -1, 1, 7};

    Elephant(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor);
    }

    @Override
    public Collection<Move> determineValidMoves(Board board) {
        final List<Move> validMoves = new ArrayList<>();
        for (final int currentPotentialOffset : POTENTIAL_MOVE_COORDINATES) {
            int potentialDestinationCoordinate = this.pieceCoordinate + currentPotentialOffset;
            if (BoardTools.isInBoundary(potentialDestinationCoordinate) && !BoardTools.isRiverOrDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                final Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new Move());
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceColor) {
                        validMoves.add(new Move());
                    }
                }
            }
        }
        return Collections.unmodifiableList(validMoves);
    }

    private static boolean isColumnZeroExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardTools.COLUMN_ZERO[currentCoordinate] && (potentialOffset == -1));
    }

   private static boolean isColumnSixExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardTools.COLUMN_SIX[currentCoordinate] && (potentialOffset == 1));
    }
}
