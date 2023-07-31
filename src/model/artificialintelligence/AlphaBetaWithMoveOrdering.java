package model.artificialintelligence;

import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.board.MoveTransition;
import model.player.Player;

import java.util.Observable;

public class AlphaBetaWithMoveOrdering extends Observable implements MoveStrategy {
    private final BoardEvaluator evaluator;
    private final int searchDepth;
    private long numEvaluatedBoards;
    private int cutOffsProduced;

    public AlphaBetaWithMoveOrdering(final int searchDepth) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.numEvaluatedBoards = 0;
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
        int highestSeenValue = Integer.MIN_VALUE; // alpha = highestSeenValue
        int lowestSeenValue = Integer.MAX_VALUE;  // beta = lowestSeenValue
        int currentValue;
        int moveCounter = 1;
        final int numMoves = board.getCurrentPlayer().getValidMoves().size();
        System.out.println(board.getCurrentPlayer() + " is thinking with with a depth of " + this.searchDepth);
        System.out.println("\tOrdered moves : " + MoveSorter.EXPENSIVE.sort(board.getCurrentPlayer().getValidMoves()));
        for (final Move move : MoveSorter.EXPENSIVE.sort(board.getCurrentPlayer().getValidMoves())) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
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
                s = "\t" + this + " (" + this.searchDepth + "), move: (" + moveCounter + "/" + numMoves + ") " + move + ", best: " + optimalMove
                        + " " + score(currentPlayer, highestSeenValue, lowestSeenValue)  + ", t: " + calculateTimeTaken(potentialMoveStartTime, System.nanoTime());
            } else {
                s = "\t" + this + " (" + this.searchDepth + ")" + ", m: (" + moveCounter + "/" + numMoves + ") " + move + " is illegal! best: " + optimalMove;
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
        // alpha = highest (currentHighest)
        // beta = lowest
        if (depth == 0 || BoardUtils.isGameOverScenarioStandardConditions(board)) {
            this.numEvaluatedBoards++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : MoveSorter.STANDARD.sort((board.getCurrentPlayer().getValidMoves()))) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentHighest = Math.max(currentHighest,
                        min(moveTransition.getToBoard(), depth - 1, currentHighest, lowest));
                if (currentHighest >= lowest) {
                    this.cutOffsProduced++;
                    return currentHighest;
                }
            }
        }
        return currentHighest;
    }

    public int min(final Board board, final int depth, final int highest, final int lowest) {
        // alpha = highest
        // beta = lowest (currentLowest)
        if (depth == 0 || BoardUtils.isGameOverScenarioStandardConditions(board)) {
            this.numEvaluatedBoards++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : MoveSorter.STANDARD.sort((board.getCurrentPlayer().getValidMoves()))) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentLowest = Math.min(currentLowest,
                        max(moveTransition.getToBoard(), depth - 1, highest, currentLowest));
                if (currentLowest <= highest) {
                    this.cutOffsProduced++;
                    return currentLowest;
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

    private static String calculateTimeTaken(final long start, final long end) {
        final long timeTaken = (end - start) / 1_000_000;
        return timeTaken + " ms";
    }
}
