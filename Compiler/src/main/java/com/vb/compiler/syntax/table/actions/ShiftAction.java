package com.vb.compiler.syntax.table.actions;

public class ShiftAction extends GotoAction {

    public ShiftAction(int gotoState) {
        super(gotoState);
    }

    @Override
    public void visit(ActionHandler handler) {
        handler.shift(gotoState);
    }
}
