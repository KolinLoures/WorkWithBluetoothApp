package com.example.nkirilov.workwithbluetoothapp.presentation.search;

import com.polidea.rxandroidble.RxBleScanResult;

/**
 * Created by n.kirilov on 21.06.2016.
 */
public interface Contarct {

    public interface Presenter{
        boolean isScanning();

        void onScanClick();

        void clearSubscription();

        void unsub();

        void attachView(Contarct.View view);
    }

    interface View{
        void showResult(RxBleScanResult result);

        void clickFab();

        void updateFabSrc();

        void clearResult();

        void onAdapterItemClick(RxBleScanResult results);
    }

}
