package com.vb.compiler.syntax.table.actions;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.tree.SyntaxNode;

public abstract class ActionHandler {
    /**
     * Shifts the input node and go the specified state.
     *
     * @param state
     */
    protected abstract void shift(int state);

    /**
     * Change current state.
     *
     * @param state
     */
    protected abstract void changeState(int state);

    /**
     * Adds the specified node.
     *
     * @param reduced The node into which the other nodes were reduced.
     */
    protected abstract void addNewNode(SyntaxNode reduced);

    /**
     * Retrieves nodes and returns to the specified number of states.
     *
     * @param length Number of nodes and states to retrieve.
     * @return Array of extracted nodes.
     */
    protected abstract SyntaxNode[] extractNodes(int length);

    protected abstract void addError(SyntaxDiagnosticInfo error);
}
