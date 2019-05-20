package com.vb.compiler.utils;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.tree.ExpressionNode;
import com.vb.compiler.syntax.tree.NonterminalNode;
import com.vb.compiler.syntax.tree.SyntaxNode;
import com.vb.compiler.syntax.tree.tokens.SyntaxIdentifier;
import com.vb.compiler.syntax.tree.tokens.SyntaxLiteralToken;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;

public class ConsoleTreePrinter implements TreeVisitor<Integer> {

    private void print(SyntaxNode node, int nTabs) {
        if (node == null) return;

        for (int i = 0; i < nTabs; i++) {
            System.out.print('\t');
        }

        System.out.print(node.toString());
        if (node.hasErrors()) {
            System.out.print(" [ERRORS: ");
            for (SyntaxDiagnosticInfo error : node.getErrors()) {
                System.out.print(error.getCode().toString() + " ");
            }
            System.out.print("]");
        }
        System.out.println();
    }

    @Override
    public void visit(ExpressionNode expression, Integer context) {
        print(expression, context);

        print(expression.getLeft(), context + 1);
        print(expression.getRight(), context + 1);
    }

    @Override
    public void visit(NonterminalNode nonterminal, Integer context) {
        print(nonterminal, context);

        for (SyntaxNode child : nonterminal.getChildren()) {
            child.accept(this, context + 1);
        }
    }

    @Override
    public void visit(SyntaxToken token, Integer context) {
        print(token, context);
    }

    @Override
    public void visit(SyntaxLiteralToken literal, Integer context) {
        print(literal, context);
    }

    @Override
    public void visit(SyntaxIdentifier identifier, Integer context) {
        print(identifier, context);
    }
}
