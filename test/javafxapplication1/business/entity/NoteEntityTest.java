/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication1.business.entity;

import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andreas
 */
public class NoteEntityTest {
    
   
    /**
     * Test of getTitle method, of class NoteEntity.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        final String TEST_TITLE="1234";
        NoteEntity instance = new NoteEntity(TEST_TITLE,"");
        String result = instance.getTitle();
        assertEquals(TEST_TITLE, result);
        assertEquals(TEST_TITLE, instance.titleProperty().getValue());
    }

    /**
     * Test of titleProperty method, of class NoteEntity.
     */
    @Test
    public void testTitleProperty() {
        System.out.println("titleProperty");
        final String TEST_TITLE="1234";
        NoteEntity instance = new NoteEntity(TEST_TITLE,"");
        SimpleStringProperty result = instance.titleProperty();
        assertEquals(TEST_TITLE, result.getValue());
    }

    /**
     * Test of setTitle method, of class NoteEntity.
     */
    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        final String TEST_TITLE = "12345";
        NoteEntity instance = new NoteEntity();
        instance.setTitle(TEST_TITLE);
        
        assertEquals(TEST_TITLE, instance.getTitle());
        assertEquals(TEST_TITLE, instance.titleProperty().getValue());
    }

    /**
     * Test of getBody method, of class NoteEntity.
     */
    @Test
    public void testGetBody() {
        System.out.println("getBody");
        final String TEST_BODY="5678 äöü ' ; { \\ /\"";
        NoteEntity instance = new NoteEntity("",TEST_BODY);
        String result = instance.getBody();
        assertEquals(TEST_BODY, result);
        assertEquals(TEST_BODY, instance.bodyProperty().getValue());
    }

    /**
     * Test of setBody method, of class NoteEntity.
     */
    @Test
    public void testSetBody() {
        System.out.println("setBody");
        final String TEST_BODY="5678 äöü ' ; { \\ /\"";
        NoteEntity instance = new NoteEntity("A","B");
        instance.setBody(TEST_BODY);
        assertEquals(TEST_BODY, instance.getBody());
        assertEquals(TEST_BODY, instance.bodyProperty().getValue());
    }

    /**
     * Test of bodyProperty method, of class NoteEntity.
     */
    @Test
    public void testBodyProperty() {
        System.out.println("bodyProperty");
        final String TEST_BODY="1234";
        NoteEntity instance = new NoteEntity("",TEST_BODY);
        SimpleStringProperty result = instance.bodyProperty();
        assertEquals(TEST_BODY, result.getValue());
    }

    /**
     * Test of getUniqueKey method, of class NoteEntity.
     */
    @Test
    public void testGetUniqueKey() {
        System.out.println("getUniqueKey");
     
        NoteEntity instance1 = new NoteEntity();
        NoteEntity instance2 = new NoteEntity();
        assertNotNull(instance1.getUniqueKey()); // wurde ein schlüssel erzeugt?
        assertNotNull(instance2.getUniqueKey()); // wurde ein schlüssel erzeugt?
        assertNotSame(instance1.getUniqueKey().toString(),instance2.getUniqueKey().toString()); // hat ein neues Objekt einen anderen schlüssel?
        
        instance1 = new NoteEntity("A","B");
        instance2 = new NoteEntity("A","B");
        assertNotNull(instance1.getUniqueKey()); // wurde ein schlüssel erzeugt?
        assertNotNull(instance2.getUniqueKey()); // wurde ein schlüssel erzeugt?
        assertNotSame(instance1.getUniqueKey().toString(),instance2.getUniqueKey().toString()); // hat ein neues Objekt einen anderen schlüssel?
    }
    /**
     * Test of getCreatedOn() method, of class NoteEntity.
     */
    @Test
    public void testGetCreatedOn() {
        System.out.println("getCreatedOn");
        
        long now =System.currentTimeMillis();
        NoteEntity instance = new NoteEntity();
                
        assertTrue(instance.getCreatedOn()>0); // größer 0

        assertTrue((instance.getCreatedOn()-now) < 5 ); // Erzeugung innerhalb 5ms
        
    }
    
    /**
     * Test of hashCode method, of class NoteEntity.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        NoteEntity instance1 = new NoteEntity();
        NoteEntity instance2 = new NoteEntity();
        assertNotSame(instance2.hashCode(), instance1.hashCode());

        instance1 = new NoteEntity("A","B");
        instance2 = new NoteEntity("A","B");
        assertNotSame(instance2.hashCode(), instance1.hashCode());
        
        assertEquals(instance1.hashCode(), instance1.hashCode());// wiederholbarkeit

        assertEquals(instance1.hashCode(), instance1.cloneElement().hashCode());// gleiches Element, gleicher HashCode
    }

    /**
     * Test of equals method, of class NoteEntity.
     */

    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        NoteEntity instance1 = new NoteEntity();
        NoteEntity instance2 = new NoteEntity();
        NoteEntity instance3 = instance1.cloneElement();
        
        assertFalse(instance1.equals(obj)); // false bei Null-Vergleich
        assertFalse(instance1.equals(instance2));
        assertFalse(instance1.equals(instance2)); // wiederholbar
        
        assertFalse(instance2.equals(instance1));
        assertTrue(instance3.equals(instance1));
        assertTrue(instance1.equals(instance3));
        
    }


    /**
     * Test of cloneElement method, of class NoteEntity.
     */
    @Test
    public void testCloneElement() {
        System.out.println("cloneElement");
        final String TEST_TITLE = "12345";
        final String TEST_BODY = "345566";
        
        NoteEntity source = new NoteEntity(TEST_TITLE,TEST_BODY);
        NoteEntity dest = source.cloneElement();

        assertTrue(source!=dest); // keine Objektgleichheit !
        assertTrue(source.hashCode()==dest.hashCode()); // gleicher HashCode
        assertEquals(source.getUniqueKey(),dest.getUniqueKey()); // gleiche UUID !
        
        assertEquals(source.getBody(), dest.getBody()); // gleicher Body
        assertEquals(source.getTitle(), dest.getTitle()); // gleicher title
    }
    
}
