package com.vb.compiler.syntax.tree;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;

import java.util.Arrays;

/**
 * Represents a node in the tree parse.
 *
 * @author haqoff
 */
public abstract class SyntaxNode {

    protected SyntaxDiagnosticInfo[] errors;
    private SyntaxKind kind;

    /**
     * Initialize new instance of {@link SyntaxNode} class with specified kind of node, position and length in the source text,
     * errors in this node.
     *
     * @param kind Kind of this node.
     */
    public SyntaxNode(SyntaxKind kind, SyntaxDiagnosticInfo... errors) {
        assert kind != null;

        this.kind = kind;
        this.errors = errors;
    }

    /**
     * Adds errors specific to this node.
     *
     * @param newErrors
     */
    public void addErrors(SyntaxDiagnosticInfo... newErrors) {
        if (newErrors.length == 0) return;

        int concatLength = errors.length + newErrors.length;
        SyntaxDiagnosticInfo[] concatArray = Arrays.copyOf(errors, concatLength);

        for (int i = 0; i < newErrors.length; i++) {
            concatArray[i + errors.length] = newErrors[i];
        }

        errors = concatArray;
    }

    /**
     * Checks if there are errors in this node.
     *
     * @return {@code True} if there is. Otherwise {@code false}.
     */
    public boolean hasErrors() {
        return errors.length > 0;
    }

    /**
     * Gets errors that are specific to this node.
     */
    public SyntaxDiagnosticInfo[] getErrors() {
        return errors;
    }

    /**
     * Accepts the visitor.
     */
    public abstract <T extends Object> void accept(TreeVisitor<T> visitor, T context);

    /**
     * Gets the kind of this node.
     */
    public SyntaxKind getKind() {
        return kind;
    }

    /**
     * Gets the kind of this node in string representation.
     */
    @Override
    public String toString() {
        return kind.toString();
    }
}
