package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.board.MoveTransition;

public class MinimaxAlgorithm implements Strategy {
    private final GameEvaluator evaluator;

    public MinimaxAlgorithm() {
        this.evaluator = new StandardBoardEvaluator();
    }

    @Override
    public String toString() {
        return "Minimax";
    }

    @Override
    public Move execute(Board board, int depth) {
        final long startTime = System.currentTimeMillis();
        Move optimalMove = null;
        int highestValue = Integer.MIN_VALUE;
        int lowestValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer() + " is THINKING with depth = " + depth);
        int numMoves = board.getCurrentPlayer().getValidMoves().size();
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAllyColor().isBlue() ?
                        min(moveTransition.getTransitionBoard(), depth - 1) :
                        max(moveTransition.getTransitionBoard(), depth - 1);
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
        if (depth == 0) {
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
        if (depth == 0) {
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
}