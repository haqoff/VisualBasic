package com.vb.compiler.error;

import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.SyntaxNode;

import java.util.EnumSet;

/**
 * Represents an error that occurs during the syntax parsing process.
 *
 * @author haqoff
 */
public class CriticalParserException extends Exception {
    private SyntaxNode errorNode;
    private int state;
    private EnumSet<SyntaxKind> expectedKinds;

    /**
     * Initialize new instance of {@link CriticalParserException} class with specified state, input-error node and expected kinds.
     *
     * @param state         The state in which the error occurred.
     * @param inputNode     The input node that could not be processed.
     * @param expectedKinds All kinds of nodes that are available in this state.
     */
    public CriticalParserException(int state, SyntaxNode inputNode, EnumSet<SyntaxKind> expectedKinds) {
        this.state = state;
        this.errorNode = inputNode;
        this.expectedKinds = expectedKinds;
    }

    /**
     * Gets all expected kinds, including non-terminals.
     */
    public EnumSet<SyntaxKind> getExpectedKinds() {
        return expectedKinds;
    }

    /**
     * Gets an unexpected node that was encountered during parsing.
     */
    public SyntaxNode getErrorNode() {
        return errorNode;
    }

    /**
     * Gets the parse state in which the error was received.
     */
    public int getState() {
        return state;
    }

    /**
     * Gets debug error information as a string.
     */
    @Override
    public String toString() {
        return String.format("In state %d, a node of kind %s was encountered. Expected kinds: %s.", state, errorNode, expectedKinds);
    }
}
