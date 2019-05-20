package com.vb.compiler.syntax.tree.tokens;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;

/**
 * Represents an identifier token.
 *
 * @author haqoff
 */
public class SyntaxIdentifier extends SyntaxToken {

    private String name;

    /**
     * Initialize new instance of {@link SyntaxIdentifier} class with specified position and length of identifier, name and errors.
     *
     * @param position The index of beginning identifier.
     * @param name     The name of identifier.
     * @param errors   Errors of identifier.
     */
    public SyntaxIdentifier(int position, String name, SyntaxDiagnosticInfo... errors) {
        super(position, name.length(), SyntaxKind.IDENTIFIER_TOKEN, errors);

        this.name = name;
    }

    @Override
    public <T> void accept(TreeVisitor<T> visitor, T context) {
        visitor.visit(this, context);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
