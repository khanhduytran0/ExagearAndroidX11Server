package com.eltechs.axs;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AppConfig {
    private static final String CONFIG_FILE_KEY = "com.eltechs.axs.CONFIG";
    private static volatile AppConfig mInstance;
    private SharedPreferences sp;

    public static synchronized AppConfig getInstance(Context context) {
        AppConfig appConfig;
        synchronized (AppConfig.class) {
            if (mInstance == null) {
                mInstance = new AppConfig();
                mInstance.sp = context.getSharedPreferences(CONFIG_FILE_KEY, 0);
            }
            appConfig = mInstance;
        }
        return appConfig;
    }

    public boolean isRunAfterNotification() {
        return this.sp.getBoolean("RUN_AFTER_NOTIFICATION", false);
    }

    public void setRunAfterNotification(boolean z) {
        this.sp.edit().putBoolean("RUN_AFTER_NOTIFICATION", z).apply();
    }

    public String getNotificationName() {
        return this.sp.getString("NOTIFICATION_NAME", null);
    }

    public void setNotificationName(String str) {
        this.sp.edit().putString("NOTIFICATION_NAME", str).apply();
    }

    public Date getFirstRunTime() {
        return new Date(this.sp.getLong("FIRST_RUN_TIME", 0));
    }

    public void setFirstRunTime(Date date) {
        this.sp.edit().putLong("FIRST_RUN_TIME", date.getTime()).apply();
    }

    public Date getLastSessionTime() {
        return new Date(this.sp.getLong("LAST_SESSION_TIME", 0));
    }

    public void setLastSessionTime(Date date) {
        this.sp.edit().putLong("LAST_SESSION_TIME", date.getTime()).apply();
    }

    public Date getExeFoundTime() {
        return new Date(this.sp.getLong("EXE_FOUND_TIME", 0));
    }

    public void setExeFoundTime(Date date) {
        this.sp.edit().putLong("EXE_FOUND_TIME", date.getTime()).apply();
    }

    public Set<String> getComlpetedRemindActions() {
        return this.sp.getStringSet("COMLPETED_REMIND_ACTIONS", new HashSet());
    }

    public void setCompletedRemindActions(Set<String> set) {
        this.sp.edit().putStringSet("COMLPETED_REMIND_ACTIONS", set).apply();
    }

    public Set<String> getControlsWithInfoShown() {
        return this.sp.getStringSet("CONTROLS_WITH_INFO_SHOWN", new HashSet());
    }

    public void setControlsWithInfoShown(Set<String> set) {
        this.sp.edit().putStringSet("CONTROLS_WITH_INFO_SHOWN", set).apply();
    }
}
