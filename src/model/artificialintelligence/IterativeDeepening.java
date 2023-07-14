package model.artificialintelligence;

import com.google.common.primitives.Ints;
import model.board.Board;
import model.board.Move;
import model.board.MoveTransition;
import view.GameFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class IterativeDeepening extends Observable implements MoveStrategy {
    private final BoardEvaluator evaluator;
    private final int searchDepth;
    private final MoveSorter moveSorter;
    private long numEvaluatedBoards;
    private int cutOffsProduced;

    public IterativeDeepening(final int searchDepth) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.moveSorter = MoveSorter.SIMPLE;
        this.numEvaluatedBoards = 0;
        this.cutOffsProduced = 0;
    }

    @Override
    public String toString() {
        return "Iterative Deepening";
    }

   @Override
    public long getNumEvaluatedBoards() {
        return this.numEvaluatedBoards;
    }

    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();
        System.out.println(board.getCurrentPlayer() + " THINKING with depth = " + this.searchDepth);

        MoveOrderingBuilder builder = new MoveOrderingBuilder();
        builder.setOrder(board.getCurrentPlayer().getAllyColor().isBlue() ? Ordering.DESC : Ordering.ASC);
        for (final Move move : board.getCurrentPlayer().getValidMoves()) {
            builder.addMoveOrderingRecord(move, 0);
        }

        Move optimalMove = Move.MoveFactory.getNullMove();
        int currentDepth = 1;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;

        while (currentDepth <= this.searchDepth) {
            final long subTimeStart = System.currentTimeMillis();

            int currentValue;
            final List<MoveScoreRecord> records = builder.build();
            builder = new MoveOrderingBuilder();
            builder.setOrder(board.getCurrentPlayer().getAllyColor().isBlue() ? Ordering.DESC : Ordering.ASC);
            for (final MoveScoreRecord record : records) {
                final Move move = record.getMove();
                final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus().isDone()) {
                    currentValue = board.getCurrentPlayer().getAllyColor().isBlue() ?
                            min(moveTransition.getToBoard(), currentDepth - 1, highestSeenValue, lowestSeenValue) :
                            max(moveTransition.getToBoard(), currentDepth - 1, highestSeenValue, lowestSeenValue);
                    builder.addMoveOrderingRecord(move, currentValue);
                    if (board.getCurrentPlayer().getAllyColor().isBlue() && currentValue > highestSeenValue) {
                        highestSeenValue = currentValue;
                        optimalMove = move;
                    } else if (board.getCurrentPlayer().getAllyColor().isRed() && currentValue < lowestSeenValue) {
                        lowestSeenValue = currentValue;
                        optimalMove = move;
                    }
                }
            }
            final long subTime = System.currentTimeMillis() - subTimeStart;
            System.out.println("\t" + this + " best move = " + optimalMove + " Depth = " + currentDepth + " took " + (subTime) + " ms, ordered moves : " + records);
            setChanged();
            notifyObservers(optimalMove);
            currentDepth++;
        }
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s SELECTS %s [#boards evaluated = %d, time taken = %d ms, eval rate = %.1f cutoffCount = %d prune percent = %.2f\n", board.getCurrentPlayer(),
                optimalMove, this.numEvaluatedBoards, executionTime, (1000 * ((double) this.numEvaluatedBoards / executionTime)), this.cutOffsProduced, 100 * ((double) this.cutOffsProduced / this.numEvaluatedBoards));
        return optimalMove;
    }

    public int max(final Board board,
                   final int depth,
                   final int highest,
                   final int lowest) {
        if (depth == 0 || GameFrame.isGameOverScenario(GameFrame.get().getChessBoard())) {
            this.numEvaluatedBoards++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : this.moveSorter.sort((board.getCurrentPlayer().getValidMoves()))) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentHighest = Math.max(currentHighest, min(moveTransition.getToBoard(),
                        depth - 1, currentHighest, lowest));
                if (lowest <= currentHighest) {
                    this.cutOffsProduced++;
                    break;
                }
            }
        }
        return currentHighest;
    }

    public int min(final Board board,
                   final int depth,
                   final int highest,
                   final int lowest) {
        if (depth == 0 || GameFrame.isGameOverScenario(GameFrame.get().getChessBoard())) {
            this.numEvaluatedBoards++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : this.moveSorter.sort((board.getCurrentPlayer().getValidMoves()))) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentLowest = Math.min(currentLowest, max(moveTransition.getToBoard(),
                        depth - 1, highest, currentLowest));
                if (currentLowest <= highest) {
                    this.cutOffsProduced++;
                    break;
                }
            }
        }
        return currentLowest;
    }

    private static class MoveScoreRecord implements Comparable<MoveScoreRecord> {
        final Move move;
        final int score;

        MoveScoreRecord(final Move move, final int score) {
            this.move = move;
            this.score = score;
        }

        Move getMove() {
            return this.move;
        }

        int getScore() {
            return this.score;
        }

        @Override
        public int compareTo(MoveScoreRecord o) {
            return Integer.compare(this.score, o.score);
        }

        @Override
        public String toString() {
            return this.move + " : " + this.score;
        }
    }

    enum Ordering {
        ASC {
            @Override
            List<MoveScoreRecord> order(final List<MoveScoreRecord> moveScoreRecords) {
                Collections.sort(moveScoreRecords, (o1, o2) -> Ints.compare(o1.getScore(), o2.getScore()));
                return moveScoreRecords;
            }
        },
        DESC {
            @Override
            List<MoveScoreRecord> order(final List<MoveScoreRecord> moveScoreRecords) {
                Collections.sort(moveScoreRecords, (o1, o2) -> Ints.compare(o2.getScore(), o1.getScore()));
                return moveScoreRecords;
            }
        };

        abstract List<MoveScoreRecord> order(final List<MoveScoreRecord> moveScoreRecords);
    }


    private static class MoveOrderingBuilder {
        List<MoveScoreRecord> moveScoreRecords;
        Ordering ordering;

        MoveOrderingBuilder() {
            this.moveScoreRecords = new ArrayList<>();
        }

        void addMoveOrderingRecord(final Move move,
                                   final int score) {
            this.moveScoreRecords.add(new MoveScoreRecord(move, score));
        }

        void setOrder(final Ordering order) {
            this.ordering = order;
        }

        List<MoveScoreRecord> build() {
            return this.ordering.order(moveScoreRecords);
        }
    }
}
