This project demonstrates how the LALR(1) parser works using the example of Visual Basic. 
The grammar of the language is as follows:

```
PROGRAM -> STATEMENTS_LIST
STATEMENTS_LIST -> STATEMENT STATEMENTS_CONTINUE  
STATEMENTS_CONTINUE -> ''  
STATEMENTS_CONTINUE ->  NEW_LINE_LIST STATEMENTS_LIST_NULLABLE  
STATEMENTS_LIST_NULLABLE -> ''  
STATEMENTS_LIST_NULLABLE -> STATEMENTS_LIST  
STATEMENTS_LIST_WITH_NL -> STATEMENT NEW_LINE_LIST STATEMENTS_LIST_WITH_NL  
STATEMENTS_LIST_WITH_NL ->  ''  
STATEMENT -> STM_DECLAREMENT_BODY   
STATEMENT -> STM_VAR_ASSIGMENT  
STATEMENT -> STM_SELECT_CASE  
NEW_LINE_LIST -> new_line  
NEW_LINE_LIST -> NEW_LINE_LIST new_line  
STM_DECLAREMENT_BODY -> Dim ID_LIST as SPECIAL_TYPE  
ID_LIST -> id  
ID_LIST -> id , ID_LIST  
SPECIAL_TYPE -> integer  
SPECIAL_TYPE -> string  
SPECIAL_TYPE -> double  
STM_VAR_ASSIGMENT -> id = expression  
STM_SELECT_CASE -> select case id NEW_LINE_LIST CASE_SET end select  
CASE_SET -> case CASE_VALUE_LINE  
CASE_SET -> case CASE_ELSE_LINE  
CASE_SET -> ''  
CASE_ELSE_LINE -> else NEW_LINE_LIST STATEMENTS_LIST_WITH_NL  
CASE_VALUE_LINE -> CASE_EXP NEW_LINE_LIST STATEMENTS_LIST_WITH_NL CASE_SET  
CASE_EXP -> literal
CASE_EXP -> literal to literal  
```

Currently only stages of lexical and syntactic analysis are implemented.
Expressions are parsed using the shunting-yard algorithm (Dijkstra).

So "AllInOne" code looks like this:
```
Dim a,b as integer
Dim x as double
Dim d as string
x = 4
d = "some string here" 'comment look like this
select case x
	case 0
		a=a+6*(5+a)
	case 2 to 6
		a=9
	case else
		b=0
end select
```