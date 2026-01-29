package app.display.common.effects;

import java.util.ArrayList;

import app.Configuration;
import app.gameengine.model.physics.Vector2D;
import javafx.scene.Node;

/**
 * Represents an animated visual effect composed of multiple frames.
 * <p>
 * This effect cycles through a sequence of JavaFX nodes over time, optionally
 * looping. It is suitable for animations with a fixed set of animation frames,
 * such as explosions or death animations.
 * 
 * @see Effect
 * @see TimedEffect
 * @see StaticEffect
 */
public class AnimatedEffect implements Effect {

    protected final boolean loop;
    protected ArrayList<Node> frames;
    protected double animationTime;
    protected double elapsedTime;

    /**
     * Construct an animated effect with the given frames. If {@code loop} is true,
     * rather than ending the effect after all frames have been cycled through, it
     * will return to the first frame. By default, each frame will be displayed for
     * {@value Configuration#ANIMATION_TIME} seconds.
     * 
     * @param frames the frames to display
     * @param loop   {@code true} if it should repeat, {@code false} otherwise
     */
    public AnimatedEffect(ArrayList<Node> frames, boolean loop) {
        this(frames, Configuration.ANIMATION_TIME, loop);
    }

    /**
     * Construct an animated effect with the given frames. If {@code loop} is true,
     * rather than ending the effect after all frames have been cycled through, it
     * will return to the first frame.
     * 
     * @param frames        the frames to display
     * @param animationTime the amount of time, in seconds, each frame will be
     *                      displayed
     * @param loop          {@code true} if it should repeat, {@code false}
     *                      otherwise
     */
    public AnimatedEffect(ArrayList<Node> frames, double animationTime, boolean loop) {
        this.frames = frames;
        this.loop = loop;
        this.animationTime = animationTime;
    }

    @Override
    public Node getFrame(Vector2D origin) {
        int index = (int) (this.elapsedTime / this.animationTime) % this.frames.size();
        this.frames.get(index).setLayoutX(origin.getX() * Configuration.SCALE_FACTOR);
        this.frames.get(index).setLayoutY(origin.getY() * Configuration.SCALE_FACTOR);
        return this.frames.get(index);
    }

    @Override
    public boolean isFinished() {
        if (this.loop) {
            return false;
        }
        return this.elapsedTime > this.frames.size() * this.animationTime;
    }

    @Override
    public void update(double dt) {
        this.elapsedTime += dt;
    }

    @Override
    public void reset() {
        this.elapsedTime = 0;
    }

}
