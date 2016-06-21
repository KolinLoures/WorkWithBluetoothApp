package com.example.nkirilov.workwithbluetoothapp.domain.domainModule;

import com.example.nkirilov.workwithbluetoothapp.domain.Interactor;
import com.example.nkirilov.workwithbluetoothapp.domain.IntetactorImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by n.kirilov on 21.06.2016.
 */
@Module
public class DomainModule {

    @Provides
    @Singleton
    public Interactor providesInteractor(){
        return new IntetactorImpl();
    }
}
