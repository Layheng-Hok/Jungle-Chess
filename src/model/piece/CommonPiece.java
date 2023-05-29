package model.piece;

import model.board.Board;
import model.board.BoardUtilities;
import model.board.Move;
import model.board.Terrain;
import model.piece.animal.Animal;
import model.player.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static model.board.Move.*;

public abstract class CommonPiece extends Piece {
    private final int[] POTENTIAL_MOVE_COORDINATES = {-7, -1, 1, 7};

    protected CommonPiece(final Animal pieceType, final int pieceCoordinate, final PlayerColor pieceColor, int pieceRank, boolean isFirstMove) {
        super(pieceType, pieceCoordinate, pieceColor, pieceRank, isFirstMove);
    }

    @Override
    public Collection<Move> determineValidMoves(final Board board) {
        final List<Move> validMoves = new ArrayList<>();
        for (final int currentPotentialOffset : POTENTIAL_MOVE_COORDINATES) {
            int potentialDestinationCoordinate = this.pieceCoordinate + currentPotentialOffset;
            if (BoardUtilities.isInBoundary(potentialDestinationCoordinate) && !BoardUtilities.isRiverOrDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                final Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new StandardMove(board, this, potentialDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceAtDestinationColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceAtDestinationColor && this.pieceAttackRank >= pieceAtDestination.pieceDefenseRank) {
                        validMoves.add(new CaptureMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(validMoves);
    }

    private static boolean isColumnZeroExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardUtilities.COLUMN_ZERO[currentCoordinate] && (potentialOffset == -1));
    }

    private static boolean isColumnSixExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardUtilities.COLUMN_SIX[currentCoordinate] && (potentialOffset == 1));
    }
}
