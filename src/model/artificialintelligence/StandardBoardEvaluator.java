package model.artificialintelligence;

import model.board.Board;
import model.piece.Piece;
import model.player.Player;
import model.player.PlayerColor;

public final class StandardBoardEvaluator implements GameEvaluator {
    private final static int DEPTH_BONUS = 100;
    private final static int NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS = 5000;
    private final static int NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY = -100;
    private final static int PENETRATE_ENEMY_DEN_BONUS = 50000;

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.getCurrentPlayer(), depth) -
                scorePlayer(board, board.getCurrentPlayer().getEnemyPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceValue(player) + versatility(player)
                + nearEnemyDenWithoutEnemy(player, depth)
                + nearEnemyDenWithEnemy(player)
                + penetrateEnemyDen(player, depth);
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPiecePower();
        }
        return pieceValueScore;
    }

    private static int versatility(Player player) {
        return player.getValidMoves().size();
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

    private static int penetrateEnemyDen(Player player, int depth) {
        return player.penetrateEnemyDen() ? PENETRATE_ENEMY_DEN_BONUS * depthBonus(depth) : 0;
    }
}
