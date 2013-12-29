/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1.business.boundary;

import javafx.beans.property.ListProperty;
import javafxapplication1.business.entity.NoteEntity;

/**
 *
 * @author Andreas
 */
public interface NoteService {

    ListProperty<NoteEntity> list();

    boolean writeNoteEntity(NoteEntity n);

    public void persistChanges();

}
