package pl.filipowm.api.utils;

import lombok.RequiredArgsConstructor;

/**
 * Simple implementation of two-value tuple/pair object.
 */
@RequiredArgsConstructor(staticName = "of")
public class Pair<T, U> {

    private final T one;
    private final U two;

    /**
     * @return value of first object passed to constructor
     */
    public T getOne() {
        return one;
    }

    /**
     * @return value of second object passed to constructor
     */
    public U getTwo() {
        return two;
    }
}

