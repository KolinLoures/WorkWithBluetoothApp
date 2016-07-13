package com.example.nkirilov.workwithbluetoothapp.data.dataModule;

import com.example.nkirilov.workwithbluetoothapp.data.Repository;
import com.example.nkirilov.workwithbluetoothapp.data.device.BleDevice;
import com.example.nkirilov.workwithbluetoothapp.data.device.CmdBraceletVibro;
import com.example.nkirilov.workwithbluetoothapp.data.device.CmdCommunicationStart;
import com.example.nkirilov.workwithbluetoothapp.data.device.CmdReadDetailedActivityData;
import com.example.nkirilov.workwithbluetoothapp.data.device.CmdRealTimeModeStart;
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

    @Provides
    @Singleton
    public CmdCommunicationStart providesCommucationStart(){
        return new CmdCommunicationStart();
    }

    @Provides
    @Singleton
    public CmdBraceletVibro providesBraceletVibro(){
        return new CmdBraceletVibro();
    }

    @Provides
    @Singleton
    public CmdReadDetailedActivityData providesCmdReadDetailedActivityData(){
        return new CmdReadDetailedActivityData();
    }

    @Provides
    @Singleton
    public CmdRealTimeModeStart providesCmdRealTimeModeStart(){
        return new CmdRealTimeModeStart();
    }
}
