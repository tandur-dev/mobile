package com.android.tandur.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.tandur.api.response.LoginResponse;
import com.google.gson.Gson;

public class AppPreference {
    static final String PREF = "PREF";
    static final String USER_PREF = "USER_PREF";
    static final String SLIDE_INTRO = "SLIDE_INTRO";

    public static void saveUser(Context context, LoginResponse.LoginModel loginModel){
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putString(USER_PREF, new Gson().toJson(loginModel)).apply();
    }

    public static LoginResponse.LoginModel getUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(USER_PREF)){
            Gson gson = new Gson();

            return gson.fromJson(pref.getString(USER_PREF, ""), LoginResponse.LoginModel.class);
        }

        return null;
    }

    public static void removeUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(USER_PREF)){
            pref.edit().remove(USER_PREF).apply();
        }
    }

    public static void saveSlideIntro(Context context, boolean status){
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putBoolean(SLIDE_INTRO, status).apply();
    }

    public static boolean getSlideIntro(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(SLIDE_INTRO)){
            return pref.getBoolean(SLIDE_INTRO, false);
        }

        return false;
    }

    public static void removeSlideIntro(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(SLIDE_INTRO)){
            pref.edit().remove(SLIDE_INTRO).apply();
        }
    }
}
