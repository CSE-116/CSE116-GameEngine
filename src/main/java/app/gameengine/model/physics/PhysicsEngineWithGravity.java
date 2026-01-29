package app.gameengine.model.physics;

import java.util.ArrayList;

import app.Settings;
import app.gameengine.Level;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import javafx.util.Pair;

/**
 * Physics engine that applies downward acceleration.
 * <p>
 * This physics engine does mostly the same as its parent, but applies
 * additional downward (+y) acceleration at each update. It also uses a
 * different processing strategy to ensure that collisions are handled
 * optimally.
 * 
 * @see PhysicsEngine
 */
public class PhysicsEngineWithGravity extends PhysicsEngine {

    private static double DEFAULT_GRAVITY = 40;

    public PhysicsEngineWithGravity() {
        this(DEFAULT_GRAVITY);
    }

    public PhysicsEngineWithGravity(double gravity) {
        super();
    }

    @Override
    public void processAllCollisions(Level level) {
        ArrayList<DynamicGameObject> dynamicObjects = level.getDynamicObjects();
        ArrayList<StaticGameObject> staticObjects = level.getStaticObjects();

        for (int i = 0; i < dynamicObjects.size(); i++) {
            DynamicGameObject object1 = dynamicObjects.get(i);
            if (Settings.noclip() && object1.isPlayer()) {
                continue;
            }
            // Detect initial static object collisions
            ArrayList<Pair<StaticGameObject, Double>> staticCollisions = new ArrayList<>();
            for (StaticGameObject object2 : staticObjects) {
                double overlap = getOverlap(object1.getHitbox(), object2.getHitbox());
                if (overlap > 0) {
                    staticCollisions.add(new Pair<>(object2, overlap));
                }
            }
            // Process static object collisions in order of distance
            staticCollisions.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
            for (int j = 0; j < staticCollisions.size(); j++) {
                StaticGameObject staticObject = staticCollisions.get(j).getKey();
                if (detectCollision(object1.getHitbox(), staticObject.getHitbox())) {
                    staticObject.collideWithDynamicObject(object1);
                    object1.collideWithStaticObject(staticObject);
                }
            }
            // Detect initial dynamic object collisions
            ArrayList<Pair<DynamicGameObject, Double>> dynamicCollisions = new ArrayList<>();
            for (int j = i + 1; j < dynamicObjects.size(); j++) {
                DynamicGameObject object2 = dynamicObjects.get(j);
                if (Settings.noclip() && object2.isPlayer()) {
                    continue;
                }
                double overlap = getOverlap(object1.getHitbox(), object2.getHitbox());
                if (overlap > 0) {
                    dynamicCollisions.add(new Pair<>(object2, overlap));
                }
            }
            // Process dynamic object collisions in order of distance
            dynamicCollisions.sort((a, b) -> Double.compare(a.getValue(), b.getValue()));
            for (int j = 0; j < dynamicCollisions.size(); j++) {
                DynamicGameObject object2 = dynamicCollisions.get(j).getKey();
                if (detectCollision(object1.getHitbox(), object2.getHitbox())) {
                    object1.collideWithDynamicObject(object2);
                    object2.collideWithDynamicObject(object1);
                }
            }
        }
    }

}
