package com.project.googlecardboard.graph;

/**
 * Created by Garrett on 09/12/2015.
 */
public class BarGraph extends Graph {

    public BarGraph(int size){
        super(size);
    }

    public void draw(){
        clear();
        drawAxes();
        for(int i = 0, j = 1; j < data.size(); i++, j++){
            float x1 = (i / data.size());
            float x2 = (j / data.size());
            float y1 = data.poll();
            //drawLine(x1, 0.0f, x1, y1);
            //drawLine(x1, y1, x2, y1);
            //drawLine(x2, y1, x2, 0.0f);
        }
    }


}
