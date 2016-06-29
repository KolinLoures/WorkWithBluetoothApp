package com.example.nkirilov.workwithbluetoothapp.presentation.connectDevice;

import com.polidea.rxandroidble.RxBleConnection;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by n.kirilov on 22.06.2016.
 */
public interface ContractConnect {

    interface ConnPresenter {
        void createBleDevice(String macAddress);

        void connectBleDevice();

        boolean isConnected();

        void onConnFail();

        void onConnReceived();

        void clearSub();

        void triggerDisconnect();

        void onConnectionStateChange(RxBleConnection.RxBleConnectionState newState);

        void setView(ContractConnect.ConnView view);

        void connListener();

        void startWriteCommucation(ArrayList<byte[]> b);

        void startReadCommucation();

        void onClickStartCommucation();

        void onClickVibroCmd();
}

    interface ConnView{
        void updateUI();

        void setTextConn(String s);

        void setTextStatus(String s);

        void setOutText(String s);
    }
}
