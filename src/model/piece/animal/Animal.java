package model.piece.animal;

public enum Animal {
    TRAPPED_ANIMAL ("0", "Trapped Animal"),
    RAT ("1", "Rat"),
    CAT ("2", "Cat"),
    DOG ("3",  "Dog"),
    WOLF ("4", "Wolf"),
    LEOPARD ("5", "Leopard"),
    TIGER ("6", "Tiger"),
    LION("7", "Lion"),
    ELEPHANT("8", "Elephant");

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
