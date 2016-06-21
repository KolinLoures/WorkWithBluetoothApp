package com.example.nkirilov.workwithbluetoothapp;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by n.kirilov on 21.06.2016.
 */
@Module
@Singleton
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context providesContext(){
        return context.getApplicationContext();
    }
}
