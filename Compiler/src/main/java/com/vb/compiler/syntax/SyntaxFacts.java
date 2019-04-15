package com.vb.compiler.syntax;

import java.util.HashMap;
import java.util.Map;

public class SyntaxFacts {
    private static final Map<String, SyntaxKind> KEYWORDS = new HashMap<String, SyntaxKind>() {{
        put("select", SyntaxKind.SELECT_KEYWORD);
        put("as", SyntaxKind.AS_KEYWORD);
        put("case", SyntaxKind.CASE_KEYWORD);
        put("end", SyntaxKind.END_KEYWORD);
        put("dim", SyntaxKind.DIM_KEYWORD);
        put("else", SyntaxKind.ELSE_KEYWORD);
        put("to", SyntaxKind.TO_KEYWORD);
        put("integer", SyntaxKind.INTEGER_KEYWORD);
        put("string", SyntaxKind.STRING_KEYWORD);
        put("double", SyntaxKind.DOUBLE_KEYWORD);
    }};

    /**
     * Gets the type of keyword in the string.
     *
     * @param identifier String representation.
     * @return If keyword exits then his kind; otherwise {@link SyntaxKind#UNKNOWN}.
     */
    public static SyntaxKind getKeywordKind(String identifier) {
        //TODO i think toLower is a bad way for performance
        String lowerCase = identifier.toLowerCase();
        Object value = KEYWORDS.get(lowerCase);
        return value != null ? (SyntaxKind) value : SyntaxKind.UNKNOWN;
    }

    /**
     * Gets keywords.
     *
     * @return
     */
    public static Iterable<String> getKeywords() {
        return KEYWORDS.keySet();
    }

    /**
     * Determines whether the specified char can be start character of the identifier.
     *
     * @param ch Character for check
     * @return {@code true} if it can, otherwise {@code false}.
     */
    public static boolean isIdentifierStartCharacter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_';
    }
}
