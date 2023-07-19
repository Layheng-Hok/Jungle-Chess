package model.artificialintelligence;

import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.board.MoveTransition;

import java.util.concurrent.atomic.AtomicLong;

public class Minimax implements MoveStrategy {
    private final BoardEvaluator evaluator;
    private final int searchDepth;
    private long numEvaluatedBoards;
    private FreqTableRow[] freqTable;
    private int freqTableIndex;

    public Minimax(final int searchDepth) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.numEvaluatedBoards = 0;
    }

    @Override
    public String toString() {
        return "Minimax";
    }

    @Override
    public long getNumEvaluatedBoards() {
        return this.numEvaluatedBoards;
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move optimalMove = Move.MoveFactory.getNullMove();
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer() + " is thinking with a depth of " + searchDepth);
        this.freqTable = new FreqTableRow[board.getCurrentPlayer().getValidMoves().size()];
        this.freqTableIndex = 0;
        int moveCounter = 1;
        final int numMoves = board.getCurrentPlayer().getValidMoves().size();
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final FreqTableRow row = new FreqTableRow(move);
                this.freqTable[this.freqTableIndex] = row;
                currentValue = board.getCurrentPlayer().getAllyColor().isBlue() ?
                        min(moveTransition.getToBoard(), this.searchDepth - 1) :
                        max(moveTransition.getToBoard(), this.searchDepth - 1);
                System.out.println("\t" + this + " is analyzing move (" + moveCounter + "/" + numMoves + ") " + move +
                        " scores " + currentValue + " " + this.freqTable[this.freqTableIndex]);
                this.freqTableIndex++;
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
        final long executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s selects %s [#total boards evaluated = %d, time taken = %dms, evaluation rate = %.1fboards/second]\n",
                board.getCurrentPlayer(), optimalMove, this.numEvaluatedBoards, executionTime, (1000 * ((double) this.numEvaluatedBoards / executionTime)));
        long total = 0;
        for (final FreqTableRow row : this.freqTable) {
            if(row != null) {
                total += row.getCount();
            }
        }
        if(this.numEvaluatedBoards != total) {
            System.out.println("Something is wrong with the # of boards evaluated!");
        }
        return optimalMove;
    }

    private int min(final Board board, final int depth) {
        if (depth == 0) {
            this.numEvaluatedBoards++;
            this.freqTable[this.freqTableIndex].increment();
            return this.evaluator.evaluate(board, depth);
        }
        if (BoardUtils.isGameOverScenario(board)) {
            return this.evaluator.evaluate(board, depth);
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
            this.numEvaluatedBoards++;
            this.freqTable[this.freqTableIndex].increment();
            return this.evaluator.evaluate(board, depth);
        }
        if (BoardUtils.isGameOverScenario(board)) {
            return this.evaluator.evaluate(board, depth);
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

    private static class FreqTableRow {
        private final Move move;
        private final AtomicLong count;

        FreqTableRow(final Move move) {
            this.count = new AtomicLong();
            this.move = move;
        }

        long getCount() {
            return this.count.get();
        }

        void increment() {
            this.count.incrementAndGet();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.move.getCurrentCoordinate()) +
                    BoardUtils.getPositionAtCoordinate(this.move.getDestinationCoordinate()) + " with frequency of " + this.count;
        }
    }
}
