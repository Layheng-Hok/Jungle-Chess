package model.player;

public enum MoveStatus {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    INVALID_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();
}
