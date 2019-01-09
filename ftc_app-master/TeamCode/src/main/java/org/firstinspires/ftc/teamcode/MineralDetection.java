/*
 * Copyright (c) 2018. RED SHIFT ROBOTICS. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.State;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

import java.io.File;
import java.io.FileOutputStream;

/**
 * This class allows the webcam on the robot to take a photo and then process it, determining the
 * position of the mineral in milliseconds.
 */

public class MineralDetection {
    private VuforiaLocalizerImpl vuforiaLocalizer;
    private LinearOpMode context;
    private Hardware hardware;

    public MineralDetection(LinearOpMode context){
        this.context = context;
    }

    public void vuforiaInit(HardwareMap hwm) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        parameters.cameraName = hwm.get(WebcamName.class, "Webcam 1");;
        parameters.vuforiaLicenseKey = "Aa/SijD/////AAABmX2lVWyTR0kWqVSis8F3WSlL9qlOXlUv0WKOxOP54vVEggfrNmqvmKRwczbGHArzVWlLJdWac1BNz3PCYEH8JSpkeJRjxWWj3be7l+Ingj+/RVpMhiQWC4XMTqNoB44IlsIiD6zyiPHU3xanV/nUTMJNbO+nM8LeT6V8fId3S1yL5WYITwy5ifPBsQMw/2awofitlWikiCKwV6y+Nx2vITJxipVyOPNQG/TVME1iK9Nx+bg5DisuXZ5WGgBDHSZzSE6O4TzJHAg2skI0Go/TRPgF1j2kwUHO5ubIPSj3oICokrNtK21220HUdedKA5JhcSZgyC0n0hIGGNIpWIJyfMlpZSvyjzUBTA+IZF4z5LRe";
        vuforiaLocalizer = new VuforiaLocalizerImpl(parameters);
        vuforiaLocalizer.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
    }

    // allows the phone to find an image and then allow it to be processed.

    public Bitmap getImage() throws InterruptedException {

        Image image = null;

        VuforiaLocalizer.CloseableFrame frame = vuforiaLocalizer.getFrameQueue().take(); //takes the frame at the head of the queue    <-----  If I uncomment this the code crashes at about 5 seconds????

        long numImages = frame.getNumImages();

        for (int i = 0; i < numImages; i++)
        {
            if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565)
            {
                image = frame.getImage(i);
            }
        }

        Bitmap bm_img = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.RGB_565);
        bm_img.copyPixelsFromBuffer(image.getPixels());

        saveImage(bm_img, "default");

        return bm_img;
    }

    // this class processes the photo and scans the individual pixels.
    public MineralPosition getPosition(Bitmap bm_img, int numSplits) {
        int centerWidth = bm_img.getWidth();
        int centerHeight = (bm_img.getHeight()/2);
        int heightOffset = 100;
        int scaleFactor = 18;

        Bitmap[] splits = new Bitmap[2];
        int[] numYellow = new int[3];
        Matrix matrix = new Matrix();
        matrix.postScale(-1,-1);

        Bitmap croppedBitmap = Bitmap.createBitmap(bm_img, 0, centerHeight - heightOffset, centerWidth, heightOffset * 2);
        Bitmap scaledCroppedBitmap = Bitmap.createScaledBitmap(croppedBitmap, croppedBitmap.getWidth() / scaleFactor, croppedBitmap.getHeight() / scaleFactor, false);

        Bitmap visualYellow = Bitmap.createScaledBitmap(bm_img, bm_img.getWidth() / scaleFactor, bm_img.getHeight() / scaleFactor, false);

        for(int i = 0; i < numSplits; i++) {
            splits[i] = Bitmap.createBitmap(scaledCroppedBitmap, i * (scaledCroppedBitmap.getWidth() / numSplits), 0,
                    scaledCroppedBitmap.getWidth() / numSplits, scaledCroppedBitmap.getHeight());

            saveImage(splits[i], "split" + i);
        }

        for (int y = 0; y < visualYellow.getHeight(); y++) {
            //for each x pixel
            for (int x = 0; x < visualYellow.getWidth(); x++) {
                int color = visualYellow.getPixel(x, y);

                int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
                if (isYellow(rgb, 1, 0.2f)) {
                    visualYellow.setPixel(x, y, Color.YELLOW);
                } else {
                    visualYellow.setPixel(x, y, Color.BLACK);
                }
            }
        }

        saveImage(scaledCroppedBitmap, "Cropped");
        saveImage(visualYellow, "Colored");


        //for each third
        for (int i = 0; i < splits.length; i++) {
            //makes array zero as initialization
            numYellow[i] = 0;
            //for each y pixel
            for (int y = 0; y < splits[i].getHeight(); y++) {
                //for each x pixel
                for (int x = 0; x < splits[i].getWidth(); x++) {
                    //gets color for every pixel
                    int color = splits[i].getPixel(x, y);

                    //converts color to cmyk
                    int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
                    if (isYellow(rgb, 1, 0.2f)) {
                        //add to array
                        numYellow[i]++;
                    }
                }
            }
        }

        context.telemetry.addData("", numYellow[0] + " , " + numYellow[1]  + " , " + numYellow[2]);
        context.telemetry.update();
        context.sleep(1000);

        // compares the amount of yellow pixels and decides the position of the mineral from the photo.
        if (numYellow[0] > numYellow[1] && numYellow[0] > numYellow[2]) {
            return MineralPosition.LEFT;
        } else if (numYellow[1] > numYellow[0] && numYellow[1] > numYellow[2]) {
            return MineralPosition.CENTER;
        } else if (numYellow[0] == 0 && numYellow[1] == 0) {
            return MineralPosition.RIGHT;
        } else {
            return MineralPosition.NULL;
        }
    }

    public float[] rgb2cmyk(int r, int g, int b) {
        float computedC = 0;
        float computedM = 0;
        float computedY = 0;
        float computedK = 0;


        // BLACK
        if (r == 0 && g == 0 && b == 0) {
            computedK = 1;
            float[] blackArray = {0, 0, 0, 1};
            return blackArray;
        }

        computedC = 1 - (r / 255);
        computedM = 1 - (g / 255);
        computedY = 1 - (b / 255);

        float minCMY = Math.min(computedC, Math.min(computedM, computedY));
        computedC = (computedC - minCMY) / (1 - (minCMY + 0.01f));
        computedM = (computedM - minCMY) / (1 - (minCMY + 0.01f));
        computedY = (computedY - minCMY) / (1 - (minCMY + 0.01f));
        computedK = minCMY;

        float[] returnArray = {computedC, computedM, computedY, computedK};
        return returnArray;
    }

    public float[] rgb2HSV(int r, int g, int b) {
        float[] HSV = new float[3];
        Color.RGBToHSV(r, g, b, HSV);

        return HSV;
    }

    // saves the images to the phones files for processing.
    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name + ".jpg";
        File file = new File(myDir, fname);
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(!file.exists()){}
    }

    // a boolean that detects weather or not a pixel is yellow.
    private boolean isYellow(int[] rgb, int method, float threashold) {

        float[] cmyk = rgb2cmyk(rgb[0], rgb[1], rgb[2]);
        float[] hsv = rgb2HSV(rgb[0], rgb[1], rgb[2]);

        switch (method) {
            case 0:
                return (cmyk[2] > cmyk[0] + threashold && cmyk[2] > cmyk[1] + threashold);

            case 1:
                return (hsv[0] < 90 - threashold && hsv[0] > 30 + threashold && hsv[1] > 0.5 && hsv[2] > 0.5);

            default:
                return false;
        }
    }
}
