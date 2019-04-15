package com.vb.compiler.syntax.tree;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;

import java.util.Arrays;

public class NonterminalNode extends SyntaxNode {
    private SyntaxNode[] children;

    public NonterminalNode(SyntaxKind kind, SyntaxNode[] children, SyntaxDiagnosticInfo[] errors) {
        super(kind, errors);
        this.children = children;
    }

    @Override
    public boolean hasErrors() {
        return super.hasErrors() || Arrays.stream(children).anyMatch(e -> e.hasErrors());
    }
}
