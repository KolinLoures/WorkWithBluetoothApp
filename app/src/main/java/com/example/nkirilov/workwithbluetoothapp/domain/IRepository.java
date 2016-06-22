package com.example.nkirilov.workwithbluetoothapp.domain;

import com.example.nkirilov.workwithbluetoothapp.data.device.CmdBraceletVibro;
import com.example.nkirilov.workwithbluetoothapp.data.device.CmdCommunicationStart;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import rx.Observable;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public interface IRepository {
    Observable<RxBleScanResult> scanDevices();

    RxBleDevice getDevice(String macAddress);

    CmdBraceletVibro getCmdBraceletVibro();

    CmdCommunicationStart getCmdCommunicationStart();
}
