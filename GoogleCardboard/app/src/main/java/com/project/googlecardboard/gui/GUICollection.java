package com.project.googlecardboard.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Garrett on 03/12/2015.
 */
public class GUICollection implements Iterable<GUI>{

    private HashMap<Float, List<GUI>> collection;

    public GUICollection(){
        this.collection = new HashMap<Float, List<GUI>>();
    }

    public void add(GUI gui){
        float radius = gui.getRadius();
        List<GUI> guis = collection.get(radius);
        if(guis == null){
            guis = new ArrayList<GUI>();
        }
        guis.add(gui);
        collection.put(radius, guis);
    }

    public void remove(GUI gui){
        List<GUI> guis = collection.get(gui.getRadius());
        if(guis != null){
            guis.remove(gui);
        }
    }

    public void update(GUI gui, float radius){
        remove(gui);
        gui.setRadius(radius);
        add(gui);
    }

    public Iterator<GUI> iterator(){
        List<GUI> guiList = new ArrayList<GUI>();
        for(List<GUI> guis : collection.values()){
            guiList.addAll(guis);
        }
        return guiList.iterator();
    }
}
