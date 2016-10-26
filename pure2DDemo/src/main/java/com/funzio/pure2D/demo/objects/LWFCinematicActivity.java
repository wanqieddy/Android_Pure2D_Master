/*******************************************************************************
 * Copyright (C) 2012-2014 GREE, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package com.funzio.pure2D.demo.objects;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.funzio.pure2D.Scene;
import com.funzio.pure2D.demo.activities.StageActivity;
import com.funzio.pure2D.gl.gl10.GLState;
import com.funzio.pure2D.gl.gl10.textures.Texture;
import com.funzio.pure2D.lwf.LWF;
import com.funzio.pure2D.lwf.LWFData;
import com.funzio.pure2D.lwf.LWFManager;
import com.funzio.pure2D.lwf.LWFObject;

import java.io.InputStream;

public class LWFCinematicActivity extends StageActivity {
    private static final String TAG = LWFCinematicActivity.class.getSimpleName();

    private LWFManager mLWFManager;
    private LWFObject mLWFObject;
    private LWFData mLWFData;
    private LWF mLWF;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!LWF.loadLibrary()) {
            Log.e(TAG, "ERROR: loadLibrary");
        }

        mLWFManager = new LWFManager();

        mLWFObject = new LWFObject();
        mScene.addChild(mLWFObject);
        mScene.setColor(COLOR_WHITE);
        mScene.setListener(new Scene.Listener() {

            @Override
            public void onSurfaceCreated(final GLState gl, final boolean firstTime) {
                if (firstTime) {
                    try {
                        InputStream stream = getAssets().open("lwf/castle4/castle4.lwf");
                        //InputStream stream = getAssets().open("lwf/Ring/Ring.lwf");

                        mLWFData = mLWFManager.createLWFData(stream);
                    } catch (Exception e) {
                        Log.e(TAG, "ERROR: " + e);
                    }

                    int textureNum = mLWFData.getTextureNum();
                    Texture[] textures = new Texture[textureNum];
                    for (int i = 0; i < textureNum; ++i) {
                        String name = mLWFData.getTextureName(i);
                        textures[i] = mScene.getTextureManager().createAssetTexture("lwf/castle4/" + name, null);
                        //textures[i] = mScene.getTextureManager().createAssetTexture("lwf/Ring/" + name, null);

                    }
                    mLWFData.setTextures(textures);

                    attachLWF(mDisplaySizeDiv2.x, mDisplaySizeDiv2.y);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLWFManager.dispose();
    }

    private void attachLWF(final float x, final float y) {

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

        // attach lwf
        mLWF = mLWFObject.attachLWF(mLWFData);
        Log.i("ss","______________________________x:"+x+"______y:"+y+"______width:"+width+"_____height:"+height+"________mLWF.getSize().x:"+mLWF.getSize().x+"______mLWF.getSize().y:"+mLWF.getSize().y);
        //10-19 11:42:13.165 2120-2596/com.longo.pure2D.demo I/ss: _____________________________width:1440_____height:2392
        //10-19 11:42:13.166 2120-2596/com.longo.pure2D.demo I/ss: ______________________________x:720.0______y:1196.0



        //10-19 11:42:32.274 2120-3085/com.longo.pure2D.demo I/ss: _____________________________width:2413_____height:1440
        //10-19 11:42:32.274 2120-3085/com.longo.pure2D.demo I/ss: ______________________________x:1206.0______y:720.0
        // position in Flash coordinate
        //mLWF.moveTo("_root", 300, -2000);
        //Log.i("ss","______________________________x/2:"+x/2+"______-y*2:"+-y*2);
        //Log.i("ss","___________________1__________mScene.getWidth():"+mScene.getWidth()+"______mScene.getHeight():"+mScene.getHeight());

        if(x < y &&  (x*2) <= mLWF.getSize().x){//坚屏时，当前屏幕的宽小于动画的宽，
            Log.i("ss","_____________________________________________________1");
            float scaleX = (x * 2) / mLWF.getSize().x;
            mLWF.moveTo("_root", 0, -y*2);
            mLWF.scale("_root", scaleX , scaleX);
        }else if( x < y && (x*2) >=  mLWF.getSize().x){//坚屏时，当前屏幕的宽大于动画的宽，

            Log.i("ss","_____________________________________________________2_____-y*2:"+(-y*2));
            float scaleX = (x * 2) / mLWF.getSize().x;

            mLWF.moveTo("_root", 0, -y*2);
            mLWF.scale("_root", scaleX , scaleX);

        }else if( x > y && (x*2) >= mLWF.getSize().x){//横屏时，当前屏幕的宽大于动画的宽，
            //______________x:1206.0______y:720.0______width:2413_____height:1440________mLWF.getSize().x:640______mLWF.getSize().y:1136
            //______________x:427.0______y:240.0______width:854_____height:480________mLWF.getSize().x:640______mLWF.getSize().y:1136

            float scaleY = (y * 2) / mLWF.getSize().y;
            Log.i("ss","_____________________________________________________3___scale:"+scaleY);

            mLWF.scale("_root", scaleY , scaleY);
            mLWF.moveTo("_root", ((x*2) - mLWF.getSize().x * scaleY)/2, -y*2);

        }else if( x > y && (x*2) <=  mLWF.getSize().x){//横屏时，当前屏幕的宽小于动画的宽，

            Log.i("ss","_____________________________________________________4");
            mLWF.moveTo("_root", ((x*2) - mLWF.getSize().x)/2 , -y*2);

        }


        //Log.i("ss","___________________2__________mLWF.getSize().x:"+mLWF.getSize().x+"______mLWF.getSize().y:"+mLWF.getSize().y);

        //mLWF.scale("_root", 2.0f,2.0f);

        //mLWF.fitForHeight(1000,1000);
        // mLWF.fitForWidth(800,800);

        // handler
        mLWF.addEventHandler("done", new LWF.Handler() {
            @Override
            public void call() {
                Log.v(TAG, "done");
            }
        });
        mLWF.addEventHandler("STOP_DRAWING_BASE_UNITS", new LWF.Handler() {
            @Override
            public void call() {
                Log.v(TAG, "STOP_DRAWING_BASE_UNITS");
            }
        });
        mLWF.addEventHandler("START_DRAWING_FINAL_UNIT", new LWF.Handler() {
            @Override
            public void call() {
                Log.v(TAG, "START_DRAWING_FINAL_UNIT");
            }
        });
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mLWF != null) {
                mLWF.gotoAndPlay("_root", "start");
            }
        }

        return true;
    }
}
