package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.player.Player;

public final class PoorBoardEvaluator implements BoardEvaluator {
    private final static int DEPTH_BONUS = 100;
    private final static int PENETRATE_ENEMY_DEN_BONUS = 50000;
    private final static int CAPTURE_MOVE_MULTIPLIER = 2;
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
        return pieceValue(player)
                + isEnemyDenPenetrated(player, depth)
                + captureMove(player);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return ("Blue Piece Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.bluePlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.bluePlayer(), depth) + "\n" +
                "Capture Move: " + captureMove(board.bluePlayer()) + "\n" +
                "-------------------------------\n" +
                "Red Piece Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.redPlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.redPlayer(), depth) + "\n" +
                "Capture Move: " + captureMove(board.redPlayer()) + "\n" +
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

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int isEnemyDenPenetrated(Player player, int depth) {
        int penetrateEnemyDenScore = 0;
        for (final Move move : player.getValidMoves()) {
            if (player.getAllyColor().isBlue() && move.getDestinationCoordinate() == 3) {
                penetrateEnemyDenScore += PENETRATE_ENEMY_DEN_BONUS * depthBonus(depth);
            } else if (player.getAllyColor().isRed() && move.getDestinationCoordinate() == 59) {
                penetrateEnemyDenScore += PENETRATE_ENEMY_DEN_BONUS * depthBonus(depth);
            }
        }
        return penetrateEnemyDenScore;
    }

    private static int captureMove(final Player player) {
        int captureScore = 0;
        for (final Move move : player.getValidMoves()) {
            if (move.isCaptureMove()) {
                captureScore += move.getCapturedPiece().getPiecePower();
            }
        }
        return captureScore * CAPTURE_MOVE_MULTIPLIER;
    }
}
