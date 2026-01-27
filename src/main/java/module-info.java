/**
 * Main (and only) module for the game engine.
 * <p>
 * {@link app.StartGame} is the main application that launches the game and
 * handles rendering and top-level timing and updates. {@link app.Configuration}
 * controls which game is run.
 */
module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;
    requires transitive javafx.graphics;
    requires java.desktop;

    exports app;
}
