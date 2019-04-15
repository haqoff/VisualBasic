package com.vb.compiler.parser;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.table.AbstractLRTable;
import com.vb.compiler.syntax.table.actions.Action;
import com.vb.compiler.syntax.table.actions.ActionHandler;
import com.vb.compiler.syntax.tree.SyntaxNode;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;

import java.util.Stack;

public class LanguageParser extends ActionHandler {
    private Stack<SyntaxNode> viewedNodes;
    private Stack<Integer> states;

    private SyntaxToken inputToken;
    private boolean lastActionReduce;

    private Lexer lexer;
    private AbstractLRTable table;

    public LanguageParser(Lexer lexer, AbstractLRTable table) {
        this.lexer = lexer;
        this.table = table;

        viewedNodes = new Stack<>();
        states = new Stack<>();
        states.add(0);

        inputToken = lexer.nextToken();
        lastActionReduce = false;
    }

    public SyntaxNode getTree() {
        parse();
        return viewedNodes.peek();
    }

    public void parse() {
        while (true) {
            assert !states.empty() && (!viewedNodes.empty() || inputToken.getKind() != SyntaxKind.END_OF_TEXT);
            SyntaxNode actionNode = (lastActionReduce) ? viewedNodes.peek() : inputToken;

            Action action = table.getAction(states.peek(), actionNode.getKind());

            assert action != null;
            action.visit(this);

            //System.out.println(Arrays.toString(viewedNodes.toArray()));
            if (action.isLastPossible()) break;
        }
    }

    @Override
    protected void shift(int state) {
        viewedNodes.add(inputToken);
        changeState(state);
        inputToken = lexer.nextToken();
        lastActionReduce = false;
    }

    @Override
    protected void changeState(int state) {
        states.add(state);
        lastActionReduce = false;
    }

    @Override
    protected SyntaxNode[] extractNodes(int length) {
        assert states.size() > length;
        assert viewedNodes.size() >= length;

        SyntaxNode[] nodes = new SyntaxNode[length];

        for (int i = length - 1; i >= 0; i--) {
            nodes[i] = viewedNodes.pop();
            states.pop();
        }

        return nodes;
    }

    @Override
    protected void addError(SyntaxDiagnosticInfo error) {
        SyntaxNode node = lastActionReduce ? viewedNodes.peek() : inputToken;
        node.addError(error);
    }

    @Override
    protected void addNewNode(SyntaxNode reduced) {
        viewedNodes.add(reduced);
        lastActionReduce = true;
    }
}
