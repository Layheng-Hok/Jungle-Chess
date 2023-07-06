package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.board.MoveTransition;
import view.GameFrame;

public class MinimaxAlgorithm implements MoveStrategy {
    private final BoardEvaluator evaluator;
    private final int searchDepth;

    public MinimaxAlgorithm(final BoardEvaluator evaluator, final int searchDepth) {
        this.evaluator = evaluator;
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "Minimax";
    }

    @Override
    public Move execute(Board board) {
        final double startTime = System.currentTimeMillis();
        Move optimalMove = Move.MoveFactory.getNullMove();
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer() + " is thinking with a depth of " + searchDepth);
        int moveCounter = 1;
        int numMoves = board.getCurrentPlayer().getValidMoves().size();
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAllyColor().isBlue() ?
                        min(moveTransition.getToBoard(), this.searchDepth - 1) :
                        max(moveTransition.getToBoard(), this.searchDepth - 1);
                System.out.println(this + " is analyzing move (" + moveCounter + "/" + numMoves + ") " + move + " scores " + currentValue);
                if (board.getCurrentPlayer().getAllyColor().isBlue() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    optimalMove = move;
                } else if (board.getCurrentPlayer().getAllyColor().isRed() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    optimalMove = move;
                }
            } else {
                System.out.println(this + " is analyzing move (" + moveCounter + "/" + numMoves + ") " + move + " is illegal!");
            }
            moveCounter++;
        }
        final double executionTime = System.currentTimeMillis() - startTime;
        System.out.println("Execution time: " + executionTime / 1000 + "s");
        return optimalMove;
    }

    private int min(final Board board, final int depth) {
        if (depth == 0) {
            return this.evaluator.evaluate(board, depth);
        }
        if (GameFrame.isGameOverScenario(board)) {
            return this.evaluator.evaluate(board, depth) * 10000000;
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getToBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    private int max(final Board board, final int depth) {
        if (depth == 0) {
            return this.evaluator.evaluate(board, depth);
        }
        if (GameFrame.isGameOverScenario(board)) {
            return this.evaluator.evaluate(board, depth) * 1000000;
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getToBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

}
