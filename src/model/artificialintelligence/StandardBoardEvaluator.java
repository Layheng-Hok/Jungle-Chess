package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.player.Player;
import model.player.PlayerColor;

public final class StandardBoardEvaluator implements GameEvaluator {
    private final static int DEPTH_BONUS = 100;
    private final static int VERSATILITY_MULTIPLIER = 5;
    private final static int NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS = 5000;
    private final static int NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY = -100;
    private final static int PENETRATE_ENEMY_DEN_BONUS = 50000;
    private final static int CAPTURE_MOVE_MULTIPLIER = 2;

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.getCurrentPlayer(), depth) -
                scorePlayer(board, board.getCurrentPlayer().getEnemyPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceValue(player)
                + versatility(player)
                + nearEnemyDenWithoutEnemy(player, depth)
                + nearEnemyDenWithEnemy(player)
                + isEnemyDenPenetrated(player, depth)
                + captureMove(player);
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
        return player.getEnemyPlayer().isDenPenetrated() ? PENETRATE_ENEMY_DEN_BONUS * depthBonus(depth) : 0;
    }

    private static int captureMove(final Player player) {
        int captureScore = 0;
        for(final Move move : player.getValidMoves()) {
            if(move.isCaptureMove()) {
                captureScore += move.getCapturedPiece().getPiecePower();
            }
        }
        return captureScore * CAPTURE_MOVE_MULTIPLIER;
    }
}
