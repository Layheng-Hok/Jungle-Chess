package model.player;

public enum PlayerColor {
    BLUE,
    RED;

    public boolean isBlue() {
        return this == PlayerColor.BLUE;
    }

    public boolean isRed() {
        return this == PlayerColor.RED;
    }
}
