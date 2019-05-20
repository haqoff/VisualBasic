package com.vb.gui.util;

import java.util.stream.Stream;

public class StringUtil {

    /**
     * Concatenates elements in the stream, separating them with {@code separator}, into a string.
     */
    public static <T> String streamToString(Stream<T> stream, String separator) {
        StringBuilder sb = new StringBuilder();
        final boolean[] isFirst = {true};

        stream.forEach(item -> {
            if (!isFirst[0]) sb.append(separator);
            sb.append(item.toString());
            isFirst[0] = false;
        });

        return sb.toString();
    }
}
