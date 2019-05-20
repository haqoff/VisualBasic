package com.vb.compiler.syntax.tree.tokens;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.SyntaxNode;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;

/**
 * Represents a token.
 *
 * @author haqoff
 */
public class SyntaxToken extends SyntaxNode {
    private int position;
    private int length;

    /**
     * @param position The index of the beginning of this node.
     * @param length   The length of this node in source text.
     * @param kind     Kind of token.
     * @param errors   Errors in token.
     */
    public SyntaxToken(int position, int length, SyntaxKind kind, SyntaxDiagnosticInfo... errors) {
        super(kind);

        this.position = position;
        this.length = length;
        this.errors = errors;
    }

    @Override
    public boolean hasErrors() {
        return errors.length > 0;
    }

    @Override
    public SyntaxDiagnosticInfo[] getErrors() {
        return errors;
    }

    @Override
    public <T> void accept(TreeVisitor<T> visitor, T context) {
        visitor.visit(this, context);
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }
}
