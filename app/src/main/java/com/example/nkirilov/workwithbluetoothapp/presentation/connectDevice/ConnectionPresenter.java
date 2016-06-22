package com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice;

import android.content.Context;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.domain.Interactor;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by n.kirilov on 22.06.2016.
 */
public class ConnectionPresenter implements ContractConnect.ConnPresenter {

    @Inject
    Interactor interactor;
    @Inject
    Context context;

    private RxBleDevice device;
    private Subscription subscription;
    private ContractConnect.ConnView view;

    public ConnectionPresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void connectBleDevice(String macAddress) {
        device = interactor.getDevice(macAddress);
        if (isConnected()){
            triggerDisconnect();
        } else {
            subscription = device.establishConnection(context, false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            clearSub();
                        }
                    })
                    .subscribe(new Action1<RxBleConnection>() {
                        @Override
                        public void call(RxBleConnection rxBleConnection) {
                            onConnReceived();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            onConnFail();
                        }
                    });
        }
    }

    @Override
    public boolean isConnected() {
        return device.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    @Override
    public void onConnFail() {
        view.setTextConn("Fail");
    }

    @Override
    public void onConnReceived() {
        view.setTextConn("Success");
    }

    @Override
    public void clearSub() {
        subscription = null;
        view.updateUI();
    }

    @Override
    public void triggerDisconnect() {
        if (subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    public void onConnectionStateChange(RxBleConnection.RxBleConnectionState newState) {
        view.setTextConn(newState.toString());
        view.updateUI();
    }

    @Override
    public void setView(ContractConnect.ConnView view) {
        this.view = view;
    }

    @Override
    public void connListener() {
        device.observeConnectionStateChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RxBleConnection.RxBleConnectionState>() {
                    @Override
                    public void call(RxBleConnection.RxBleConnectionState rxBleConnectionState) {
                        onConnectionStateChange(rxBleConnectionState);
                    }
                });
    }


}
