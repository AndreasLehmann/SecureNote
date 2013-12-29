/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1.business.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Andreas 
 * UUID uniqueKey = UUID.randomUUID();
 */
public class NoteEntity implements Serializable{

    private UUID uniqueKey;
    private SimpleStringProperty title;
    private SimpleStringProperty body;
    private long createdOn = 0L;
    private long lastSavedOn = 0L;

    public NoteEntity() {
        this.title = new SimpleStringProperty();
        this.body = new SimpleStringProperty();
        this.uniqueKey = UUID.randomUUID();
        this.createdOn = System.currentTimeMillis();
    }

    public NoteEntity(String title, String body) {
        super();
        this.title = new SimpleStringProperty(title);
        this.body = new SimpleStringProperty(body);
        this.uniqueKey = UUID.randomUUID();
        this.createdOn = System.currentTimeMillis();
    }

    public String getTitle() {
        return title.getValueSafe();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && title.length() > 0) {
            this.title.set(title);
        }
    }

    public String getBody() {
        return body.getValueSafe();
    }

    public void setBody(String body) {
        if (body != null && body.length() > 0) {
            this.body.set(body);
        }
    }

    public SimpleStringProperty bodyProperty() {
        return body;
    }

    public UUID getUniqueKey() {
        return uniqueKey;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public long getLastSavedOn() {
        return lastSavedOn;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.uniqueKey);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NoteEntity other = (NoteEntity) obj;
        return Objects.equals(this.uniqueKey, other.uniqueKey);
    }

    @Override
    public String toString() {
        return "NoteEntity{ id=" + uniqueKey + ", title=" + title + ", body=" + body + '}';
    }

    /**
     * Diese Methode erzeugt eine komplette Deep-Copy
     * des Elements inklusive der ID, DateCreated usw.
     * 
     * Der clone hat den gleichen HasCode wie das Original.
     * 
     * @return 
     */
    public NoteEntity cloneElement() {
        NoteEntity clone = new NoteEntity(this.getTitle(),this.getBody());
        clone.uniqueKey=this.uniqueKey;
        clone.createdOn=this.createdOn;
        clone.lastSavedOn=this.lastSavedOn;
        return clone;
    }    
}