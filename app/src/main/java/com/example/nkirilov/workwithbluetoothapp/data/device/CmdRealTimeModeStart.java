package com.example.nkirilov.workwithbluetoothapp.data.device;

/**
 * Created by n.kirilov on 13.07.2016.
 */
public class CmdRealTimeModeStart extends BleDevice {

    public CmdRealTimeModeStart() {
        super();

        writeValue[0] = 9;
    }
}
