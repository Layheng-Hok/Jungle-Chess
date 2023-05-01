package model.board;

import model.player.PlayerColor;

public class BoardTools {
    public static final boolean[] COLUMN_ZERO = null;
    public static final boolean[] COLUMN_SIX = null;

    private BoardTools() {
        throw new RuntimeException("You cannot instantiate an object of \"BoardTools\" class.");
    }

    public static boolean isInBoundary(int coordinate) {
        return coordinate >= 0 && coordinate < 63;
    }

    public static boolean isRiver(int coordinate) {
        return (coordinate == 22 || coordinate == 23 ||
                coordinate == 29 || coordinate == 30 ||
                coordinate == 36 || coordinate == 37 ||
                coordinate == 25 || coordinate == 26 ||
                coordinate == 32 || coordinate == 33 ||
                coordinate == 39 || coordinate == 40);
    }

    public static boolean isDen(int coordinate, PlayerColor pieceColor) {
        if (pieceColor == PlayerColor.BLUE) {
            return (coordinate == 59);
        } else if (pieceColor == PlayerColor.RED) {
            return (coordinate == 3);
        } else {
            return false;
        }
    }

    public static boolean isRiverOrDen(int coordinate, PlayerColor pieceColor) {
        return isRiver(coordinate) || isDen(coordinate, pieceColor);
    }

    public static boolean isTrap(int coordinate, PlayerColor pieceColor) {
        if (pieceColor == PlayerColor.BLUE) {
            return (coordinate == 52 || coordinate == 58 || coordinate == 60);
        } else if (pieceColor == PlayerColor.RED) {
            return (coordinate == 2 || coordinate == 4 || coordinate == 10);
        } else {
            return false;
        }
    }
}
