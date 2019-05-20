package com.vb.compiler.error;

/**
 * Represents the error code of {@link SyntaxDiagnosticInfo}.
 *
 * @author haqoff
 */
public enum ErrorCode {
    ERR_UNEXPECTED_CHAR,
    ERR_INTEGER_OVERFLOW,
    ERR_LOST_QUOTE,
    ERR_UNEXPECTED_TOKEN,
    ERR_UNBALANCED_RIGHT_PARENTHESES,
    ERR_MISSING_EXPRESSION,
    ERR_MISSING_OPERATOR
}
