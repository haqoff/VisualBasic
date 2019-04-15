package com.vb.compiler.syntax.table;

import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.table.actions.Action;
import com.vb.compiler.syntax.table.actions.UnexpectedErrorAction;

public abstract class AbstractLRTable {

    private Action[][] actions;

    private final Action DEFAULT_ACTION;

    protected AbstractLRTable(int numberStates) {
        this(numberStates, new UnexpectedErrorAction());
    }

    protected AbstractLRTable(int numberStates, final Action defaultAction) {
        actions = new Action[numberStates][SyntaxKind.values().length];
        DEFAULT_ACTION = defaultAction;

        for (int i = 0; i < numberStates; i++) {
            for (int j = 0; j < actions[i].length; j++) {
                actions[i][j] = defaultAction;
            }
        }

        initializeTable();
    }

    protected abstract void initializeTable();

    /**
     * Sets the action for the specified state at the specified input node.
     */
    public void setAction(int state, SyntaxKind inputNodeKind, Action action) {
        actions[state][inputNodeKind.ordinal()] = action;
    }

    public void changeDefaultAction(int state, Action action) {
        for (int i = 0; (i < actions[state].length); i++) {
            if (actions[state][i] == DEFAULT_ACTION) actions[state][i] = action;
        }
    }

    public Action getAction(int state, SyntaxKind inputNodeKind) {
        Action action = actions[state][inputNodeKind.ordinal()];
        assert (action != null);
        return action;
    }
}
