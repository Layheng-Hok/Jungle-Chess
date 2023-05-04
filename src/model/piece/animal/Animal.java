package model.piece.animal;

public enum Animal {
    TRAPPED_ANIMAL ("0", "A"),
    RAT ("一", "R"),
    CAT ("二", "C"),
    DOG ("三",  "D"),
    WOLF ("四", "W"),
    LEOPARD ("五", "P"),
    TIGER ("六", "T"),
    LION("七", "L"),
    ELEPHANT("八", "E");

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
