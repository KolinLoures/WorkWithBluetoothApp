package com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice;

import android.content.Context;
import android.util.Log;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.data.device.BleDevice;
import com.example.nkirilov.workwithbluetoothapp.domain.Interactor;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

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
    private Observable<RxBleConnection> connectionObservable;
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
            connectionObservable = device.establishConnection(context, false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnUnsubscribe(new Action0() {
                        @Override
                        public void call() {
                            clearSub();
                        }
                    }).compose(new ConnectionSharingAdapter());
            subscription = connectionObservable.subscribe(new Action1<RxBleConnection>() {
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
        connectionObservable = null;
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

    @Override
    public void startWriteCommucation(final byte[] b) {
        if (isConnected()){
            connectionObservable
                    .flatMap(new Func1<RxBleConnection, Observable<?>>() {
                        @Override
                        public Observable<?> call(RxBleConnection rxBleConnection) {

                            return rxBleConnection.writeCharacteristic(
                                    BleDevice.characteristicWrite, b);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            view.setTextStatus("Success Write!");
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            view.setTextStatus("Write Fail!");
                        }
                    });
        }
    }

    @Override
    public void startReadCommucation(){
        if (isConnected()){
            connectionObservable.flatMap(new Func1<RxBleConnection, Observable<byte[]>>() {
                        @Override
                        public Observable<byte[]> call(RxBleConnection rxBleConnection) {
                            return rxBleConnection
                                    .readCharacteristic(BleDevice.characteristicRead);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<byte[]>() {
                        @Override
                        public void call(byte[] bytes) {
                            view.setOutText(new String(bytes));
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
//                            Log.e("EX", "Тут эксптион", throwable);
                            throwable.printStackTrace();
                            view.setOutText("!!!Read Fail!!!");
                        }
                    });
        }
    }

    @Override
    public void onClickStartCommucation() {
        startWriteCommucation(interactor.getCmdCommStartByte());
        startReadCommucation();
    }

    @Override
    public void onClickVibroCmd() {
        startWriteCommucation(interactor.getCmdBraceletVibroByte());
        startReadCommucation();
    }


}
