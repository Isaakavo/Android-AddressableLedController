package com.avocado.ws2812bledcontroller;

import Adapter.DeviceListAdapter;
import Model.ListDevices;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectDeviceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListDevices> listItems;
    private BluetoothAdapter bluetoothAdapter;
    private String deviceName;
    private String deviceAddress;
    private Button refresh;

    public static String EXTRA_DEVICE_ADDRESS = "device address";

    private int REQUEST_ENABLE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        refresh = findViewById(R.id.refreshButton);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()){
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        if (bluetoothAdapter.isEnabled()){
            viewRecyclerFun();

        }else{
            Toast.makeText(this, "Turn Bluetooth on please.", Toast.LENGTH_SHORT).show();
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRecyclerFun();
            }
        });
    }

    public void viewRecyclerFun(){
        recyclerView = findViewById(R.id.device_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        /*for (BluetoothDevice device: bluetoothAdapter.getBondedDevices()){
            ListDevices item = new ListDevices(bluetoothAdapter,device);
            listItems.add(item);
            Log.d("Items:", String.valueOf(device));
        }*/
        if (!bluetoothAdapter.getBondedDevices().isEmpty()){
            for (BluetoothDevice device: bluetoothAdapter.getBondedDevices()) {
                deviceName = device.getName();
                deviceAddress = device.getAddress();
                ListDevices item = new ListDevices(deviceName,deviceAddress);
                listItems.add(item);
                Log.d("Name: ", deviceName);
                Log.d("Address", deviceAddress);
            }
            Log.d("Size of bonded Devices: ", String.valueOf(listItems.size()));
            adapter = new DeviceListAdapter(this, listItems);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (resultCode == Activity.RESULT_OK){
                if (bluetoothAdapter.isEnabled()){
                    Toast.makeText(this,"Bluetooth Has Been Enabled", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Bluetooth Has Been Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
