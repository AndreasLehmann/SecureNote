/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1.business.boundary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafxapplication1.business.entity.NoteEntity;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andreas
 */
public class FilebasedNoteServiceTest {

    FilebasedNoteService service = null;
    final String baseTestNoteRepository = "f:/tmp/MySecretNoteStorage_DEBUG/";
    private final String SPARE_TESTFILE_UUID = "10d2ec23-048c-4974-b7ed-f10d76ebc5a1";
    private final String MODIFY_TESTFILE_UUID = "025192d9-bcfb-49d2-81da-6c2e0d6f4d61";

    private static final boolean SPARE2JSON = true;
    private static final boolean JSON2SPARE = false;

    public FilebasedNoteServiceTest() {
        service = new FilebasedNoteService(baseTestNoteRepository);

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
        final String TEST_UUID = "025192d9-bcfb-49d2-81da-6c2e0d6f4d61";
        final String TEST_FILENAME = baseTestNoteRepository + TEST_UUID + ".json";
        final String expectedTitle = "t1";
        final String expectedBodyPart = "B1</body>";

        NoteEntity e = service.readNoteEntity(UUID.fromString(TEST_UUID));
        assertNotNull(e); // geladen?

        assertEquals(TEST_UUID, e.getUniqueKey().toString()); // UUID korrekt?
        assertEquals(expectedTitle, e.getTitle()); // Titel korrekt?
        assertTrue(e.getBody().indexOf(expectedBodyPart) >= 0); // Body Inhalt testen
    }

    /**
     * Dies ist ein High-Level test, der selbst wieder von readNoteEntity() und
     * writeNoteEntity() gebrauch macht!
     */
    @Test
    public void testMergeList() throws IOException {
        System.out.println("testMergeList");

        // Lese original Liste ein
        List<NoteEntity> baseList;
        List<NoteEntity> l1 = service.list().getValue();
        baseList = clone(l1); // clone die Liste und alle Elemente!

        // ##########################################
        /// Test 1 alles ist unverändert
        l1 = service.list().getValue(); // neu einlesen!
        assertEquals(l1.toString(), baseList.toString()); // sind die Listen nach dem erneuten Einlesen noch gleich?

        // ##########################################
        /// Test 2 neues NoteEntity hinzugekommen
        toggleSpareElement(SPARE2JSON);
        l1 = service.list().getValue(); // neu einlesen!
        toggleSpareElement(JSON2SPARE);
        assertEquals(baseList.size() + 1, l1.size());

        // ##########################################
        /// Test 3 das neue NoteEntity wurde wieder entfernt
        l1 = service.list().getValue(); // neu einlesen!
        assertEquals(baseList.size(), l1.size());

        /*        
         // ##########################################
         /// Test 4 ein NoteEntity wurde geändert
         try{
         modifyElement(); // Ändere eine Datei
         l1 = service.list().getValue(); // neu einlesen!
         assertNotSame("Geändertes Element wurde nicht eingelesen!", baseList.toString(), l1.toString());
        
         } catch (IOException ex) {
         Logger.getLogger(FilebasedNoteServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        
         } finally{
         restoreElement();
         }
         */
        fail("The test case is a prototype.");
    }

    /**
     * Test of persistChanges method, of class FilebasedNoteService.
     */
    @Test
    public void testPersistChanges() {
        System.out.println("persistChanges");
        //service.persistChanges();

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testLastChangedUpdate() throws IOException {
        System.out.println("LastChangedUpdate");
        try {
            modifyElement(); // Ändere eine Datei

            NoteEntity e = service.readNoteEntity(UUID.fromString(MODIFY_TESTFILE_UUID));
            long beforeChange = e.getLastSavedOn();
            e.setTitle("modified Title");
            service.writeNoteEntity(e);
            long afterChange = e.getLastSavedOn();

            assertTrue(afterChange > beforeChange);

        } catch (IOException ex) {
            Logger.getLogger(FilebasedNoteServiceTest.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            restoreElement(); // alles Rückgängig machen
        }
    }
    /*########################################################################*/
    /* H I L F S  F U N K T I O N E N                                         */
    /*########################################################################*/

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

    /**
     * Klone die komplette Liste mit neuen Elementen.
     *
     * @param sourceList
     * @return
     */
    private List<NoteEntity> clone(List<NoteEntity> sourceList) {
        List<NoteEntity> destList = new ArrayList<>();
        for (NoteEntity e : sourceList) {
            destList.add(e.cloneElement());
        }
        return destList;
    }

    private void toggleSpareElement(boolean direction) {
        final String SPARE_FILE = baseTestNoteRepository + SPARE_TESTFILE_UUID;
        File spareFile = new File(SPARE_FILE + ".spare");
        File jsonFile = new File(SPARE_FILE + ".json");

        if (direction == SPARE2JSON) {
            assertTrue(spareFile.renameTo(jsonFile));
        } else {
            assertTrue(jsonFile.renameTo(spareFile));
        }
    }

    /**
     * Ändert ein bestehendes NoteEntity im Titel.
     */
    private void modifyElement() throws IOException {
        String originalFilename = baseTestNoteRepository + MODIFY_TESTFILE_UUID + ".json";
        String backupFilename = baseTestNoteRepository + MODIFY_TESTFILE_UUID + ".json.bak";
        copyFile(new File(originalFilename), new File(backupFilename));

        NoteEntity e = service.readNoteEntity(UUID.fromString(MODIFY_TESTFILE_UUID));

        e.setTitle("modified Title");
        service.writeNoteEntity(e);
    }

    private void restoreElement() throws IOException {
        String originalFilename = baseTestNoteRepository + MODIFY_TESTFILE_UUID + ".json";
        String backupFilename = baseTestNoteRepository + MODIFY_TESTFILE_UUID + ".json.bak";

        File originalF = new File(originalFilename);
        File backupF = new File(backupFilename);

        originalF.delete();
        backupF.renameTo(new File(originalFilename));

    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

}
