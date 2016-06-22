package com.example.nkirilov.workwithbluetoothapp.presentation.search;

import com.example.nkirilov.workwithbluetoothapp.App;
import com.example.nkirilov.workwithbluetoothapp.domain.Interactor;
import com.polidea.rxandroidble.RxBleScanResult;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public class Presenter implements Contarct.Presenter {

    @Inject
    Interactor interactor;

    private Contarct.View view;

    private Subscription scanSubscription;

    public Presenter() {
        App.getComponent().inject(this);
    }


    @Override
    public boolean isScanning() {
        return scanSubscription != null;
    }

    @Override
    public void onScanClick() {
        if (isScanning()){
            scanSubscription.unsubscribe();
        } else {
            scanSubscription = interactor.scanningDevices()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            view.clearResult();
                        }
                    })
                    .subscribe(new Action1<RxBleScanResult>() {
                        @Override
                        public void call(RxBleScanResult result) {
                            view.showResult(result);
                        }
                    });
        }
        view.updateFabSrc();
    }

    @Override
    public void clearSubscription() {
        scanSubscription = null;

    }

    @Override
    public void unsub() {
        scanSubscription.unsubscribe();
    }

    @Override
    public void attachView(Contarct.View view) {
        this.view = view;
    }


}
