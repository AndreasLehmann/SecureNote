/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1.business.boundary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.beans.property.ListProperty;
import javafxapplication1.business.entity.NoteEntity;
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
public class FilebasedNoteServiceTest {

    FilebasedNoteService service = null;
    final String baseTestNoteRepository = "f:/tmp/MySecretNoteStorage_DEBUG/";

    public FilebasedNoteServiceTest() {
        service = new FilebasedNoteService(baseTestNoteRepository);

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class FilebasedNoteService.
     *
     * Dieser Test testet nur, ob die list() methode 2 Notes zurückliefert.
     */
    @Test
    public void testList() {
        System.out.println("list");
        int expResult = 2;
        ListProperty<NoteEntity> result = service.list();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of writeNoteEntity method, of class FilebasedNoteService.
     *
     * @throws java.io.IOException wenn der Leseversuch auf der Datei scheitert
     */
    @Test
    public void testWriteNoteEntity() throws IOException {
        System.out.println("writeNoteEntity");

        final String testTitle = "Test-Titel";
        final String testBody = "Test-Body { [ : \\ ÄÖÜ \" ";

        int numFilesBefore = service.list().size();

        NoteEntity n = new NoteEntity(testTitle, testBody);

        String newFilename = baseTestNoteRepository + n.getUniqueKey() + ".json"; // erzeuge Dateinamen zum Löschen!
        File f = new File(newFilename); // erzeuge Dateihandle;

        try {

            boolean success = service.writeNoteEntity(n);
            assertTrue(success); // schreiben erfolgreich ?

            int numFilesAfter = service.list().size(); // der Test folgt später...
            assertEquals(numFilesBefore + 1, numFilesAfter); // eine Datei mehr? (Test wird erst hier ausgeführt, damit in jedem Fall gelöscht wird.)

            success = f.canRead();
            assertTrue(success); // lesen möglich?

            // prüfe Inhalt
            String content = readFile(f);
            assertTrue(content.indexOf(testTitle) >= 0); // ist der Titel in der Datei enthalten?
            assertTrue(content.indexOf(n.getUniqueKey().toString()) >= 0); // ist die UUID in der Datei enthalten?

            success = f.delete();
            assertTrue(success); // löschen hat geklappt?
            f = null;
        } finally {
            if (f != null) {
                f.delete(); // Lösche Datei !
            }
        }

    }

    @Test
    public void testReadNoteEntity() {
        System.out.println("testReadNoteEntity");
        final String TEST_FILENAME = baseTestNoteRepository+"025192d9-bcfb-49d2-81da-6c2e0d6f4d61.json";
        
        
        fail("The test case is a prototype.");
    }

    @Test
    public void testMergeList() {
        System.out.println("testMergeList");

        fail("The test case is a prototype.");
    }

    /**
     * Test of persistChanges method, of class FilebasedNoteService.
     */
    @Test
    public void testPersistChanges() {
        System.out.println("persistChanges");
        FilebasedNoteService instance = null;
        instance.persistChanges();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Liest eine Datei von Filesystem
     *
     * @param f File
     * @return Inhalt der Datei als String
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String readFile(File f) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder b = new StringBuilder();
        String zeile;
        do {
            zeile = br.readLine();
            b.append(zeile);
        } while (zeile != null);
        br.close();
        return b.toString();
    }

}
