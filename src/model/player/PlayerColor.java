package model.player;

public enum PlayerColor {
    BLUE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getInverseDirection() {
            return 1;
        }

        @Override
        public boolean isBlue() {
            return true;
        }

        @Override
        public boolean isRed() {
            return false;
        }

        @Override
        public Player choosePlayer(BluePlayer bluePlayer, RedPlayer redPlayer) {
            return bluePlayer;
        }
    },
    RED {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getInverseDirection() {
            return -1;
        }

        @Override
        public boolean isBlue() {
            return false;
        }

        @Override
        public boolean isRed() {
            return true;
        }

        @Override
        public Player choosePlayer(BluePlayer bluePlayer, RedPlayer redPlayer) {
            return redPlayer;
        }
    };

    public abstract int getDirection();
    public abstract int getInverseDirection();
    public abstract boolean isBlue();
    public abstract boolean isRed();
    public abstract Player choosePlayer(BluePlayer bluePlayer, RedPlayer redPlayer);
}
