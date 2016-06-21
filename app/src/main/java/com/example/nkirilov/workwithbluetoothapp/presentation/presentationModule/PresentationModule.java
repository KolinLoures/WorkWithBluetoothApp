package com.example.nkirilov.workwithbluetoothapp.presentation.presentationModule;

import com.example.nkirilov.workwithbluetoothapp.presentation.Contarct;
import com.example.nkirilov.workwithbluetoothapp.presentation.Presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by n.kirilov on 21.06.2016.
 */
@Module
public class PresentationModule {

    @Provides
    @Singleton
    Contarct.Presenter providesPresenter(){
        return new Presenter();
    }

}
