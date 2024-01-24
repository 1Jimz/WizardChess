import greenfoot.*;

/**
 * Class representing a tile on the game board.
 * Each tile can be empty or occupied by a piece.
 * Tiles may have different statuses (e.g., normal, burning).
 * Handles events related to tiles.
 * 
 * @author Jimmy Zhu, Mekaeel Malik
 * @version January 21st, 2023
 */
public class Tile extends Actor {
    private Piece occupyingPiece;
    private int r, c, status; // 0: normal, 1: burning, 2: idk make something up
    private boolean isNew = false, isBlue = false, isGreen = false;
    private boolean occupied;

    /**
     * Method called when the object is added to the world.
     * It prevents z-sorting problems by placing the tile only once.
     * 
     * @param w The world to which the tile is added.
     */
    public void addedToWorld(World w) {
        if (!isNew) {
            isNew = true;
            BoardManager.placeTile(this, r, c);
        }
    }

    /**
     * Constructor for the Tile class.
     * Initializes the tile with its row and column.
     * 
     * @param r The row index of the tile.
     * @param c The column index of the tile.
     */
    public Tile(int r, int c) {
        this.r = r;
        this.c = c;
        turnNormal();
    }

    /**
     * Act method called on every game cycle.
     * Handles actions related to the tile.
     */
    public void act() {
        // You can uncomment and modify this section for tile-related actions
        // if(occupyingPiece!=null&&occupyingPiece.isDying())empty();
        // if(Greenfoot.mouseClicked(this)){
        //    if(Game.isSpellActivated()){
        //      // Spell activation logic goes here
        //    }
        // }
    }

    /**
     * Set the image of the tile to a red version.
     * Used for highlighting.
     */
    public void turnRed() {
        setImage(new GreenfootImage("Tile_" + (((r + c) % 2 == 0) ? 1 : 0) + "_r.png"));
    }

    /**
     * Set the image of the tile to a green version.
     * Used for highlighting.
     */
    public void turnGreen() {
        setImage(new GreenfootImage("Tile_" + (((r + c) % 2 == 0) ? 1 : 0) + "_g.png"));
        isGreen = true;
    }

    /**
     * Set the image of the tile to a blue version.
     * Used for highlighting.
     */
    public void turnBlue() {
        setImage(new GreenfootImage("Tile_" + (((r + c) % 2 == 0) ? 1 : 0) + "_b.png"));
        isBlue = true;
    }

    /**
     * Set the image of the tile to the normal version.
     */
    public void turnNormal() {
        setImage(new GreenfootImage("Tile_" + (((r + c) % 2 == 0) ? 1 : 0) + ".png"));
        isBlue = false;
    }

    /**
     * Check if the tile is highlighted in blue.
     * 
     * @return True if the tile is highlighted in blue, otherwise false.
     */
    public boolean isBlue() {
        return isBlue;
    }

    /**
     * Check if the tile is highlighted in green.
     * 
     * @return True if the tile is highlighted in green, otherwise false.
     */
    public boolean isGreen() {
        return isGreen;
    }

    /**
     * Get the piece occupying the tile.
     * 
     * @return The piece occupying the tile.
     */
    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }

    /**
     * Check if the tile is empty.
     * 
     * @return True if the tile is empty, otherwise false.
     */
    public boolean isEmpty() {
        return !occupied;
    }

    /**
     * Get the row index of the tile.
     * 
     * @return The row index of the tile.
     */
    public int getR() {
        return r;
    }

    /**
     * Get the column index of the tile.
     * 
     * @return The column index of the tile.
     */
    public int getC() {
        return c;
    }

    /**
     * Place a piece on the tile.
     * 
     * @param p The piece to be placed on the tile.
     */
    public void placePiece(Piece p) {
        getWorld().addObject(p, p.getTargetH(), p.getTargetV() - 30); // -30 for now
        occupyingPiece = p;
        occupied = true;
    }

    /**
     * Remove the occupying piece from the tile, making it empty.
     */
    public void empty() {
        occupyingPiece = null;
        occupied = false;
    }
}
