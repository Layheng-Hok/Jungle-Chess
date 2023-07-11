package model.artificialintelligence;

import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.player.Player;

public final class ConcreteBoardEvaluator implements BoardEvaluator {
    private final static int DEPTH_MULTIPLIER = 100;
    private final static int MOBILITY_MULTIPLIER = 5;
    private final static int CAPTURE_MOVES_MULTIPLIER = 10;
    private final static int NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY = -10000;
    private final static int NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS = 50000;
    private final static int ENEMY_DEN_PENETRATED_MULTIPLIER = 50000;
    private static final int ENEMY_RUNNING_OUT_OF_VALID_MOVES_MULTIPLIER = 50000;
    private static final int ENEMY_RUNNING_OUT_OF_PIECES_MULTIPLIER = 50000;
    private static final ConcreteBoardEvaluator INSTANCE = new ConcreteBoardEvaluator();

    public static ConcreteBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board.bluePlayer(), depth) - scorePlayer(board.redPlayer(), depth);
    }

    private int scorePlayer(final Player player, final int depth) {
        return pieceValue(player)
                + mobility(player)
                + captureMoves(player)
                + getNearEnemyDenWithEnemyNearby(player)
                + getNearEnemyDenWithoutEnemyNearby(player, depth)
                + isEnemyDenPenetrated(player, depth)
                + isEnemyRunningOutOfValidMoves(player, depth)
                + isEnemyRunningOutOfPieces(player, depth);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return ("Blue Board's Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.bluePlayer()) + "\n" +
                "Mobility: " + mobility(board.bluePlayer()) + "\n" +
                "Capture Move: " + captureMoves(board.bluePlayer()) + "\n" +
                "Get Near Enemy Den With Enemy Nearby: " + getNearEnemyDenWithEnemyNearby(board.bluePlayer()) + "\n" +
                "Get Near Enemy Den Without Enemy Nearby: " + getNearEnemyDenWithoutEnemyNearby(board.bluePlayer(), depth) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.bluePlayer(), depth) + "\n" +
                "Is Enemy Running Out Of Valid Moves: " + isEnemyRunningOutOfValidMoves(board.bluePlayer(), depth) + "\n" +
                "Is Enemy Running Out Of Pieces: " + isEnemyRunningOutOfPieces(board.bluePlayer(), depth) + "\n" +
                "-------------------------------\n" +
                "Red Board's Evaluation" + " \n" +
                "Piece Value: " + pieceValue(board.redPlayer()) + "\n" +
                "Mobility: " + mobility(board.redPlayer()) + "\n" +
                "Capture Move: " + captureMoves(board.redPlayer()) + "\n" +
                "Get Near Enemy Den With Enemy Nearby: " + getNearEnemyDenWithEnemyNearby(board.redPlayer()) + "\n" +
                "Get Near Enemy Den Without Enemy Nearyby: " + getNearEnemyDenWithoutEnemyNearby(board.redPlayer(), depth) + "\n" +
                "Is Enemy Den Penetrated: " + isEnemyDenPenetrated(board.redPlayer(), depth) + "\n" +
                "Is Enemy Running Out Of Valid Moves: " + isEnemyRunningOutOfValidMoves(board.bluePlayer(), depth) + "\n" +
                "Is Enemy Running Out Of Pieces: " + isEnemyRunningOutOfPieces(board.bluePlayer(), depth) + "\n" +
                "-------------------------------\n" +
                "Net Score: " + evaluate(board, depth) + "\n");
    }

    static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPiecePower();
        }
        return pieceValueScore;
    }

    private static int mobility(Player player) {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(Player player) {
        return (int) ((player.getValidMoves().size() * 10.0f) / player.getEnemyPlayer().getValidMoves().size());
    }

    static int captureMoves(final Player player) {
        int captureScore = 0;
        for (final Move move : player.getValidMoves()) {
            if (move.isCaptureMove()) {
                captureScore += move.getCapturedPiece().getPiecePower();
            }
        }
        return captureScore * CAPTURE_MOVES_MULTIPLIER;
    }

    private static int getNearEnemyDenWithEnemyNearby(Player player) {
        int getNearEnemyDenPenalty = 0;
        if (player.getAllyColor().isBlue()) {
            for (final Piece piece : player.getActivePieces()) {
                if (piece.getPieceCoordinate() == 2) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 1 || enemyPiece.getPieceCoordinate() == 9) {
                            getNearEnemyDenPenalty += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 4) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 5 || enemyPiece.getPieceCoordinate() == 11) {
                            getNearEnemyDenPenalty += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 10) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 9 || enemyPiece.getPieceCoordinate() == 11 || enemyPiece.getPieceCoordinate() == 17) {
                            getNearEnemyDenPenalty += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
            }
        } else if (player.getAllyColor().isRed()) {
            for (final Piece piece : player.getActivePieces()) {
                if (piece.getPieceCoordinate() == 52) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 45 || enemyPiece.getPieceCoordinate() == 51 || enemyPiece.getPieceCoordinate() == 53) {
                            getNearEnemyDenPenalty += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 58) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 51 || enemyPiece.getPieceCoordinate() == 57) {
                            getNearEnemyDenPenalty += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
                if (piece.getPieceCoordinate() == 60) {
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 53 || enemyPiece.getPieceCoordinate() == 61) {
                            getNearEnemyDenPenalty += NEAR_ENEMY_DEN_WITH_ENEMY_PENALTY;
                        }
                    }
                }
            }
        }
        return getNearEnemyDenPenalty;
    }

    private static int getNearEnemyDenWithoutEnemyNearby(Player player, int depth) {
        int getNearEnemyDenScore = 0;
        if (player.getAllyColor().isBlue()) {
            for (final Piece piece : player.getActivePieces()) {
                if (piece.getPieceCoordinate() == 2) {
                    boolean isEnemyNearDen = false;
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 1 || enemyPiece.getPieceCoordinate() == 9) {
                            isEnemyNearDen = true;
                            break;
                        }
                    }
                    if (!isEnemyNearDen) {
                        getNearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                    }
                }
                if (piece.getPieceCoordinate() == 4) {
                    boolean isEnemyNearDen = false;
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 5 || enemyPiece.getPieceCoordinate() == 11) {
                            isEnemyNearDen = true;
                            break;
                        }
                    }
                    if (!isEnemyNearDen) {
                        getNearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                    }
                }
                if (piece.getPieceCoordinate() == 10) {
                    boolean isEnemyNearDen = false;
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 9 || enemyPiece.getPieceCoordinate() == 11 || enemyPiece.getPieceCoordinate() == 17) {
                            isEnemyNearDen = true;
                            break;
                        }
                    }
                    if (!isEnemyNearDen) {
                        getNearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                    }
                }
            }
        } else if (player.getAllyColor().isRed()) {
            for (final Piece piece : player.getActivePieces()) {
                if (piece.getPieceCoordinate() == 52) {
                    boolean isEnemyNearDen = false;
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 45 || enemyPiece.getPieceCoordinate() == 51 || enemyPiece.getPieceCoordinate() == 53) {
                            isEnemyNearDen = true;
                            break;
                        }
                    }
                    if (!isEnemyNearDen) {
                        getNearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                    }
                }
                if (piece.getPieceCoordinate() == 58) {
                    boolean isEnemyNearDen = false;
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 51 || enemyPiece.getPieceCoordinate() == 57) {
                            isEnemyNearDen = true;
                            break;
                        }
                    }
                    if (!isEnemyNearDen) {
                        getNearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                    }
                }
                if (piece.getPieceCoordinate() == 60) {
                    boolean isEnemyNearDen = false;
                    for (final Piece enemyPiece : player.getEnemyPlayer().getActivePieces()) {
                        if (enemyPiece.getPieceCoordinate() == 53 || enemyPiece.getPieceCoordinate() == 61) {
                            isEnemyNearDen = true;
                            break;
                        }
                    }
                    if (!isEnemyNearDen) {
                        getNearEnemyDenScore += NEAR_ENEMY_DEN_WITHOUT_ENEMY_BONUS;
                    }
                }
            }
        }
        return getNearEnemyDenScore * depthBonus(depth);
    }

    static int isEnemyDenPenetrated(Player player, int depth) {
        return player.getEnemyPlayer().isDenPenetrated() ? ENEMY_DEN_PENETRATED_MULTIPLIER * depthBonus(depth) : 0;
    }

    private static int isEnemyRunningOutOfValidMoves(Player player, int depth) {
        return player.getEnemyPlayer().getValidMoves().size() == 0 ? ENEMY_RUNNING_OUT_OF_VALID_MOVES_MULTIPLIER * depthBonus(depth) : 0;
    }

    private static int isEnemyRunningOutOfPieces(Player player, int depth) {
        return player.getEnemyPlayer().getActivePieces().size() == 0 ? ENEMY_RUNNING_OUT_OF_PIECES_MULTIPLIER * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_MULTIPLIER * depth;
    }
}
