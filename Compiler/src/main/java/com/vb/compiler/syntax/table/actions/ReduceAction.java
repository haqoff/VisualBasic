package com.vb.compiler.syntax.table.actions;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.NonterminalNode;
import com.vb.compiler.syntax.tree.SyntaxNode;

public class ReduceAction implements Action {

    protected SyntaxKind kind;
    private int length;

    public ReduceAction(SyntaxKind kind, int length) {
        this.kind = kind;
        this.length = length;
    }

    public SyntaxNode createNode(SyntaxNode[] nodes) {
        return new NonterminalNode(kind, nodes, new SyntaxDiagnosticInfo[0]);
    }

    @Override
    public final void visit(ActionHandler handler) {
        SyntaxNode[] nodes = handler.extractNodes(length);
        SyntaxNode reduceNode = createNode(nodes);
        handler.addNewNode(reduceNode);
    }

    @Override
    public boolean isLastPossible() {
        return false;
    }
}
