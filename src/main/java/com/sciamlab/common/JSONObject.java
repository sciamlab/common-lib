package com.sciamlab.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.json.JSONException;

/**
 * this is a serializable version of the org.json.JSONObject class :(
 * @author paolo
 *
 */
public class JSONObject implements Serializable {

	private static final long serialVersionUID = -5796452511123441758L;
	
	private transient org.json.JSONObject json;

    public JSONObject(org.json.JSONObject json) {
        this.json = json;
    }
    
    public org.json.JSONObject json(){
    	return json;
    }


    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(json.toString());
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException, JSONException {
        ois.defaultReadObject();
        json = new org.json.JSONObject((String) ois.readObject());
    }
}
