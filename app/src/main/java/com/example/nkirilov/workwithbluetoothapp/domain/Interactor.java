package com.example.nkirilov.workwithbluetoothapp.domain;

import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import rx.Observable;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public interface Interactor {
    Observable<RxBleScanResult> scanningDevices();

    RxBleDevice getDevice(String macAddress);

    byte[] getCmdBraceletVibroByte();

    byte[] getCmdCommStartByte();

    byte[] getCmdReadDetailedActivityData();

    byte[] getCmdRealTimeModeStart();
}
