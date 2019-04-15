package com.vb.compiler.syntax.tree;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;

import java.util.Arrays;

public abstract class SyntaxNode {
    private SyntaxKind kind;
    private SyntaxDiagnosticInfo[] errors;

    public SyntaxNode(SyntaxKind kind, SyntaxDiagnosticInfo[] errors) {
        assert kind != null;
        assert errors != null;

        this.kind = kind;
        this.errors = errors;
    }

    public SyntaxKind getKind() {
        return kind;
    }

    public void addError(SyntaxDiagnosticInfo newError) {
        SyntaxDiagnosticInfo[] errorsArray = new SyntaxDiagnosticInfo[1];
        errorsArray[0] = newError;
        addErrors(errorsArray);
    }

    public void addErrors(SyntaxDiagnosticInfo[] newErrors) {
        int concatLength = errors.length + newErrors.length;
        SyntaxDiagnosticInfo[] concatArray = Arrays.copyOf(errors, concatLength);

        for (int i = 0; i < newErrors.length; i++) {
            concatArray[i + errors.length] = newErrors[i];
        }

        errors = concatArray;
    }

    public SyntaxDiagnosticInfo[] getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors.length > 0;
    }

    @Override
    public String toString() {
        return kind.toString();
    }
}
