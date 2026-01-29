package app.display.common.ui;

import java.util.HashMap;

import app.gameengine.Game;
import app.gameengine.Level;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Manages a collection of UI elements for a game.
 * <p>
 * Provides methods to add, remove, update, and reset UI elements, and exposes a
 * single renderable pane containing all elements.
 * 
 * @see UIElement
 * @see Game
 */
public class UICollection {

    protected HashMap<String, UIElement> elements;
    protected StackPane UIPane = new StackPane();

    /**
     * Constructs a new UICollection with the specified elements.
     *
     * @param elements a map of element names to UIElement instances
     */
    public UICollection(HashMap<String, UIElement> elements) {
        this.elements = elements;
        this.elements.forEach((k, v) -> this.UIPane.getChildren().add(v.getRenderable()));
    }

    /**
     * Adds a new UI element to the collection.
     *
     * @param name    the name of the element
     * @param element the UIElement to add
     */
    public void addElement(String name, UIElement element) {
        this.elements.put(name, element);
        this.UIPane.getChildren().add(element.getRenderable());
    }

    /**
     * Removes a UI element from the collection by name.
     *
     * @param name the name of the element to remove
     */
    public void removeElement(String name) {
        UIElement element = this.elements.remove(name);
        if (element != null) {
            this.UIPane.getChildren().remove(element.getRenderable());
        }
    }

    /**
     * Removes all UI elements from the collection.
     */
    public void clearElements() {
        this.elements.clear();
        this.UIPane.getChildren().clear();
    }

    /**
     * Returns the JavaFX node containing all UI elements in this collection. Note
     * that if this class is extended, it is expected that this is the root node of
     * the element, and while the content may freely change, that root node should
     * not.
     *
     * @return the renderable UI pane
     */
    public Node getRenderableUI() {
        return this.UIPane;
    }

    /**
     * Updates all UI elements in the collection.
     *
     * @param dt    the time elapsed since the last update, in seconds
     * @param level the current game level
     */
    public void update(double dt, Level level) {
        this.elements.forEach((k, v) -> v.update(dt, level));
    }

    /**
     * Resets all UI elements in the collection to their initial states.
     */
    public void reset() {
        this.elements.values().forEach(UIElement::reset);
    }

}
