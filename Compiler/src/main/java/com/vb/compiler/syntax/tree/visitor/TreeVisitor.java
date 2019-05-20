package com.vb.compiler.syntax.tree.visitor;

import com.vb.compiler.syntax.tree.ExpressionNode;
import com.vb.compiler.syntax.tree.NonterminalNode;
import com.vb.compiler.syntax.tree.tokens.SyntaxIdentifier;
import com.vb.compiler.syntax.tree.tokens.SyntaxLiteralToken;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;

/**
 * Provides the ability to visit the node.
 *
 * @param <T> Type of context.
 */
public interface TreeVisitor<T extends Object> {

    /**
     * Visit the expression node.
     */
    void visit(ExpressionNode expression, T context);

    /**
     * Visit the non-terminal node.
     */
    void visit(NonterminalNode nonterminal, T context);

    /**
     * Visit the token node.
     */
    void visit(SyntaxToken token, T context);

    /**
     * Visit the literal node.
     */
    void visit(SyntaxLiteralToken literal, T context);

    /**
     * Visit the identifier node.
     */
    void visit(SyntaxIdentifier identifier, T context);
}
