package com.vb.compiler.syntax.tree;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.syntax.tree.visitor.TreeVisitor;

/**
 * Represents the node of an expression.
 *
 * @author haqoff
 */
public class ExpressionNode extends NonterminalNode {
    private SyntaxToken operator;
    private ExpressionNode left;
    private ExpressionNode right;

    /**
     * Initialize new instance of {@see ExpressionNode} class with the specified operator and operands.
     *
     * @param operator Operator.
     * @param left     Left operand.
     * @param right    Right operand.
     */
    public ExpressionNode(SyntaxToken operator, ExpressionNode left, ExpressionNode right) {
        super(operator.getKind(), createChildrenArray(left, right));

        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    /**
     * Initialize new instance of {@see ExpressionNode} class with the specified operator and given errors.
     *
     * @param operator Operator.
     * @param errors   Errors.
     */
    public ExpressionNode(SyntaxToken operator, SyntaxDiagnosticInfo... errors) {
        this(operator, null, null);
        addErrors(errors);
    }

    private static ExpressionNode[] createChildrenArray(ExpressionNode left, ExpressionNode right) {
        int length = 0;

        //since non-terminal children cannot be null, only non-null elements need to be placed.
        if (left != null) length++;
        if (right != null) length++;

        ExpressionNode[] expressionNodes = new ExpressionNode[length];
        if (right != null) {
            expressionNodes[--length] = right;
        }
        if (left != null) {
            expressionNodes[--length] = left;
        }

        return expressionNodes;
    }

    @Override
    public <T> void accept(TreeVisitor<T> visitor, T context) {
        visitor.visit(this, context);
    }

    public SyntaxToken getOperator() {
        return operator;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }
}
