package model.piece.animal;

import model.board.Board;
import model.board.BoardTools;
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

    protected Rat(final int pieceCoordinate, final PlayerColor pieceColor) {
        super(pieceCoordinate, pieceColor, AnimalRank.RAT.ordinal());
    }

    @Override
    public Collection<Move> determineValidMoves(final Board board) {
        final List<Move> validMoves = new ArrayList<>();
        for (final int currentPotentialOffset : POTENTIAL_MOVE_COORDINATES) {
            int potentialDestinationCoordinate = this.pieceCoordinate + currentPotentialOffset;
            if (BoardTools.isInBoundary(potentialDestinationCoordinate) && !BoardTools.isDen(potentialDestinationCoordinate, this.pieceColor)) {
                if (isColumnZeroExclusion(this.pieceCoordinate, currentPotentialOffset) || isColumnSixExclusion(this.pieceCoordinate, currentPotentialOffset)) {
                    continue;
                }
                final Terrain potentialDestinationTerrain = board.getTerrain(potentialDestinationCoordinate);
                if (!potentialDestinationTerrain.isTerrainOccupied()) {
                    validMoves.add(new MajorMove(board, this, potentialDestinationCoordinate));
                    if (BoardTools.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
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
                            if (BoardTools.isLand(this.pieceCoordinate) && !BoardTools.isRiver(potentialDestinationCoordinate) ||
                                    BoardTools.isRiver(this.pieceCoordinate) && BoardTools.isRiver(potentialDestinationCoordinate)) {
                                validMoves.add(new AttackMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
                                if (BoardTools.isEnemyTrap(potentialDestinationCoordinate, this.pieceColor)) {
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
        return (BoardTools.COLUMN_ZERO[currentCoordinate] && (potentialOffset == -1));
    }

    private static boolean isColumnSixExclusion(final int currentCoordinate, final int potentialOffset) {
        return (BoardTools.COLUMN_SIX[currentCoordinate] && (potentialOffset == 1));
    }
}
