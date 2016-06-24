package com.example.nkirilov.workwithbluetoothapp.presentation.presentationModule;

import com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice.ConnectionPresenter;
import com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice.ContractConnect;
import com.example.nkirilov.workwithbluetoothapp.presentation.search.Contarct;
import com.example.nkirilov.workwithbluetoothapp.presentation.search.Presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subjects.PublishSubject;

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

    @Provides
    @Singleton
    ContractConnect.ConnPresenter connectionPresenter(){
        return new ConnectionPresenter();
    }

    @Provides
    @Singleton
    PublishSubject<Void> providesTriggerSubject(){
        return PublishSubject.create();
    }

}
