package com.vb.gui.controller;

import com.vb.compiler.error.SyntaxDiagnosticInfo;
import com.vb.compiler.parser.Lexer;
import com.vb.compiler.syntax.SyntaxFacts;
import com.vb.compiler.syntax.SyntaxKind;
import com.vb.compiler.syntax.tree.tokens.SyntaxToken;
import com.vb.compiler.text.SourceTextReader;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    @FXML
    private TextArea sourceCode;

    @FXML
    private ListView<String> lvConsole;

    @FXML
    private TableView<SyntaxToken> lexTable;
    @FXML
    private TableColumn<SyntaxToken, String> inputLexColumn;
    @FXML
    private TableColumn<SyntaxToken, String> resultLexColumn;

    @FXML
    private TableView keywordTable;
    @FXML
    private TableColumn<String, String> keywordColumn;

    @FXML
    private TableView<String> separatorTable;
    @FXML
    private TableColumn<String, String> separatorColumn;

    @FXML
    private TableView<String> literalTable;
    @FXML
    private TableColumn<String, String> literalColumn;

    @FXML
    private TableView<String> identifierTable;
    @FXML
    private TableColumn<String, String> identifierColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sourceCode.textProperty().addListener(handler -> onSourceTextChanged());
        inputLexColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(formatReadable(param.getValue())));
        resultLexColumn.setCellValueFactory(param -> defineResult(param));
        literalColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        separatorColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        identifierColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        setKeywords();
        setSeparatorTable();
    }

    private String formatReadable(SyntaxToken token) {
        String text = token.getTextValue();
        if (text.equals("\n") || text.equals(Character.toString(SourceTextReader.NULL_CHARACTER)))
            text = token.getKind().name();

        return text;
    }

    private ObservableValue<String> defineResult(TableColumn.CellDataFeatures<SyntaxToken, String> param) {
        SyntaxToken token = param.getValue();

        String res = null;
        switch (token.getKind()) {

            case UNKNOWN:
                res = token.getErrors()[0].getCode().name();
                break;
            case IDENTIFIER_TOKEN:
                res = String.format("(4, %s)", checkAndAdd(token.getTextValue().toLowerCase(), identifierTable));
                break;
            case LITERAL_TOKEN:
                res = String.format("(3, %s)", checkAndAdd(token.getTextValue(), literalTable));
                break;
            case DIM_KEYWORD:
            case AS_KEYWORD:
            case SELECT_KEYWORD:
            case CASE_KEYWORD:
            case TO_KEYWORD:
            case ELSE_KEYWORD:
            case END_KEYWORD:
            case INTEGER_KEYWORD:
            case STRING_KEYWORD:
            case DOUBLE_KEYWORD:
                res = String.format("(1, %s)", checkAndAdd(token.getTextValue(), keywordTable));
                break;
            default:
                String text = formatReadable(token);
                res = String.format("(2, %s)", checkAndAdd(text, separatorTable));
                break;
        }

        return new ReadOnlyStringWrapper(res);
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
        keywordTable.setItems(FXCollections.observableArrayList(SyntaxFacts.KEYWORDS.keySet()));
    }

    private void setSeparatorTable() {
        separatorColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        separatorTable.setItems(FXCollections.observableArrayList(
                "(", ")", "*", "+", ",", "-", "=", "NEW_LINE_TOKEN", "END_OF_TEXT"));
    }
}
