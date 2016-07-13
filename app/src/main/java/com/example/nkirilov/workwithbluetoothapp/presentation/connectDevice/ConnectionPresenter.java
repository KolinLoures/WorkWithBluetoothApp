package com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice;

import android.content.Context;
import android.util.Log;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.data.device.BleDevice;
import com.example.nkirilov.workwithbluetoothapp.domain.Interactor;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created by n.kirilov on 22.06.2016.
 */
public class ConnectionPresenter implements ContractConnect.ConnPresenter {

    private int i = 0;
    private boolean flag = true;


    @Inject
    Interactor interactor;
    @Inject
    Context context;

    private PublishSubject<Void> publishSubject;


    private RxBleDevice device;

    private Observable<RxBleConnection> connectionObservable;
    private ContractConnect.ConnView view;
    private Queue<byte[]> listCmd = new LinkedList<>();

    public ConnectionPresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void createBleDevice(String macAddress) {
        device = interactor.getDevice(macAddress);

        publishSubject = PublishSubject.create();


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
        if (isConnected()) {
            triggerDisconnect();
        } else {
            connectBleDevice();
        }

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
        i = 0;
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
    public void startWriteCommucation(final byte[] b) {

        if (isConnected()) {
            connectionObservable
                    .flatMap(new Func1<RxBleConnection, Observable<byte[]>>() {
                        @Override
                        public Observable<byte[]> call(RxBleConnection rxBleConnection) {
                            Log.e("Observer", Arrays.toString(b));
                            return rxBleConnection
                                    .writeCharacteristic(BleDevice.characteristicWrite, b);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<byte[]>() {
                        @Override
                        public void call(byte[] bytes) {
                            view.setTextStatus("Write success");
                            Log.e("Subscriber", Arrays.toString(bytes));
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }

    }


    /*

   */


    /*
        connectionObservable
                    .flatMap(new Func1<RxBleConnection, Observable<byte[]>>() {
                        @Override
                        public Observable<byte[]> call(RxBleConnection rxBleConnection) {
                            Observable<byte[]> mergedObservable = null;
                            for (final byte[] byt : b) {
                                Log.e("Bracelet", Arrays.toString(byt));
                                final Observable<byte[]> writeObservable = rxBleConnection
                                        .writeCharacteristic(BleDevice.characteristicWrite, byt);

                                if (mergedObservable == null){
                                    mergedObservable = writeObservable;
                                } else {
                                    mergedObservable = mergedObservable.concatWith(writeObservable);
                                }

                            }
                            return mergedObservable;
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
     */

    @Override
    public void startReadCommucation() {
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
                            if (!listCmd.isEmpty()) {
                                startWriteCommucation(listCmd.remove());
                            } else {
                                flag = true;
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            view.setOutText("!!!FAIL READ!!!");
                        }
                    });

        }
    }

    /*
    Log.e("Read", Arrays.toString(bytes));
                            view.setOutText(Arrays.toString(bytes));
                            if (!listCmd.isEmpty()) {
                                startWriteCommucation(listCmd.remove());
                            } else {
                                flag = true;
                            }

     */

    @Override
    public void onClickStartCommucation() {
        Log.i("Start", "Start работай");


        listCmd.add(interactor.getCmdCommStartByte());
//        listCmd.add(interactor.getCmdReadDetailedActivityData());
        listCmd.add(interactor.getCmdBraceletVibroByte());
//        listCmd.add(interactor.getCmdRealTimeModeStart());

        if (flag) {
            flag = false;
            startWriteCommucation(listCmd.remove());
        }
//        startWriteCommucation(listCmd.remove());


    }

    @Override
    public void onClickVibroCmd() {

    }

}
