import greenfoot.*;
/**
 * <h1>Text Class</h1>
 * <p>The <em>Text</em> class represents text that can be displayed in the game. It allows customization of the text's size, font, color, and content.</p>
 *
 * <h2>Class Attributes:</h2>
 * <ul>
 *     <li><strong>type (int):</strong> An integer representing the type of text.</li>
 *     <li><strong>size (int):</strong> The size of the text.</li>
 *     <li><strong>font (String):</strong> The font style of the text.</li>
 * </ul>
 * 
 * @Jimmy Zhu
 * @1124
 */
public class Text extends Widget
{
    private int type,size;
    private String font;
    /**
     * <h2>Constructors:</h2>
     * <ul>
     *     <li><strong>Text(int size, String font, String text, int type):</strong> Creates a text with the specified size, font, text content, and type.</li>
     *     <li><strong>Text(int size, String font, String text):</strong> Creates a text with the specified size, font, and text content.</li>
     * </ul>
     *
     */
    public Text(int size, String font, String text, int type){
        this.size=size;
        this.font=font;
        changeText(text);
        this.type=type;
    }
    /**
     * <h3>Text(int size, String font, String text)</h3>
     * <p>Creates a text with the specified size, font, and text content.</p>
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *     <li><strong>size (int):</strong> The size of the text.</li>
     *     <li><strong>font (String):</strong> The font style of the text.</li>
     *     <li><strong>text (String):</strong> The text content of the text object.</li>
     * </ul>
     */
    public Text(int size, String font, String text){
        this.size=size;
        this.font=font;
        changeText(text);
        this.type=type;
    }
    /**
     * <h3>changeText(String text)</h3>
     * <p>Changes the text content of the text object.</p>
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *     <li><strong>text (String):</strong> The new text content.</li>
     * </ul>
     */
    public void changeText(String text){
        GreenfootImage gfi = new GreenfootImage(size*6,size*2);
        gfi.setColor(Color.BLACK);
        gfi.setFont(new Font(font, true, false, size)); 
        gfi.drawString(text, size/2, size);
        setImage(gfi);
    }
    /**
     * <h3>changeText(String text, Color c)</h3>
     * <p>Changes the text content and color of the text object.</p>
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *     <li><strong>text (String):</strong> The new text content.</li>
     *     <li><strong>c (Color):</strong> The new color of the text.</li>
     * </ul>
     */
    public void changeText(String text, Color c){
        GreenfootImage gfi = new GreenfootImage(size*6,size*2);
        gfi.setColor(c);
        gfi.setFont(new Font(font, true, false, size)); 
        gfi.drawString(text, size/2, size);
        setImage(gfi);
    }
    /**
     * <h3>updateText()</h3>
     * <p>Updates the Text objects with the latest statistics.</p>
     */
    private static void updateText()
    {
        //textBoxes[0].changeText(casinoTarget + "");
        //textBoxes[1].changeText(moneySpent + "");
        //textBoxes[2].changeText(casinoProfit + "");
        //textBoxes[3].changeText(gamblerWins + "/" + gamblerLosses);
    }
}
