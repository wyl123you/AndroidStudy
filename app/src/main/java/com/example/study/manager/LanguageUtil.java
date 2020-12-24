package com.example.study.manager;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LanguageUtil {

    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResource(context, language);
        } else {
            return context;
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private static Context updateResource(@NotNull Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = new Locale(language);

        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        config.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(config);
    }


    public static void switchLanguage(Context context, String newLanguage) {
        if (TextUtils.isEmpty(newLanguage)) return;

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        Locale locale = new Locale(newLanguage);
        config.setLocale(locale);
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(config, dm);
    }
}
