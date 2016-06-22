package com.example.nkirilov.workwithbluetoothapp.data.device;

/**
 * Created by n.kirilov on 22.06.2016.
 */
public class CmdBraceletVibro extends BleDevice {


    public CmdBraceletVibro() {
        super();
        writeValue[0] = 0x36;
        writeValue[1] = 0x1;
    }

}
