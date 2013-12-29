/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication1.presentation;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafxapplication1.business.entity.NoteEntity;

/**
 *
 * @author Andreas
 */
  public class NoteTitleCellFactory implements Callback<ListView<NoteEntity>, ListCell<NoteEntity>> {

        public NoteTitleCellFactory() {
        }

        @Override
        public ListCell<NoteEntity> call(ListView<NoteEntity> p) {
            return new TextFieldListCell(new StringConverter<NoteEntity>() {

                @Override
                public String toString(NoteEntity n) {
                    if(n==null || n.getTitle()==null)
                        return "";
                    return n.getTitle();
                }

                @Override
                public NoteEntity fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        }
    }
