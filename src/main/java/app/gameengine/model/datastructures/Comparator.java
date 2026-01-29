package app.gameengine.model.datastructures;

/**
 * Basic generic comparator interface.
 * <p>
 * This interface is intended to be implemented to by other classes for
 * comparisons.
 */
public interface Comparator<T> {

    /**
     * Returns whether the first object comes before the second object, according to
     * the ordering of the specific comparator.
     * 
     * @param a the first object
     * @param b the second object
     * @return {@code true} if the first object comes first, {@code false} otherwise
     */
    boolean compare(T a, T b);

}
