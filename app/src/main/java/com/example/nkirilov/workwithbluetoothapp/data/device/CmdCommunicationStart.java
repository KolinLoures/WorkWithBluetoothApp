package com.example.nkirilov.workwithbluetoothapp.data.device;

/**
 * Created by n.kirilov on 22.06.2016.
 */
public class CmdCommunicationStart extends BleDevice {

    public CmdCommunicationStart() {
        super();

        writeValue[0] = 0x3c;
        writeValue[1] = 0x61;
        writeValue[2] = 0x59;
        writeValue[3] = 0x36;
        writeValue[4] = 0x71;
        writeValue[5] = 0x33;
        writeValue[6] = 0x77;
        writeValue[7] = 0x39;
        writeValue[8] = 0x65;
        writeValue[9] = 0x32;
        writeValue[10] = 0x72;
        writeValue[11] = 0x30;
        writeValue[12] = 0x74;
        writeValue[13] = 0x50;
        writeValue[14] = 0x76;
    }

}
