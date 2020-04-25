package com.avocado.ws2812bledcontroller;

import Bluetooth.BluetoothService;
import Model.ListPatterns;
import Adapter.PatternListAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Bundle extras;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService myBTService;
    //UI
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListPatterns> listPatterns;
    private ProgressBar connectBT;

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get local default adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        uiSetup();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Start the connectivity with the device
        if (myBTService == null){
            myBTService = new BluetoothService(getApplicationContext());
            connectDevice(getIntent(), false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (myBTService != null){
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (myBTService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
               // myBTService.start();
                Toast.makeText(this,"Bluetooth disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure){

        Bundle extras = data.getExtras();

        String address = extras.getString("Address");

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        myBTService.connect(device,secure);

    }

    private void uiSetup(){
        connectBT = findViewById(R.id.connectingID);
        recyclerView = findViewById(R.id.pattern_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listPatterns = new ArrayList<>();
        ListPatterns item = new ListPatterns("Commet");
        listPatterns.add(item);

        adapter = new PatternListAdapter(this, listPatterns);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // disconnect();
    }

    /*private void disconnect(){
        if (bluetoothSocket != null){
            try {
                bluetoothSocket.close();
                bluetoothSocket = null;
                isConnected = false;
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        finish();
    }

    /*private class connectToDevice extends AsyncTask<Void, Void, String>{
        private boolean connectSuccess = true;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            connectBT.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (!isConnected || bluetoothSocket != null){
                try {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(extras.getString("Address"));
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    connectSuccess = false;
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!connectSuccess){
                Toast.makeText(getApplicationContext(),"Couldn't connect", Toast.LENGTH_SHORT).show();
            }else{
                isConnected = true;
                Toast.makeText(getApplicationContext(),"Connected!", Toast.LENGTH_SHORT).show();
                Log.d("Is connected?", "Connected!");
            }
            connectBT.setVisibility(View.INVISIBLE);
        }
    }*/
}
