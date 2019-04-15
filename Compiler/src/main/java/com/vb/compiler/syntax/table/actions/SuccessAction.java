package com.vb.compiler.syntax.table.actions;

public class SuccessAction implements Action {
    @Override
    public void visit(ActionHandler handler) {
        //System.out.println("done all");
    }

    @Override
    public boolean isLastPossible() {
        return true;
    }
}
