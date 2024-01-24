import greenfoot.*;

/**
 * The `CardManager` class manages the throwing of cards in the game.
 * It includes predefined card throwing configurations and a method to initiate the card throw.
 * @author Jimmy Zhu
 * @version January 21st, 2023
*/
public class CardManager {
    
    // Card throw configurations: {drag point x, drag point y, drag active, left border, spawn x, spawn y}
    private static int cardThrows[][] = {
        {440, 180, 430, 0, -100, 1000},
        {350, 100, 400, 0, -50, 1000},
        {0, -1280, 721, 1, 370, 994},
        {258, -970, 425, 1, 185, 1000},
        {0, -400, 1090, 1, 300, 1000},
        {-300, -1100, 650, 1, 500, 1200},
        {420, -2600, 215, 1, 280, 1000},
        {200, -400, 620, 1, 300, 1150}
    };

    /**
     * Initiate a card throw with a randomly selected configuration.
     * The method chooses a random card throw configuration and invokes the throwCard method in the Game class.
     */
    public static void readyThrowCard() {
        int random = Greenfoot.getRandomNumber(cardThrows.length);
        Game.throwCard(
            cardThrows[random][0],   // drag point x
            cardThrows[random][1],   // drag point y
            cardThrows[random][2],   // drag active
            cardThrows[random][3],   // left border
            cardThrows[random][4],   // spawn x
            cardThrows[random][5]    // spawn y
        );
    }
}
