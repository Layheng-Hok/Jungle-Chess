package model.artificialintelligence;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import model.board.Board;
import model.board.Move;
import model.board.MoveTransition;
import model.piece.Piece;
import model.player.Player;

import java.util.Collection;
import java.util.Comparator;

public enum MoveSorter {
    STANDARD {
        @Override
        public Collection<Move> sort(final Collection<Move> moves) {
            return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                    .compareTrueFirst(move1.isCaptureMove(), move2.isCaptureMove())
                    .compare(move1.getMovedPiece().getPieceType().getMovePriority(), move2.getMovedPiece().getPieceType().getMovePriority())
                    .result()).immutableSortedCopy(moves);
        }
    },

    EXPENSIVE {
        @Override
        public Collection<Move> sort(final Collection<Move> moves) {
            return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                    .compareTrueFirst(getIntoEnemyDen(move1), getIntoEnemyDen(move2))
                    .compareTrueFirst(getIntoEnemyTrapWithoutEnemyNearby(move1), getIntoEnemyTrapWithoutEnemyNearby(move2))
                    .compareTrueFirst(move1.isCaptureMove(), move2.isCaptureMove())
                    .compare(move1.getMovedPiece().getPieceType().getMovePriority(), move2.getMovedPiece().getPieceType().getMovePriority())
                    .result()).immutableSortedCopy(moves);
        }
    };

    public abstract Collection<Move> sort(Collection<Move> moves);

    private static boolean getIntoEnemyDen(final Move move) {
        final Board board = move.getBoard();
        final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
        return moveTransition.getToBoard().getCurrentPlayer().isDenPenetrated();
    }

    private static boolean getIntoEnemyTrapWithoutEnemyNearby(final Move move) {
        final Player moveMaker = move.getBoard().getCurrentPlayer();
        boolean isInEnemyTrap = false;
        boolean isEnemyNearTrap = false;
        if (moveMaker.getAllyColor().isBlue()) {
            if (move.getDestinationCoordinate() == 2) {
                isInEnemyTrap = true;
                for (final Piece enemyPiece : moveMaker.getEnemyPlayer().getActivePieces()) {
                    if (enemyPiece.getPieceCoordinate() == 1 || enemyPiece.getPieceCoordinate() == 9) {
                        isEnemyNearTrap = true;
                        break;
                    }
                }
            }
            if (move.getDestinationCoordinate() == 4) {
                isInEnemyTrap = true;
                for (final Piece enemyPiece : moveMaker.getEnemyPlayer().getActivePieces()) {
                    if (enemyPiece.getPieceCoordinate() == 5 || enemyPiece.getPieceCoordinate() == 11) {
                        isEnemyNearTrap = true;
                        break;
                    }
                }
            }
            if (move.getDestinationCoordinate() == 10) {
                isInEnemyTrap = true;
                for (final Piece enemyPiece : moveMaker.getEnemyPlayer().getActivePieces()) {
                    if (enemyPiece.getPieceCoordinate() == 9 || enemyPiece.getPieceCoordinate() == 11 || enemyPiece.getPieceCoordinate() == 17) {
                        isEnemyNearTrap = true;
                        break;
                    }
                }
            }
        } else if (moveMaker.getAllyColor().isRed()) {
            if (move.getDestinationCoordinate() == 52) {
                isInEnemyTrap = true;
                for (final Piece enemyPiece : moveMaker.getEnemyPlayer().getActivePieces()) {
                    if (enemyPiece.getPieceCoordinate() == 45 || enemyPiece.getPieceCoordinate() == 51 || enemyPiece.getPieceCoordinate() == 53) {
                        isEnemyNearTrap = true;
                        break;
                    }
                }
            }
            if (move.getDestinationCoordinate() == 58) {
                isInEnemyTrap = true;
                for (final Piece enemyPiece : moveMaker.getEnemyPlayer().getActivePieces()) {
                    if (enemyPiece.getPieceCoordinate() == 51 || enemyPiece.getPieceCoordinate() == 57) {
                        isEnemyNearTrap = true;
                        break;
                    }
                }
            }
            if (move.getDestinationCoordinate() == 60) {
                isInEnemyTrap = true;
                for (final Piece enemyPiece : moveMaker.getEnemyPlayer().getActivePieces()) {
                    if (enemyPiece.getPieceCoordinate() == 53 || enemyPiece.getPieceCoordinate() == 61) {
                        isEnemyNearTrap = true;
                        break;
                    }
                }

            }
        }
        return isInEnemyTrap && !isEnemyNearTrap;
    }
}
