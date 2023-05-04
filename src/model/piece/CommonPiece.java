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
            if (BoardUtils.isInBoundary(potentialDestinationCoordinate) && !BoardUtils.isRiverOrDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                final Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new StandardMove(board, this, potentialDestinationCoordinate));
                    if (BoardUtils.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
                        this.defensePieceRank = 0;
                    } else {
                        this.defensePieceRank = this.attackPieceRank;
                    }
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceColor && this.attackPieceRank >= pieceAtDestination.defensePieceRank) {
                        validMoves.add(new CaptureMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
                        if (BoardUtils.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
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
        return (BoardUtils.COLUMN_ZERO[currentCoordinate] && (potentialOffset == -1));
    }

    private static boolean isColumnSixExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardUtils.COLUMN_SIX[currentCoordinate] && (potentialOffset == 1));
    }
}
