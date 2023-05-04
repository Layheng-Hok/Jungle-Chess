package model.board;

import model.player.PlayerColor;

public class BoardUtils {
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


    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate an object of \"BoardTools\" class.");
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


}
