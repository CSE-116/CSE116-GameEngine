package app.display.common.controller;

import app.Settings;
import app.gameengine.Game;
import app.gameengine.model.physics.Vector2D;
import app.games.mario.MarioControls;
import app.games.platformerobjects.PlatformerPlayer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Keyboard controls suitable for a Platformer.
 * <p>
 * This class defines many constants that can be tuned for use in various
 * platformers. The defaults were chosen to feel somewhat similar to the game
 * Animal Well, because that game is awesome.
 * <p>
 * This class offers a lot of flexibility, and a high degree of variability can
 * be achieved simply through extending this class and modifying the default
 * values. These values control qualities such as the movement speed, movement
 * acceleration, and friction, all of which can be separately controlled for the
 * ground and in the air.
 * <p>
 * Note that this class assumes it is being used with a platformer, and that the
 * player is a {@code PlatformerPlayer} or a subclass of such.
 * 
 * @see PlatformerPlayer
 * @see KeyboardControls
 * @see MarioControls
 */
public class PlatformerControls extends KeyboardControls {

    // -------------------- Directional movement constants --------------------
    /**
     * Maximum player movement speed in either air or on ground. A greater value
     * results in faster movement. Units/s.
     */
    protected double maxMoveSpeed = 7;
    /**
     * How quickly the player accelerates on the ground. Set to
     * {@link Double#POSITIVE_INFINITY} to instantly switch directions on the
     * ground. If you change this, you should probably also change the
     * {@link #groundTurnaroundAcceleration}. Units/s^2.
     */
    protected double groundAcceleration = 40;
    /**
     * How quickly the player accelerates on the ground when turning. Turning is
     * considered to be when the direction of intended motion is opposite to the
     * current direction of motion. For example, if the player is currently on the
     * ground, moving right, and presses left, this is the acceleration that will be
     * used. It should usually be set the same as, or similar to, the
     * {@link #groundAcceleration}. Units/s^2.
     */
    protected double groundTurnaroundAcceleration = groundAcceleration;
    /**
     * How quickly the player accelerates in the air. Set to
     * {@link Double#POSITIVE_INFINITY} to instantly switch directions in the air,
     * or {@code 0} to disable in-air acceleration. If you change this, you should
     * probably also change the {@link #airTurnaroundAcceleration}. Units/s^2.
     */
    protected double airAcceleration = 30;
    /**
     * How quickly the player accelerates in the air when turning. Turning is
     * considered to be when the direction of intended motion is opposite to the
     * current direction of motion. For example, if the player is currently in the
     * air, moving right, and presses left, this is the acceleration that will be
     * used. It should usually be set the same as, or similar to, the
     * {@link #airAcceleration}. Units/s^2.
     */
    protected double airTurnaroundAcceleration = airAcceleration;
    /**
     * How quickly the player decelerates on the ground when no directional keys are
     * pressed. Set to {@link Double#POSITIVE_INFINITY} to instantly stop, or
     * {@code 0} for fun ice physics. Units/s^2.
     */
    protected double groundFriction = 30;
    /**
     * How quickly the player decelerates in the air when no directional keys are
     * pressed. Set to {@link Double#POSITIVE_INFINITY} to instantly stop, or
     * {@code 0} to continue moving unhindered. Units/s^2.
     */
    protected double airFriction = 0;

    // ---------------------- Jumping movement constants ----------------------
    /**
     * The downward acceleration that is applied to the player. Note that this
     * <em>only</em> affects the player, and other objects' gravity is expected to
     * be handled separately. Units/s^2
     */
    protected double gravity = 40;
    /**
     * When the jump button is pressed, the upward (negative Y) speed to which the
     * player is set. A greater value result in higher jumps. Units/s.
     */
    protected double jumpSpeed = 17;
    /**
     * How long the jump button can be held to continue upward movement. For this
     * duration, holding the jump button allows you to move higher, though at the
     * same constant velocity. Set to {@code 0} for more standard single-jump
     * controls. Seconds.
     */
    protected double maxJumpTime = 0;
    /**
     * Time spent in the air.
     */
    private double airTime = 0;

    // --------------------------- Active controls ----------------------------
    protected boolean leftPressed = false;
    protected boolean rightPressed = false;
    protected boolean jumpPressed = false;

    /**
     * This is really just included for the benefit of the Mario controls, which
     * have a couple physics values that changes based on horizontal player speed at
     * the time of the jump.
     */
    protected double horizontalJumpSpeed = 0;

    /**
     * Construct a new keyboard platformer controller.
     * 
     * @param game the game associated with these controls.
     */
    public PlatformerControls(Game game) {
        super(game);
    }

    private void jumpButtonPressed() {
        if (this.game.getPlayer().isOnGround()) {
            this.jumpPressed = true;
            this.airTime = 0;
            ((PlatformerPlayer) this.game.getPlayer()).jump();
            this.horizontalJumpSpeed = Math.abs(this.game.getPlayer().getVelocity().getX());
        }
    }

    private void jumpButtonReleased() {
        this.jumpPressed = false;
    }

    @Override
    public void handle(KeyEvent event) {
        super.handle(event);
        if (Settings.paused()) {
            return;
        }

        boolean upPressed = this.keyStates.getOrDefault(KeyCode.UP, false)
                || this.keyStates.getOrDefault(KeyCode.W, false);
        this.leftPressed = this.keyStates.getOrDefault(KeyCode.LEFT, false)
                || this.keyStates.getOrDefault(KeyCode.A, false);
        this.rightPressed = this.keyStates.getOrDefault(KeyCode.RIGHT, false)
                || this.keyStates.getOrDefault(KeyCode.D, false);

        if (leftPressed && rightPressed) {
            this.game.getPlayer().getOrientation().setX(0);
            this.game.getPlayer().freezeAnimations();
        } else if (leftPressed) {
            this.game.getPlayer().getOrientation().setX(-1.0);
            this.game.getPlayer().setAnimationState("walk_left");
        } else if (rightPressed) {
            this.game.getPlayer().getOrientation().setX(1.0);
            this.game.getPlayer().setAnimationState("walk_right");
        }

        if (upPressed && event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
            this.jumpButtonPressed();
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
            this.jumpButtonReleased();
        }
    }

    @Override
    public void processInput(double dt) {
        PlatformerPlayer player = (PlatformerPlayer) this.game.getPlayer();
        double acceleration;
        double low = -maxMoveSpeed;
        double high = maxMoveSpeed;
        if (leftPressed && !rightPressed || !leftPressed && rightPressed) {
            // Movement key pressed, set acceleration direction
            if (leftPressed && player.getVelocity().getX() > 0 || rightPressed && player.getVelocity().getX() < 0) {
                // Turning around
                acceleration = player.isOnGround() ? this.groundTurnaroundAcceleration : this.airTurnaroundAcceleration;
            } else {
                // Standard movement
                acceleration = player.isOnGround() ? this.groundAcceleration : this.airAcceleration;
            }
            acceleration *= leftPressed ? -1 : 1;
        } else if (player.isOnGround()) {
            // No movement key, player on ground
            if (Double.isInfinite(this.groundFriction)) {
                acceleration = 0;
                low = 0;
                high = 0;
            } else {
                acceleration = this.groundFriction * -Math.signum(player.getVelocity().getX());
                low = acceleration >= 0 ? low : 0;
                high = acceleration > 0 ? 0 : high;
            }
        } else {
            // No movement key, player in air
            if (Double.isInfinite(this.airFriction)) {
                acceleration = 0;
                low = 0;
                high = 0;
            } else {
                acceleration = this.airFriction * -Math.signum(player.getVelocity().getX());
                low = acceleration >= 0 ? low : 0;
                high = acceleration > 0 ? 0 : high;
            }
        }
        // Apply calculated acceleration
        double vx = player.getVelocity().getX();
        vx = Math.clamp(vx + acceleration * dt, low, high);
        player.getVelocity().setX(vx);

        if (player.getVelocity().equals(new Vector2D(0, 0))) {
            this.game.getPlayer().freezeAnimations();
        }
        // Apply gravity
        if (!player.isOnGround()) {
            player.getVelocity().setY(player.getVelocity().getY() + this.gravity * dt);
        }
        // Jump logic
        if (this.jumpPressed && this.airTime <= this.maxJumpTime) {
            if (!player.isOnGround() && player.getVelocity().getY() >= 0) {
                // Mid-air collision or other Y velocity change
                this.jumpPressed = false;
            } else {
                // Continue jumping
                this.airTime += dt;
                player.getVelocity().setY(-jumpSpeed);
            }
        } else if (airTime > maxJumpTime) {
            this.jumpPressed = false;
        }
    }
}
