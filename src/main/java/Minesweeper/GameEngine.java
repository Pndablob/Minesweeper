package Minesweeper;

public class GameEngine {
    private static final Minesweeper iMS = new Minesweeper();

    public static void buttonClicked(int x, int y) {

    }

    public static void buttonRightClicked(int x, int y) {
        MineButton[][] grid = iMS.getGrid();
        MineButton mine = grid[x][y];
        mine.setFlagged(!mine.isFlagged());

    }
}
