package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.player.Player;
import model.player.PlayerColor;

public final class ConcreteBoardEvaluator implements BoardEvaluator {
    private final static int DEPTH_BONUS = 100;
    private final static int VERSATILITY_MULTIPLIER = 5;
    private final static int NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS = 5000;
    private final static int NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY = -1000;
    private final static int PENETRATE_ENEMY_DEN_BONUS = 50000;
    private final static int CAPTURE_MOVE_MULTIPLIER = 2;
    private static final ConcreteBoardEvaluator INSTANCE = new ConcreteBoardEvaluator();

    private ConcreteBoardEvaluator() {
    }

    public static ConcreteBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board.bluePlayer(), depth) -
                scorePlayer(board.redPlayer(), depth);
    }

    private int scorePlayer(final Player player, final int depth) {
        return pieceValue(player)
                + versatility(player)
                + nearEnemyDenWithoutEnemy(player, depth)
                + nearEnemyDenWithEnemy(player)
                + isEnemyDenPenetrated(player, depth)
                + captureMove(player);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return ("Blue Piece Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.bluePlayer()) + "\n" +
                "Versatility: " + versatility(board.bluePlayer()) + "\n" +
                "Near Enemy Den Without Enemy: " + nearEnemyDenWithoutEnemy(board.bluePlayer(), depth) + "\n" +
                "Near Enemy Den With Enemy: " + nearEnemyDenWithEnemy(board.bluePlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.bluePlayer(), depth) + "\n" +
                "Capture Move: " + captureMove(board.bluePlayer()) + "\n" +
                "-------------------------------\n" +
                "Red Piece Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.redPlayer()) + "\n" +
                "Versatility: " + versatility(board.redPlayer()) + "\n" +
                "Near Enemy Den Without Enemy: " + nearEnemyDenWithoutEnemy(board.redPlayer(), depth) + "\n" +
                "Near Enemy Den With Enemy: " + nearEnemyDenWithEnemy(board.redPlayer()) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.redPlayer(), depth) + "\n" +
                "Capture Move: " + captureMove(board.redPlayer()) + "\n" +
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

    private static int versatility(Player player) {
        return VERSATILITY_MULTIPLIER * versatilityRatio(player);
    }

    private static int versatilityRatio(Player player) {
        return (int) ((player.getValidMoves().size() * 10.0f) / player.getEnemyPlayer().getValidMoves().size());
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int nearEnemyDenWithoutEnemy(Player player, int depth) {
        int nearEnemyDenScore = 0;
        if (player.getAllyColor() == PlayerColor.BLUE) {
            for (final Piece piece : player.getActivePieces()) {
                if (piece.getPieceCoordinate() == 2) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() != 1 && enemyPiece.getPieceCoordinate() != 9) {
                            nearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 4) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() != 5 && enemyPiece.getPieceCoordinate() != 11) {
                            nearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 10) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() != 9 && enemyPiece.getPieceCoordinate() != 11 && enemyPiece.getPieceCoordinate() != 17) {
                            nearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                        }
                    }
                }
            }
        }
        return nearEnemyDenScore * depthBonus(depth);
    }

    private static int nearEnemyDenWithEnemy(Player player) {
        int nearEnemyDenScore = 0;
        if (player.getAllyColor() == PlayerColor.BLUE) {
            for (final Piece piece : player.getActivePieces()) {
                if (piece.getPieceCoordinate() == 2) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 1 || enemyPiece.getPieceCoordinate() == 9) {
                            nearEnemyDenScore += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 4) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 5 || enemyPiece.getPieceCoordinate() == 11) {
                            nearEnemyDenScore += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 10) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 9 || enemyPiece.getPieceCoordinate() == 11 || enemyPiece.getPieceCoordinate() == 17) {
                            nearEnemyDenScore += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
            }
        }
        return nearEnemyDenScore;
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
