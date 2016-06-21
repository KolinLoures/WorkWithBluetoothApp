package com.example.nkirilov.workwithbluetoothapp.data.dataModule;

import com.example.nkirilov.workwithbluetoothapp.data.Repository;
import com.example.nkirilov.workwithbluetoothapp.domain.IRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by n.kirilov on 21.06.2016.
 */
@Module
public class DataModule {

    @Provides
    @Singleton
    public IRepository providesRepository(){
        return new Repository();
    }
}
