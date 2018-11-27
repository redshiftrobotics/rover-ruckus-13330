/* Copyright (c) 2018 FIRST. All rights reserved.
*
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
import android.os.Environment;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

import java.io.File;
import java.io.FileOutputStream;


public class MineralDetection {
    private VuforiaLocalizerImpl vuforiaLocalizer;
    private LinearOpMode context;

    public MineralDetection(LinearOpMode context){
        this.context = context;
    }

    public void vuforiaInit() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        parameters.vuforiaLicenseKey = "Aa/SijD/////AAABmX2lVWyTR0kWqVSis8F3WSlL9qlOXlUv0WKOxOP54vVEggfrNmqvmKRwczbGHArzVWlLJdWac1BNz3PCYEH8JSpkeJRjxWWj3be7l+Ingj+/RVpMhiQWC4XMTqNoB44IlsIiD6zyiPHU3xanV/nUTMJNbO+nM8LeT6V8fId3S1yL5WYITwy5ifPBsQMw/2awofitlWikiCKwV6y+Nx2vITJxipVyOPNQG/TVME1iK9Nx+bg5DisuXZ5WGgBDHSZzSE6O4TzJHAg2skI0Go/TRPgF1j2kwUHO5ubIPSj3oICokrNtK21220HUdedKA5JhcSZgyC0n0hIGGNIpWIJyfMlpZSvyjzUBTA+IZF4z5LRe";
        vuforiaLocalizer = new VuforiaLocalizerImpl(parameters);
        vuforiaLocalizer.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGBA8888, true);
    }

    public MineralPosition getPosition(Bitmap bm_img) {
        int centerWidth = bm_img.getWidth();
        int centerHeight = bm_img.getHeight()/4;
        int heightOffset = 500;
        int scaleFactor = 24;

        Bitmap[] thirds = new Bitmap[3];
        Bitmap[] detectionTest = new Bitmap[2];
        int[] numYellow = new int[3];

        Bitmap croppedBitmap = Bitmap.createBitmap(bm_img, 0, (bm_img.getHeight() / 2 - centerHeight / 2) + heightOffset, centerWidth, centerHeight);
        Bitmap scaledCroppedBitmap = Bitmap.createScaledBitmap(croppedBitmap, croppedBitmap.getWidth() / scaleFactor, croppedBitmap.getHeight() / scaleFactor, false);

        Bitmap visualYellow = Bitmap.createScaledBitmap(bm_img, bm_img.getWidth() / scaleFactor, bm_img.getHeight() / scaleFactor, false);

        thirds[0] = Bitmap.createBitmap(scaledCroppedBitmap, 0, 0, scaledCroppedBitmap.getWidth() / 3, scaledCroppedBitmap.getHeight());
        thirds[1] = Bitmap.createBitmap(scaledCroppedBitmap, scaledCroppedBitmap.getWidth() / 3, 0, scaledCroppedBitmap.getWidth() / 3, scaledCroppedBitmap.getHeight());
        thirds[2] = Bitmap.createBitmap(scaledCroppedBitmap, scaledCroppedBitmap.getWidth() / 3 * 2, 0, scaledCroppedBitmap.getWidth() / 3, scaledCroppedBitmap.getHeight());


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
        saveImage(thirds[0], "left");
        saveImage(thirds[1], "center");
        saveImage(thirds[2], "right");

        //for each third
        for (int i = 0; i < 2; i++) {
            //makes array zero as initialization
            numYellow[i] = 0;
            //for each y pixel
            for (int y = 0; y < thirds[i].getHeight(); y++) {
                //for each x pixel
                for (int x = 0; x < thirds[i].getWidth(); x++) {
                    //gets color for every pixel
                    int color = thirds[i].getPixel(x, y);

                    //converts color to cmyk
                    int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
                    if (isYellow(rgb, 1, 0.2f)) {
                        //add to array
                        numYellow[i]++;
                    }
                }
            }
        }


        if (numYellow[0] > numYellow[1] && numYellow[0] > numYellow[2]) {
            return MineralPosition.LEFT;
        } else if (numYellow[1] > numYellow[0] && numYellow[1] > numYellow[2]) {
            return MineralPosition.CENTER;
        } else if (numYellow[2] > numYellow[0] && numYellow[2] > numYellow[1]) {
            return MineralPosition.RIGHT;
        } else {
            return MineralPosition.NULL;
        }
    }

    public Bitmap getImage() throws InterruptedException {

        Image image;
        image = getImageFromFrame(vuforiaLocalizer.getFrameQueue().take(), PIXEL_FORMAT.RGBA8888);
        Bitmap bm_img = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        bm_img.copyPixelsFromBuffer(image.getPixels());

        return bm_img;
    }

    public Image getImageFromFrame(VuforiaLocalizer.CloseableFrame frame, int format) {
        long numImages = frame.getNumImages();
        for (int i = 0; i < numImages; i++) {
            if (frame.getImage(i).getFormat() == format) {
                return frame.getImage(1);
            }
        }
        return null;
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

        while(context.opModeIsActive() && !file.exists()){}
    }

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
