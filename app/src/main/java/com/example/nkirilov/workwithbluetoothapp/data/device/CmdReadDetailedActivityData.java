package com.example.nkirilov.workwithbluetoothapp.data.device;

/**
 * Created by n.kirilov on 13.07.2016.
 */
public class CmdReadDetailedActivityData extends BleDevice {

    public CmdReadDetailedActivityData() {
        super();

        writeValue[0] = 0x43;

    }


}
