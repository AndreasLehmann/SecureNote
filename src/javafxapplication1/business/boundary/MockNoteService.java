/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication1.business.boundary;

import java.util.ArrayList;
import java.util.UUID;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafxapplication1.business.entity.NoteEntity;

/**
 *
 * @author Andreas
 */
public class MockNoteService implements NoteService {

    ListProperty<NoteEntity> noteList = new SimpleListProperty<>(javafx.collections.FXCollections.observableList(new ArrayList<NoteEntity>()));


    public MockNoteService() {
        
        noteList.add( new NoteEntity("Title1","Body 1"));
        noteList.add( new NoteEntity("Title2","<h1>HTML</h1>Message."));
        
    }

    @Override
    public ListProperty<NoteEntity> list() {
        return noteList;
    }
    @Override
    public boolean writeNoteEntity(NoteEntity n){
        throw new UnsupportedOperationException();
    }

    @Override
    public void persistChanges() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NoteEntity readNoteEntity(UUID id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
