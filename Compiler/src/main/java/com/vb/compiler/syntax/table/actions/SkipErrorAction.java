package com.vb.compiler.syntax.table.actions;

import com.vb.compiler.error.ErrorCode;
import com.vb.compiler.error.SyntaxDiagnosticInfo;

public class SkipErrorAction extends ShiftAction {
    public SkipErrorAction(int gotoState) {
        super(gotoState);
    }

    @Override
    public void visit(ActionHandler handler) {
        SyntaxDiagnosticInfo error = new SyntaxDiagnosticInfo(ErrorCode.ERR_UNEXPECTED_NODE, 0, 1);
        handler.addError(error);
        super.visit(handler);
    }
}
