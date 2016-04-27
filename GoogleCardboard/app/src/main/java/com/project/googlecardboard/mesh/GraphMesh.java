package com.project.googlecardboard.mesh;

import com.project.googlecardboard.render.shader.ShaderType;

/**
 * Created by Garrett on 08/01/2016.
 */
public class GraphMesh extends Mesh {

    protected volatile int size;

    public GraphMesh(ShaderType shaderType, int capacity){
        super(shaderType, capacity);
        this.size = 0;
        this.vertex = new float[capacity * 3];
        for(int i = 0; i < capacity * 3; i += 3){
            vertex[i] = calcNewX(i);
            vertex[i + 1] = -0.5f;
            vertex[i + 2] = 0.0f;
        }
    }

    public int size(){
        return size;
    }

    public synchronized void addPoint(float newPoint) {
        float[] newVertex = copyPoints(vertex);
        for(int i = 0; i < capacity * 3; i += 3){
            newVertex[i] = calcNewX(i); // New X-position of points; shift to left
        }
        newVertex[(size * 3) + 1] = newPoint;
        size++;

        vertex = newVertex;
    }

    /* MISCELLANEOUS */

    private float calcNewX(int i){
        float value = 0.5f - (((float) i / 3.0f) * 1.0f / (float) size);
        return (value < -0.5f) ? -0.5f : value;
    }

    private float[] copyPoints(float[] vertex){
        float[] newVertex = new float[capacity * 3];
        if(size == capacity){
            System.arraycopy(vertex, 3, newVertex, 0, (capacity - 1) * 3);
            size--;
        } else{
            System.arraycopy(vertex, 0, newVertex, 0, capacity * 3);
        }
        return newVertex;
    }
}
