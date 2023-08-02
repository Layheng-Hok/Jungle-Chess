package model.board;

import model.piece.Piece;
import model.player.Player;
import model.player.PlayerColor;
import view.GameFrame;

import java.util.*;

public class BoardUtils {
    public static final int START_TERRAIN_INDEX = 0;
    public static final int NUM_TERRAINS = 63;
    public static final int NUM_TERRAINS_PER_ROW = 7;
    public static final boolean[] COLUMN_ZERO = populateColumn(0);
    public static final boolean[] COLUMN_SIX = populateColumn(6);
    private static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    private static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();


    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate an object of \"BoardTools\" class.");
    }


    public static boolean isInBoundary(final int coordinate) {
        return coordinate >= 0 && coordinate < 63;
    }

    public static boolean isLand(final int coordinate) {
        return (!isRiver(coordinate)
                && !isDen(coordinate, PlayerColor.BLUE) && !isDen(coordinate, PlayerColor.RED)
                && !isEnemyTrap(coordinate, PlayerColor.BLUE) && !isEnemyTrap(coordinate, PlayerColor.RED));
    }

    public static boolean isRiver(final int coordinate) {
        return (coordinate == 22 || coordinate == 23 ||
                coordinate == 29 || coordinate == 30 ||
                coordinate == 36 || coordinate == 37 ||
                coordinate == 25 || coordinate == 26 ||
                coordinate == 32 || coordinate == 33 ||
                coordinate == 39 || coordinate == 40);
    }

    public static boolean isDen(final int coordinate, PlayerColor pieceColor) {
        if (pieceColor.isBlue()) {
            return (coordinate == 59);
        } else if (pieceColor.isRed()) {
            return (coordinate == 3);
        } else {
            return false;
        }
    }

    public static boolean isRiverOrDen(final int coordinate, PlayerColor pieceColor) {
        return (isRiver(coordinate) || isDen(coordinate, pieceColor));
    }

    public static boolean isEnemyTrap(final int coordinate, PlayerColor pieceColor) {
        if (pieceColor.isBlue()) {
            return (coordinate == 2 || coordinate == 4 || coordinate == 10);
        } else if (pieceColor.isRed()) {
            return (coordinate == 52 || coordinate == 58 || coordinate == 60);
        } else {
            return false;
        }
    }

    private static boolean[] populateColumn(int columnIndex) {
        final boolean[] column = new boolean[NUM_TERRAINS];
        do {
            column[columnIndex] = true;
            columnIndex += NUM_TERRAINS_PER_ROW;
        } while (columnIndex < NUM_TERRAINS);
        return column;
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TERRAIN_INDEX; i < NUM_TERRAINS; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a9", "b9", "c9", "d9", "e9", "f9", "g9",
                "a8", "b8", "c8", "d8", "e8", "f8", "g8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1"));
    }

    public static List<Move> lastNMoves(final Board board, int N) {
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = board.getTransitionMove();
        int i = 0;
        while (currentMove != Move.MoveFactory.getNullMove() && i < N) {
            moveHistory.add(currentMove);
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        return Collections.unmodifiableList(moveHistory);
    }

    public static void checkThreeFoldRepetition(MoveLog currentPlayerMoveLog) {
        if (currentPlayerMoveLog.size() < 6) {
            return;
        }
        final Piece last2MovesPiece = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 2).getMovedPiece();
        final Piece last4MovesPiece = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 4).getMovedPiece();
        final Piece last6MovesPiece = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 6).getMovedPiece();
        if (last2MovesPiece.equalsTypeTwo(last4MovesPiece) && last4MovesPiece.equalsTypeTwo(last6MovesPiece)) {
            final int last2MovesDestination = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 2).getDestinationCoordinate();
            final int last4MovesDestination = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 4).getDestinationCoordinate();
            final int last6MovesDestination = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 6).getDestinationCoordinate();
            if (last2MovesDestination == last4MovesDestination && last4MovesDestination == last6MovesDestination) {
                if (clearRepetitionMoveLog(currentPlayerMoveLog)) {
                    if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isBlue()) {
                        GameFrame.get().getBlueMoveLog().clear();
                    } else if (GameFrame.get().getChessBoard().getCurrentPlayer().getAllyColor().isRed()) {
                        GameFrame.get().getRedMoveLog().clear();
                    }
                    return;
                }
                List<Move> currentPlayerOldValidMoves = (List<Move>) GameFrame.get().getChessBoard().getCurrentPlayer().getValidMoves();
                List<Move> currentPlayerNewValidMoves = new ArrayList<>();
                for (Move currentPlayerOldValidMove : currentPlayerOldValidMoves) {
                    if (currentPlayerOldValidMove.getDestinationCoordinate() != last2MovesDestination) {
                        currentPlayerNewValidMoves.add(currentPlayerOldValidMove);
                    } else if (currentPlayerOldValidMove.getDestinationCoordinate() == last2MovesDestination) {
                        currentPlayerNewValidMoves.add(Move.MoveFactory.createBannedRepetitiveMove(
                                currentPlayerOldValidMove.getBoard(),
                                currentPlayerOldValidMove.getCurrentCoordinate(),
                                currentPlayerOldValidMove.getDestinationCoordinate()));
                    }
                }
                GameFrame.get().getChessBoard().getCurrentPlayer().setValidMoves(Collections.unmodifiableList(currentPlayerNewValidMoves));
            }
        }
    }

    private static boolean clearRepetitionMoveLog(MoveLog currentPlayerMoveLog) {
        boolean shouldBeCleared = false;
        if (currentPlayerMoveLog.size() < 7) {
            return false;
        }
        final Piece last3MovesPiece = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 3).getMovedPiece();
        final Piece last5MovesPiece = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 5).getMovedPiece();
        final Piece last7MovesPiece = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 7).getMovedPiece();
        if (last3MovesPiece.equalsTypeTwo(last5MovesPiece) && last5MovesPiece.equalsTypeTwo(last7MovesPiece)) {
            final int last3MovesDestination = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 3).getDestinationCoordinate();
            final int last5MovesDestination = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 5).getDestinationCoordinate();
            final int last7MovesDestination = currentPlayerMoveLog.getMove(currentPlayerMoveLog.size() - 7).getDestinationCoordinate();
            if (last3MovesDestination == last5MovesDestination && last5MovesDestination == last7MovesDestination) {
                shouldBeCleared = true;
            }
        }
        return shouldBeCleared;
    }

    public static boolean getIntoEnemyTrap(final Move move) {
        final Player moveMaker = move.getBoard().getCurrentPlayer();
        boolean isInEnemyTrap = false;
        if (moveMaker.getAllyColor().isBlue()) {
            if (move.getDestinationCoordinate() == 2
                    || move.getDestinationCoordinate() == 4
                    || move.getDestinationCoordinate() == 10) {
                isInEnemyTrap = true;
            }
        } else if (moveMaker.getAllyColor().isRed()) {
            if (move.getDestinationCoordinate() == 52
                    || move.getDestinationCoordinate() == 58
                    || move.getDestinationCoordinate() == 60) {
                isInEnemyTrap = true;
            }
        }
        return isInEnemyTrap;
    }

    public static boolean isGameOverScenarioStandardConditions(final Board board) {
        return board.getCurrentPlayer().isDenPenetrated()
                || board.getCurrentPlayer().getActivePieces().isEmpty()
                || board.getCurrentPlayer().getValidMoves().isEmpty();
    }

    private static boolean isGameOverScenarioSpecialGameModeConditions() {
        return GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode() == 0
                || GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode() == 0
                || GameFrame.get().isGameResigned()
                || isGameDrawnScenario();
    }

    public static boolean isGameDrawnScenario() {
        return GameFrame.get().getPlayerPanel().getRoundNumber() == 201;
    }

    public static boolean isGameOverScenario(final Board board) {
        return isGameOverScenarioStandardConditions(board)
                || isGameOverScenarioSpecialGameModeConditions();
    }

    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
}
