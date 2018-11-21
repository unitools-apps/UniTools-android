package com.github.ali77gh.unitools.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.Model.UClass;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;

import java.util.List;
import java.util.Locale;

/**
 * Created by ali77gh on 11/21/18.
 */

public class ShowNextClassWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SetupLang(context);

        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            List<UClass> classes = UserInfoRepo.getUserInfo().Classes;
            Sort.SortClass(classes);

            String number = Translator.getUClassReadable(classes.get(0));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_show_next_class);
            remoteViews.setTextViewText(R.id.tv_widget_next_class, number);

            Intent intent = new Intent(context, ShowNextClassWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
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
