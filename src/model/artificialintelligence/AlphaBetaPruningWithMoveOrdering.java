package model.artificialintelligence;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import model.board.Board;
import model.board.BoardUtilities;
import model.board.Move;
import model.board.MoveTransition;
import model.piece.Piece;
import model.piece.animal.Animal;
import model.player.Player;
import view.GameFrame;

import java.util.Collection;
import java.util.Comparator;
import java.util.Observable;

public class AlphaBetaPruningWithMoveOrdering extends Observable implements MoveStrategy {
    private final BoardEvaluator evaluator;
    private final int searchDepth;
    private long numEvaluatedBoards;
    private int quiescenceCount;
    private int cutOffsProduced;
    private static final int MAX_QUIESCENCE = 5000 * 5;

    private enum MoveSorter {
        STANDARD {
            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                        .compare(mvvlva(move2), mvvlva(move1))
                        .result()).immutableSortedCopy(moves);
            }
        },

        EXPENSIVE {
            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return Ordering.from((Comparator<Move>) (move1, move2) -> ComparisonChain.start()
                        .compareTrueFirst(getIntoEnemyDen(move1), getIntoEnemyDen(move2))
                        .compareTrueFirst(getIntoEnemyTrapWithoutEnemyNearby(move1), getIntoEnemyTrapWithoutEnemyNearby(move2))
                        .compare(mvvlva(move2), mvvlva(move1))
                        .result()).immutableSortedCopy(moves);
            }
        };

        abstract Collection<Move> sort(Collection<Move> moves);

    }

    public AlphaBetaPruningWithMoveOrdering(final int searchDepth) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.numEvaluatedBoards = 0;
        this.quiescenceCount = 0;
        this.cutOffsProduced = 0;
    }

    @Override
    public String toString() {
        return "Alpha Beta Pruning + Move Ordering";
    }

    @Override
    public long getNumEvaluatedBoards() {
        return this.numEvaluatedBoards;
    }

    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();
        final Player currentPlayer = board.getCurrentPlayer();
        Move optimalMove = Move.MoveFactory.getNullMove();
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        int moveCounter = 1;
        final int numMoves = board.getCurrentPlayer().getValidMoves().size();
        System.out.println(board.getCurrentPlayer() + " is thinking with with a depth of " + this.searchDepth);
        System.out.println("\tOrdered moves : " + MoveSorter.EXPENSIVE.sort(board.getCurrentPlayer().getValidMoves()));
        for (final Move move : MoveSorter.EXPENSIVE.sort(board.getCurrentPlayer().getValidMoves())) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            this.quiescenceCount = 0;
            final String s;
            if (moveTransition.getMoveStatus().isDone()) {
                final long potentialMoveStartTime = System.nanoTime();
                currentValue = currentPlayer.getAllyColor().isBlue() ?
                        min(moveTransition.getToBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue) :
                        max(moveTransition.getToBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue);
                if (currentPlayer.getAllyColor().isBlue() && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    optimalMove = move;
                    if (moveTransition.getToBoard().redPlayer().isDenPenetrated()
                            || moveTransition.getToBoard().redPlayer().getActivePieces().isEmpty()
                            || moveTransition.getToBoard().redPlayer().getValidMoves().isEmpty()) {
                        break;
                    }
                } else if (currentPlayer.getAllyColor().isRed() && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    optimalMove = move;
                    if (moveTransition.getToBoard().bluePlayer().isDenPenetrated()
                            || moveTransition.getToBoard().bluePlayer().getActivePieces().isEmpty()
                            || moveTransition.getToBoard().bluePlayer().getValidMoves().isEmpty()) {
                        break;
                    }
                }
                final String quiescenceInfo = " " + score(currentPlayer, highestSeenValue, lowestSeenValue) + " q: " + this.quiescenceCount;
                s = "\t" + this + "(" + this.searchDepth + "), m: (" + moveCounter + "/" + numMoves + ") " + move + ", best:  " + optimalMove

                        + quiescenceInfo + ", t: " + calculateTimeTaken(potentialMoveStartTime, System.nanoTime());
            } else {
                s = "\t" + this + "(" + this.searchDepth + ")" + ", m: (" + moveCounter + "/" + numMoves + ") " + move + " is illegal! best: " + optimalMove;
            }
            System.out.println(s);
            setChanged();
            notifyObservers(s);
            moveCounter++;
        }
        final long executionTime = System.currentTimeMillis() - startTime;
        final String result = String.format("%s selects %s [#total boards evaluated = %d," +
                        " time taken = %dms, evaluation rate = %.1fboards/second," +
                        " cut-off count = %d, prune percent = %.2f]\n",
                board.getCurrentPlayer(), optimalMove, this.numEvaluatedBoards,
                executionTime, (1000 * ((double) this.numEvaluatedBoards / executionTime)),
                this.cutOffsProduced, 100 * ((double) this.cutOffsProduced / (this.numEvaluatedBoards + this.cutOffsProduced)));
        System.out.print(result);
        setChanged();
        notifyObservers(result);
        return optimalMove;
    }

    public int max(final Board board, final int depth, final int highest, final int lowest) {
        if (depth == 0 || GameFrame.isGameOverScenario(board)) {
            this.numEvaluatedBoards++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : MoveSorter.STANDARD.sort((board.getCurrentPlayer().getValidMoves()))) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentHighest = Math.max(currentHighest,
                        min(moveTransition.getToBoard(),
                                calculateQuiescenceDepth(moveTransition.getToBoard(), depth),
                                currentHighest, lowest));
                if (currentHighest >= lowest) {
                    this.cutOffsProduced++;
                    return lowest;
                }
            }
        }
        return currentHighest;
    }

    public int min(final Board board, final int depth, final int highest, final int lowest) {
        if (depth == 0 || GameFrame.isGameOverScenario(board)) {
            this.numEvaluatedBoards++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : MoveSorter.STANDARD.sort((board.getCurrentPlayer().getValidMoves()))) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentLowest = Math.min(currentLowest,
                        max(moveTransition.getToBoard(),
                                calculateQuiescenceDepth(moveTransition.getToBoard(), depth),
                                highest, currentLowest));
                if (currentLowest <= highest) {
                    this.cutOffsProduced++;
                    return highest;
                }
            }
        }
        return currentLowest;
    }

    private static String score(final Player currentPlayer, final int highestSeenValue, final int lowestSeenValue) {
        if (currentPlayer.getAllyColor().isBlue()) {
            return "[score: " + highestSeenValue + "]";
        } else if (currentPlayer.getAllyColor().isRed()) {
            return "[score: " + lowestSeenValue + "]";
        }
        throw new RuntimeException("Something went wrong!");
    }

    private int calculateQuiescenceDepth(final Board toBoard, final int depth) {
        if (depth == 1 && this.quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            for (final Move move : BoardUtilities.lastNMoves(toBoard, 2)) {
                if (move.isCaptureMove()) {
                    activityMeasure += 1;
                }
            }
            if (activityMeasure >= 2) {
                this.quiescenceCount++;
                return 2;
            }
        }
        return depth - 1;
    }

    private static String calculateTimeTaken(final long start, final long end) {
        final long timeTaken = (end - start) / 1000000;
        return timeTaken + " ms";
    }

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

    private static int mvvlva(final Move move) {
        final Piece movedPiece = move.getMovedPiece();
        if (move.isCaptureMove()) {
            final Piece capturedPiece = move.getCapturedPiece();
            return (capturedPiece.getPiecePower() - movedPiece.getPiecePower()) + Animal.LION.getPiecePower() * 100;
        }
        return Animal.LION.getPiecePower() - movedPiece.getPiecePower();
    }
}
