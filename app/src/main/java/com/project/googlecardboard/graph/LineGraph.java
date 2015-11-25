package com.project.googlecardboard.graph;

import java.util.Iterator;

/**
 * Created by Garrett on 24/11/2015.
 */
public class LineGraph extends Graph{

    public LineGraph(int size){
        super(size);
    }

    public void draw(){
        for(int i = 0, j = 1; j < data.size(); i++, j++){
            drawLine(i, data.get(i), j, data.get(j));
        }
    }

}
