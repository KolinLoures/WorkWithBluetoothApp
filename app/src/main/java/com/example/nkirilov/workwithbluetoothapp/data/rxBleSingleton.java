package com.example.nkirilov.workwithbluetoothapp.data;

import android.content.Context;

import com.polidea.rxandroidble.RxBleClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by n.kirilov on 21.06.2016.
 */

@Module
public class RxBleSingleton {

    private static RxBleClient rxBleClient;

    @Singleton
    @Provides
    public static RxBleClient getInstanse(Context context){
        if (rxBleClient == null){
            rxBleClient = RxBleClient.create(context);
        }
        return rxBleClient;
    }

    @Singleton
    private RxBleSingleton() {
    }
}
