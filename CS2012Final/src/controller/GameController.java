package controller;

import javafx.scene.input.KeyEvent;
import model.*;
import view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
  Game controller implementation class,
 * which implements room object generation, neutral creature generation, and player control
 */
public class GameController implements Controller {
    // Room array
    private Entity[][] cells;
    // Map Size
    private CellSizeType type;
    // Player object
    private Player player;
    // Save the picture of the player's previous grid
    private String pre;
    // Current player's position X
    private int playerX;
    // Current player's position Y
    private int playerY;
    // Game view object
    private View view;
    // Random number object, used to randomize the position of the player and the position of the creatures in each room
    private final Random random;
    // Tips for the room around the player
    private String tips;
    // Number of neutral creatures
    private static final int NEUTRAL_CREATURE = 3;
    // Number of traps
    private static final int TRAP_NUM = 3;
    // Number of bullets
    private static final int BULLET_NUM = 3;
    // Whether to win
    private boolean isWin;
    // Whether it is debug mode
    private boolean isDebugMode = false;
    // Picture of trap
    private static final String[] TRAPS = {
            "/trap/bomb.png",
            "/trap/monster.png",
            "/trap/fire.jpg",
            "/trap/trap.png"
    };
    // Neutral Creature Pictures
    private static final String[] NEUTRAL = {
            "/neutral/fox.png",
            "/neutral/bear.png",
            "/neutral/rabbit.jpg",
            "/neutral/monkey.png"
    };
    // Bullet room picture
    private static final String BULLET = "/bullet/bullet.png";
    // Default pictures of each room
    private static final String HOUSE = "/cell/house.png";
    // Player picture
    private static final String PLAYER_IMG = "/player/player.png";
    // BOSS PICTURES
    private static final String BOSS = "/boss/dinosaur.jpg";
    // Trap mark, used to determine whether to fall into a trap
    private final Set<Entity> traps;
    // Bullets, used to store the player bullets
    private final Set<Entity> bullet;
    // Neutral creatures, each neutral creature can only play a role once
    private final Set<Entity> neutral;
    // BOSS OBJECT
    private Entity boss;
    // boss X
    private int bossX;
    // boss Y
    private int bossY;

    /**
      Initialize the basic objects, the real controller initialization needs to read the user input
     */
    public GameController() {
        random = new Random(System.currentTimeMillis());
        tips = "";
        traps = new HashSet<>();
        bullet = new HashSet<>();
        neutral = new HashSet<>();
    }

    //TODO Each shot will cause the boss to move to an adjacent position

    /**
    Handle events of player movement and interaction with other creatures
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        if (player == null)
            return;
        // The game is over
        if (isOver() || isWin()) {
            view.update();
            return;
        }
        // Record the previous picture of the player, in order to restore the picture after the player moves
        if (pre != null)
            cells[playerX][playerY].setImg(pre);
        // The player moves and shoots in four directions
        switch (keyEvent.getCode()) {
            //top
            case W:
                moveUp();
                break;
            case S:
                moveDown();
                break;
            case A:
                moveLeft();
                break;
            case D:
                moveRight();
                break;
            // shoot on top
            case I:
                if (shootUp()) {
                    isWin = true;
                    System.out.println("shoot");
                } else {
                    bossMove();
                }
                break;
            case K:
                if (shootDown()) {
                    isWin = true;
                } else {
                    bossMove();
                }
                break;
            case J:
                if (shootLeft()) {
                    isWin = true;
                } else {
                    bossMove();
                }
                break;
            case L:
                if (shootRight()) {
                    isWin = true;
                } else {
                    bossMove();
                }
                break;
            default:
                return;
        }
        // Determine if the player has died
        move();

        view.update();
    }

    /**
     After the player shoots, the boss is not killed, and the boss randomly moves around once
     */
    private void bossMove() {
        // Randomly choose a direction


        int x = bossX;
        int y = bossY;
        while (x == bossX && y == bossY) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0:
                    bossY = Math.max(bossY - 1, 0);
                    break;
                case 1:
                    bossY = Math.min(type.getWidth() - 1, bossY + 1);
                    break;
                case 2:
                    bossX = Math.max(0, bossX - 1);
                    break;
                case 3:
                    bossX = Math.min(type.getHeight() - 1, bossX + 1);
                    break;
            }
        }
        boolean vis = cells[x][y].isVisible();
        String boss = cells[x][y].getImg();
        String adj = cells[bossX][bossY].getImg();
        cells[x][y].setImg(adj);
        cells[bossX][bossY].setImg(boss);
        cells[bossX][bossY].setVisible(vis);
    }

    /**
     Move the player once, if it encounters a neutral creature, the player will be thrown at a random location on the map
     */
    private boolean move() {
        Entity cell = cells[playerX][playerY];
        cell.setVisible(true);
        if (traps.contains(cell) || bossX == playerX && bossY == playerY) {
            player.dead();
        } else if (bullet.contains(cell)) {
            player.bulletIncr();
            bullet.remove(cell);
        } else if (neutral.contains(cell)) {
            while (true) {
                playerX = random.nextInt(cells.length);
                playerY = random.nextInt(cells[0].length);
                Entity c = cells[playerX][playerY];
                if ((c instanceof Cell) && ((Cell) c).getContent() == null) {
                    break;
                }
            }
            cell = cells[playerX][playerY];
            cell.setVisible(true);
            neutral.remove(cell);
        }
        if (pre == null || !pre.equals(player.getImg()))
            pre = cell.getImg();
        cell.setImg(player.getImg());
        cell.setVisible(true);
        addTips();
        return false;
    }

    /**
      Calculate the prompt information of the player's room creatures
     */
    private void addTips() {
        tips = "";
        if (playerX > 0) {
            tips += cells[playerX - 1][playerY].getDescription() + "\n";
        }
        if (playerX < cells.length - 1) {
            tips += cells[playerX + 1][playerY].getDescription() + "\n";
        }
        if (playerY > 0) {
            tips += cells[playerX][playerY - 1].getDescription() + "\n";
        }
        if (playerY < cells[0].length - 1) {
            tips += cells[playerX][playerY + 1].getDescription() + "\n";
        }
    }

    private void moveUp() {
        if (playerX == 0)
            return;
        playerX--;

    }

    private void moveDown() {
        if (playerX == cells.length - 1)
            return;
        playerX++;
    }

    private void moveLeft() {
        if (playerY == 0)
            return;
        playerY--;
    }

    private void moveRight() {
        if (playerY == cells[0].length - 1)
            return;
        playerY++;
    }

    private boolean shootUp() {
        if (playerX == 0)
            return false;
        return player.shoot() && bossX == playerX - 1 && bossY == playerY;
    }

    private boolean shootDown() {
        if (playerX == cells.length - 1)
            return false;
        return player.shoot() && bossX == playerX + 1 && bossY == playerY;
    }

    private boolean shootLeft() {
        if (playerY == 0)
            return false;
        return player.shoot() && bossX == playerX && bossY == playerY - 1;
    }

    private boolean shootRight() {
        if (playerY == cells[0].length - 1)
            return false;
        return player.shoot() && bossX == playerX && bossY == playerY + 1;
    }

    /**
      Get all room objects
     */
    @Override
    public Entity[][] getCells() {
        return cells;
    }

    /**
     * @param type Types of game maps
     * @return void
     * @author admin
     * @date 2021/5/11 22:24
     * @description There are three types of maps: 55 107 77
     */
    @Override
    public void setSize(CellSizeType type) {
        traps.clear();
        this.bullet.clear();
        neutral.clear();
        player = new Player(PLAYER_IMG);
        isWin = false;
        this.type = type;
        cells = new Entity[type.getHeight()][type.getWidth()];
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                cells[r][c] = new Cell(HOUSE);
            }
        }

        placeNeutral();
        placeTrap();
        placeBullet();
        placeBoss();
        placePlayer();
    }

    /**
     * @author admin
     * @date 2021/5/11 22:41
     * @description Randomly find a location on the map to place the player
     */
    private void placePlayer() {
        while (true) {
            playerX = random.nextInt(cells.length);
            playerY = random.nextInt(cells[0].length);
            if (playerX == 0)
                continue;
            if (cells[playerX - 1][playerY] instanceof Cell) {
                if (((Cell) cells[playerX - 1][playerY]).getContent() == null) {
                    break;
                }
            }
        }

    }

    /**
     * @author admin
     * @date 2021/5/11 22:41
     * @description Put neutral creatures in the blank squares
     */
    private void placeNeutral() {
        int i = 0;
        int numOfNeutral = NEUTRAL_CREATURE;
        switch (type) {
            case TINY:
                numOfNeutral--;
                break;
            case LARGE:
                numOfNeutral++;
                break;
            default:
                break;
        }
        while (i < numOfNeutral) {
            int x = random.nextInt(cells.length);
            int y = random.nextInt(cells[0].length);
            if (cells[x][y] instanceof Cell) {
                Cell cell = ((Cell) cells[x][y]);
                if (cell.getContent() != null) {
                    continue;
                }
                neutral.add(cell);
                cell.setVisible(isDebugMode);
                cell.setContent(new Content(NEUTRAL[i], "Did you hear the voice saying hello to you?"));
                i++;
            }
        }
    }

    /**
     * @author admin
     * @date 2021/5/11 22:41
     * @description Place traps in blank rooms on the map
     */
    private void placeTrap() {
        int i = 0;
        int numOfTrap = TRAP_NUM;
        switch (type) {
            case TINY:
                numOfTrap--;
                break;
            case LARGE:
                numOfTrap++;
                break;
            default:
                break;
        }
        while (i < numOfTrap) {
            int x = random.nextInt(cells.length);
            int y = random.nextInt(cells[0].length);
            if (cells[x][y] instanceof Cell) {
                Cell cell = ((Cell) cells[x][y]);
                if (cell.getContent() != null) {
                    continue;
                }
                cell.setVisible(isDebugMode);
                traps.add(cell);
                cell.setContent(new Content(TRAPS[i], "Be careful, it's terribly quiet around here!"));
                i++;
            }

        }
    }

    /**
     * @author admin
     * @date 2021/5/11 22:42
     * @description Put bullets in blank rooms on the map
     */
    private void placeBullet() {
        int i = 0;
        int numOfBullets = BULLET_NUM;
        switch (type) {
            case TINY:
                numOfBullets--;
                break;
            case LARGE:
                numOfBullets++;
                break;
            default:
                break;
        }
        while (i < numOfBullets) {
            int x = random.nextInt(cells.length);
            int y = random.nextInt(cells[0].length);
            if (cells[x][y] instanceof Cell) {
                Cell cell = ((Cell) cells[x][y]);
                if (cell.getContent() != null) {
                    continue;
                }
                cell.setVisible(isDebugMode);
                bullet.add(cell);
                cell.setContent(new Content(BULLET, "Did you hear the sound of making bullets?"));
                i++;
            }
        }
    }

    /**
     * @author admin
     * @date 2021/5/11 22:42
     * @description Put a blank room on the map into the boss
     */
    private void placeBoss() {
        int i = 0;
        while (i != 1) {
            int x = random.nextInt(cells.length);
            int y = random.nextInt(cells[0].length);
            if (cells[x][y] instanceof Cell) {
                Cell cell = ((Cell) cells[x][y]);
                if (cell.getContent() != null) {
                    continue;
                }
                bossX = x;
                bossY = y;
                cell.setVisible(isDebugMode);
                boss = cell;
                cell.setContent(new Content(BOSS, "There seems to be a high-value treasure nearby, " +
                        "but it may be dangerous!"));
                i++;
            }
        }
    }

    /**
     * @param view The main window view of the game
     * @author admin
     * @date 2021/5/11 22:25
     * @description Game view rendering engine
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * @return java.lang.String Get prompt information provided by surrounding rooms
     * @author admin
     * @date 2021/5/11 22:25
     * @description Get prompt information for display on the window
     */
    @Override
    public String getTips() {
        return tips;
    }

    /**
     * @return model.Player Returns the current player object
     * @author admin
     * @date 2021/5/11 22:26
     * @description Get the player object
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * @return boolean Determine if the game is over
     * @author admin
     * @date 2021/5/11 22:26
     * @description Running out of bullets, being eaten by the boss,
     * or stepping on a trap will cause the player to die and the game will end
     */
    @Override
    public boolean isOver() {
        return !player.isAlive() || player.getBullet() == 0;
    }

    /**
     * @return boolean Have you won
     * @author admin
     * @date 2021/5/11 22:27
     * @description You will win when you kill the boss with bullets, The bullet can't pass through the room
     */
    @Override
    public boolean isWin() {
        return isWin;
    }

    /**
     * @param mode mode
     * @author admin
     * @date 2021/5/11 22:27
     * @description When true, it means it is in debug mode. At this time, all creatures in the room will be displayed.
     */
    @Override
    public void setMode(boolean mode) {
        isDebugMode = mode;
    }
}
