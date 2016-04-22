package com.project.googlecardboard.gui;

import com.google.vrtoolkit.cardboard.HeadTransform;
import com.project.googlecardboard.graph.Graph;
import com.project.googlecardboard.matrix.TransformationMatrix;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.Constants;

import pl.googlecardboard.AltitudePacket;
import pl.googlecardboard.HumidityPacket;
import pl.packet.Packet;

/**
 * Created by Garrett on 08/01/2016.
 */
public class GraphGUI extends GUI{

    private final Graph graph;
    public HeadTransform headTran;

    public GraphGUI(float radius, float pitch, float yaw, Graph graph) {
        super(radius, pitch, yaw, ShaderType.GRAPH);
        this.graph = graph;
    }

    /* GETTERS */

    public Graph getGraph(){
        return graph;
    }

    /* DRAWING */

    public float[] getMatrixThis(){
        float[] tM = new float[3];
        headTran.getEulerAngles(tM, 0);
        TransformationMatrix matrix = new TransformationMatrix(getRadius(), 0.0f
                , (tM[0]*-57.2958f)-getPitch(), getYaw()+(tM[1]*57.2958f) + 180);
        return matrix.getMatrix();
//        TransformationMatrix matrix = new TransformationMatrix(this.getRadius(), 0.0f, this.getPitch(), 90 - this.getYaw() + 100);
//        return matrix.getMatrix();
    }

    public void draw(){
        Shader shader = getShader();
        shader.start();
        shader.loadTransformationMatrix(getMatrixThis());
        shader.loadColour(isBeingViewed() ? Constants.CUBE_FOUND_COLORS : Constants.CUBE_COLORS);
        graph.draw();
    }

    /* UPDATING */

    public void update(Packet packet){
        System.out.println("G::Received packet!: " + packet);
        //Random rand = new Random();
        //graph.add(rand.nextFloat() - 0.5f);
        if(packet instanceof HumidityPacket){
            //HumidityPacket hPacket = (HumidityPacket) packet;
            //graph.add(hPacket.getHumidity() / 30 - 0.5f);
        }
        if(packet instanceof AltitudePacket){
            AltitudePacket aPacket = (AltitudePacket) packet;
            graph.add(aPacket.getAltitude() / 10 - 0.5f);
        }
    }

    /* TEARDOWN */

    public void teardown(){

    }
}
