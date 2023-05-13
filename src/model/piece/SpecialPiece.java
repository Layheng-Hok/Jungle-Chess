package model.piece;

import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.board.Terrain;
import model.piece.animal.Animal;
import model.player.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class SpecialPiece extends Piece {
    private final int[] POTENTIAL_MOVE_COORDINATES = {-7, -1, 1, 7};

    protected SpecialPiece(final Animal pieceType, final int pieceCoordinate, final PlayerColor pieceColor, int pieceRank, boolean isFirstMove) {
        super(pieceType, pieceCoordinate, pieceColor, pieceRank, isFirstMove);
    }

    @Override
    public Collection<Move> determineValidMoves(final Board board) {
        final List<Move> validMoves = new ArrayList<>();
        for (final int currentPotentialOffset : POTENTIAL_MOVE_COORDINATES) {
            int potentialDestinationCoordinate = this.pieceCoordinate + currentPotentialOffset;
            int tempPotentialDestinationCoordinate = potentialDestinationCoordinate;
            if (BoardUtils.isInBoundary(potentialDestinationCoordinate) && !BoardUtils.isDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
                Terrain tempPotentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
                if ((currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[0] || currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[3])
                        && BoardUtils.isRiver(potentialDestinationCoordinate)) {
                    boolean isRiverTerrainOccupied = false;
                    for (int i = 0; i < 3; i++) {
                        potentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
                        if (potentialDestinationTerrain.isTerrainOccupied()) {
                            isRiverTerrainOccupied = true;
                            break;
                        }
                        tempPotentialDestinationCoordinate += currentPotentialOffset;
                    }
                    if (!isRiverTerrainOccupied) {
                        if (BoardUtils.isInBoundary(tempPotentialDestinationCoordinate)) {
                            potentialDestinationCoordinate = tempPotentialDestinationCoordinate;
                            potentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
                        }
                    } else {
                        continue;
                    }
                }
                if ((currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[1] || currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[2])
                        && BoardUtils.isRiver(potentialDestinationCoordinate)) {
                    boolean isRiverTerrainOccupied = false;
                    for (int i = 0; i < 2; i++) {
                        potentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
                        if (potentialDestinationTerrain.isTerrainOccupied()) {
                            isRiverTerrainOccupied = true;
                            break;
                        }
                        tempPotentialDestinationCoordinate += currentPotentialOffset;
                    }
                    if (!isRiverTerrainOccupied) {
                        if (BoardUtils.isInBoundary(tempPotentialDestinationCoordinate)) {
                            potentialDestinationCoordinate = tempPotentialDestinationCoordinate;
                            potentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
                        }
                    } else {
                        continue;
                    }
                }
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new Move.StandardMove(board, this, potentialDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceAtDestinationColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceAtDestinationColor && this.pieceAttackRank >= pieceAtDestination.pieceDefenseRank) {
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
