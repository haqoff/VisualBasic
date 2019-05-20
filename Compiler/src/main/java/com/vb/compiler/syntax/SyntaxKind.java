package com.vb.compiler.syntax;

/**
 * Kind of node parse tree.
 *
 * @author haqoff
 */
public enum SyntaxKind {
    UNKNOWN,

    //-----------------------------------
    //              TOKENS
    ASTERISK_TOKEN,
    MINUS_TOKEN,
    PLUS_TOKEN,
    EQUALS_TOKEN,
    SLASH_TOKEN,
    OPEN_PAREN_TOKEN,
    CLOSE_PAREN_TOKEN,
    COMMA_TOKEN,
    NEW_LINE_TOKEN,
    END_OF_TEXT,
    IDENTIFIER_TOKEN,
    LITERAL_TOKEN,

    DIM_KEYWORD,
    AS_KEYWORD,
    SELECT_KEYWORD,
    CASE_KEYWORD,
    TO_KEYWORD,
    ELSE_KEYWORD,
    END_KEYWORD,
    INTEGER_KEYWORD,
    STRING_KEYWORD,
    DOUBLE_KEYWORD,
    //-----------------------------------

    //-----------------------------------
    //          NONTERMINALS
    PROGRAM_NONTERM,
    STATEMENTS_LIST_NONTERM,
    STATEMENTS_CONTINUE_NONTERM,
    STATEMENTS_LIST_NULLABLE_NONTERM,
    STATEMENTS_LIST_WITH_NL_NONTERM,
    STATEMENT_NONTERM,
    NEW_LINE_LIST_NONTERM,
    STM_DECLAREMENT_BODY_NONTERM,
    ID_LIST_NONTERM,
    SPECIAL_TYPE_NONTERM,
    STM_VAR_ASSIGMENT_NONTERM,
    VALUE_NONTERM,
    STM_SELECT_CASE_NONTERM,
    CASE_SET_NONTERM,
    CASE_ELSE_LINE_NONTERM,
    CASE_VALUE_LINE_NONTERM,
    CASE_EXP_NONTERM,
    ERROR_NONTERM
    //-----------------------------------
}
