package com.vb.compiler.syntax;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides syntax information.
 *
 * @author haqoff
 */
public class SyntaxFacts {

    //NOTE: keyword must be lowercase here for correct comparison
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
        String lowerCase = identifier.toLowerCase();
        Object value = KEYWORDS.get(lowerCase);
        return value != null ? (SyntaxKind) value : SyntaxKind.UNKNOWN;
    }

    /**
     * Gets the read-only set of keywords.
     */
    public static Set<String> getKeywords() {
        return Collections.unmodifiableSet(KEYWORDS.keySet());
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

    /**
     * Determines whether the specified kind is a nonterminal.
     */
    public static boolean isNonterminal(SyntaxKind kind) {
        switch (kind) {
            case PROGRAM_NONTERM:
            case STATEMENTS_LIST_NONTERM:
            case STATEMENTS_CONTINUE_NONTERM:
            case STATEMENTS_LIST_NULLABLE_NONTERM:
            case STATEMENTS_LIST_WITH_NL_NONTERM:
            case STATEMENT_NONTERM:
            case NEW_LINE_LIST_NONTERM:
            case STM_DECLAREMENT_BODY_NONTERM:
            case ID_LIST_NONTERM:
            case SPECIAL_TYPE_NONTERM:
            case STM_VAR_ASSIGMENT_NONTERM:
            case VALUE_NONTERM:
            case STM_SELECT_CASE_NONTERM:
            case CASE_SET_NONTERM:
            case CASE_ELSE_LINE_NONTERM:
            case CASE_VALUE_LINE_NONTERM:
            case CASE_EXP_NONTERM:
            case ERROR_NONTERM:
                return true;
            default:
                return false;
        }
    }

    /**
     * Determines whether the specified kind is a keyword.
     */
    public static boolean isKeyword(SyntaxKind kind) {
        switch (kind) {
            case DIM_KEYWORD:
            case AS_KEYWORD:
            case SELECT_KEYWORD:
            case CASE_KEYWORD:
            case TO_KEYWORD:
            case ELSE_KEYWORD:
            case END_KEYWORD:
            case INTEGER_KEYWORD:
            case STRING_KEYWORD:
            case DOUBLE_KEYWORD:
                return true;
            default:
                return false;
        }
    }

    /**
     * Determines whether a given kind can be in an expression.
     */
    public static boolean canBeInExpression(SyntaxKind kind) {
        switch (kind) {
            case ASTERISK_TOKEN:
            case MINUS_TOKEN:
            case PLUS_TOKEN:
            case SLASH_TOKEN:
            case OPEN_PAREN_TOKEN:
            case CLOSE_PAREN_TOKEN:
            case IDENTIFIER_TOKEN:
            case LITERAL_TOKEN:
                return true;
            default:
                return false;
        }
    }

    /**
     * Gets the precedence of the operation.
     *
     * @param kind Type of operation.
     * @return A number representing the priority of the operation;
     * otherwise, if the priority for the specified kind is not set, then 0
     * (for example, the specified kind is not an operator).
     */
    public static int getOperationPrecedence(SyntaxKind kind) {
        switch (kind) {
            case ASTERISK_TOKEN:
            case SLASH_TOKEN:
                return 2;
            case MINUS_TOKEN:
            case PLUS_TOKEN:
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Gets the associativity of the operation.
     *
     * @param kind
     * @return {@code true} if the specified operator is left-associative. If the operator is right-associative, then {@code false}.
     * @throws RuntimeException Occurs when specified kind is not an operator.
     */
    public static boolean isLeftAssociative(SyntaxKind kind) {
        switch (kind) {
            case ASTERISK_TOKEN:
            case SLASH_TOKEN:
            case MINUS_TOKEN:
            case PLUS_TOKEN:
                return true;
            default:
                throw new RuntimeException("The specified kind has not been processed here.");
        }
    }
}
