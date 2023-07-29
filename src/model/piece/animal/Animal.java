package model.piece.animal;

public enum Animal {
    TRAPPED_ANIMAL(0, 0, "Trapped Animal"),
    RAT(500, 4, "Rat"),
    CAT(200, 8, "Cat"),
    DOG(300, 7, "Dog"),
    WOLF(400, 6, "Wolf"),
    LEOPARD(500, 5, "Leopard"),
    TIGER(800, 2, "Tiger"),
    LION(900, 1, "Lion"),
    ELEPHANT(1000, 3, "Elephant");

    private final int piecePower;
    private final int movePriority;
    private final String pieceName;

    Animal(final int piecePower, final int movePriority, final String pieceName) {
        this.piecePower = piecePower;
        this.movePriority = movePriority;
        this.pieceName = pieceName;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }

    public int getPiecePower() {
        return this.piecePower;
    }

    public int getMovePriority() {
        return this.movePriority;
    }
}
