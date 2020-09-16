package Model;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class ListDevices {

    private String deviceName;
    private String deviceAddress;

    public ListDevices(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }
    /*public ListDevices(BluetoothAdapter bluetoothAdapter, BluetoothDevice device) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.device = device;
    }

    public String getDeviceName(){
        return device.getName();
    }

    public String getDeviceAddress(){
        return device.getAddress();
    }
*/


}
