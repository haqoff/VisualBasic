package com.vb.compiler.parser;

import com.vb.compiler.error.CriticalParserException;
import com.vb.compiler.error.ErrorCode;
import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxFacts;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.table.FSMTable;
import com.vb.compiler.syntax.tree.ExpressionNode;
import com.vb.compiler.syntax.tree.NonterminalNode;
import com.vb.compiler.syntax.tree.SyntaxNode;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.utils.ReusableStack;

import java.util.ArrayList;
import java.util.EnumSet;

import static com.vb.compiler.syntax.SyntaxKind.*;

/**
 * Represents a syntax analyze class.
 *
 * @author haqoff
 */
public class LanguageParser {
    private static final int NUMBER_OF_STATES = 48;

    private final ReusableStack<SyntaxNode> viewedNodes;
    private final ReusableStack<ExpressionNode> expressionNodes;
    private final ReusableStack<SyntaxToken> expressionOperators;
    private final ReusableStack<Integer> states;

    private final Lexer lexer;
    private final FSMTable table;

    private SyntaxToken inputToken;
    private int currentState;
    private boolean lastActionReduce;

    private boolean parsed;
    private volatile boolean continueRunning;

    /**
     * Initialize new instance of {@see LanguageParser} class with the specified lexer.
     *
     * @param lexer
     */
    public LanguageParser(Lexer lexer) {
        this.lexer = lexer;

        viewedNodes = new ReusableStack<>();
        expressionNodes = new ReusableStack<>();
        expressionOperators = new ReusableStack<>();
        states = new ReusableStack<>();
        states.add(0);

        inputToken = lexer.nextToken();

        lastActionReduce = false;
        parsed = false;
        continueRunning = true;

        table = new FSMTable(NUMBER_OF_STATES);
        initializeTable();
    }

    /**
     * Gets the root of parsed tree.
     *
     * @return Main nonterminal of parsed text.
     * @throws CriticalParserException Error that occurs when parsing cannot continue.
     */
    public NonterminalNode getTree() throws CriticalParserException {
        if (!parsed) parse();

        return (NonterminalNode) viewedNodes.peek();
    }

    private void parse() throws CriticalParserException {
        while (continueRunning && !parsed) {
            assert !states.empty() && (!viewedNodes.empty() || inputToken.getKind() != END_OF_TEXT);

            SyntaxNode currentNode = lastActionReduce ? viewedNodes.peek() : inputToken;
            currentState = states.peek();

            Runnable r = table.get(currentState, currentNode.getKind());
            if (r == null) {
                EnumSet<SyntaxKind> expectedKinds = table.get(currentState);
                throw new CriticalParserException(currentState, currentNode, expectedKinds);
            }
            r.run();
        }
    }

    private void initializeTable() {
        inState(0);
        {
            bind(DIM_KEYWORD, () -> shift(6));
            bind(IDENTIFIER_TOKEN, () -> shift(7));
            bind(SELECT_KEYWORD, () -> shift(8));
            bind(STATEMENTS_LIST_NONTERM, () -> gotoState(1));
            bind(STATEMENT_NONTERM, () -> gotoState(2));
            bind(STM_DECLAREMENT_BODY_NONTERM, () -> gotoState(3));
            bind(STM_VAR_ASSIGMENT_NONTERM, () -> gotoState(4));
            bind(STM_SELECT_CASE_NONTERM, () -> gotoState(5));
        }

        inState(1);
        {
            bind(END_OF_TEXT, () -> parsed = true);
        }

        inState(2);
        {
            bind(NEW_LINE_TOKEN, () -> shift(11));
            bind(END_OF_TEXT, () -> reduce(STATEMENTS_CONTINUE_NONTERM, 0));
            bind(STATEMENTS_CONTINUE_NONTERM, () -> gotoState(9));
            bind(NEW_LINE_LIST_NONTERM, () -> gotoState(10));
        }

        inState(3);
        {
            bind(NEW_LINE_TOKEN, () -> reduce(STATEMENT_NONTERM, 1));
            bind(END_OF_TEXT, () -> reduce(STATEMENT_NONTERM, 1));
        }

        inState(4);
        {
            bind(NEW_LINE_TOKEN, () -> reduce(STATEMENT_NONTERM, 1));
            bind(END_OF_TEXT, () -> reduce(STATEMENT_NONTERM, 1));
        }

        inState(5);
        {
            bind(NEW_LINE_TOKEN, () -> reduce(STATEMENT_NONTERM, 1));
            bind(END_OF_TEXT, () -> reduce(STATEMENT_NONTERM, 1));
        }

        inState(6);
        {
            bind(IDENTIFIER_TOKEN, () -> shift(13));
            bind(ID_LIST_NONTERM, () -> gotoState(12));
        }

        inState(7);
        {
            bind(EQUALS_TOKEN, () -> shift(14));
        }

        inState(8);
        {
            bind(CASE_KEYWORD, () -> shift(15));
        }

        inState(9);
        {
            bind(END_OF_TEXT, () -> reduce(STATEMENTS_LIST_NONTERM, 2));
        }

        inState(10);
        {
            bind(NEW_LINE_TOKEN, () -> shift(17));
            bind(DIM_KEYWORD, () -> shift(6));
            bind(IDENTIFIER_TOKEN, () -> shift(7));
            bind(SELECT_KEYWORD, () -> shift(8));
            bind(END_OF_TEXT, () -> reduce(STATEMENTS_LIST_NONTERM, 0));
            bind(STATEMENTS_LIST_NONTERM, () -> gotoState(18));
            bind(STATEMENTS_LIST_NULLABLE_NONTERM, () -> gotoState(16));
            bind(STATEMENT_NONTERM, () -> gotoState(2));
            bind(STM_DECLAREMENT_BODY_NONTERM, () -> gotoState(3));
            bind(STM_VAR_ASSIGMENT_NONTERM, () -> gotoState(4));
            bind(STM_SELECT_CASE_NONTERM, () -> gotoState(5));


        }

        inState(11);
        {
            table.bind(EnumSet.of(NEW_LINE_TOKEN, DIM_KEYWORD, IDENTIFIER_TOKEN, SELECT_KEYWORD, CASE_KEYWORD, END_KEYWORD, END_OF_TEXT),
                    () -> reduce(NEW_LINE_LIST_NONTERM, 1));
        }

        inState(12);
        {
            bind(AS_KEYWORD, () -> shift(19));
        }

        inState(13);
        {
            bind(AS_KEYWORD, () -> reduce(ID_LIST_NONTERM, 1));
            bind(COMMA_TOKEN, () -> shift(20));
        }

        inState(14);
        {
            bind(() -> {
                parseExpression();
                gotoState(21);
            }, false);
        }

        inState(15);
        {
            bind(IDENTIFIER_TOKEN, () -> shift(22));
        }

        inState(16);
        {
            bind(END_OF_TEXT, () -> reduce(STATEMENTS_CONTINUE_NONTERM, 2));
        }

        inState(17);
        {
            table.bind(EnumSet.of(NEW_LINE_TOKEN, DIM_KEYWORD, IDENTIFIER_TOKEN, SELECT_KEYWORD, CASE_KEYWORD, END_KEYWORD, END_OF_TEXT),
                    () -> reduce(NEW_LINE_LIST_NONTERM, 2));
        }

        inState(18);
        {
            bind(END_OF_TEXT, () -> reduce(STATEMENTS_LIST_NULLABLE_NONTERM, 1));
        }

        inState(19);
        {
            bind(INTEGER_KEYWORD, () -> shift(24));
            bind(STRING_KEYWORD, () -> shift(25));
            bind(DOUBLE_KEYWORD, () -> shift(26));
            bind(SPECIAL_TYPE_NONTERM, () -> gotoState(23));
        }

        inState(20);
        {
            bind(IDENTIFIER_TOKEN, () -> shift(13));
            bind(ID_LIST_NONTERM, () -> gotoState(27));
        }

        inState(21);
        {
            bind(EnumSet.of(NEW_LINE_TOKEN, END_OF_TEXT),
                    () -> reduce(STM_VAR_ASSIGMENT_NONTERM, 3));
        }

        inState(22);
        {
            bind(NEW_LINE_TOKEN, () -> shift(11));
            bind(NEW_LINE_LIST_NONTERM, () -> gotoState(28));
        }

        inState(23);
        {
            bind(EnumSet.of(NEW_LINE_TOKEN, END_OF_TEXT), () -> reduce(STM_DECLAREMENT_BODY_NONTERM, 4));
        }

        inState(24);
        {
            bind(EnumSet.of(NEW_LINE_TOKEN, END_OF_TEXT), () -> reduce(SPECIAL_TYPE_NONTERM, 1));
        }

        inState(25);
        {
            bind(EnumSet.of(NEW_LINE_TOKEN, END_OF_TEXT), () -> reduce(SPECIAL_TYPE_NONTERM, 1));
        }

        inState(26);
        {
            bind(EnumSet.of(NEW_LINE_TOKEN, END_OF_TEXT), () -> reduce(SPECIAL_TYPE_NONTERM, 1));
        }

        inState(27);
        {
            bind(AS_KEYWORD, () -> reduce(ID_LIST_NONTERM, 3));
        }

        inState(28);
        {
            bind(NEW_LINE_TOKEN, () -> shift(17));
            bind(CASE_KEYWORD, () -> shift(30));
            bind(END_KEYWORD, () -> reduce(CASE_SET_NONTERM, 0));
            bind(CASE_SET_NONTERM, () -> gotoState(29));
        }

        inState(29);
        {
            bind(END_KEYWORD, () -> shift(31));
        }

        inState(30);
        {
            bind(ELSE_KEYWORD, () -> shift(35));
            bind(LITERAL_TOKEN, () -> shift(36));
            bind(CASE_ELSE_LINE_NONTERM, () -> gotoState(33));
            bind(CASE_VALUE_LINE_NONTERM, () -> gotoState(32));
            bind(CASE_EXP_NONTERM, () -> gotoState(34));
        }

        inState(31);
        {
            bind(SELECT_KEYWORD, () -> shift(37));
        }

        inState(32);
        {
            bind(END_KEYWORD, () -> reduce(CASE_SET_NONTERM, 2));
        }

        inState(33);
        {
            bind(END_KEYWORD, () -> reduce(CASE_SET_NONTERM, 2));
        }

        inState(34);
        {
            bind(NEW_LINE_TOKEN, () -> shift(11));
            bind(NEW_LINE_LIST_NONTERM, () -> gotoState(38));
        }

        inState(35);
        {
            bind(NEW_LINE_TOKEN, () -> shift(11));
            bind(NEW_LINE_LIST_NONTERM, () -> gotoState(39));
        }

        inState(36);
        {
            bind(NEW_LINE_TOKEN, () -> reduce(CASE_EXP_NONTERM, 1));
            bind(TO_KEYWORD, () -> shift(40));
        }

        inState(37);
        {
            bind(EnumSet.of(NEW_LINE_TOKEN, END_OF_TEXT), () -> reduce(STM_SELECT_CASE_NONTERM, 7));
        }

        inState(38);
        {
            bind(NEW_LINE_TOKEN, () -> shift(17));
            bind(DIM_KEYWORD, () -> shift(6));
            bind(IDENTIFIER_TOKEN, () -> shift(7));
            bind(SELECT_KEYWORD, () -> shift(8));
            bind(CASE_KEYWORD, () -> reduce(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
            bind(END_KEYWORD, () -> reduce(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
            bind(STATEMENTS_LIST_WITH_NL_NONTERM, () -> gotoState(41));
            bind(STATEMENT_NONTERM, () -> gotoState(42));
            bind(STM_DECLAREMENT_BODY_NONTERM, () -> gotoState(3));
            bind(STM_VAR_ASSIGMENT_NONTERM, () -> gotoState(4));
            bind(STM_SELECT_CASE_NONTERM, () -> gotoState(5));
        }

        inState(39);
        {
            bind(NEW_LINE_TOKEN, () -> shift(17));
            bind(DIM_KEYWORD, () -> shift(6));
            bind(IDENTIFIER_TOKEN, () -> shift(7));
            bind(SELECT_KEYWORD, () -> shift(8));
            bind(END_KEYWORD, () -> reduce(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
            bind(STATEMENTS_LIST_WITH_NL_NONTERM, () -> gotoState(43));
            bind(STATEMENT_NONTERM, () -> gotoState(42));
            bind(STM_DECLAREMENT_BODY_NONTERM, () -> gotoState(3));
            bind(STM_VAR_ASSIGMENT_NONTERM, () -> gotoState(4));
            bind(STM_SELECT_CASE_NONTERM, () -> gotoState(5));
        }

        inState(40);
        {
            bind(LITERAL_TOKEN, () -> shift(44));
        }

        inState(41);
        {
            bind(CASE_KEYWORD, () -> shift(30));
            bind(END_KEYWORD, () -> reduce(CASE_SET_NONTERM, 0));
            bind(CASE_SET_NONTERM, () -> gotoState(45));
        }

        inState(42);
        {
            bind(NEW_LINE_TOKEN, () -> shift(11));
            bind(NEW_LINE_LIST_NONTERM, () -> gotoState(46));
        }

        inState(43);
        {
            bind(END_KEYWORD, () -> reduce(CASE_ELSE_LINE_NONTERM, 3));
        }

        inState(44);
        {
            bind(NEW_LINE_TOKEN, () -> reduce(CASE_EXP_NONTERM, 3));
        }

        inState(45);
        {
            bind(END_KEYWORD, () -> reduce(CASE_VALUE_LINE_NONTERM, 4));
        }

        inState(46);
        {
            bind(NEW_LINE_TOKEN, () -> shift(17));
            bind(DIM_KEYWORD, () -> shift(6));
            bind(IDENTIFIER_TOKEN, () -> shift(7));
            bind(SELECT_KEYWORD, () -> shift(8));
            bind(CASE_KEYWORD, () -> reduce(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
            bind(END_KEYWORD, () -> reduce(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
            bind(STATEMENTS_LIST_WITH_NL_NONTERM, () -> gotoState(47));
            bind(STATEMENT_NONTERM, () -> gotoState(42));
            bind(STM_DECLAREMENT_BODY_NONTERM, () -> gotoState(3));
            bind(STM_VAR_ASSIGMENT_NONTERM, () -> gotoState(4));
            bind(STM_SELECT_CASE_NONTERM, () -> gotoState(5));
        }

        inState(47);
        {
            bind(EnumSet.of(CASE_KEYWORD, END_KEYWORD),
                    () -> reduce(STATEMENTS_LIST_WITH_NL_NONTERM, 3));
        }
    }

    private void parseExpression() {

        ArrayList<SyntaxDiagnosticInfo> deferredErrors = new ArrayList<>();
        SyntaxKind inputKind = inputToken.getKind();

        while (inputKind != NEW_LINE_TOKEN && inputKind != END_OF_TEXT) {

            if (inputKind == OPEN_PAREN_TOKEN) expressionOperators.add(inputToken);
            else if (inputKind == CLOSE_PAREN_TOKEN) {

                boolean openParenFound = false;

                while (!expressionOperators.empty()) {
                    SyntaxToken token = expressionOperators.pop();

                    if (token.getKind() != OPEN_PAREN_TOKEN) makeExpressionNode(token);
                    else {
                        openParenFound = true;
                        break;
                    }
                }

                if (!openParenFound) {
                    SyntaxDiagnosticInfo unbalancedRightParen = new SyntaxDiagnosticInfo(ErrorCode.ERR_UNBALANCED_RIGHT_PARENTHESES, 0, 1);
                    deferredErrors.add(unbalancedRightParen);
                }
            } else {
                int precedenceOp1 = SyntaxFacts.getOperationPrecedence(inputKind);
                if (precedenceOp1 > 0) {
                    while (true) {
                        if (expressionOperators.empty()) break;

                        int precedenceOp2 = SyntaxFacts.getOperationPrecedence(expressionOperators.peek().getKind());

                        boolean leftAs = SyntaxFacts.isLeftAssociative(inputKind) && precedenceOp1 <= precedenceOp2;
                        boolean rightAs = !SyntaxFacts.isLeftAssociative(inputKind) && precedenceOp1 < precedenceOp2;

                        if (leftAs || rightAs) {
                            SyntaxToken op2 = expressionOperators.pop();
                            makeExpressionNode(op2);
                        } else break;
                    }
                    expressionOperators.add(inputToken);
                } else {

                    if (!SyntaxFacts.canBeInExpression(inputKind)) {
                        SyntaxDiagnosticInfo unexpectedTokenInExp =
                                new SyntaxDiagnosticInfo(ErrorCode.ERR_UNEXPECTED_TOKEN, 0, 1);
                        deferredErrors.add(unexpectedTokenInExp);
                    } else {
                        expressionNodes.add(new ExpressionNode(inputToken));
                    }
                }
            }

            inputToken = lexer.nextToken();
            inputKind = inputToken.getKind();
        }

        while (!expressionOperators.empty()) {
            makeExpressionNode(expressionOperators.pop());
        }

        SyntaxNode expression = expressionNodes.pop();
        if (expression == null) {
            expression = new NonterminalNode(ERROR_NONTERM);
            expression.addErrors(new SyntaxDiagnosticInfo(ErrorCode.ERR_MISSING_EXPRESSION, 0, 1));
        }

        expression.addErrors(deferredErrors.toArray(new SyntaxDiagnosticInfo[0]));
        if (expressionNodes.size() > 0)
            expression.addErrors(new SyntaxDiagnosticInfo(ErrorCode.ERR_MISSING_OPERATOR, 1, 1));

        viewedNodes.add(expression);

        expressionNodes.reset();
        expressionOperators.reset();
    }

    private void makeExpressionNode(SyntaxToken token, SyntaxDiagnosticInfo... errors) {
        boolean expressionMissing = expressionNodes.size() < 2;

        ExpressionNode right = expressionNodes.pop();
        ExpressionNode left = expressionNodes.pop();

        ExpressionNode node = new ExpressionNode(token, left, right);
        node.addErrors(errors);
        if (expressionMissing) node.addErrors(new SyntaxDiagnosticInfo(ErrorCode.ERR_MISSING_EXPRESSION, 0, 0));
        expressionNodes.add(node);
    }

    private void shift() {
        viewedNodes.add(inputToken);
        inputToken = lexer.nextToken();
    }

    private void shift(int state) {
        shift();
        gotoState(state);
    }

    private void gotoState(int state) {
        states.add(state);
        lastActionReduce = false;
    }

    private void reduce(SyntaxKind kind, int length) {
        reduce(kind, length, new SyntaxDiagnosticInfo[0]);
    }

    private void reduce(SyntaxKind kind, int length, SyntaxDiagnosticInfo[] errors) {
        assert states.size() > length;
        assert viewedNodes.size() >= length;

        SyntaxNode[] children = new SyntaxNode[length];

        for (int i = length - 1; i >= 0; i--) {
            children[i] = viewedNodes.pop();
            states.pop();
        }

        SyntaxNode reduced = new NonterminalNode(kind, children);
        reduced.addErrors(errors);

        viewedNodes.add(reduced);
        lastActionReduce = true;
    }

    //To keep the initialization of the table as short as possible.
    private void bind(SyntaxKind kind, Runnable r) {
        table.bind(kind, r);
    }

    private void bind(Runnable r, boolean setOnEmptyOnly) {
        table.bind(r, setOnEmptyOnly);
    }

    private void bind(EnumSet<SyntaxKind> kinds, Runnable r) {
        table.bind(kinds, r);
    }

    private void inState(int state) {
        table.inState(state);
    }
}
