package com.project.googlecardboard.render.animation;

/**
 * Created by Garrett on 10/01/2016.
 */
public class PopAnimation extends Animation{

    public PopAnimation(float near, float far, float increment){
        for(float i = far; i > near; i -= increment){
            this.animation.offer(i);
        }
        for(float i = near; i <= far; i += increment){
            this.animation.offer(i);
        }
    }
}
