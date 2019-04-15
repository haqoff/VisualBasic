package com.vb.compiler.syntax.table;

import com.vb.compiler.syntax.table.actions.GotoAction;
import com.vb.compiler.syntax.table.actions.ReduceAction;
import com.vb.compiler.syntax.table.actions.ShiftAction;
import com.vb.compiler.syntax.table.actions.SuccessAction;

import static com.vb.compiler.syntax.SyntaxKind.*;

public class VBTable extends AbstractLRTable {
    public VBTable() {
        super(50);
    }

    @Override
    protected void initializeTable() {
        setAction(0, DIM_KEYWORD, new ShiftAction(6));
        setAction(0, IDENTIFIER_TOKEN, new ShiftAction(7));
        setAction(0, SELECT_KEYWORD, new ShiftAction(8));
        setAction(0, STATEMENTS_LIST_NONTERM, new GotoAction(1));
        setAction(0, STATEMENT_NONTERM, new GotoAction(2));
        setAction(0, STM_DECLAREMENT_BODY_NONTERM, new GotoAction(3));
        setAction(0, STM_VAR_ASSIGMENT_NONTERM, new GotoAction(4));
        setAction(0, STM_SELECT_CASE_NONTERM, new GotoAction(5));

        setAction(1, END_OF_TEXT, new SuccessAction());

        setAction(2, NEW_LINE_TOKEN, new ShiftAction(11));
        setAction(2, END_OF_TEXT, new ReduceAction(STATEMENTS_CONTINUE_NONTERM, 0));
        setAction(2, STATEMENTS_CONTINUE_NONTERM, new GotoAction(9));
        setAction(2, NEW_LINE_LIST_NONTERM, new GotoAction(10));

        setAction(3, NEW_LINE_TOKEN, new ReduceAction(STATEMENT_NONTERM, 1));
        setAction(3, END_OF_TEXT, new ReduceAction(STATEMENT_NONTERM, 1));

        setAction(4, NEW_LINE_TOKEN, new ReduceAction(STATEMENT_NONTERM, 1));
        setAction(4, END_OF_TEXT, new ReduceAction(STATEMENT_NONTERM, 1));

        setAction(5, NEW_LINE_TOKEN, new ReduceAction(STATEMENT_NONTERM, 1));
        setAction(5, END_OF_TEXT, new ReduceAction(STATEMENT_NONTERM, 1));

        setAction(6, IDENTIFIER_TOKEN, new ShiftAction(13));
        setAction(6, ID_LIST_NONTERM, new GotoAction(12));

        setAction(7, EQUALS_TOKEN, new ShiftAction(14));

        setAction(8, CASE_KEYWORD, new ShiftAction(15));

        setAction(9, END_OF_TEXT, new ReduceAction(STATEMENTS_LIST_NONTERM, 2));

        setAction(10, NEW_LINE_TOKEN, new ShiftAction(17));
        setAction(10, DIM_KEYWORD, new ShiftAction(6));
        setAction(10, IDENTIFIER_TOKEN, new ShiftAction(7));
        setAction(10, SELECT_KEYWORD, new ShiftAction(8));
        setAction(10, END_OF_TEXT, new ReduceAction(STATEMENTS_LIST_NULLABLE_NONTERM, 0));
        setAction(10, STATEMENTS_LIST_NONTERM, new GotoAction(18));
        setAction(10, STATEMENTS_LIST_NULLABLE_NONTERM, new GotoAction(16));
        setAction(10, STATEMENTS_LIST_NULLABLE_NONTERM, new GotoAction(16));
        setAction(10, STATEMENT_NONTERM, new GotoAction(2));
        setAction(10, STM_DECLAREMENT_BODY_NONTERM, new GotoAction(3));
        setAction(10, STM_VAR_ASSIGMENT_NONTERM, new GotoAction(4));
        setAction(10, STM_SELECT_CASE_NONTERM, new GotoAction(5));

        setAction(11, NEW_LINE_TOKEN, new ReduceAction(NEW_LINE_LIST_NONTERM, 1));
        setAction(11, DIM_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 1));
        setAction(11, IDENTIFIER_TOKEN, new ReduceAction(NEW_LINE_LIST_NONTERM, 1));
        setAction(11, SELECT_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 1));
        setAction(11, CASE_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 1));
        setAction(11, END_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 1));
        setAction(11, END_OF_TEXT, new ReduceAction(NEW_LINE_LIST_NONTERM, 1));

        setAction(12, AS_KEYWORD, new ShiftAction(19));

        setAction(13, AS_KEYWORD, new ReduceAction(ID_LIST_NONTERM, 1));
        setAction(13, COMMA_TOKEN, new ShiftAction(20));

        changeDefaultAction(14, new ShiftAction(21));

        setAction(15, IDENTIFIER_TOKEN, new ShiftAction(22));

        setAction(16, END_OF_TEXT, new ReduceAction(STATEMENTS_CONTINUE_NONTERM, 2));

        setAction(17, NEW_LINE_TOKEN, new ReduceAction(NEW_LINE_LIST_NONTERM, 2));
        setAction(17, DIM_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 2));
        setAction(17, IDENTIFIER_TOKEN, new ReduceAction(NEW_LINE_LIST_NONTERM, 2));
        setAction(17, SELECT_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 2));
        setAction(17, CASE_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 2));
        setAction(17, END_KEYWORD, new ReduceAction(NEW_LINE_LIST_NONTERM, 2));
        setAction(17, END_OF_TEXT, new ReduceAction(NEW_LINE_LIST_NONTERM, 2));

        setAction(18, END_OF_TEXT, new ReduceAction(STATEMENTS_LIST_NULLABLE_NONTERM, 1));

        setAction(19, INTEGER_KEYWORD, new ShiftAction(24));
        setAction(19, STRING_KEYWORD, new ShiftAction(25));
        setAction(19, DOUBLE_KEYWORD, new ShiftAction(26));
        setAction(19, SPECIAL_TYPE_NONTERM, new GotoAction(23));

        setAction(20, IDENTIFIER_TOKEN, new ShiftAction(13));
        setAction(20, ID_LIST_NONTERM, new GotoAction(27));

        setAction(21, NEW_LINE_TOKEN, new ReduceAction(STM_VAR_ASSIGMENT_NONTERM, 3));
        setAction(21, END_OF_TEXT, new ReduceAction(STM_VAR_ASSIGMENT_NONTERM, 3));

        setAction(22, NEW_LINE_TOKEN, new ShiftAction(11));
        setAction(22, NEW_LINE_LIST_NONTERM, new GotoAction(28));

        setAction(23, NEW_LINE_TOKEN, new ReduceAction(STM_DECLAREMENT_BODY_NONTERM, 4));
        setAction(23, END_OF_TEXT, new ReduceAction(STM_DECLAREMENT_BODY_NONTERM, 4));

        setAction(24, NEW_LINE_TOKEN, new ReduceAction(SPECIAL_TYPE_NONTERM, 1));
        setAction(24, END_OF_TEXT, new ReduceAction(SPECIAL_TYPE_NONTERM, 1));

        setAction(25, NEW_LINE_TOKEN, new ReduceAction(SPECIAL_TYPE_NONTERM, 1));
        setAction(25, END_OF_TEXT, new ReduceAction(SPECIAL_TYPE_NONTERM, 1));

        setAction(26, NEW_LINE_TOKEN, new ReduceAction(SPECIAL_TYPE_NONTERM, 1));
        setAction(26, END_OF_TEXT, new ReduceAction(SPECIAL_TYPE_NONTERM, 1));

        setAction(27, AS_KEYWORD, new ReduceAction(ID_LIST_NONTERM, 3));

        setAction(28, NEW_LINE_TOKEN, new ShiftAction(17));
        setAction(28, CASE_KEYWORD, new ShiftAction(30));
        setAction(28, END_KEYWORD, new ReduceAction(CASE_SET_NONTERM, 0));
        setAction(28, CASE_SET_NONTERM, new GotoAction(29));

        setAction(29, END_KEYWORD, new ShiftAction(31));

        setAction(30, ELSE_KEYWORD, new ShiftAction(35));
        setAction(30, LITERAL_TOKEN, new ShiftAction(36));
        setAction(30, CASE_ELSE_LINE_NONTERM, new GotoAction(33));
        setAction(30, CASE_VALUE_LINE_NONTERM, new GotoAction(32));
        setAction(30, CASE_EXP_NONTERM, new GotoAction(34));

        setAction(31, SELECT_KEYWORD, new ShiftAction(37));

        setAction(32, END_KEYWORD, new ReduceAction(CASE_SET_NONTERM, 2));

        setAction(33, END_KEYWORD, new ReduceAction(CASE_SET_NONTERM, 2));

        setAction(34, NEW_LINE_TOKEN, new ShiftAction(11));
        setAction(34, NEW_LINE_LIST_NONTERM, new GotoAction(38));

        setAction(35, NEW_LINE_TOKEN, new ShiftAction(11));
        setAction(35, NEW_LINE_LIST_NONTERM, new GotoAction(39));

        setAction(36, NEW_LINE_TOKEN, new ReduceAction(CASE_EXP_NONTERM, 1));
        setAction(36, TO_KEYWORD, new ShiftAction(40));

        setAction(37, NEW_LINE_TOKEN, new ReduceAction(STM_SELECT_CASE_NONTERM, 7));
        setAction(37, END_OF_TEXT, new ReduceAction(STM_SELECT_CASE_NONTERM, 7));

        setAction(38, NEW_LINE_TOKEN, new ShiftAction(17));
        setAction(38, DIM_KEYWORD, new ShiftAction(6));
        setAction(38, IDENTIFIER_TOKEN, new ShiftAction(7));
        setAction(38, SELECT_KEYWORD, new ShiftAction(8));
        setAction(38, CASE_KEYWORD, new ReduceAction(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
        setAction(38, END_KEYWORD, new ReduceAction(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
        setAction(38, STATEMENTS_LIST_WITH_NL_NONTERM, new GotoAction(41));
        setAction(38, STATEMENT_NONTERM, new GotoAction(42));
        setAction(38, STM_DECLAREMENT_BODY_NONTERM, new GotoAction(3));
        setAction(38, STM_VAR_ASSIGMENT_NONTERM, new GotoAction(4));
        setAction(38, STM_SELECT_CASE_NONTERM, new GotoAction(5));

        setAction(39, NEW_LINE_TOKEN, new ShiftAction(17));
        setAction(39, DIM_KEYWORD, new ShiftAction(6));
        setAction(39, IDENTIFIER_TOKEN, new ShiftAction(7));
        setAction(39, SELECT_KEYWORD, new ShiftAction(8));
        setAction(39, END_KEYWORD, new ReduceAction(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
        setAction(39, STATEMENTS_LIST_WITH_NL_NONTERM, new GotoAction(43));
        setAction(39, STATEMENT_NONTERM, new GotoAction(42));
        setAction(39, STM_DECLAREMENT_BODY_NONTERM, new GotoAction(3));
        setAction(39, STM_VAR_ASSIGMENT_NONTERM, new GotoAction(4));
        setAction(39, STM_SELECT_CASE_NONTERM, new GotoAction(5));

        setAction(40, LITERAL_TOKEN, new ShiftAction(44));

        setAction(41, CASE_KEYWORD, new ShiftAction(30));
        setAction(41, END_KEYWORD, new ReduceAction(CASE_SET_NONTERM, 0));
        setAction(41, CASE_SET_NONTERM, new GotoAction(45));

        setAction(42, NEW_LINE_TOKEN, new ShiftAction(11));
        setAction(42, NEW_LINE_LIST_NONTERM, new GotoAction(46));

        setAction(43, END_KEYWORD, new ReduceAction(CASE_ELSE_LINE_NONTERM, 3));

        setAction(44, NEW_LINE_TOKEN, new ReduceAction(CASE_EXP_NONTERM, 3));

        setAction(45, END_KEYWORD, new ReduceAction(CASE_VALUE_LINE_NONTERM, 4));

        setAction(46, NEW_LINE_TOKEN, new ShiftAction(17));
        setAction(46, DIM_KEYWORD, new ShiftAction(6));
        setAction(46, IDENTIFIER_TOKEN, new ShiftAction(7));
        setAction(46, SELECT_KEYWORD, new ShiftAction(8));
        setAction(46, CASE_KEYWORD, new ReduceAction(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
        setAction(46, END_KEYWORD, new ReduceAction(STATEMENTS_LIST_WITH_NL_NONTERM, 0));
        setAction(46, STATEMENTS_LIST_WITH_NL_NONTERM, new GotoAction(47));
        setAction(46, STATEMENT_NONTERM, new GotoAction(42));
        setAction(46, STM_DECLAREMENT_BODY_NONTERM, new GotoAction(3));
        setAction(46, STM_VAR_ASSIGMENT_NONTERM, new GotoAction(4));
        setAction(46, STM_SELECT_CASE_NONTERM, new GotoAction(5));

        setAction(47, CASE_KEYWORD, new ReduceAction(STATEMENTS_LIST_WITH_NL_NONTERM, 3));
        setAction(47, END_KEYWORD, new ReduceAction(STATEMENTS_LIST_WITH_NL_NONTERM, 3));
    }
}
