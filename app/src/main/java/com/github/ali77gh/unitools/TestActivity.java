package com.github.ali77gh.unitools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ali77gh.unitools.core.QrCode.QrCodeTools;
import com.github.ali77gh.unitools.core.audio.VoicePlayer;
import com.github.ali77gh.unitools.core.audio.VoiceRecorder;
import com.github.ali77gh.unitools.data.Model.Friend;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        VoiceRecorder voiceRecorder = new VoiceRecorder(this);
//        VoicePlayer voicePlayer = new VoicePlayer(this);

        //recorder test
//        voiceRecorder.Record();
//
//        findViewById(android.R.id.content).postDelayed(() -> {
//            voiceRecorder.stop();
//            voiceRecorder.SaveTempAs("uyuti");
//            voicePlayer.play("uyuti");
//        }, 10000);


        //QrCodeTools.LaunchCameraFromActivity(this);

        String friendJson = "{\"name\":\"ali\",\"classList\":[{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}},{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}},{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}},{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}},{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}},{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}},{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}},{\"id\":\"fsalkfvslmv\",\"where\":\"b110\",\"what\":\"شیمی\",\"dayOfWeek\":3,\"time\":{\"hour\":12,\"min\":30}}]}\n";
        Friend friend = QrCodeTools.Parse(friendJson);
        ImageView imageView = findViewById(R.id.image_qr_test);
        imageView.setImageBitmap(QrCodeTools.Generate(friend));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
