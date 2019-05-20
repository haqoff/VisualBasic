package com.vb.gui.controller;

import com.vb.compiler.error.CriticalParserException;
import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.parser.LanguageParser;
import com.vb.compiler.parser.Lexer;
import com.vb.compiler.syntax.SyntaxFacts;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.NonterminalNode;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.text.SourceTextReader;
import com.vb.gui.util.ExpressionTreeViewBuilder;
import com.vb.gui.util.StringUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class MainFormController implements Initializable {
    public TreeView<String> tvExpressionTree;
    public TextArea sourceCode;
    public ListView<String> lvConsole;

    public TableView<SyntaxToken> lexTable;
    public TableColumn<SyntaxToken, String> inputLexColumn;
    public TableColumn<SyntaxToken, String> resultLexColumn;

    public TableView keywordTable;
    public TableColumn<String, String> keywordColumn;

    public TableView separatorTable;
    public TableColumn<String, String> separatorColumn;

    public TableView<String> literalTable;
    public TableColumn<String, String> literalColumn;

    public TableView<String> identifierTable;
    public TableColumn<String, String> identifierColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sourceCode.textProperty().addListener(handler -> onSourceTextChanged());
        inputLexColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().toString()));
        resultLexColumn.setCellValueFactory(param -> defineTokenTable(param.getValue()));
        literalColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        identifierColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        setKeywords();
        setSeparatorTable();
    }

    private ObservableValue<String> defineTokenTable(SyntaxToken token) {
        SyntaxKind kind = token.getKind();
        String textValue = token.toString();

        int nTable = 0;
        int nRow = 0;

        if (kind == SyntaxKind.IDENTIFIER_TOKEN) {
            nTable = 4;
            nRow = checkAndAdd(textValue, identifierTable);
        } else if (kind == SyntaxKind.LITERAL_TOKEN) {
            nTable = 3;
            nRow = checkAndAdd(textValue, literalTable);
        } else if (SyntaxFacts.isKeyword(kind)) {
            nTable = 1;
            nRow = checkAndAdd(textValue, keywordTable);
        }
        //all that remains is separators and an unknown token. but we will not add unknown token.
        else if (kind != SyntaxKind.UNKNOWN) {
            nTable = 2;
            nRow = checkAndAdd(textValue, separatorTable);
        }

        return new ReadOnlyStringWrapper(String.format("(%s, %s)", nTable, nRow));
    }

    private int checkAndAdd(String value, TableView<String> table) {
        int pos = table.getItems().indexOf(value) + 1;
        if (pos == 0) {
            table.getItems().add(value);
            pos = table.getItems().size();
        }

        return pos;
    }

    private void onSourceTextChanged() {
        lvConsole.getItems().clear();
        identifierTable.getItems().clear();
        literalTable.getItems().clear();

        showLexicalAnalyze();
        showSyntaxAnalyze();
    }

    private void showSyntaxAnalyze() {
        SourceTextReader reader = new SourceTextReader(sourceCode.getText());
        Lexer lexer = new Lexer(reader);
        LanguageParser parser = new LanguageParser(lexer);

        try {
            NonterminalNode tree = parser.getTree();
            SyntaxDiagnosticInfo[] errors = tree.getErrors();

            for (SyntaxDiagnosticInfo info : errors) {
                lvConsole.getItems().add(info.getCode().name());
            }

            TreeItem<String> mainExpressionRoot = ExpressionTreeViewBuilder.getExpressionRoots(tree);
            mainExpressionRoot.setExpanded(true);
            tvExpressionTree.setRoot(mainExpressionRoot);
            tvExpressionTree.setShowRoot(false);

        } catch (CriticalParserException e) {
            Stream<SyntaxKind> expectedKindsStream = e.getExpectedKinds().stream().filter(kind -> !SyntaxFacts.isNonterminal(kind));
            String expectedKindsString = StringUtil.streamToString(expectedKindsStream, ", ");

            String errorString = String.format("Encountered %s, but expected: [%s].", e.getErrorNode(), expectedKindsString);
            lvConsole.getItems().add(errorString);
        }
    }

    private void showLexicalAnalyze() {

        SourceTextReader reader = new SourceTextReader(sourceCode.getText());
        Lexer lexer = new Lexer(reader);

        ArrayList<SyntaxToken> tokens = new ArrayList<>();
        SyntaxToken token;
        do {
            token = lexer.nextToken();
            if (token.hasErrors()) {
                for (SyntaxDiagnosticInfo info : token.getErrors()) {
                    lvConsole.getItems().add(
                            String.format("ERROR: %s [%s - %s]", info.getCode().name(), info.getOffset(), info.getOffset() + info.getLength() - 1));
                }
            }

            tokens.add(token);
        } while (token.getKind() != SyntaxKind.END_OF_TEXT);

        lexTable.setItems(FXCollections.observableArrayList(tokens));
    }

    private void setKeywords() {
        keywordColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        String[] keyArray = Arrays
                .stream(SyntaxKind.values())
                .filter(SyntaxFacts::isKeyword)
                .map(Enum::name)
                .toArray(String[]::new);

        ObservableList<String> kinds = FXCollections.observableArrayList(keyArray);
        keywordTable.setItems(kinds);
    }

    private void setSeparatorTable() {
        separatorColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        String[] sepArray = Arrays
                .stream(SyntaxKind.values())
                .filter(kind -> !SyntaxFacts.isKeyword(kind) && !SyntaxFacts.isNonterminal(kind) && kind != SyntaxKind.UNKNOWN)
                .map(Enum::name)
                .toArray(String[]::new);

        ObservableList<String> kinds = FXCollections.observableArrayList(sepArray);
        separatorTable.setItems(kinds);
    }
}
