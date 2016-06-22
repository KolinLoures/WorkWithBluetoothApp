package com.example.nkirilov.workwithbluetoothapp.domain;

import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import rx.Observable;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public interface IRepository {
    Observable<RxBleScanResult> scanDevices();

    RxBleDevice getDevice(String macAddress);
}
