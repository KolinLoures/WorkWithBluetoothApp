package com.example.nkirilov.workwithbluetoothapp.domain;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.data.device.CmdBraceletVibro;
import com.example.nkirilov.workwithbluetoothapp.data.device.CmdCommunicationStart;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleScanResult;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public class IntetactorImpl implements Interactor{

    @Inject
    IRepository repository;

    public IntetactorImpl() {
        App.getComponent().inject(this);
    }


    @Override
    public Observable<RxBleScanResult> scanningDevices() {
        return repository.scanDevices();
    }

    @Override
    public RxBleDevice getDevice(String macAddress) {
        return repository.getDevice(macAddress);
    }

    @Override
    public byte[] getCmdBraceletVibroByte() {
        CmdBraceletVibro cmdBraceletVibro = repository.getCmdBraceletVibro();
        byte[] array = cmdBraceletVibro.getWriteValue();
        return array;
    }

    @Override
    public byte[] getCmdCommStartByte() {
        CmdCommunicationStart cmdCommunicationStart = repository.getCmdCommunicationStart();
        byte[] array = cmdCommunicationStart.getWriteValue();
        return array;
    }
}
