package model.piece.animal;

public enum Animal {
    TRAPPED_ANIMAL ("0", "困"),
    RAT ("一", "鼠"),
    CAT ("二", "猫"),
    DOG ("三",  "狗"),
    WOLF ("四", "狼"),
    LEOPARD ("五", "豹"),
    TIGER ("六", "虎"),
    LION("七", "狮"),
    ELEPHANT("八", "象");

    private final String pieceRank;
    private final String pieceName;

    Animal(final String pieceRank, final String pieceName) {
        this.pieceRank = pieceRank;
        this.pieceName = pieceName;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }

    public String getPieceRank() {
        return this.pieceRank;
    }
}
