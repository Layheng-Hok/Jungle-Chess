package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.player.Player;

public final class PoorBoardEvaluator implements BoardEvaluator {
    private static final PoorBoardEvaluator INSTANCE = new PoorBoardEvaluator();

    private PoorBoardEvaluator() {
    }

    public static PoorBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.bluePlayer(), depth) -
                scorePlayer(board, board.redPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return ConcreteBoardEvaluator.pieceValue(player)
                + ConcreteBoardEvaluator.captureMove(player)
                + ConcreteBoardEvaluator.isEnemyDenPenetrated(player, depth);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return ("Blue Piece Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.bluePlayer()) + "\n" +
                "Capture Move: " + ConcreteBoardEvaluator.captureMove(board.bluePlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + ConcreteBoardEvaluator.isEnemyDenPenetrated(board.bluePlayer(), depth) + "\n" +
                "-------------------------------\n" +
                "Red Piece Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.redPlayer()) + "\n" +
                "Capture Move: " + ConcreteBoardEvaluator.captureMove(board.redPlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + ConcreteBoardEvaluator.isEnemyDenPenetrated(board.redPlayer(), depth) + "\n" +
                "-------------------------------\n" +
                "Net Score: " + evaluate(board, depth) + "\n");
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPiecePower();
        }
        return pieceValueScore;
    }
}
