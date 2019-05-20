package com.vb.compiler.parser;

import com.vb.compiler.error.CriticalParserException;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.SyntaxNode;
import com.vb.compiler.text.SourceTextReader;
import com.vb.compiler.utils.ConsoleTreePrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LanguageParserTest {

    private ConsoleTreePrinter treePrinter;

    @Before
    public void setUp() {
        treePrinter = new ConsoleTreePrinter();
    }

    @Test
    public void testAllStatementsWithNewLineOnEnd() throws CriticalParserException {
        String data = "Dim variable as integer\n" +
                "Dim b as double\n\n" +
                "Dim c as string\n\n\n " +
                "   'comment here:)\n" +
                "variable = 10\n" +
                "b = 1.9\n" +
                "c = \"testString\"  \n" +
                "select case variable\n" +
                "   case 1 to 5\n" +
                "       'empty\n" +
                "   case 5\n" +
                "       c = \"error  string\"\n" +
                "   case 6 to 9 \n" +
                "       b = (8 + 3) * 4 - 10 / variable \n" +
                "   case else \n" +
                "       c = \"nice\" + \" that incredible\"\n" +
                "end select\n" +
                "\n\n";

        SyntaxNode tree = getTree(data);
        tree.accept(treePrinter, 0);

        Assert.assertEquals(SyntaxKind.STATEMENTS_LIST_NONTERM, tree.getKind());
        Assert.assertFalse(tree.hasErrors());
    }

    @Test
    public void testValidMathExpression() throws CriticalParserException {
        String source = "Dim id as integer\n" +
                "id = id + (8)\n" +
                "id = \"sentences\" + 9 * 5\n" +
                "id = id + id - id * id\n" +
                "id = (9)\n" +
                "id = (2) * 4 * (id + 8 / 2)\n";

        SyntaxNode tree = getTree(source);
        tree.accept(treePrinter, 0);

        Assert.assertEquals(SyntaxKind.STATEMENTS_LIST_NONTERM, tree.getKind());
        Assert.assertFalse(tree.hasErrors());
    }

    @Test
    public void testWrongParenMathExpression() throws CriticalParserException {
        String source = "id = 2+(3))";

        SyntaxNode tree = getTree(source);
        tree.accept(treePrinter, 0);

        Assert.assertEquals(SyntaxKind.STATEMENTS_LIST_NONTERM, tree.getKind());
        Assert.assertTrue(tree.hasErrors());
    }

    @Test
    public void testWrongOperationMathExpression() throws CriticalParserException {
        String source = "id = +";
        SyntaxNode tree = getTree(source);
        tree.accept(treePrinter, 0);

        Assert.assertEquals(SyntaxKind.STATEMENTS_LIST_NONTERM, tree.getKind());
        Assert.assertTrue(tree.hasErrors());
    }

    @Test
    public void testLongText() throws CriticalParserException, IOException {
        String source = new String(Files.readAllBytes(Paths.get("src/test/test.txt")), StandardCharsets.UTF_8);

        SyntaxNode tree = getTree(source);
        tree.accept(treePrinter, 0);

        Assert.assertEquals(SyntaxKind.STATEMENTS_LIST_NONTERM, tree.getKind());
        Assert.assertFalse(tree.hasErrors());
    }

    private SyntaxNode getTree(String sourceText) throws CriticalParserException {
        SourceTextReader reader = new SourceTextReader(sourceText);
        Lexer lexer = new Lexer(reader);
        LanguageParser parser = new LanguageParser(lexer);

        return parser.getTree();
    }
}