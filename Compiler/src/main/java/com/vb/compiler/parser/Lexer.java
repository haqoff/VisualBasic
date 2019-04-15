package com.vb.compiler.parser;

import com.vb.compiler.error.ErrorCode;
import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.syntax.SpecialType;
import com.vb.compiler.syntax.SyntaxFacts;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.tokens.SyntaxIdentifier;
import com.vb.compiler.syntax.tree.tokens.SyntaxLiteralToken;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.text.SourceTextReader;

import java.util.ArrayList;

/**
 * Represents a lexer that splits the input text into tokens.
 */
public class Lexer {

    private SourceTextReader reader;
    private ArrayList<SyntaxDiagnosticInfo> errors;

    /**
     * Initializes new instance of {@see Lexer} class based on specified reader.
     *
     * @param reader Reader of source text.
     * @throws IllegalArgumentException Occurs when {@code reader == null}.
     */
    public Lexer(SourceTextReader reader) {
        if (reader == null) throw new IllegalArgumentException("reader cannot be null.");

        this.reader = reader;
        errors = new ArrayList<>();
    }

    /**
     * Gets the next token.
     *
     * @return Next available token; otherwise, when the end of the text is reached, {@link SyntaxKind#END_OF_TEXT} token.
     */
    public SyntaxToken nextToken() {
        errors.clear();

        skipTrivia();

        TokenInfo info = new TokenInfo();
        scanToken(info);

        return create(info);
    }

    private void skipTrivia() {
        while (true) {
            switch (reader.peekChar()) {
                case ' ':
                case '\r':
                case '\t':
                case '\f':
                case '\u001A':
                    reader.advanceChar();
                    break;

                case '\'':
                    skipComment();
                    break;

                default:
                    return;
            }
        }
    }

    private void skipComment() {
        int startIndex = reader.getCurrentUnreadCharPos();
        int offset = 1;

        while (true) {
            char curChar = reader.charAt(startIndex + offset);

            if (curChar == '\n' || curChar == SourceTextReader.NULL_CHARACTER)
                break;

            offset++;
        }

        reader.advanceChar(offset);
    }

    private void scanToken(TokenInfo info) {
        assert (info != null);

        char ch = reader.peekChar();

        info.textValue = Character.toString(ch);
        info.kind = SyntaxKind.UNKNOWN;
        info.tokenStartPosition = reader.getCurrentUnreadCharPos();

        switch (ch) {
            case '\"':
                scanStringLiteral(info);
                break;

            case '/':
                reader.advanceChar();
                info.kind = SyntaxKind.SLASH_TOKEN;
                break;

            case '-':
                reader.advanceChar();
                info.kind = SyntaxKind.MINUS_TOKEN;
                break;

            case '+':
                reader.advanceChar();
                info.kind = SyntaxKind.PLUS_TOKEN;
                break;

            case '*':
                reader.advanceChar();
                info.kind = SyntaxKind.ASTERISK_TOKEN;
                break;

            case '=':
                reader.advanceChar();
                info.kind = SyntaxKind.EQUALS_TOKEN;
                break;

            case '(':
                reader.advanceChar();
                info.kind = SyntaxKind.OPEN_PAREN_TOKEN;
                break;

            case ')':
                reader.advanceChar();
                info.kind = SyntaxKind.CLOSE_PAREN_TOKEN;
                break;

            case ',':
                reader.advanceChar();
                info.kind = SyntaxKind.COMMA_TOKEN;
                break;

            case '\n': //should we use \r as well? now it is skipped in skipTrivia()
                reader.advanceChar();
                info.kind = SyntaxKind.NEW_LINE_TOKEN;
                break;

            case SourceTextReader.NULL_CHARACTER:
                info.kind = SyntaxKind.END_OF_TEXT;
                break;

            default:
                if (SyntaxFacts.isIdentifierStartCharacter(ch)) {
                    scanIdentifierOrKeyword(info);
                } else if (Character.isDigit(ch)) {
                    scanNumericLiteral(info);
                } else {
                    addError(ErrorCode.ERR_UNEXPECTED_CHAR, info.tokenStartPosition, 1);
                    reader.advanceChar();
                }
                break;
        }
    }

    private void scanStringLiteral(TokenInfo info) {
        assert (info != null);

        info.kind = SyntaxKind.LITERAL_TOKEN;
        info.type = SpecialType.STRING;

        int start = reader.getCurrentUnreadCharPos();
        int offset = 1;

        while (true) {
            char ch = reader.charAt(start + offset);

            if (ch == SourceTextReader.NULL_CHARACTER || ch == '\n') {
                addError(ErrorCode.ERR_LOST_QUOTE, start + offset, 1);
                break;
            }

            offset++;

            if (ch == '\"') {
                break;
            }
        }

        //case "END_OF_TEXT_TOKEN
        info.textValue = offset > 1 ? reader.substring(start + 1, start + offset - 1) : "";

        reader.advanceChar(offset);
    }

    private void scanNumericLiteral(TokenInfo info) {
        assert (info != null);

        int start = reader.getCurrentUnreadCharPos();
        int offset = 1;
        boolean isDecimal = false;

        info.kind = SyntaxKind.LITERAL_TOKEN;

        while (true) {
            int curIndex = start + offset;
            char curChar = reader.charAt(curIndex);

            if (Character.isDigit(curChar)) offset++;
            else if (curChar == '.' && !isDecimal && Character.isDigit(reader.charAt(curIndex + 1))) {
                offset += 2;
                isDecimal = true;
            } else break;
        }

        info.textValue = reader.substring(start, start + offset);

        if (isDecimal) {
            info.type = SpecialType.DOUBLE;
            parseDouble(info);
        } else {
            info.type = SpecialType.INTEGER;
            parseInt(info);
        }

        reader.advanceChar(offset);
    }

    private void scanIdentifierOrKeyword(TokenInfo info) {
        assert (info != null);

        int startPos = reader.getCurrentUnreadCharPos();
        int offset = 1;

        while (SyntaxFacts.isIdentifierStartCharacter(reader.charAt(startPos + offset))
                || Character.isDigit(reader.charAt(startPos + offset))) {
            offset++;
        }

        info.textValue = reader.substring(startPos, startPos + offset);

        SyntaxKind keyword = SyntaxFacts.getKeywordKind(info.textValue);
        info.kind = (keyword != SyntaxKind.UNKNOWN) ? keyword : SyntaxKind.IDENTIFIER_TOKEN;

        reader.advanceChar(offset);
    }

    private SyntaxToken create(TokenInfo info) {
        assert (info != null);

        SyntaxDiagnosticInfo[] errorsArray = errors.toArray(new SyntaxDiagnosticInfo[0]);

        if (info.kind == SyntaxKind.LITERAL_TOKEN) {
            switch (info.type) {
                case INTEGER:
                    return new SyntaxLiteralToken<>(info.intValue, errorsArray);
                case DOUBLE:
                    return new SyntaxLiteralToken<>(info.doubleValue, errorsArray);
                case STRING:
                    return new SyntaxLiteralToken<>(info.textValue, errorsArray);
                default:
                    throw new RuntimeException("info.type was not handled.");
            }
        } else if (info.kind == SyntaxKind.IDENTIFIER_TOKEN) {
            return new SyntaxIdentifier(info.textValue, errorsArray);
        } else {
            return new SyntaxToken(info.textValue, info.kind, errorsArray);
        }
    }

    private void addError(ErrorCode code, int offset, int length) {
        SyntaxDiagnosticInfo info = new SyntaxDiagnosticInfo(code, offset, length);
        errors.add(info);
    }

    private void parseInt(TokenInfo info) {
        assert (info != null);
        assert (info.kind == SyntaxKind.LITERAL_TOKEN);
        assert (info.type == SpecialType.INTEGER);

        try {
            info.intValue = Integer.parseInt(info.textValue);
        } catch (NumberFormatException ex) {
            addError(ErrorCode.ERR_INTEGER_OVERFLOW, info.tokenStartPosition, info.textValue.length());
        }
    }

    private void parseDouble(TokenInfo info) {
        assert (info != null);
        assert (info.kind == SyntaxKind.LITERAL_TOKEN);
        assert (info.type == SpecialType.DOUBLE);

        //TODO write real-parser with overflow check.
        info.doubleValue = Double.parseDouble(info.textValue);
    }

    private class TokenInfo {
        String textValue;
        SyntaxKind kind;
        SpecialType type;

        int tokenStartPosition;

        int intValue;
        double doubleValue;
    }
}
