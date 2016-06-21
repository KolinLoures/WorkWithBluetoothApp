package com.example.nkirilov.workwithbluetoothapp.domain;

import com.example.nkirilov.workwithbluetoothapp.App;
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
}
