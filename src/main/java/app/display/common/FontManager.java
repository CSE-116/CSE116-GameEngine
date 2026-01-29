package app.display.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import app.Configuration;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Manages loading and caching of custom fonts for the game UI and graphics.
 * <p>
 * Provides static methods to retrieve fonts with various weights, postures, and
 * sizes. Fonts are loaded from disk only once and reused by family name for
 * efficiency. If a font file cannot be found, the system default font is used
 * instead.
 * <p>
 * Note that using system fonts can be unreliable, as other devices may lack
 * those fonts. As such, any font used besides the system default should be
 * placed in the fonts directory and loaded with this class.
 *
 * @see javafx.scene.text.Font
 * @see Configuration
 */
public class FontManager {

    /**
     * Prevent instantiation, as this class is intended to be static.
     */
    private FontManager() {
    }

    private static HashMap<String, String> loadedFonts = new HashMap<>();
    private static final String FONTS_DIRECTORY = "data/fonts/";

    /**
     * Returns a font loaded from the specified file with default weight, posture,
     * and size.
     *
     * @param fileName the font file name within the fonts directory
     * @return the loaded {@code Font} object, or system default if not found
     */
    public static Font getFont(String fileName) {
        return getFont(fileName, FontWeight.NORMAL, FontPosture.REGULAR, Configuration.DEFAULT_TEXT_SIZE);
    }

    /**
     * Returns a font loaded from the specified file with the given size and default
     * weight/posture.
     *
     * @param fileName the font file name within the fonts directory
     * @param size     the font size
     * @return the loaded {@code Font} object, or system default if not found
     */
    public static Font getFont(String fileName, double size) {
        return getFont(fileName, FontWeight.NORMAL, FontPosture.REGULAR, size);
    }

    /**
     * Returns a font loaded from the specified file with the given weight and size,
     * using regular posture.
     *
     * @param fileName the font file name within the fonts directory
     * @param weight   the font weight
     * @param size     the font size
     * @return the loaded {@code Font} object, or system default if not found
     */
    public static Font getFont(String fileName, FontWeight weight, double size) {
        return getFont(fileName, weight, FontPosture.REGULAR, size);
    }

    /**
     * Returns a font loaded from the specified file with the given posture and
     * size, using normal weight.
     *
     * @param fileName the font file name within the fonts directory
     * @param posture  the font posture
     * @param size     the font size
     * @return the loaded {@code Font} object, or system default if not found
     */
    public static Font getFont(String fileName, FontPosture posture, double size) {
        return getFont(fileName, FontWeight.NORMAL, posture, size);
    }

    /**
     * Returns a font loaded from the specified file with the given weight, posture,
     * and size.
     *
     * @param fileName the font file name within the fonts directory
     * @param weight   the font weight
     * @param posture  the font posture
     * @param size     the font size
     * @return the loaded {@code Font} object, or system default if not found
     */
    public static Font getFont(String fileName, FontWeight weight, FontPosture posture, double size) {
        if (loadedFonts.containsKey(fileName)) {
            return Font.font(loadedFonts.get(fileName), weight, posture, size);
        } else {
            try {
                Font font = Font.loadFont(new FileInputStream(FONTS_DIRECTORY + fileName), size);
                if (font == null) {
                    throw new FileNotFoundException();
                }
                String family = font.getFamily();
                loadedFonts.put(fileName, family);
                return Font.font(family, weight, posture, size);
            } catch (FileNotFoundException e) {
                System.err.println("** Invalid font or file cannot be found " + fileName + ". Using system default **");
                return new Font(size);
            }
        }
    }

}
