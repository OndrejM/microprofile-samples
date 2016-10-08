package io.microprofile.sample.jcache.model;

import java.io.Serializable;

public class CD implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;

    public CD() {
    }

    public CD(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
