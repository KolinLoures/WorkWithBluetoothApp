package com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice;

import android.content.Context;
import android.util.Log;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.data.device.BleDevice;
import com.example.nkirilov.workwithbluetoothapp.domain.Interactor;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by n.kirilov on 22.06.2016.
 */
public class ConnectionPresenter implements ContractConnect.ConnPresenter {

    private int i = 0;

    @Inject
    Interactor interactor;
    @Inject
    Context context;
    @Inject
    PublishSubject<Void> publishSubject;

    private RxBleDevice device;
    private Observable<RxBleConnection> connectionObservable;
    private ContractConnect.ConnView view;

    public ConnectionPresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void createBleDevice(String macAddress) {
        device = interactor.getDevice(macAddress);
        connectionObservable = device
                .establishConnection(context, false)
                .takeUntil(publishSubject)
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startReadCommucation();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        clearSub();
                    }
                }).compose(new ConnectionSharingAdapter());
        if (isConnected()){
            triggerDisconnect();
        } else
        connectBleDevice();
    }

    @Override
    public void connectBleDevice() {
        connectionObservable.subscribe(new Action1<RxBleConnection>() {
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
        view.updateUI();
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
        connectionObservable = null;
        view.updateUI();
    }

    @Override
    public void triggerDisconnect() {
        publishSubject.onNext(null);
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
    public void startWriteCommucation(final ArrayList<byte[]> b) {
        if (isConnected()){
                connectionObservable
                        .flatMap(new Func1<RxBleConnection, Observable<Observable<byte[]>>>() {
                            @Override
                            public Observable<Observable<byte[]>> call(final RxBleConnection rxBleConnection) {
                                final List<Observable<byte[]>> list = new ArrayList<>();
                                for (byte[] bytes: b){
                                    Log.e("Observer", Arrays.toString(bytes));
                                    list.add(rxBleConnection
                                            .writeCharacteristic(BleDevice.characteristicWrite, bytes));
                                }
                                return Observable.from(list);
                            }
                        })
                        .concatMap(new Func1<Observable<byte[]>, Observable<byte[]>>() {
                            @Override
                            public Observable<byte[]> call(Observable<byte[]> observable) {
                                return observable;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<byte[]>() {
                            @Override
                            public void call(byte[] bytes) {
                                view.setTextStatus("Write success");
                                Log.e("Subscriber", Arrays.toString(bytes));
                            }
                        });
            }
    }


    @Override
    public void startReadCommucation(){
        if (isConnected()) {
            connectionObservable
                    .flatMap(new Func1<RxBleConnection, Observable<Observable<byte[]>>>() {
                        @Override
                        public Observable<Observable<byte[]>> call(final RxBleConnection rxBleConnection) {
                            return rxBleConnection
                                    .setupNotification(BleDevice.characteristicRead);
                        }
                    })
                    .concatMap(new Func1<Observable<byte[]>, Observable<byte[]>>() {
                        @Override
                        public Observable<byte[]> call(Observable<byte[]> observable) {
                            return observable;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<byte[]>() {
                        @Override
                        public void call(byte[] bytes) {
                            Log.e("Read", Arrays.toString(bytes));
                            view.setOutText(Arrays.toString(bytes));
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            view.setOutText("!!!FAIL READ!!!");
                        }
                    });

        }
    }

    @Override
    public void onClickStartCommucation() {
        Log.i("Start", "Start работай");
        ArrayList<byte[]> listCmd = new ArrayList<>();
        listCmd.add(interactor.getCmdCommStartByte());
        listCmd.add(interactor.getCmdBraceletVibroByte());
        startWriteCommucation(listCmd);
    }

    @Override
    public void onClickVibroCmd() {

    }


}
