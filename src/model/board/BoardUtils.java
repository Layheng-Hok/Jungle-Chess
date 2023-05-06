package model.board;

import model.player.PlayerColor;

import java.util.*;

public class BoardUtils {
    public static final int START_TERRAIN_INDEX = 0;
    public static final int NUM_TERRAINS = 63;
    public static final int NUM_TERRAINS_PER_ROW = 7;
    public static final boolean[] COLUMN_ZERO = populateColumn(0);
    public static final boolean[] COLUMN_SIX = populateColumn(6);
    public static final boolean[] ROW_ZERO = populateRow(0);
    public static final boolean[] ROW_ONE = populateRow(7);
    public static final boolean[] ROW_TWO = populateRow(14);
    public static final boolean[] ROW_THREE = populateRow(21);
    public static final boolean[] ROW_FOUR = populateRow(28);
    public static final boolean[] ROW_FIVE = populateRow(35);
    public static final boolean[] ROW_SIX = populateRow(42);
    public static final boolean[] ROW_SEVEN = populateRow(49);
    public static final boolean[] ROW_EIGHT = populateRow(56);
    public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final  Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();


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
        if (pieceColor == PlayerColor.BLUE) {
            return (coordinate == 59);
        } else if (pieceColor == PlayerColor.RED) {
            return (coordinate == 3);
        } else {
            return false;
        }
    }

    public static boolean isRiverOrDen(final int coordinate, PlayerColor pieceColor) {
        return (isRiver(coordinate) || isDen(coordinate, pieceColor));
    }

    public static boolean isEnemyTrap(final int coordinate, PlayerColor pieceColor) {
        if (pieceColor == PlayerColor.BLUE) {
            return (coordinate == 2 || coordinate == 4 || coordinate == 10);
        } else if (pieceColor == PlayerColor.RED) {
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

    private static boolean[] populateRow(int rowIndex) {
        final boolean[] row = new boolean[NUM_TERRAINS];
        do {
            row[rowIndex] = true;
            rowIndex++;
        } while (rowIndex % NUM_TERRAINS_PER_ROW != 0);
        return row;
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
                "(0,0)", "(0,1)", "(0,2)", "(0,3)", "(0,4)", "(0,5)", "(0,6)",
                "(1,0)", "(1,1)", "(1,2)", "(1,3)", "(1,4)", "(1,5)", "(1,6)",
                "(2,0)", "(2,1)", "(2,2)", "(2,3)", "(2,4)", "(2,5)", "(2,6)",
                "(3,0)", "(3,1)", "(3,2)", "(3,3)", "(3,4)", "(3,5)", "(3,6)",
                "(4,0)", "(4,1)", "(4,2)", "(4,3)", "(4,4)", "(4,5)", "(4,6)",
                "(5,0)", "(5,1)", "(5,2)", "(5,3)", "(5,4)", "(5,5)", "(5,6)",
                "(6,0)", "(6,1)", "(6,2)", "(6,3)", "(6,4)", "(6,5)", "(6,6)",
                "(7,0)", "(7,1)", "(7,2)", "(7,3)", "(7,4)", "(7,5)", "(7,6)",
                "(8,0)", "(8,1)", "(8,2)", "(8,3)", "(8,4)", "(8,5)", "(8,6)"));
    }

    public int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
}
