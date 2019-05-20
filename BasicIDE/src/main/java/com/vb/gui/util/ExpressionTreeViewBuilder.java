package com.vb.gui.util;

import com.vb.compiler.syntax.tree.ExpressionNode;
import com.vb.compiler.syntax.tree.NonterminalNode;
import com.vb.compiler.syntax.tree.SyntaxNode;
import com.vb.compiler.syntax.tree.tokens.SyntaxIdentifier;
import com.vb.compiler.syntax.tree.tokens.SyntaxLiteralToken;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTreeViewBuilder {

    public static TreeItem<String> getExpressionRoots(NonterminalNode treeRoot) {
        ExpressionVisitor vis = new ExpressionVisitor();
        TreeItem<String> mainRoot = new TreeItem<>("MAIN ROOT");
        treeRoot.accept(vis, null);
        mainRoot.getChildren().addAll(vis.roots);

        return mainRoot;
    }

    private static class ExpressionVisitor implements TreeVisitor<TreeItem<String>> {
        private List<TreeItem<String>> roots = new ArrayList<>();

        @Override
        public void visit(ExpressionNode expression, TreeItem<String> parent) {
            TreeItem<String> current = new TreeItem<>(getReadableValue(expression.getOperator()));
            if (parent == null) {

                parent = new TreeItem<>("Expression №:" + (roots.size() + 1));
                parent.getChildren().add(current);
                roots.add(parent);

            } else {
                parent.getChildren().add(current);
            }

            if (expression.getLeft() != null) expression.getLeft().accept(this, current);
            if (expression.getRight() != null) expression.getRight().accept(this, current);
        }

        @Override
        public void visit(NonterminalNode nonterminal, TreeItem<String> context) {
            for (SyntaxNode child : nonterminal.getChildren()) {
                child.accept(this, null);
            }
        }

        @Override
        public void visit(SyntaxToken token, TreeItem<String> context) {
            //skip the token we only need the expression node
        }

        @Override
        public void visit(SyntaxLiteralToken literal, TreeItem<String> context) {
            //skip the literal we only need the expression node
        }

        @Override
        public void visit(SyntaxIdentifier identifier, TreeItem<String> context) {
            //skip the identifier we only need the expression node
        }

        private String getReadableValue(SyntaxToken token) {
            if (token instanceof SyntaxLiteralToken) {
                return ((SyntaxLiteralToken) token).getValue().toString();
            } else if (token instanceof SyntaxIdentifier) {
                return ((SyntaxIdentifier) token).getName();
            }

            return token.toString();
        }
    }
}
