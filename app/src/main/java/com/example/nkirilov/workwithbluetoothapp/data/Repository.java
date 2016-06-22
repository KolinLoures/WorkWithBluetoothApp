package com.example.nkirilov.workwithbluetoothapp.data;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.domain.IRepository;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public class Repository implements IRepository {

    @Inject
    RxBleClient rxBleClient;

    public Repository() {
        App.getComponent().inject(this);
    }


    @Override
    public Observable<RxBleScanResult> scanDevices() {
        return rxBleClient.scanBleDevices();
    }

    @Override
    public RxBleDevice getDevice(String macAddress) {
        return rxBleClient.getBleDevice(macAddress);
    }
}
