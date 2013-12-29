/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1.business.boundary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafxapplication1.business.entity.NoteEntity;

/**
 *
 * @author Andreas
 */
public class FilebasedNoteService implements NoteService {

    protected static final String FILE_SUFFIX = ".json";
    public final String basePath;
    private final JSONNameFilter jsonFilenameFilter = new JSONNameFilter();

    ListProperty<NoteEntity> noteList = new SimpleListProperty<>(javafx.collections.FXCollections.observableList(new ArrayList<NoteEntity>()));

    private final Gson gson;

    public FilebasedNoteService(String basepath) {
        this.basePath = basepath;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SimpleStringProperty.class, new SimpleStringPropertyTypeAdapter());
        gson = gsonBuilder.create();

    }

    @Override
    public ListProperty<NoteEntity> list() {
        refreshList(noteList);
        return noteList;
    }

    private List<String> readDir() {
        File dir = new File(basePath);
        File[] files = dir.listFiles(jsonFilenameFilter);
        ArrayList<String> filenames = new ArrayList<>();
        for (File f : files) {
            if (f.isFile()) {
                filenames.add(f.getName());
            }
        }
        return filenames;
    }

    @Override
    public NoteEntity readNoteEntity(UUID id) {
        return readNoteEntity(buildFilename(id));
    }

    private NoteEntity readNoteEntity(String filepath) {

        Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.FINER, "read: filepath=" + filepath);

        BufferedReader br = null;
        StringBuilder sb;
        sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new FileReader(filepath));

            line = br.readLine();
            while (line != null) {
                sb.append(line);
                //sb.append("\n"); // Zeilenumbrüche sind nicht nötig.
                line = br.readLine();
            }

        } catch (IOException ex) {
            Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, null, ex);
                // nix tun...
            }
        }

        NoteEntity n;
        Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.FINEST, "read json string=" + sb.toString());
        n = (NoteEntity) gson.fromJson(sb.toString(), NoteEntity.class);

        return n;
    }

    @Override
    public boolean writeNoteEntity(NoteEntity n) {
        return writeNoteEntity(n, buildFilename(n.getUniqueKey()));
    }

    protected String buildFilename(UUID id) {
        return basePath + "/" + id + FILE_SUFFIX;
    }

    private boolean writeNoteEntity(NoteEntity n, String filepath) {
        Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.FINER, "write: filepath=" + filepath);

        String json = gson.toJson(n);
        File f = new File(filepath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(json.getBytes());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, null, ex);
                // hier kann man nichts mehr machen...
            }
        }
        n.setLastSavedOn(System.currentTimeMillis());
        return true;
    }

    @Override
    public void persistChanges() {
        for (NoteEntity noteEntity : noteList) {
            boolean writeSuccessful = this.writeNoteEntity(noteEntity);
            if (!writeSuccessful) {
                Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, "Can't write note entity to disk: " + noteEntity);
            }
        }
    }

    private void refreshList(ListProperty<NoteEntity> list) {
        List<String> filenameList = readDir();
        for (Iterator<String> it = filenameList.iterator(); it.hasNext();) {
            NoteEntity e = readNoteEntity(basePath + "/" + it.next());

            if (list.contains(e)) {
                // Skip !
                // TODO: Prüfen, ob Datei neu eingelesen werden muss
                Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, "list merge: skipped: " + e);
            } else {
                // add new 
                list.add(e);
                Logger.getLogger(FilebasedNoteService.class.getName()).log(Level.SEVERE, "list merge: added  : " + e);
            }

        }
    }

    private static class SimpleStringPropertyTypeAdapter implements JsonSerializer<SimpleStringProperty>, JsonDeserializer<SimpleStringProperty> {

        @Override
        public JsonElement serialize(SimpleStringProperty t, Type type, JsonSerializationContext jsc) {
            return new JsonPrimitive(t.getValue());
        }

        @Override
        public SimpleStringProperty deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return new SimpleStringProperty(je.getAsJsonPrimitive().getAsString());
        }
    }

    private static class JSONNameFilter implements FilenameFilter {

        @Override
        public boolean accept(File file, String string) {
            return string.endsWith(FILE_SUFFIX);
        }
    }

}
