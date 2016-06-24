package com.example.nkirilov.workwithbluetoothapp.data.device;

import java.util.UUID;

/**
 * Created by n.kirilov on 22.06.2016.
 */
public class BleDevice {

    protected byte[] writeValue;
    public static final UUID characteristicWrite =
            UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb");
    public static final UUID characteristicRead =
            UUID.fromString("0000fff7-0000-1000-8000-00805f9b34fb");

    public BleDevice() {
        writeValue = new byte[16];
    }

    private void createLastByte() {
        writeValue[15] = 0;
        for (int i = 0; i < this.writeValue.length - 1; i++) {
            this.writeValue[15] += this.writeValue[i];
        }
    }

    public byte[] getWriteValue() {
        createLastByte();
        return writeValue;
    }
}
