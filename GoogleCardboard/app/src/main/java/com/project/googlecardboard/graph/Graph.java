package com.project.googlecardboard.graph;

import com.project.googlecardboard.gui.GUIModel;

import java.util.AbstractQueue;
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
    //public static final Graph EMPTY = new LineGraph(0);

    protected final int size;

    protected Queue<Float> data;
    private List<Float> model;
    private int index = 0;
    private float[] arrayOfPoints;

    public Graph(int size){
        this.size = size; // Per coord
        //arrayOfPoints = new float[this.size];
        //this.data = new LinkedList<Float>();
        //this.model = new ArrayList<Float>();
        //this.index = 1;
    }

    /**
     * Add an element to the graph
     * @param t
     */
    public void add(float t){
        drawLine(index*0.1f, arrayOfPoints[index*3], (index+1)*0.1f, t, index*3);
        index++;
        //data.offer(t);
        if(data.size() > size){
            data.poll();
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

    protected void drawLine(float x1, float y1, float x2, float y2, int position){
        // How can I draw a line in OpenGL ES 2.0 ?
        // We will draw a line by adding it to a large array, which will represent the Graph as a Model
        if (position+5 > 18)
        {
            return;
        }
        arrayOfPoints[position] = 1.0f - x1;
        arrayOfPoints[position+1] = y1;
        arrayOfPoints[position+2] = -1.0f;
        arrayOfPoints[position+3] = 1.0f - x2;
        arrayOfPoints[position+4] = y2;
        arrayOfPoints[position+5] = -1.0f;
        /*model.add(1.0f - x1);
        model.add(y1);
        model.add(-1.0f);
        model.add(1.0f - x2);
        model.add(y2);
        model.add(-1.0f);*/
    }

    /**
     * Draws the axes
     */
    protected void drawAxes(){
        // Check TouchDevelop
        // Add to model
        // Also include text in the future
        // drawLine(); // X axis
        //drawLine(-1.0f, -1.0f, -1.0f, 1.0f);
        // drawLine(); // Y axis
        //drawLine(-1.0f, -1.0f, 1.0f, -1.0f);
        drawLine(0.0f, 0.0f, 1.0f, 0.0f, 0);
        drawLine(0.0f, 0.0f, 0.0f, 1.0f, 6);
    }

    public float[] getModel(){
        int size = model.size();
        float[] array = new float[size];
        for(int i = 0; i < size; i++){
            array[i] = model.get(i);
        }
        return array;
    }

    public void clear(){
        arrayOfPoints = new float[18];
        model.clear();
    }
}
