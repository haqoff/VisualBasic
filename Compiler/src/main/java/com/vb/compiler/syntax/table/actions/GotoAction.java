package com.vb.compiler.syntax.table.actions;

public class GotoAction implements Action {
    protected int gotoState;

    public GotoAction(int gotoState) {
        this.gotoState = gotoState;
    }

    @Override
    public void visit(ActionHandler handler) {
        handler.changeState(gotoState);
    }

    @Override
    public boolean isLastPossible() {
        return false;
    }
}
