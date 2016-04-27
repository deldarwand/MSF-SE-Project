package com.project.googlecardboard.mesh;

import android.opengl.GLES20;

import com.project.googlecardboard.graph.Drawing;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by danieleldar on 11/12/2015.
 */
public abstract class Mesh implements Drawing {

    private final ShaderType shaderType;
    protected final int capacity;
    protected volatile float[] vertex;
    protected volatile float[] normal;

    public Mesh(ShaderType shaderType, int capacity){
        this.shaderType = shaderType;
        this.capacity = capacity;
    }

    public Mesh(ShaderType shaderType, float[] vertex, int capacity){
        this(shaderType, capacity);
        this.vertex = vertex;
    }

    public Mesh(ShaderType shaderType, String objFile)
    {
        this(shaderType, 18);
        String[] arrayOfLines = objFile.split("\n");
        ArrayList<Float> arrayOfPositionsF = new ArrayList<Float>();
        ArrayList<Float> arrayOfNormalsF = new ArrayList<Float>();
        int currentVertex = 0;
        float[] vertexData = new float[18*3];
        float[] normalData = new float[18*3];
        for(String line : arrayOfLines)
        {
            if(line.charAt(0) == 'v' && line.charAt(1) == ' ') //new vertex
            {
                String[] arrayOfPositions = line.substring(2).split(" ");
                arrayOfPositionsF.add(Float.parseFloat(arrayOfPositions[0]));
                arrayOfPositionsF.add(Float.parseFloat(arrayOfPositions[1]));
                arrayOfPositionsF.add(Float.parseFloat(arrayOfPositions[2]));
            }
            else if(line.charAt(0) == 'v' && line.charAt(1) == 'n') //new vertex
            {
                String[] arrayOfNormals = line.substring(3).split(" ");
                arrayOfNormalsF.add(Float.parseFloat(arrayOfNormals[0]));
                arrayOfNormalsF.add(Float.parseFloat(arrayOfNormals[1]));
                arrayOfNormalsF.add(Float.parseFloat(arrayOfNormals[2]));
            }
            else if(line.charAt(0) == 'f')
            {
                String[] arrayOfFaces = line.substring(2).split(" ");
                int[] arrayOfIndices = new int[3];
                arrayOfIndices[0] = arrayOfFaces[0].charAt(0)-'0';
                arrayOfIndices[1] = arrayOfFaces[1].charAt(0)-'0';
                arrayOfIndices[2] = arrayOfFaces[2].charAt(0)-'0';

                for(int i = 0; i < 3; i++)
                {
                    int currentVertexUsed = arrayOfIndices[i]-1;
                    for(int j = 0; j < 3; j++)
                    {
                        float one = arrayOfPositionsF.get((currentVertexUsed*3)+j);
                        vertexData[(currentVertex*3)+j] = one;
                        float two = arrayOfNormalsF.get((currentVertexUsed*3)+j);
                        normalData[(currentVertex*3)+j] = two;
                    }
                    currentVertex++;
                }

            }
        }
        this.vertex = vertexData;
        this.normal = normalData;
        //int capacityObj = 0;

        //this(shaderType, vertexData, 18);
    }

    public Mesh(ShaderType shaderType, float[] vertex){
        this(shaderType, vertex, vertex.length / 3);
    }

    /* GETTERS */

    public Shader getShader(){
        return shaderType.getShader();
    }

    public int capacity(){
        return capacity;
    }

    /* DRAWING */

    public void draw(){
        Shader shader = getShader();
        shader.loadPosition(multiply(vertex, 4.0f));
        shader.enableAttributes();
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, capacity);
    }

    /* MISCELLANEOUS */

    protected float[] multiply(float[] vertex, float scalar){
        float[] newVertex = new float[vertex.length];
        for(int index = 0; index < vertex.length; index++){
            newVertex[index] = vertex[index] * scalar;
        }
        return newVertex;
    }
}

