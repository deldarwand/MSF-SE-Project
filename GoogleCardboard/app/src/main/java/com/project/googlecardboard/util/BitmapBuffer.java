package com.project.googlecardboard.util;

import android.graphics.Bitmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Garrett on 07/01/2016.
 *
 * To store the incoming bitmaps in a queue/buffer/list
 *
 */
public class BitmapBuffer implements Iterable<Bitmap>{

    private final Queue<Bitmap> buffer;
    private Bitmap lastImage = null;

    public BitmapBuffer(){
        this.buffer = new LinkedList<Bitmap>();
    }

    public BitmapBuffer(Collection<Bitmap> bitmaps){
        this();
        push(bitmaps);
    }

    public void push(Bitmap bitmap){
        buffer.offer(bitmap);
        lastImage = bitmap;
    }

    public void push(Collection<Bitmap> bitmaps){
        if(bitmaps.isEmpty()){
            return;
        }
        Iterator<Bitmap> iterator = bitmaps.iterator();
        Bitmap bitmap = null;
        while(iterator.hasNext()){
            bitmap = iterator.next();
            buffer.offer(bitmap);
        }
        lastImage = bitmap;
    }

    public Bitmap pull(){
        Bitmap bitmap = buffer.poll();
        return (bitmap != null) ? bitmap : lastImage;
    }

    public Iterator<Bitmap> iterator(){
        return buffer.iterator();
    }

    public int size(){
        return buffer.size();
    }

    public void recycle(){
        while(buffer.size() != 0){
            Bitmap bitmap = pull();
            bitmap.recycle();
        }
    }
}
