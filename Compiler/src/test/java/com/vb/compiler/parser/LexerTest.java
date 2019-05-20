package com.vb.compiler.parser;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.tokens.SyntaxLiteralToken;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.text.SourceTextReader;
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testValidString() {
        String source = "\"test string 123\"";
        SourceTextReader reader = new SourceTextReader(source);
        Lexer lexer = new Lexer(reader);

        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.LITERAL_TOKEN, token.getKind());

        SyntaxLiteralToken<String> literalToken = (SyntaxLiteralToken<String>) token;
        assertEquals("test string 123", literalToken.getValue());
    }

    @Test
    public void testStringMissingClosingQuote() {
        String source = "\"missing quote";
        SourceTextReader reader = new SourceTextReader(source);
        Lexer lexer = new Lexer(reader);

        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.LITERAL_TOKEN, token.getKind());
        assertTrue(token.hasErrors());

        SyntaxLiteralToken<String> literalToken = (SyntaxLiteralToken<String>) token;
        assertEquals("missing quote", literalToken.getValue());
    }

    @Test
    public void testEmptyString() {
        String source = "\"\"";
        SourceTextReader reader = new SourceTextReader(source);
        Lexer lexer = new Lexer(reader);

        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.LITERAL_TOKEN, token.getKind());
        assertFalse(token.hasErrors());

        SyntaxLiteralToken<String> literalToken = (SyntaxLiteralToken<String>) token;
        assertEquals("", literalToken.getValue());
    }

    @Test
    public void testStartQuoteBeforeEndOfText() {
        String source = "\"";
        SourceTextReader reader = new SourceTextReader(source);
        Lexer lexer = new Lexer(reader);

        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.LITERAL_TOKEN, token.getKind());
        assertTrue(token.hasErrors());

        SyntaxLiteralToken<String> literalToken = (SyntaxLiteralToken<String>) token;
        assertEquals("", literalToken.getValue());
    }

    @Test
    public void testStringSurrounded() {
        String source = "Dim i as integer\n" +
                "i = \"123\"\n" +
                "Dim b as String";

        SourceTextReader reader = new SourceTextReader(source);
        Lexer lexer = new Lexer(reader);

        for (int i = 0; i < 7; i++) {
            //skip until string literal token
            SyntaxToken token = lexer.nextToken();
            assertFalse(token.hasErrors());
        }

        SyntaxToken literal = lexer.nextToken();
        assertEquals(SyntaxKind.LITERAL_TOKEN, literal.getKind());
        assertFalse(literal.hasErrors());

        SyntaxLiteralToken<String> literalToken = (SyntaxLiteralToken<String>) literal;
        assertEquals("123", literalToken.getValue());

        SyntaxToken remain = lexer.nextToken();
        while (remain.getKind() != SyntaxKind.END_OF_TEXT) {
            assertFalse(errorArrayToString(remain.getErrors()), remain.hasErrors());
            remain = lexer.nextToken();
        }
    }

    private String errorArrayToString(SyntaxDiagnosticInfo[] errors) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < errors.length; i++) {
            if (i != 0) sb.append(',');
            sb.append(errors[i]);
        }

        return sb.toString();
    }
}