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
        super(pieceCoordinate, pieceColor, AnimalRank.RAT.ordinal());
    }

    @Override
    public String toString() {
        return AnimalRank.RAT.toString();
    }

    @Override
    public String getPieceRank() {
        return AnimalRank.RAT.getPieceRank();
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
                    validMoves.add(new MajorMove(board, this, potentialDestinationCoordinate));
                    if (BoardUtils.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
                        this.defensePieceRank = 0;
                    } else {
                        this.defensePieceRank = this.attackPieceRank;
                    }
                } else {
                    final Piece pieceAtDestination = potentialDestinationTerrain.getPiece();
                    final PlayerColor pieceColor = pieceAtDestination.getPieceColor();
                    if (this.pieceColor != pieceColor && this.attackPieceRank >= pieceAtDestination.getDefensePieceRank()) {
                        if (pieceAtDestination.getDefensePieceRank() == AnimalRank.ELEPHANT.ordinal() ||
                                pieceAtDestination.getDefensePieceRank() == AnimalRank.RAT.ordinal()) {
                            if (BoardUtils.isLand(this.pieceCoordinate) && !BoardUtils.isRiver(potentialDestinationCoordinate) ||
                                    BoardUtils.isRiver(this.pieceCoordinate) && BoardUtils.isRiver(potentialDestinationCoordinate)) {
                                validMoves.add(new AttackMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
                                if (BoardUtils.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
                                    this.defensePieceRank = 0;
                                } else {
                                    this.defensePieceRank = this.attackPieceRank;
                                }
                            }
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
