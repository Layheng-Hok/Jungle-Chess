package model.board;

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

    public static boolean isGameOverScenario(final Board board) {
        return board.getCurrentPlayer().isDenPenetrated()
                || board.getCurrentPlayer().getActivePieces().isEmpty()
                || board.getCurrentPlayer().getValidMoves().isEmpty()
                || GameFrame.get().getPlayerPanel().getBlueCurrentTimerSecondsBlitzMode() == 0
                || GameFrame.get().getPlayerPanel().getRedCurrentTimerSecondsBlitzMode() == 0
                || GameFrame.get().isGameResigned();
    }

    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
}
