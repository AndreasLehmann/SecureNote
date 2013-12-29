/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication1.business.boundary;

import java.util.Iterator;
import javafxapplication1.business.entity.NoteEntity;
import org.junit.Test;

/**
 *
 * @author Andreas
 */
public class NoteServiceTest {
    
    public NoteServiceTest() {
    }
    
    @Test
    public void testListImpl() {
    
        NoteService s = new NoteServiceImpl();
     
        for (Iterator<NoteEntity> iter = s.list().listIterator(); iter.hasNext();) {
            NoteEntity note = iter.next();
            System.out.println(note);
        }
    }

    @Test
    public void testListMock() {
    
        NoteService s = new NoteServiceMock();
        
        for (Iterator<NoteEntity> iter = s.list().listIterator(); iter.hasNext();) {
            NoteEntity note = iter.next();
            System.out.println(note);
        }
    }
    
}
