package com.github.ali77gh.unitools.core.QrCode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.app.Fragment;
import android.graphics.Color;

import com.github.ali77gh.unitools.data.Model.Friend;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by ali on 10/5/18.
 */

public class QrCodeTools {

    // to Qr code

    public static Bitmap Generate(String content) {

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512,hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static Bitmap Generate(Friend friend) {
        return Generate(new Gson().toJson(friend));
    }

    // from QrCode

    public static void LaunchCameraFromActivity(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public static void LaunchCameraFromFragment(Fragment fragment) {
        IntentIntegrator integrator = IntentIntegrator.forFragment(fragment);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public static Friend Parse(String text) {
        return new Gson().fromJson(text, Friend.class);
    }

}
