package com.github.ali77gh.unitools.uI.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.activities.SplashActivity;

import java.util.List;
import java.util.Locale;

/**
 * Created by ali77gh on 11/21/18.
 */

public class ShowNextClassWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ContextHolder.initStatics(context);
        SetupLang(context);

        Update(context,appWidgetManager,appWidgetIds);
    }

    public static void Update(Context context,AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            List<UClass> classes = UserInfoRepo.getUserInfo().Classes;
            if (classes.size()==0) return;
            Sort.SortClass(classes);

            String number = context.getString(R.string.next_class) + " : " + Translator.getUClassReadable(classes.get(0));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_show_next_class);
            remoteViews.setTextViewText(R.id.tv_widget_next_class, number);

            Intent intent = new Intent(context, SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            remoteViews.setOnClickPendingIntent(R.id.tv_widget_next_class, pendingIntent);

            Intent intent2 = new Intent(context, ShowNextClassWidget.class);
            intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void SetupLang(Context context) {

        String lang = UserInfoRepo.getUserInfo().LangId;

        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

}
