package com.github.ali77gh.unitools.core.onlineapi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ali77gh on 12/11/18.
 */

public class DonateUsApi {

    private static String getPayLink(int howMatch) {

        //todo generate link here
        return "http://todoPaymentLink.com";
    }

    public static void OpenDonationGate(Context context, int howMatch) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getPayLink(howMatch)));
        context.startActivity(browserIntent);
    }
}
