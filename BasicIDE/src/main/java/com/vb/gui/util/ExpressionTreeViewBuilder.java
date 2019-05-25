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
import java.util.Stack;

public class ExpressionTreeViewBuilder {

    public static ExpressionInfo getExpressionInfo(NonterminalNode treeRoot) {
        ExpressionVisitor vis = new ExpressionVisitor();

        TreeItem<String> mainTreeRoot = new TreeItem<>("MAIN TREE ROOT");
        TreeItem<String> mainMatrixRoot = new TreeItem<>("MAIN MATRIX ROOT");

        treeRoot.accept(vis, null);

        mainTreeRoot.getChildren().addAll(vis.expressionRoots);
        mainMatrixRoot.getChildren().addAll(vis.matrixRoots);

        return new ExpressionInfo(mainTreeRoot, mainMatrixRoot);
    }

    private static class ExpressionVisitor implements TreeVisitor<TreeItem<String>> {
        private List<TreeItem<String>> expressionRoots = new ArrayList<>();

        private Stack<TreeItem<String>> matrixRoots = new Stack<>();
        private Stack<String> names = new Stack<>();
        private int currentOperation;

        @Override
        public void visit(ExpressionNode expression, TreeItem<String> parent) {
            TreeItem<String> current = new TreeItem<>(getReadableValue(expression.getOperator()));

            if (parent == null) {
                String expressionStartName = "Expression №:" + (expressionRoots.size() + 1);

                TreeItem<String> rootTreeItem = new TreeItem<>(expressionStartName);
                rootTreeItem.getChildren().add(current);
                expressionRoots.add(rootTreeItem);

                TreeItem<String> matrixRootItem = new TreeItem<>(expressionStartName);
                matrixRoots.push(matrixRootItem);
                currentOperation = 0;
            } else {
                parent.getChildren().add(current);
            }

            if (expression.getLeft() != null) expression.getLeft().accept(this, current);
            if (expression.getRight() != null) expression.getRight().accept(this, current);

            makeExpressionMatrix(expression, parent);
        }

        private void makeExpressionMatrix(ExpressionNode expression, TreeItem<String> parent) {
            String tokenTextValue = getReadableValue(expression.getOperator());

            if (expression.getLeft() != null && expression.getRight() != null) {

                String right = names.pop();
                String left = names.pop();

                String ruleName = String.format("M%s", ++currentOperation);

                String rule = String.format("%s := %s %s %s", ruleName, tokenTextValue, left, right);
                matrixRoots.peek().getChildren().add(new TreeItem<>(rule));
                names.push(ruleName);

            } else if (parent == null) {

                String rule = String.format("M1 := %s", tokenTextValue);
                matrixRoots.peek().getChildren().add(new TreeItem<>(rule));

            } else {
                names.push(tokenTextValue);
            }
        }

        private String getReadableValue(SyntaxToken token) {
            if (token instanceof SyntaxLiteralToken) {
                return ((SyntaxLiteralToken) token).getValue().toString();
            } else if (token instanceof SyntaxIdentifier) {
                return ((SyntaxIdentifier) token).getName();
            }

            switch (token.getKind()){
                case ASTERISK_TOKEN:
                    return "*";
                case MINUS_TOKEN:
                    return "-";
                case PLUS_TOKEN:
                    return "+";
                case SLASH_TOKEN:
                    return "/";
            }

            return token.toString();
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
    }

    public static class ExpressionInfo {
        public TreeItem<String> treeRoot;
        public TreeItem<String> matrixRoot;

        public ExpressionInfo(TreeItem<String> treeRoot, TreeItem<String> matrixRoot) {
            this.treeRoot = treeRoot;
            this.matrixRoot = matrixRoot;
        }
    }
}
