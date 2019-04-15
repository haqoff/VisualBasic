package com.vb.compiler.text;

/**
 * Represents a class for reading the source text.
 */
public class SourceTextReader {

    /**
     * Represents an invalid character. Used when trying to get a character out of range of text.
     */
    public static final char NULL_CHARACTER = Character.MIN_VALUE;

    private CharSequence text;
    private int currentUnreadCharPos;

    /**
     * Initialize new instance of {@see SourceTextReader} class with specified text.
     *
     * @param text Source program text.
     * @throws IllegalArgumentException Occurs when source text equals {@code null}.
     */
    public SourceTextReader(CharSequence text) {
        if (text == null) throw new IllegalArgumentException("text cannot be null");

        this.text = text;
        currentUnreadCharPos = 0;
    }

    /**
     * Gets the character at the specified index.
     *
     * @return The symbol at the specified index, if the symbol index is in the text; otherwise {@link #NULL_CHARACTER}.
     */
    public char charAt(int index) {
        return isIndexInText(index) ? text.charAt(index) : NULL_CHARACTER;
    }

    /**
     * Gets the next character without moving a position.
     *
     * @return Next available character. If the end of the stream was reached, then {@link #NULL_CHARACTER}.
     */
    public char peekChar() {
        return charAt(currentUnreadCharPos);
    }

    /**
     * Advances a position by one character without checking for out of range.
     */
    public void advanceChar() {
        advanceChar(1);
    }

    /**
     * Advances a position by the specified number without checking for out of range.
     *
     * @param count The amount that determines how much to advance the position.
     */
    public void advanceChar(int count) {
        currentUnreadCharPos += count;
    }

    /**
     * Gets a substring of the source text, starting at the specified index and ends endIndex - 1.
     */
    public String substring(int startIndex, int endIndex) {
        return text.subSequence(startIndex, endIndex).toString();
    }

    /**
     * Gets current unread character position.
     *
     * @return index.
     */
    public int getCurrentUnreadCharPos() {
        return currentUnreadCharPos;
    }

    private boolean isIndexInText(int index) {
        return index >= 0 && index < text.length();
    }
}
