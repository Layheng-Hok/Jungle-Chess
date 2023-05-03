package model.player;

public enum MoveStatus {
    DONE {
        @Override
        boolean isDone() {
            return true;
        }
    },
    INVALID_MOVE {
        @Override
        boolean isDone() {
            return false;
        }
    };
    abstract boolean isDone();
}
