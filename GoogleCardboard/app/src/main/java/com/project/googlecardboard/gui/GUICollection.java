package com.project.googlecardboard.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Garrett on 03/12/2015.
 */
public class GUICollection implements Iterable<GUI>{

    private List<GUI> guis;

    public GUICollection(){
        this.guis = Collections.synchronizedList(new ArrayList<GUI>());
    }

    /**
     * Add a gui to the collection
     * The gui is inserted into a sorted list
     * The efficiency of this insertion can be
     * dealt with later, if need be
     * @param gui
     */
    public void add(GUI gui){
        int index = 0;
        for(GUI g : this){
            if(g.getRadius() > gui.getRadius()){
                break;
            }
            index++;
        }
        guis.add(index, gui);
    }

    /**
     * Remove a gui from the collection
     * @param gui
     */
    public void remove(GUI gui){
        guis.remove(gui);
    }

    public Iterator<GUI> iterator(){
        return guis.iterator();
    }

    public int size(){
        return guis.size();
    }

    public void sort(){
        Collections.sort(guis);
    }
}
