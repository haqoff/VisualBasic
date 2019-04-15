package com.vb.compiler.syntax.tree.tokens;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;

public class SyntaxIdentifier extends SyntaxToken {

    private String name;

    public SyntaxIdentifier(String name, SyntaxDiagnosticInfo[] errors) {
        super(name, SyntaxKind.IDENTIFIER_TOKEN, errors);

        if (name == null || name.length() == 0) throw new IllegalArgumentException("name cannot be null or empty");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
