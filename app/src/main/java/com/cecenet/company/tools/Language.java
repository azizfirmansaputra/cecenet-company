package com.cecenet.company.tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class Language {

    public static ContextWrapper changeLanguage(Context context, String languageCode){
        Resources resources         = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale systemLocale         = configuration.getLocales().get(0);

        if(!languageCode.isEmpty() && !systemLocale.getLanguage().equals(languageCode)){
            Locale locale = new Locale(languageCode);

            Locale.setDefault(locale);
            configuration.setLocale(locale);
            Preferences.setLanguage(languageCode);
            context.createConfigurationContext(configuration);
        }

        return new ContextWrapper(context);
    }

    public static Context getLanguage(Context context){
        return changeLanguage(context, Preferences.getLanguage());
    }
}