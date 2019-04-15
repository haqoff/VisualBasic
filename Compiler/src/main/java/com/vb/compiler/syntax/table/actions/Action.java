package com.vb.compiler.syntax.table.actions;

public interface Action {
    /**
     * @param handler
     */
    void visit(ActionHandler handler);

    /**
     * Determines whether the parsing can continue after this action.
     *
     * @return {@code true} if the parsing can't go on; otherwise {@code false}.
     */
    boolean isLastPossible();
}
