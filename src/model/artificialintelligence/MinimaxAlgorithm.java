package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.board.MoveTransition;

public class MinimaxAlgorithm implements Strategy {
    private final GameEvaluator evaluator;
    private final int depth;

    public MinimaxAlgorithm(final int searchDepth, final GameEvaluator evaluator) {
        this.evaluator = evaluator;
        this.depth = searchDepth;
    }

    @Override
    public String toString() {
        return "Minimax";
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move optimalMove = null;
        int highestValue = Integer.MIN_VALUE;
        int lowestValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer() + " is THINKING with a depth of " + depth);
        int numMoves = board.getCurrentPlayer().getValidMoves().size();
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAllyColor().isBlue() ?
                        min(moveTransition.getTransitionBoard(), this.depth - 1) :
                        max(moveTransition.getTransitionBoard(), this.depth - 1);
                if (board.getCurrentPlayer().getAllyColor().isBlue() && currentValue >= highestValue) {
                    highestValue = currentValue;
                    optimalMove = move;
                } else if (board.getCurrentPlayer().getAllyColor().isRed() && currentValue <= lowestValue) {
                    lowestValue = currentValue;
                    optimalMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;
        return optimalMove;
    }

    public int min(final Board board, final int depth) {
        if (depth == 0 || isGameOverScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }
        int lowestValue = Integer.MAX_VALUE;
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestValue) {
                    lowestValue = currentValue;
                }
            }
        }
        return lowestValue;
    }

    public int max(final Board board, final int depth) {
        if (depth == 0 || isGameOverScenario(board)){
            return this.evaluator.evaluate(board, depth);
        }
        int highestValue = Integer.MIN_VALUE;
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestValue) {
                    highestValue = currentValue;
                }
            }
        }
        return highestValue;
    }

    private static boolean isGameOverScenario(final Board board) {
        return board.getCurrentPlayer().isDenPenetrated();
    }
}
