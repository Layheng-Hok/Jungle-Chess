package model.player;

public enum PlayerColor {
    BLUE {
        @Override
        public String toString() {
            return "Blue";
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
        public int getDirection() {
            return UP_DIRECTION;
        }

        @Override
        public int getInverseDirection() {
            return DOWN_DIRECTION;
        }

        @Override
        public Player choosePlayer(BluePlayer bluePlayer, RedPlayer redPlayer) {
            return bluePlayer;
        }

        @Override
        public int ratDevelopmentScore(int position) {
            return BLUE_RAT_PREFERRED_POSITIONS[position];
        }

        @Override
        public int catDevelopmentScore(int position) {
            return BLUE_CAT_PREFERRED_POSITIONS[position];
        }

        @Override
        public int dogDevelopmentScore(int position) {
            return BLUE_DOG_PREFERRED_POSITIONS[position];
        }

        @Override
        public int wolfDevelopmentScore(int position) {
            return BLUE_WOLF_PREFERRED_POSITIONS[position];
        }

        @Override
        public int leopardDevelopmentScore(int position) {
            return BLUE_LEOPARD_PREFERRED_POSITIONS[position];
        }

        @Override
        public int tigerDevelopmentScore(int position) {
            return BLUE_TIGER_PREFERRED_POSITIONS[position];
        }

        @Override
        public int lionDevelopmentScore(int position) {
            return BLUE_LION_PREFERRED_POSITIONS[position];
        }

        @Override
        public int elephantDevelopmentScore(int position) {
            return BLUE_ELEPHANT_PREFERRED_POSITIONS[position];
        }
    },

    RED {
        @Override
        public String toString() {
            return "Red";
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
        public int getDirection() {
            return DOWN_DIRECTION;
        }

        @Override
        public int getInverseDirection() {
            return UP_DIRECTION;
        }

        @Override
        public Player choosePlayer(BluePlayer bluePlayer, RedPlayer redPlayer) {
            return redPlayer;
        }

        @Override
        public int ratDevelopmentScore(int position) {
            return RED_RAT_PREFERRED_POSITIONS[position];
        }

        @Override
        public int catDevelopmentScore(int position) {
            return RED_CAT_PREFERRED_POSITIONS[position];
        }

        @Override
        public int dogDevelopmentScore(int position) {
            return RED_DOG_PREFERRED_POSITIONS[position];
        }

        @Override
        public int wolfDevelopmentScore(int position) {
            return RED_WOLF_PREFERRED_POSITIONS[position];
        }

        @Override
        public int leopardDevelopmentScore(int position) {
            return RED_LEOPARD_PREFERRED_POSITIONS[position];
        }

        @Override
        public int tigerDevelopmentScore(int position) {
            return RED_TIGER_PREFERRED_POSITIONS[position];
        }

        @Override
        public int lionDevelopmentScore(int position) {
            return RED_LION_PREFERRED_POSITIONS[position];
        }

        @Override
        public int elephantDevelopmentScore(int position) {
            return RED_ELEPHANT_PREFERRED_POSITIONS[position];
        }
    };

    public abstract boolean isBlue();

    public abstract boolean isRed();

    public abstract int getDirection();

    public abstract int getInverseDirection();

    public abstract Player choosePlayer(BluePlayer bluePlayer, RedPlayer redPlayer);

    public abstract int ratDevelopmentScore(int position);

    public abstract int catDevelopmentScore(int position);

    public abstract int dogDevelopmentScore(int position);

    public abstract int wolfDevelopmentScore(int position);

    public abstract int leopardDevelopmentScore(int position);

    public abstract int tigerDevelopmentScore(int position);

    public abstract int lionDevelopmentScore(int position);

    public abstract int elephantDevelopmentScore(int position);

    private static final int UP_DIRECTION = -1;

    private static final int DOWN_DIRECTION = 1;

    private static final int[] BLUE_RAT_PREFERRED_POSITIONS = {
            11, 13, 50, 2_000_000_000, 50, 13, 13,
            11, 12, 13, 50, 13, 13, 13,
            10, 11, 11, 13, 13, 13, 13,
            8, 9, 9, 11, 12, 12, 13,
            8, 9, 9, 11, 12, 12, 12,
            8, 9, 9, 10, 12, 12, 11,
            8, 8, 8, 9, 10, 10, 10,
            8, 8, 8, 9, 9, 9, 9,
            8, 8, 8, 0, 8, 8, 8
    };

    private static final int[] RED_RAT_PREFERRED_POSITIONS = {
            8, 8, 8, 0, 8, 8, 8,
            9, 9, 9, 9, 8, 8, 8,
            10, 10, 10, 9, 8, 8, 8,
            11, 12, 12, 10, 9, 9, 8,
            12, 12, 12, 11, 9, 9, 8,
            13, 12, 12, 11, 9, 9, 8,
            13, 13, 13, 13, 11, 11, 10,
            13, 13, 13, 50, 13, 12, 11,
            13, 13, 50, 2_000_000_000, 50, 13, 11
    };

    private static final int[] BLUE_CAT_PREFERRED_POSITIONS = {
            11, 15, 50, 2_000_000_000, 50, 15, 11,
            11, 11, 15, 50, 15, 11, 11,
            10, 11, 11, 15, 11, 11, 10,
            10, 0, 0, 10, 0, 0, 8,
            10, 0, 0, 8, 0, 0, 8,
            10, 0, 0, 8, 0, 0, 8,
            10, 10, 10, 8, 8, 8, 8,
            13, 10, 8, 8, 8, 8, 8,
            8, 8, 8, 0, 8, 8, 8
    };

    private static final int[] RED_CAT_PREFERRED_POSITIONS = {
            8, 8, 8, 0, 8, 8, 8,
            8, 8, 8, 8, 8, 10, 13,
            8, 8, 8, 8, 10, 10, 10,
            8, 0, 0, 8, 0, 0, 10,
            8, 0, 0, 8, 0, 0, 10,
            8, 0, 0, 10, 0, 0, 10,
            10, 11, 11, 15, 11, 11, 10,
            11, 11, 15, 50, 15, 11, 11,
            11, 15, 50, 2_000_000_000, 50, 15, 11
    };

    private static final int[] BLUE_DOG_PREFERRED_POSITIONS = {
            11, 15, 50, 2_000_000_000, 50, 15, 11,
            10, 11, 15, 50, 15, 11, 10,
            9, 10, 11, 15, 11, 10, 9,
            9, 0, 0, 10, 0, 0, 9,
            8, 0, 0, 8, 0, 0, 8,
            8, 0, 0, 8, 0, 0, 8,
            8, 8, 8, 8, 8, 8, 8,
            8, 8, 8, 8, 13, 10, 8,
            8, 8, 8, 0, 12, 12, 8
    };

    private static final int[] RED_DOG_PREFERRED_POSITIONS = {
            8, 12, 12, 0, 8, 8, 8,
            8, 10, 13, 8, 8, 8, 8,
            8, 8, 8, 8, 8, 8, 8,
            8, 0, 0, 8, 0, 0, 8,
            8, 0, 0, 8, 0, 0, 8,
            9, 0, 0, 10, 0, 0, 9,
            9, 10, 11, 15, 11, 10, 9,
            10, 11, 15, 50, 15, 11, 10,
            11, 15, 50, 2_000_000_000, 50, 15, 11
    };

    private static final int[] BLUE_WOLF_PREFERRED_POSITIONS = {
            11, 15, 50, 2_000_000_000, 50, 15, 11,
            10, 11, 15, 50, 15, 11, 10,
            9, 10, 11, 15, 11, 10, 9,
            9, 0, 0, 10, 0, 0, 9,
            8, 0, 0, 8, 0, 0, 8,
            8, 0, 0, 8, 0, 0, 8,
            8, 8, 10, 8, 8, 8, 8,
            8, 12, 13, 8, 8, 8, 8,
            8, 12, 12, 0, 8, 8, 8
    };

    private static final int[] RED_WOLF_PREFERRED_POSITIONS = {
            8, 8, 8, 0, 12, 12, 8,
            8, 8, 8, 8, 13, 12, 8,
            8, 8, 8, 8, 10, 8, 8,
            8, 0, 0, 8, 0, 0, 8,
            8, 0, 0, 8, 0, 0, 8,
            9, 0, 0, 10, 0, 0, 9,
            9, 10, 11, 15, 11, 10, 9,
            10, 11, 15, 50, 15, 11, 10,
            11, 15, 50, 2_000_000_000, 50, 15, 11
    };

    private static final int[] BLUE_LEOPARD_PREFERRED_POSITIONS = {
            14, 15, 50, 2_000_000_000, 50, 15, 14,
            13, 14, 15, 50, 15, 14, 13,
            13, 13, 14, 15, 14, 13, 13,
            12, 0, 0, 15, 0, 0, 12,
            11, 0, 0, 14, 0, 0, 11,
            10, 0, 0, 13, 0, 0, 10,
            9, 9, 9, 10, 10, 9, 9,
            9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 0, 9, 9, 9
    };

    private static final int[] RED_LEOPARD_PREFERRED_POSITIONS = {
            9, 9, 9, 0, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9,
            9, 9, 10, 10, 9, 9, 9,
            10, 0, 0, 13, 0, 0, 10,
            11, 0, 0, 14, 0, 0, 11,
            12, 0, 0, 15, 0, 0, 12,
            13, 13, 14, 15, 14, 13, 13,
            13, 14, 15, 50, 15, 14, 13,
            14, 15, 50, 2_000_000_000, 50, 15, 14
    };

    private static final int[] BLUE_TIGER_PREFERRED_POSITIONS = {
            25, 30, 50, 2_000_000_000, 50, 30, 25,
            25, 25, 30, 50, 30, 25, 25,
            18, 20, 20, 30, 20, 20, 18,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            14, 16, 16, 14, 16, 16, 14,
            12, 14, 12, 12, 12, 12, 12,
            10, 12, 12, 0, 12, 12, 10
    };

    private static final int[] RED_TIGER_PREFERRED_POSITIONS = {
            10, 12, 12, 0, 12, 12, 10,
            12, 12, 12, 12, 12, 14, 12,
            14, 16, 16, 14, 16, 16, 14,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            18, 20, 20, 30, 20, 20, 18,
            25, 25, 30, 50, 30, 25, 25,
            25, 30, 50, 2_000_000_000, 50, 30, 25
    };

    private static final int[] BLUE_LION_PREFERRED_POSITIONS = {
            25, 30, 50, 2_000_000_000, 50, 30, 25,
            25, 25, 30, 50, 30, 25, 25,
            18, 20, 20, 30, 20, 20, 18,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            14, 16, 16, 14, 16, 16, 14,
            12, 12, 12, 12, 12, 14, 12,
            10, 12, 12, 0, 12, 12, 10
    };

    private static final int[] RED_LION_PREFERRED_POSITIONS = {
            10, 12, 12, 0, 12, 12, 10,
            12, 14, 12, 12, 12, 12, 12,
            14, 16, 16, 14, 16, 16, 14,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            15, 0, 0, 15, 0, 0, 15,
            18, 20, 20, 30, 20, 20, 18,
            25, 25, 30, 50, 30, 25, 25,
            25, 30, 50, 2_000_000_000, 50, 30, 25
    };

    private static final int[] BLUE_ELEPHANT_PREFERRED_POSITIONS = {
            25, 30, 50, 2_000_000_000, 50, 30, 25,
            25, 25, 30, 50, 30, 25, 25,
            18, 20, 20, 30, 20, 20, 18,
            16, 0, 0, 16, 0, 0, 16,
            14, 0, 0, 14, 0, 0, 14,
            12, 0, 0, 12, 0, 0, 12,
            10, 15, 14, 14, 14, 14, 12,
            11, 11, 11, 11, 11, 11, 11,
            11, 11, 11, 0, 11, 11, 11
    };

    private static final int[] RED_ELEPHANT_PREFERRED_POSITIONS = {
            11, 11, 11, 0, 11, 11, 11,
            11, 11, 11, 11, 11, 11, 11,
            12, 14, 14, 14, 14, 15, 10,
            12, 0, 0, 12, 0, 0, 12,
            14, 0, 0, 14, 0, 0, 14,
            16, 0, 0, 16, 0, 0, 16,
            18, 20, 20, 30, 20, 20, 18,
            25, 25, 30, 50, 30, 25, 25,
            25, 30, 50, 2_000_000_000, 50, 30, 25
    };
}
