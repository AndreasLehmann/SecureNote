/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafxapplication1.business.boundary.NoteService;
import javafxapplication1.business.boundary.NoteServiceImpl;
import javafxapplication1.business.entity.NoteEntity;
import javafxapplication1.presentation.NoteTitleCellFactory;

/**
 *
 * @author Andreas
 */
public class FXMLDocumentController implements Initializable {

    //private final NoteService noteService = new NoteServiceMock();
    private final NoteService noteService = new NoteServiceImpl("f:/tmp/MySecretNoteStorage_A/");
    public SimpleObjectProperty<NoteEntity> selectedEntity;

    @FXML
    private Label label;
    @FXML
    private HTMLEditor editor;
    @FXML
        private ListView<NoteEntity> noteList;

    @FXML
    private ListView<?> tagList;
    @FXML
    private VBox editor_vbox;
    @FXML
    private TextField editor_title;
    @FXML
    private BorderPane borderPane;

    /**
     * Behandelt den Test-Knopf 'Click Me!' in der View.
     *
     * @param event
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("Saving Changes!");
        
        noteService.persistChanges();
        
    }

    /**
     * Wird aufgerufen, wenn ein Element in der Liste aller Notizen angeklickt
     * wird.
     *
     * @param event
     */
    @FXML
    private void noteListClicked(MouseEvent event) {
        NoteEntity n = noteList.getSelectionModel().getSelectedItem();
        if (n != null) {
            //System.out.println("'click:" + n.getTitle());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        assert borderPane != null : "fx:id=\"borderPane\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert editor != null : "fx:id=\"editor\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert editor_title != null : "fx:id=\"editor_title\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert editor_vbox != null : "fx:id=\"editor_vbox\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert noteList != null : "fx:id=\"noteList\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert tagList != null : "fx:id=\"tagList\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        
        // setze CellFactory um den Titel der Notizen anzuzeigen
        noteList.setCellFactory(new NoteTitleCellFactory());
        // Setze Note-Service als Datenquelle
        noteList.itemsProperty().bind(noteService.list());

        // setze "OnChange"-Listener um Änderung der Selektion zu erfahren
        //noteList.getSelectionModel().selectedItemProperty().addListener(new NoteListChangeListener());
        // Erzeuge neues Property um Änderungen an der NoteList mitzubekommen
        selectedEntity = new ReadOnlyObjectWrapper<>();
        selectedEntity.bind(noteList.getSelectionModel().selectedItemProperty());
        selectedEntity.addListener(new NoteListChangeListener(this));

        // Focus Listener hinzufügen
        noteList.focusedProperty().addListener(new NoteListFocusChangedListener());
        editor_title.focusedProperty().addListener(new EditorTitleFocusChangedListener());

    }

    /**
     * Wird immer aufgerufen, wenn sich in der Liste aller Notizen das
     * selektierte Item ändert.
     */
    private static class NoteListChangeListener implements ChangeListener<NoteEntity> {

        FXMLDocumentController ctrl;

        public NoteListChangeListener(FXMLDocumentController ctrl) {
            this.ctrl = ctrl;
        }

        @Override
        public void changed(ObservableValue<? extends NoteEntity> ov, NoteEntity oldNote, NoteEntity newNote) {
            System.out.println("NewSelected:" + newNote);

            // Trenne die Bindung und sichere den HTML Text
            if(oldNote!=null){
                ctrl.editor_title.textProperty().unbindBidirectional(oldNote.titleProperty());
                final String htmlText = ctrl.editor.getHtmlText();
                oldNote.setBody(htmlText);
            }
            // Binde das Title-Feld an das Modell
            ctrl.editor_title.textProperty().bindBidirectional(newNote.titleProperty());
            // Übertrage den Bodytext
            ctrl.editor.setHtmlText(newNote.getBody());


        }
    }

    /**
     * Wird aufgerufen, wenn die Liste aller Notizen den Focus erhält oder
     * verliert.
     */
    private static class NoteListFocusChangedListener implements ChangeListener<Boolean> {

        public NoteListFocusChangedListener() {
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
            if (newPropertyValue) {
                //System.out.println("noteList on focus");
            } else {
                //System.out.println("noteList out focus");
            }
        }
    }

    /**
     * Wird aufgerufen, wenn der Editor für den Titel der Notiz den Focus erhält
     * oder verliert.
     */
    private static class EditorTitleFocusChangedListener implements ChangeListener<Boolean> {

        public EditorTitleFocusChangedListener() {
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
            if (newPropertyValue) {
                //System.out.println("Textfield on focus");
            } else {
                //System.out.println("Textfield out focus");
            }
        }
    }

}
