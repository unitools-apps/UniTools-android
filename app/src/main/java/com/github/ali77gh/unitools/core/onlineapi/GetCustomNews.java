package com.github.ali77gh.unitools.core.onlineapi;

import com.github.ali77gh.unitools.core.AppNotification;
import com.github.ali77gh.unitools.data.model.PushNotifyModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetCustomNews {

    private static final String API_URL = "https://unitools.ir/config/pushNotification.json";

    public static void Check() {

        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder page = new StringBuilder();
                String inLine;

                while ((inLine = in.readLine()) != null) {
                    page.append(inLine);
                }

                in.close();
                PushNotifyModel pnm = new Gson().fromJson(page.toString(), PushNotifyModel.class);
                if (pnm.enable)
                    AppNotification.ShowCustomNewsNotification(pnm.title, pnm.text, pnm.link);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
