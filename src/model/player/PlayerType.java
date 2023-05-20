package model.player;

public enum PlayerType {
    HUMAN {
        @Override
        public String toString() {
            return "Human";
        }
    },
    AI {
        @Override
        public String toString() {
            return "AI";
        }
    }
}
