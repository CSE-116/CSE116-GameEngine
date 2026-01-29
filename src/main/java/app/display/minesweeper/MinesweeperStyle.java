package app.display.minesweeper;

import app.Configuration;
import app.display.common.FontManager;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class for describing the visual style of a game of Minesweeper.
 * <p>
 * Most aspects of the visuals can be tweaked with this class, including colors,
 * sizes, and sprites.
 * 
 * @see MinesweeperGame
 * @see MinesweeperMenu
 */
public class MinesweeperStyle {

    /**
     * The file name of the spritesheet containing the face sprites.
     */
    public static final String FACE_FILE = "minesweeper/faces.png";

    /**
     * The file name of the spritesheet containing the tile sprites.
     */
    public static final String TILE_FILE = "minesweeper/tiles.png";

    /**
     * Returns the font size for the UI timer and flag counter.
     * 
     * @return the font size
     */
    public static double fontSizeCounter() {
        return 20 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the large font size.
     * 
     * @return the font size
     */
    public static double fontSizeLarge() {
        return 15 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the medium font size.
     * 
     * @return the font size
     */
    public static double fontSizeMedium() {
        return 10 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the small font size.
     * 
     * @return the font size
     */
    public static double fontSizeSmall() {
        return 7 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the amount of space between buttons.
     * 
     * @return the button spacing
     */
    public static double buttonSpacing() {
        return 3 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the width of buttons.
     * 
     * @return the button width
     */
    public static double buttonWidth() {
        return 70 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the height of buttons.
     * 
     * @return the button height
     */
    public static double buttonHeight() {
        return 15 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the size of the beveled border surrounding buttons.
     * 
     * @return the button border size
     */
    public static double buttonBorderSize() {
        return 1.5 * Configuration.ZOOM;
    }

    /**
     * Returns the width of each entry in the scoreboard.
     * 
     * @return the scoreboard width
     */
    public static double sbWidth() {
        return 100 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the height of each entry in the scoreboard.
     * 
     * @return the scoreboard height
     */
    public static double sbHeight() {
        return 15 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the amount of space between scoreboard entries.
     * 
     * @return the scoreboard padding
     */
    public static double sbPadding() {
        return 2 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the size of the beveled border surrounding scoreboard entries.
     * 
     * @return the scoreboard border size
     */
    public static double sbBorderSize() {
        return 1.5 * Configuration.TEXT_SCALE;
    }

    /**
     * Returns the amount that the the UI timer and flag counter are inset within
     * the UI panel.
     * 
     * @return the counter insets size
     */
    public static double counterInsetsSize() {
        return 2 * Configuration.ZOOM;
    }

    /**
     * Returns the amount of horizontal padding within the UI timer and flag
     * counter.
     * 
     * @return the counter padding size
     */
    public static double counterPadding() {
        return 4 * Configuration.ZOOM;
    }

    /**
     * Returns the size of the beveled borders surrounding the UI timer and flag
     * counter.
     * 
     * @return the counter border size
     */
    public static double counterBorderSize() {
        return 1 * Configuration.ZOOM;
    }

    /**
     * Returns the size of the beveled borders surrounding main UI elements.
     * 
     * @return the UI border size
     */
    public static double uiBorderSize() {
        return 3 * Configuration.ZOOM;
    }

    /**
     * Returns the font used by the UI timer and flag counter.
     * 
     * @return the counter font
     */
    public static Font counterFont() {
        return FontManager.getFont("digital-7/digital-7 (mono).ttf", fontSizeCounter());
    }

    /**
     * Returns the large font used by title elements.
     * 
     * @return the large font
     */
    public static Font largeFont() {
        return FontManager.getFont("digital-7/digital-7.ttf", fontSizeLarge());
    }

    /**
     * Returns the medium font used by most elements.
     * 
     * @return the medium font
     */
    public static Font mediumFont() {
        return FontManager.getFont("digital-7/digital-7.ttf", fontSizeMedium());
    }

    /**
     * Returns the font used by scoreboard entries.
     * 
     * @return the scoreboard font
     */
    public static Font scoreboardFont() {
        return FontManager.getFont("digital-7/digital-7 (mono).ttf", fontSizeMedium());
    }

    /**
     * Returns the small font used by text fields.
     * 
     * @return the small font
     */
    public static Font smallFont() {
        return FontManager.getFont("digital-7/digital-7.ttf", fontSizeSmall());
    }

    /**
     * Returns the color of title elements.
     * 
     * @return the title color
     */
    public static Color titleColor() {
        return Color.BLACK;
    }

    /**
     * Returns the color of scoreboard entries.
     * 
     * @return the scoreboard text color
     */
    public static Color sbTextColor() {
        return Color.LIMEGREEN;
    }

    /**
     * Returns the color of empty scoreboard entries.
     * 
     * @return the scoreboard empty color
     */
    public static Color sbEmptyColor() {
        return Color.DARKGRAY;
    }

    /**
     * Returns the color used when no scoreboard is present. This will likely never
     * be used.
     * 
     * @return the no scoreboard color
     */
    public static Color sbNoneColor() {
        return Color.DARKGRAY;
    }

    /**
     * Returns the background color of scoreboard entries.
     * 
     * @return the scoreboard background color
     */
    public static Color sbBgColor() {
        return Color.BLACK;
    }

    /**
     * Returns the light color of scoreboard entry bevels (the bottom right).
     * 
     * @return the scoreboard light color
     */
    public static Color sbLightColor() {
        return uiLightColor();
    }

    /**
     * Returns the dark color of scoreboard entry bevels (the top left).
     * 
     * @return the scoreboard dark color
     */
    public static Color sbDarkColor() {
        return uiDarkColor();
    }

    /**
     * Returns the button text color.
     * 
     * @return the button text color
     */
    public static Color buttonTextColor() {
        return Color.BLACK;
    }

    /**
     * Returns the button background color.
     * 
     * @return the button background color
     */
    public static Color buttonBgColor() {
        return uiBgColor().deriveColor(0, 1, 1.2, 1);
    }

    /**
     * Returns the light color of button bevels (the top left).
     * 
     * @return the button light color
     */
    public static Color buttonLightColor() {
        return uiLightColor().deriveColor(0, 1, 1.2, 1);
    }

    /**
     * Returns the dark color of button bevels (the bottom right).
     * 
     * @return the button dark color
     */
    public static Color buttonDarkColor() {
        return uiDarkColor().deriveColor(0, 1, 1.2, 1);
    }

    /**
     * Returns the color of a button when the mouse is over it.
     * 
     * @return the button hover color
     */
    public static Color buttonHoverColor() {
        return buttonBgColor().deriveColor(0, 1, 1.1, 1);
    }

    /**
     * Returns the color of a button when it is clicked.
     * 
     * @return the button pressed color
     */
    public static Color buttonPressedColor() {
        return buttonBgColor().deriveColor(0, 1, 0.8, 1);
    }

    /**
     * Returns the main text color of the UI timer and flag counter.
     * 
     * @return the counter text color
     */
    public static Color counterTextColor() {
        return Color.RED;
    }

    /**
     * Returns the secondary text color of the UI timer and flag counter.
     * 
     * @return the secondary counter text color
     */
    public static Color counterSecondaryColor() {
        return Color.DARKRED.deriveColor(0, 1, 0.5, 1);
    }

    /**
     * Returns the background color of the UI timer and flag counter.
     * 
     * @return the counter background color
     */
    public static Color counterBackgroundColor() {
        return Color.BLACK;
    }

    /**
     * Returns the light color of the UI timer and flag counter bevels (the bottom
     * right).
     * 
     * @return the counter light color
     */
    public static Color counterLightColor() {
        return uiLightColor();
    }

    /**
     * Returns the dark color of the UI timer and flag counter bevels (the top
     * left).
     * 
     * @return the counter dark color
     */
    public static Color counterDarkColor() {
        return uiDarkColor();
    }

    /**
     * Returns the background color of the UI panel.
     * 
     * @return the UI panel background color
     */
    public static Color uiBgColor() {
        return Color.web("#a6a6a6");
    }

    /**
     * Returns the background color of the pause menu.
     * 
     * @return the menu background color
     */
    public static Color menuBgColor() {
        return uiBorderColor();
    }

    /**
     * Returns the color of the borders surrounding main UI elements.
     * 
     * @return the UI border color
     */
    public static Color uiBorderColor() {
        return Color.web("#808080");
    }

    /**
     * Returns the light color of the beveled border surrounding main UI elements.
     * 
     * @return the UI light color
     */
    public static Color uiLightColor() {
        return Color.web("#CDCDCD");
    }

    /**
     * Returns the dark color of the beveled border surrounding main UI elements.
     * 
     * @return the UI dark color
     */
    public static Color uiDarkColor() {
        return Color.web("#3F3F3F");
    }

    /**
     * Returns a CSS style used for the tooltips for difficulty descriptions.
     * 
     * @return the tooltip style
     */
    public static String tooltipStyle() {
        return "-fx-background-color: black; " +
                "-fx-text-fill: limegreen; " +
                "-fx-font-family: 'digital-7'; " +
                "-fx-font-size: " + fontSizeSmall() + "px; " +
                "-fx-padding: 4 8 4 8;";
    }

    /**
     * Returns a CSS style used for the text fields for custom difficulties.
     * 
     * @return the text field style
     */
    public static String textFieldStyle() {
        return "-fx-background-color: white; " +
                "-fx-text-fill: black; " +
                "-fx-prompt-text-fill: gray; " +
                "-fx-font-family: 'digital-7'; " +
                "-fx-font-size: " + fontSizeSmall() + "px; " +
                "-fx-padding: 4 8 4 8;";
    }

}
