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

//  class for Lion and Tiger to inherit
public abstract class SpecialPiece extends Piece {
    private final int[] POTENTIAL_MOVE_COORDINATES = {-7, -1, 1, 7};

    protected SpecialPiece(final int pieceCoordinate, final PlayerColor pieceColor, int pieceRank) {
        super(pieceCoordinate, pieceColor, pieceRank);
    }

    @Override
    public Collection<Move> determineValidMoves(final Board board) {
        final List<Move> validMoves = new ArrayList<>();
        for (final int currentPotentialOffset : POTENTIAL_MOVE_COORDINATES) {
            int potentialDestinationCoordinate = this.pieceCoordinate + currentPotentialOffset;
            int tempPotentialDestinationCoordinate = potentialDestinationCoordinate;
            Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
            Terrain tempPotentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
            if ((currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[0] || currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[3])
                    && BoardTools.isRiver(potentialDestinationCoordinate)) {
                boolean isWaterTerrainOccupied = false;
                for (int i = 0; i < 3; i++) {
                    potentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
                    if (potentialDestinationTerrain.isTerrainOccupied()) {
                        isWaterTerrainOccupied = true;
                        break;
                    }
                    tempPotentialDestinationCoordinate += currentPotentialOffset;
                }
                if (!isWaterTerrainOccupied) {
                    potentialDestinationCoordinate += currentPotentialOffset * 4;
                    potentialDestinationTerrain = board.getTerrain(currentPotentialOffset * 4);
                }
            } else if ((currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[1] || currentPotentialOffset == POTENTIAL_MOVE_COORDINATES[2])
                    && BoardTools.isRiver(potentialDestinationCoordinate)) {
                boolean isWaterTerrainOccupied = false;
                for (int i = 0; i < 2; i++) {
                    potentialDestinationTerrain = board.getTerrain(tempPotentialDestinationCoordinate);
                    if (potentialDestinationTerrain.isTerrainOccupied()) {
                        isWaterTerrainOccupied = true;
                        break;
                    }
                    tempPotentialDestinationCoordinate += currentPotentialOffset;
                }
                if (!isWaterTerrainOccupied) {
                    potentialDestinationCoordinate += currentPotentialOffset * 3;
                    potentialDestinationTerrain = board.getTerrain(currentPotentialOffset * 3);
                }
            }
            if (BoardTools.isInBoundary(potentialDestinationCoordinate) && !BoardTools.isDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new Move.MajorMove(board, this, potentialDestinationCoordinate));
                    if (BoardTools.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
                        this.defensePieceRank = 0;
                    } else {
                        this.defensePieceRank = this.attackPieceRank;
                    }
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceColor && this.attackPieceRank >= pieceAtDestination.defensePieceRank) {
                        validMoves.add(new Move.AttackMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
                        if (BoardTools.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
                            this.defensePieceRank = 0;
                        } else {
                            this.defensePieceRank = this.attackPieceRank;
                        }
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
