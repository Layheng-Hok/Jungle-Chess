package model.artificialintelligence;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import model.board.Board;
import model.board.Move;
import model.board.MoveTransition;

import java.util.Collection;
import java.util.Comparator;

enum MoveSorter {
    STANDARD {
        @Override
        Collection<Move> sort(final Collection<Move> moves) {
            return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                    .compareTrueFirst(move1.isCaptureMove(), move2.isCaptureMove())
                    .compare(move1.getMovedPiece().getPieceType().getMovePriority(), move2.getMovedPiece().getPieceType().getMovePriority())
                    .result()).immutableSortedCopy(moves);
        }
    },

    EXPENSIVE {
        @Override
        Collection<Move> sort(final Collection<Move> moves) {
            return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                    .compareTrueFirst(getIntoEnemyDen(move1), getIntoEnemyDen(move2))
                    .compareTrueFirst(getIntoEnemyTrapWithoutEnemyNearby(move1), getIntoEnemyTrapWithoutEnemyNearby(move2))
                    .compareTrueFirst(move1.isCaptureMove(), move2.isCaptureMove())
                    .compare(move1.getMovedPiece().getPieceType().getMovePriority(), move2.getMovedPiece().getPieceType().getMovePriority())
                    .result()).immutableSortedCopy(moves);
        }
    };

    abstract Collection<Move> sort(Collection<Move> moves);

    private static boolean getIntoEnemyDen(final Move move) {
        final Board board = move.getBoard();
        final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
        return moveTransition.getToBoard().getCurrentPlayer().isDenPenetrated();
    }

    private static boolean getIntoEnemyTrapWithoutEnemyNearby(final Move move) {
        if (move.getMovedPiece().getPieceColor().isBlue()) {
            if (move.getDestinationCoordinate() == 2) {
                if (!move.getBoard().getTerrain(1).isTerrainOccupied()
                        || !move.getBoard().getTerrain(9).isTerrainOccupied()) {
                    return true;
                }
                if (move.getBoard().getTerrain(1).isTerrainOccupied()
                        && move.getBoard().getTerrain(1).getPiece().getPieceColor().isBlue()) {
                    return true;
                }
                if (move.getBoard().getTerrain(9).isTerrainOccupied()
                        && move.getBoard().getTerrain(9).getPiece().getPieceColor().isBlue()) {
                    return true;
                }
            }
            if (move.getDestinationCoordinate() == 4) {
                if (!move.getBoard().getTerrain(5).isTerrainOccupied()
                        || !move.getBoard().getTerrain(11).isTerrainOccupied()) {
                    return true;
                }
                if (move.getBoard().getTerrain(5).isTerrainOccupied()
                        && move.getBoard().getTerrain(5).getPiece().getPieceColor().isBlue()) {
                    return true;
                }
                if (move.getBoard().getTerrain(11).isTerrainOccupied()
                        && move.getBoard().getTerrain(11).getPiece().getPieceColor().isBlue()) {
                    return true;
                }
            }
            if (move.getDestinationCoordinate() == 10) {
                if (!move.getBoard().getTerrain(9).isTerrainOccupied()
                        || !move.getBoard().getTerrain(11).isTerrainOccupied()
                        || !move.getBoard().getTerrain(17).isTerrainOccupied()) {
                    return true;
                }
                if (move.getBoard().getTerrain(9).isTerrainOccupied()
                        && move.getBoard().getTerrain(9).getPiece().getPieceColor().isBlue()) {
                    return true;
                }
                if (move.getBoard().getTerrain(11).isTerrainOccupied()
                        && move.getBoard().getTerrain(11).getPiece().getPieceColor().isBlue()) {
                    return true;
                }
                if (move.getBoard().getTerrain(17).isTerrainOccupied()
                        && move.getBoard().getTerrain(17).getPiece().getPieceColor().isBlue()) {
                    return true;
                }
            }
        } else if (move.getMovedPiece().getPieceColor().isRed()) {
            if (move.getDestinationCoordinate() == 52) {
                if (!move.getBoard().getTerrain(45).isTerrainOccupied()
                        || !move.getBoard().getTerrain(51).isTerrainOccupied()
                        || !move.getBoard().getTerrain(53).isTerrainOccupied()) {
                    return true;
                }
                if (move.getBoard().getTerrain(45).isTerrainOccupied()
                        && move.getBoard().getTerrain(45).getPiece().getPieceColor().isRed()) {
                    return true;
                }
                if (move.getBoard().getTerrain(51).isTerrainOccupied()
                        && move.getBoard().getTerrain(51).getPiece().getPieceColor().isRed()) {
                    return true;
                }
                if (move.getBoard().getTerrain(53).isTerrainOccupied()
                        && move.getBoard().getTerrain(53).getPiece().getPieceColor().isRed()) {
                    return true;
                }
                if (move.getDestinationCoordinate() == 58) {
                    if (!move.getBoard().getTerrain(51).isTerrainOccupied()
                            || !move.getBoard().getTerrain(57).isTerrainOccupied()) {
                        return true;
                    }
                    if (move.getBoard().getTerrain(51).isTerrainOccupied()
                            && move.getBoard().getTerrain(51).getPiece().getPieceColor().isRed()) {
                        return true;
                    }
                    if (move.getBoard().getTerrain(57).isTerrainOccupied()
                            && move.getBoard().getTerrain(57).getPiece().getPieceColor().isRed()) {
                        return true;
                    }
                }
                if (move.getDestinationCoordinate() == 60) {
                    if (!move.getBoard().getTerrain(53).isTerrainOccupied()
                            || !move.getBoard().getTerrain(61).isTerrainOccupied()) {
                        return true;
                    }
                    if (move.getBoard().getTerrain(53).isTerrainOccupied()
                            && move.getBoard().getTerrain(53).getPiece().getPieceColor().isRed()) {
                        return true;
                    }
                    if (move.getBoard().getTerrain(61).isTerrainOccupied()
                            && move.getBoard().getTerrain(61).getPiece().getPieceColor().isRed()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
