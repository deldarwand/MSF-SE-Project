package com.project.googlecardboard.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Garrett on 24/11/2015.
 */
public class LineGraph extends Graph{

    public LineGraph(int size){
        super(size);
    }

    public void draw(){
        clear();
        drawAxes();
        //Queue<Float> dataCopy = new LinkedList<Float>();
        float increment = 1.0f / 10.0f;

        Float[] array = new Float[data.size()];
        array = data.toArray(array);

        /*for(int i = 0, j = 1; j < arrayOfPoints.length; i++, j++){
            float y1 = arrayOfPoints[i];
            float y2 = arrayOfPoints[j];
            drawLine(i*increment, y1, j*increment, y2, (i*6)+12);
            //dataCopy.offer(y1);
            //drawLine(i, data.get(i), j, data.get(j));
        }*/
        //data = dataCopy;
    }
}
