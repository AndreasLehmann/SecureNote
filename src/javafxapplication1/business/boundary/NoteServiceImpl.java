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
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public class NoteServiceImpl implements NoteService {

    public static String basePath = "f:/tmp/MySecretNoteStorage_A/";

    ListProperty<NoteEntity> noteList = new SimpleListProperty<>(javafx.collections.FXCollections.observableList(new ArrayList<NoteEntity>()));

    private final Gson gson;
            
    public NoteServiceImpl() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SimpleStringProperty.class, new SimpleStringPropertyTypeAdapter());
        gson = gsonBuilder.create();

        List<String> filenameList = readDir();
        for (Iterator<String> it = filenameList.iterator(); it.hasNext();) {
            noteList.add(readNoteEntity(basePath + "/" + it.next()));
        }     
    }

    @Override
    public ListProperty<NoteEntity> list() {
        return noteList;
    }

    private static List<String> readDir() {
        File dir = new File(basePath);
        File[] files = dir.listFiles();
        ArrayList<String> filenames = new ArrayList<>();
        for (File f : files) {
            if(f.isFile()){
                filenames.add(f.getName());
            }
        }
        return filenames;
    }

    private NoteEntity readNoteEntity(String filepath) {

        Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.FINER, "read: filepath=" + filepath);

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
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                // nix tun...
            }
        }

        
        NoteEntity n;
        Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.FINEST, "read json string=" + sb.toString());
        n = (NoteEntity) gson.fromJson(sb.toString(), NoteEntity.class);

        return n;
    }

    @Override
    public boolean writeNoteEntity(NoteEntity n) {
        return writeNoteEntity(n, basePath + "/" + n.getUniqueKey()+".json");
    }

    private boolean writeNoteEntity(NoteEntity n, String filepath) {
        Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.FINER, "write: filepath=" + filepath);

        String json = gson.toJson(n);
        File f = new File(filepath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(json.getBytes());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                // hier kann man nichts mehr machen...
            }
        }
        return true;
    }

    @Override
    public void persistChanges() {
        for (NoteEntity noteEntity : noteList) {
            boolean writeSuccessful = this.writeNoteEntity(noteEntity);
            if(!writeSuccessful){
                Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, "Can't write note entity to disk: "  +  noteEntity);
            }
        }
    }

    private static class SimpleStringPropertyTypeAdapter implements JsonSerializer<SimpleStringProperty>,JsonDeserializer<SimpleStringProperty>  {
        @Override
        public JsonElement serialize(SimpleStringProperty t, Type type, JsonSerializationContext jsc) {
            return new JsonPrimitive( t.getValue() );
        }
        @Override
        public SimpleStringProperty deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return new SimpleStringProperty(je.getAsJsonPrimitive().getAsString());
        }
    }

}
