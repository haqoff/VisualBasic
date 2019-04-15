package com.vb.compiler.syntax.tree.tokens;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.SyntaxNode;

public class SyntaxToken extends SyntaxNode {

    private String textValue;

    public SyntaxToken(String textValue, SyntaxKind kind, SyntaxDiagnosticInfo[] errors) {
        super(kind, errors);

        this.textValue = textValue;
    }

    public String getTextValue() {
        return textValue;
    }
}
