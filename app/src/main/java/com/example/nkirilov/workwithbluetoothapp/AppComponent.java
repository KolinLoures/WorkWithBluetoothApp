package com.example.nkirilov.workwithbluetoothapp;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by n.kirilov on 21.06.2016.
 */
@Singleton
@Component(modules =
        {
                AppModule.class
        })
public interface AppComponent {

    void inject(MainActivity mainActivity);
}
