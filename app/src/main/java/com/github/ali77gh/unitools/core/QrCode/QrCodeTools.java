package com.github.ali77gh.unitools.core.QrCode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.app.Fragment;

import com.github.ali77gh.unitools.data.Model.Friend;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by ali on 10/5/18.
 */

public class QrCodeTools {

    //to Qr code

    public static Bitmap Generate(String content) {

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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
