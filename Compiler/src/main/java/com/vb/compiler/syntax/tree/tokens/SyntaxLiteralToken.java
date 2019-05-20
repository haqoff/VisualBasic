package com.vb.compiler.syntax.tree.tokens;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;

/**
 * Represents literal-token with value.
 *
 * @param <T> Type of value.
 * @author haqoff
 */
public class SyntaxLiteralToken<T extends Object> extends SyntaxToken {
    private T value;

    /**
     * Initialize new instance of {@see SyntaxLiteralToken} with the specified position, length, value and errors.
     *
     * @param position Token start position.
     * @param length   Full length of token.
     * @param value    Value of token.
     * @param errors   Errors.
     */
    public SyntaxLiteralToken(int position, int length, T value, SyntaxDiagnosticInfo[] errors) {
        super(position, length, SyntaxKind.LITERAL_TOKEN, errors);
        this.value = value;
    }

    @Override
    public <T> void accept(TreeVisitor<T> visitor, T context) {
        visitor.visit(this, context);
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

