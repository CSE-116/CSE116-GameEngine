package app.gameengine.utils;

import java.util.ArrayList;
import java.util.Random;

import app.gameengine.model.physics.Vector2D;

/**
 * Wrapper class for performing random operations.
 */
public class Randomizer {

    private static Random random = new Random();

    /**
     * Prevent instantiation, as this class is intended to be static.
     */
    private Randomizer() {
    }

    /**
     * Sets the seed for random generation.
     * 
     * @param seed the seed all random generation will be based off of.
     */
    public static void setSeed(int seed) {
        random.setSeed(seed);
    }

    /**
     * Returns a pseudorandom selection from the input list, which must have at
     * least one element.
     * 
     * @param <T>  The type of element stored in the list
     * @param list the list to select from
     * @return a pseudorandom selection from the list
     * @throws IllegalArgumentException if the list is empty
     */
    public static <T> T randomSelect(ArrayList<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List argument must contain at least one element");
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Returns a pseudorandom selection from the input list that is not contained
     * within the exceptions list. The input list must contain at least one element.
     * If the input list does not contain any elements not also contained within the
     * exceptions list, this method returns null.
     * 
     * @param <T>        the type of element stored in the lists
     * @param list       the list to select from
     * @param exceptions the list to exclude from selections
     * @return a pseudorandom selection from the list that is not in the exceptions
     * @throws IllegalArgumentException if the list is empty
     */
    public static <T> T randomSelect(ArrayList<T> list, ArrayList<T> exceptions) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List argument must contain at least one element");
        }
        ArrayList<T> candidates = new ArrayList<>(list);
        candidates.removeAll(exceptions);

        if (candidates.isEmpty()) {
            return null;
        }

        return randomSelect(candidates);
    }

    /**
     * Returns a pseudorandom selection from the input array.
     * 
     * @param <T>   The type of element stored in the array
     * @param array the array to select from
     * @return a pseudorandom selection from the array
     * @throws IllegalArgumentException if the list is empty
     */
    public static <T> T randomSelect(T[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array argument must contain at least one element");
        }
        return array[random.nextInt(array.length)];
    }

    /**
     * Returns a pseudorandom int between 0 (inclusive) and the {@code upperBound}
     * (exclusive).
     * 
     * @param upperBound the upper bound (exclusive) for the returned value
     * @return a pseudorandom int between the specified bounds
     * @throws IllegalArgumentException if {@code upperBound} is not positive
     */
    public static int randomInt(int upperBound) {
        return random.nextInt(upperBound);
    }

    /**
     * Returns a pseudorandom int between the {@code lowerBound} (inclusive) and the
     * {@code upperBound} (exclusive).
     * 
     * @param lowerBound the lowest value that can be returned
     * @param upperBound the upper bound (exclusive) for the returned value
     * @return a pseudorandom int between the specified bounds
     * @throws IllegalArgumentException if {@code lowerBound} is greater than or
     *                                  equal to {@code upperBound}
     */
    public static int randomInt(int lowerBound, int upperBound) {
        return random.nextInt(lowerBound, upperBound);
    }

    /**
     * Returns a pseudorandom double between 0.0 (inclusive) and 1.0 (exclusive).
     * 
     * @return a pseudorandom double between 0.0 (inclusive) and 1.0 (exclusive)
     */
    public static double randomDouble() {
        return random.nextDouble();
    }

    /**
     * Returns a pseudorandom double between 0.0 (inclusive) and {@code upperBound}
     * (exclusive).
     * 
     * @param upperBound the upper bound (exclusive) of the returned value
     * @return a pseudorandom double between 0.0 (inclusive) and {@code upperBound}
     *         (exclusive)
     * @throws IllegalArgumentException if {@code upperBound} is not both positive
     *                                  and finite
     */
    public static double randomDouble(double upperBound) {
        return random.nextDouble(upperBound);
    }

    /**
     * Returns a pseudorandom double between {@code lowerBound} (inclusive) and
     * {@code upperBound} (exclusive).
     * 
     * @param lowerBound the lowest value that can be returned
     * @param upperBound the upper bound (exclusive) for the returned value
     * @return a pseudorandom double between the specified bounds
     * @throws IllegalArgumentException if {@code lowerBound} is greater than or
     *                                  equal to {@code upperBound}, or if either
     *                                  parameter is infinite
     */
    public static double randomDouble(double lowerBound, double upperBound) {
        return random.nextDouble(lowerBound, upperBound);
    }

    /**
     * Returns a pseudorandom boolean.
     * 
     * @return a pseudorandom boolean.
     */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    /**
     * Creates a copy of an {@code ArrayList} and shuffles the contents.
     *
     * @param <T>      the type of the original {@code ArrayList}.
     * @param original the {@code ArrayList} to be copied and shuffled.
     * @return a copy of the original {@code ArrayList} that is also shuffled.
     */
    public static <T> ArrayList<T> shuffleArrayList(ArrayList<T> original) {
        ArrayList<T> newList = new ArrayList<>(original);
        for (int i = newList.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            T temp = newList.get(index);
            newList.set(index, newList.get(i));
            newList.set(i, temp);
        }
        return newList;
    }

    /**
     * Returns a pseudorandom {@code Vector2D} whose X and Y components are each
     * integers between 0.0 (inclusive) and the X and Y components of the
     * {@code upperBounds}, respectively (exclusive). {@code upperBounds} must have
     * positive components.
     * 
     * @param upperBounds the upper limits (exclusive) of the X and Y components of
     *                    the returned vector
     * @return a pseudorandom vector bounded by {@code upperBounds}
     * @throws IllegalArgumentException if X and Y components of {@code upperBounds}
     *                                  are not both positive
     */
    public static Vector2D randomIntVector2D(Vector2D upperBounds) {
        return new Vector2D(randomInt((int) upperBounds.getX()), randomInt((int) upperBounds.getY()));
    }

    /**
     * Returns a pseudorandom {@code Vector2D} whose X and Y components are each
     * integers between 0.0 (inclusive) and the X and Y components of the
     * {@code upperBounds}, respectively (exclusive), and which is not contained
     * within the list of exceptions. {@code upperBounds} must have
     * positive components.
     * <p>
     * Since the generated vectors have integer components, so should the list of
     * exceptions. If the list of exceptions contains all vectors within the bounds,
     * this method will return null.
     * 
     * @param upperBounds the upper limits (exclusive) of the X and Y components of
     *                    the returned vector
     * @param exceptions  the list of exceptions
     * @return a random vector with integer components not contained within the
     *         exceptions
     * @throws IllegalArgumentException if X and Y components of {@code upperBounds}
     *                                  are not both positive
     */
    public static Vector2D randomIntVector2D(Vector2D upperBounds, ArrayList<Vector2D> exceptions) {
        if (upperBounds.getX() <= 0 || upperBounds.getY() <= 0) {
            throw new IllegalArgumentException("Both components of upperBounds argument must be positive");
        }
        ArrayList<Vector2D> candidates = new ArrayList<>();
        for (int i = 0; i < upperBounds.getX(); i++) {
            for (int j = 0; j < upperBounds.getY(); j++) {
                candidates.add(new Vector2D(i, j));
            }
        }
        return randomSelect(candidates, exceptions);
    }

}
