package model.artificialintelligence;

import com.google.common.annotations.VisibleForTesting;
import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
    private final static int DEPTH_MULTIPLIER = 10;
    private final static int MOBILITY_MULTIPLIER = 5;
    private final static int CAPTURE_MOVES_MULTIPLIER = 1;
    private static final int ENEMY_RUNNING_OUT_OF_PIECES_MULTIPLIER = 25_000;
    private static final int ENEMY_RUNNING_OUT_OF_VALID_MOVES_MULTIPLIER = 25_000;
    private final static int ENEMY_DEN_PENETRATED_MULTIPLIER = 50_000;
    private static final StandardBoardEvaluator INSTANCE = new StandardBoardEvaluator();

    public static StandardBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board.bluePlayer(), depth) - scorePlayer(board.redPlayer(), depth);
    }

    @VisibleForTesting
    private int scorePlayer(final Player player, final int depth) {
        return pieceValue(player, depth)
                + mobility(player, depth)
                + captureMoves(player)
                + isEnemyDenPenetrated(player, depth);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return ("Blue Board's Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.bluePlayer(), depth) + "\n" +
                "Mobility: " + mobility(board.bluePlayer(), depth) + "\n" +
                "Capture Move: " + captureMoves(board.bluePlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.bluePlayer(), depth) + "\n" +
                "-------------------------------\n" +
                "Red Board's Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.redPlayer(), depth) + "\n" +
                "Mobility: " + mobility(board.redPlayer(), depth) + "\n" +
                "Capture Move: " + captureMoves(board.redPlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.redPlayer(), depth) + "\n" +
                "-------------------------------\n" +
                "Net Score: " + evaluate(board, depth) + "\n");
    }

    private static int pieceValue(final Player player, int depth) {
        int pieceValueScore = 0;
        int enemyOutOfPiecesBonus = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += (piece.getPiecePower() + piece.positionDevelopmentScore());
        }
        if (player.getEnemyPlayer().getActivePieces().isEmpty()) {
            enemyOutOfPiecesBonus = ENEMY_RUNNING_OUT_OF_PIECES_MULTIPLIER * depthBonus(depth);
        }
        return pieceValueScore + enemyOutOfPiecesBonus;
    }

    private static int mobility(Player player, int depth) {
        if (player.getEnemyPlayer().getValidMoves().isEmpty() && player.getEnemyPlayer().getActivePieces().isEmpty()) {
            return ENEMY_RUNNING_OUT_OF_VALID_MOVES_MULTIPLIER * depthBonus(depth);
        }
        if (player.getEnemyPlayer().getValidMoves().isEmpty() && !player.getEnemyPlayer().getActivePieces().isEmpty()) {
            return 2 * ENEMY_RUNNING_OUT_OF_VALID_MOVES_MULTIPLIER * depthBonus(depth);
        }
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(Player player) {
        return (int) ((player.getValidMoves().size() * 10.0f) / player.getEnemyPlayer().getValidMoves().size());
    }

    private static int captureMoves(final Player player) {
        int captureScore = 0;
        for (final Move move : player.getValidMoves()) {
            if (move.isCaptureMove()) {
                captureScore += move.getCapturedPiece().getPiecePower();
            }
        }
        return captureScore * CAPTURE_MOVES_MULTIPLIER;
    }

    private static int isEnemyDenPenetrated(Player player, int depth) {
        return player.getEnemyPlayer().isDenPenetrated() ? ENEMY_DEN_PENETRATED_MULTIPLIER * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_MULTIPLIER * depth;
    }
}
