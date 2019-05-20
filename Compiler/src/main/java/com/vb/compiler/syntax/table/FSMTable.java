package com.vb.compiler.syntax.table;

import com.vb.compiler.syntax.SyntaxKind;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Represents a state machine table.
 *
 * @author haqoff
 */
public class FSMTable {
    private Runnable[][] table;
    private int currentState;

    /**
     * Initialize new instance of {@link FSMTable} class with specified number of possible states.
     *
     * @param nStates
     */
    public FSMTable(int nStates) {
        table = new Runnable[nStates][SyntaxKind.values().length];
    }

    /**
     * Sets the state for which the binding will be made.
     */
    public void inState(int state) {
        currentState = state;
    }

    /**
     * Associates specified kind of node with the action.
     *
     * @param kind Input kind.
     * @param r    Action.
     */
    public void bind(SyntaxKind kind, Runnable r) {
        assert table[currentState][kind.ordinal()] == null;

        table[currentState][kind.ordinal()] = r;
    }

    /**
     * Associates all specified kinds of nodes with an action.
     *
     * @param kinds Set of kinds.
     * @param r     Action.
     */
    public void bind(EnumSet<SyntaxKind> kinds, Runnable r) {
        for (SyntaxKind kind : kinds) {
            bind(kind, r);
        }
    }

    /**
     * Sets the specified action for all empty node kinds (cells).
     *
     * @param r Action.
     */
    public void bind(Runnable r) {
        bind(r, true);
    }

    /**
     * Sets the specified action either for all kinds of nodes, or only for empty ones.
     *
     * @param r            Action.
     * @param setOnlyEmpty If true, then it will be set only for those cells that are null. If false, it will be set for all cells.
     */
    public void bind(Runnable r, boolean setOnlyEmpty) {
        for (int i = 0; i < table[currentState].length; i++) {
            if (table[currentState][i] == null && setOnlyEmpty) continue;
            table[currentState][i] = r;
        }
    }

    /**
     * Gets the action in the specified state for the specified input node kind.
     *
     * @return The action of the {@link Runnable}, if it has been set; otherwise {@code null}.
     */
    public Runnable get(int state, SyntaxKind kind) {
        return table[state][kind.ordinal()];
    }

    /**
     * Gets set of kinds which have the binding.
     */
    public EnumSet<SyntaxKind> get(int state) {
        ArrayList<SyntaxKind> relatedKinds = new ArrayList<>();

        for (int i = 0; i < table[state].length; i++) {
            if (table[state][i] != null)
                relatedKinds.add(SyntaxKind.values()[i]);
        }

        return EnumSet.copyOf(relatedKinds);
    }
}