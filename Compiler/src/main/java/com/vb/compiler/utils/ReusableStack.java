package com.vb.compiler.utils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents a LIFO stack.
 *
 * @author haqoff
 * @apiNote Note Not thread-safe!
 */
public class ReusableStack<T extends Object> implements Iterable<T> {

    private int currentSize;

    private float raiseRatio;
    private T[] holder;

    /**
     * Initialize new instance of {@see ReusableStack} class with the initial capacity equals 32 and raise ratio equals 1.5f.
     */
    public ReusableStack() {
        this(32, 1.5f);
    }

    /**
     * Initialize new instance of {@see ReusableStack} class with the specified initial capacity and raise ratio.
     *
     * @param initialCapacity The initial stack capacity.
     * @param raiseRatio      The ratio of the increase in stack capacity.
     * @apiNote initial capacity must be greater than 0 and raise ratio greater than 1.
     */
    public ReusableStack(int initialCapacity, float raiseRatio) {
        assert initialCapacity > 0 && raiseRatio > 1;

        this.holder = (T[]) new Object[initialCapacity];
        this.raiseRatio = raiseRatio;
    }

    /**
     * Adds an item to the top of the stack.
     *
     * @param value Item to add.
     */
    public void add(T value) {
        ensureCapacity(currentSize + 1);

        holder[currentSize] = value;
        currentSize++;
    }

    /**
     * Gets an item on top of the stack, without deleting it.
     *
     * @return The item at the top of the stack, or if no item is left then null.
     */
    public T peek() {
        return (currentSize != 0) ? holder[currentSize - 1] : null;
    }

    /**
     * Gets an item on top of the stack, deleting it.
     *
     * @return The item at the top of the stack, or if no item is left then null.
     */
    public T pop() {
        if (currentSize == 0) return null;

        currentSize--;
        return holder[currentSize];
    }

    /**
     * Gets n the specified items on top of the stack deleting them.
     *
     * @param n Number of items
     * @return Array of items.
     * @implNote n must be less or equal current size of stack
     */
    public T[] pop(int n) {
        assert n <= currentSize;

        currentSize -= n;
        return Arrays.copyOf(holder, n);
    }

    /**
     * Determines if the stack is empty.
     */
    public boolean empty() {
        return currentSize == 0;
    }

    /**
     * Gets current size of stack.
     */
    public int size() {
        return currentSize;
    }

    /**
     * Clears stack contents.
     */
    public void reset() {
        for (int i = 0; i < holder.length; i++) holder[i] = null;

        currentSize = 0;
    }

    private void ensureCapacity(int min) {
        if (min <= holder.length) return;

        int newLength = (int) Math.ceil(holder.length * raiseRatio);
        holder = Arrays.copyOf(holder, newLength);
    }

    /**
     * Gets an iterator that runs from the bottom of the stack to the top of the stack.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int curIndex = 0;

            @Override
            public boolean hasNext() {
                return curIndex < currentSize;
            }

            @Override
            public T next() {
                return holder[curIndex++];
            }
        };
    }
}
