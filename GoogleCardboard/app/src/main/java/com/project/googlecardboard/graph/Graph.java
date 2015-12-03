package com.project.googlecardboard.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Garrett on 24/11/2015.
 */
public abstract class Graph implements Iterable<Float>{

    // Null Object design pattern
    public static final Graph EMPTY = new LineGraph(0);

    protected final int size;
    protected List<Float> data;

    public Graph(int size){
        this.size = size;
        this.data = new ArrayList<Float>();
    }

    /**
     * Add an element to the graph
     * @param t
     */
    public void add(float t){
        data.add(t);
        if(data.size() > size){
            data.remove(0);
        }
    }

    /**
     * Iterate over the graph data
     * @return
     */
    public Iterator<Float> iterator(){
        return data.iterator();
    }

    /**
     * Draw the graph
     */
    public abstract void draw();

    /**
     * Draws a line (x1, y1),(x2,y2)
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    protected void drawLine(float x1, float y1, float x2, float y2){
        // How can I draw a line in OpenGL ES 2.0 ?
    }

    /**
     * Draws the axes
     */
    protected void drawAxes(){
        // Check TouchDevelop
    }

}
