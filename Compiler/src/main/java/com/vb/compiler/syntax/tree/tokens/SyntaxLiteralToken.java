package com.vb.compiler.syntax.tree.tokens;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;

public class SyntaxLiteralToken<T extends Object> extends SyntaxToken {
    private T value;

    public SyntaxLiteralToken(T value, SyntaxDiagnosticInfo[] errors) {
        super(value.toString(), SyntaxKind.LITERAL_TOKEN, errors);
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
