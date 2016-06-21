package com.example.nkirilov.workwithbluetoothapp;

import android.app.Application;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public class App extends Application {
    private static AppComponent component;

    public static AppComponent getComponent(){
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
