package com.github.ali77gh.unitools.core.onlineapi;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.AppNotification;
import com.github.ali77gh.unitools.core.CH;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class CheckForUpdate {

    private static final String API_URL = "https://unitools.ir/config/version.txt";

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
                if (!CH.getString(R.string.app_version).equals(page.toString()))
                    AppNotification.ShowUpdateAvailable();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
