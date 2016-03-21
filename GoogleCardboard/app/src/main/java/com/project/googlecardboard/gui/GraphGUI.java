package com.project.googlecardboard.gui;

import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.graph.Graph;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;

import java.util.Random;

import pl.googlecardboard.HumidityPacket;
import pl.packet.Packet;

/**
 * Created by Garrett on 08/01/2016.
 */
public class GraphGUI extends GUI{

    private final Graph graph;

    public GraphGUI(float radius, float pitch, float yaw, Graph graph) {
        super(radius, pitch, yaw, ShaderType.GRAPH);
        this.graph = graph;
    }

    /* GETTERS */

    public Graph getGraph(){
        return graph;
    }

    /* DRAWING */

    public void draw(){
        Shader shader = getShader();
        shader.start();
        shader.loadTransformationMatrix(getMatrix());
        shader.loadColour(isBeingViewed() ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
        graph.draw();
    }

    /* UPDATING */

    public void update(Packet packet){
        System.out.println("G::Received packet!: " + packet);
        //Random rand = new Random();
        //graph.add(rand.nextFloat() - 0.5f);
        if(packet instanceof HumidityPacket){
            HumidityPacket hPacket = (HumidityPacket) packet;
            graph.add(hPacket.getHumidity() - 0.5f);
        }
    }

    /* TEARDOWN */

    public void teardown(){

    }
}
