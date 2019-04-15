package com.vb.compiler.parser;

import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.table.VBTable;
import com.vb.compiler.syntax.tree.SyntaxNode;
import com.vb.compiler.text.SourceTextReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class LanguageParserTest {

    private LanguageParser parser;

    @Before
    public void setUp() throws Exception {
        String data = new String(Files.readAllBytes(Paths.get("D:\\My\\Develop\\Java\\VisualBasic\\Compiler\\src\\test\\test.txt")));
        Lexer lexer = new Lexer(new SourceTextReader(data));

        parser = new LanguageParser(lexer, new VBTable());
    }

    @Test
    public void getTree() {
        long start = System.currentTimeMillis();
        SyntaxNode node = parser.getTree();
        long end = System.currentTimeMillis();
        System.out.println("DEBUG: Parsing A took " + (end - start) + " MilliSeconds");
        Assert.assertEquals(node.getKind(), SyntaxKind.STATEMENTS_LIST_NONTERM);
    }
}