package com.vb.compiler.syntax.tree;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxFacts;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a non-terminal which contains child nodes.
 *
 * @author haqoff
 */
public class NonterminalNode extends SyntaxNode {

    private SyntaxNode[] children; //note: dont allow to store null values.

    /**
     * Initialize new instance of {@code NonterminalNode} class with the specified kind and child nodes.
     *
     * @param kind     Kind of this nonterminal.
     * @param children Not-null array with not-null nodes.
     */
    public NonterminalNode(SyntaxKind kind, SyntaxNode... children) {
        super(kind);

        assert children != null;
        assert Arrays.stream(children).allMatch(Objects::nonNull);

        this.children = children;
    }

    /**
     * Gets all errors found in child nodes.
     *
     * @return Array of {@code SyntaxDiagnosticInfo}.
     */
    public SyntaxDiagnosticInfo[] getChildrenErrors() {
        ArrayList<SyntaxDiagnosticInfo> errors = new ArrayList<>();
        Arrays.stream(children).map(SyntaxNode::getErrors).forEach(childErrors -> errors.addAll(Arrays.asList(childErrors)));

        return errors.toArray(new SyntaxDiagnosticInfo[0]);
    }

    /**
     * Gets errors for this terminal only, excluding its children.
     */
    public SyntaxDiagnosticInfo[] getNonterminalErrors() {
        return this.errors;
    }

    /**
     * Gets errors contained in this terminal, including children's errors.
     */
    @Override
    public SyntaxDiagnosticInfo[] getErrors() {
        return Stream.concat(Arrays.stream(this.errors), Arrays.stream(getChildrenErrors())).toArray(SyntaxDiagnosticInfo[]::new);
    }

    /**
     * Checks if this terminal contains errors, including children's errors.
     */
    @Override
    public boolean hasErrors() {
        return this.errors.length > 0 || Arrays.stream(children).anyMatch(c -> c.hasErrors());
    }

    public SyntaxNode[] getChildren() {
        return children;
    }

    @Override
    public <T> void accept(TreeVisitor<T> visitor, T context) {
        visitor.visit(this, context);
    }
}
