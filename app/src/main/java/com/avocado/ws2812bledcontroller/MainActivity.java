package com.avocado.ws2812bledcontroller;

import Adapter.PatternListAdapter;
import Bluetooth.BluetoothService;
import Commands.Commands;
import Constants.Constants;
import Model.ListPatterns;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService myBTService;


    //Seekbars
    SeekBar satSeekbar;
    SeekBar colorSeekbar;
    SeekBar timeSeekbar;
    SeekBar brightnessSeekbar;

    //Colors arrays and control variables
    ArrayList<Integer> colors = new ArrayList<>();
    int brightness = 50;
    int tone = 0;
    int colorWheel = 50;


    private static final String TAG = "MainActivity seekbars";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get local default adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBTService == null){
            myBTService = new BluetoothService(getApplicationContext(), mHandler);
            connectDevice(getIntent(), false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < 5; i++){
            colors.add(0);
        }
        uiSetup();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myBTService.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBTService.stop();
    }

    private void connectDevice(Intent data, boolean secure){

        Bundle extras = data.getExtras();

        if (extras != null){
            String address = extras.getString("Address");
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            myBTService.connect(device,secure);
        }
    }

    private void uiSetup(){

        //Recycler view Setup
        LinearLayoutManager mLinearLM = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.pattern_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLM);
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(),mLinearLM.getOrientation());
        recyclerView.addItemDecoration(itemDecor);

        List<ListPatterns> listItems = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        items = initItems(items);
        for (int i = 0; i < items.size(); i++){
            ListPatterns item = new ListPatterns(items.get(i));
            listItems.add(item);
        }
        RecyclerView.Adapter adapter = new PatternListAdapter(this, listItems, myBTService);
        recyclerView.setAdapter(adapter);

        //Setting up buttons
        Button colorOne = findViewById(R.id.color1ID);
        colorOne.setOnClickListener(this);
        Button colorTwo = findViewById(R.id.color2ID);
        colorTwo.setOnClickListener(this);
        Button colorThree = findViewById(R.id.color3ID);
        colorThree.setOnClickListener(this);
        Button colorFour = findViewById(R.id.color4ID);
        colorFour.setOnClickListener(this);
        Button colorFive = findViewById(R.id.color5ID);
        colorFive.setOnClickListener(this);

        //Setting up seekbars
        colorSeekbar = findViewById(R.id.colorSeekBarID);
        colorSeekbar.setOnSeekBarChangeListener(this);
        satSeekbar = findViewById(R.id.saturationID);
        satSeekbar.setProgress(tone);
        satSeekbar.setOnSeekBarChangeListener(this);
        timeSeekbar = findViewById(R.id.timeSeekBarID);
        timeSeekbar.setOnSeekBarChangeListener(this);
        brightnessSeekbar = findViewById(R.id.brightnessID);
        brightnessSeekbar.setProgress(brightness);
        brightnessSeekbar.setOnSeekBarChangeListener(this);

        //seting up switch
        Switch rainbowButton = findViewById(R.id.rainbowID);

        rainbowButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    colors.set(0, Commands.flagRainbow);
                    colors.set(1,0xAA);
                    colors.set(2, 0xAA);
                    colors.set(3, 0xAB);
                    colors.set(4, Commands.flagRainbowEnd);
                    myBTService.write(colors);
                }else{
                    colors.set(0, Commands.flagRainbow);
                    colors.set(1,0xAB);
                    colors.set(2, 0xAA);
                    colors.set(3, 0xAA);
                    colors.set(4, Commands.flagRainbowEnd);
                    myBTService.write(colors);
                }
            }
        });
    }

    private ArrayList<String> initItems(ArrayList<String> item){
        item.add(getString(R.string.meteor));
        item.add(getString(R.string.breathing));
        item.add(getString(R.string.catch_up));
        item.add(getString(R.string.cyclone_bounce));
        item.add(getString(R.string.fixed));
        item.add(getString(R.string.flash));
        item.add(getString(R.string.static_rainbow));
        item.add(getString(R.string.running_lights));
        item.add(getString(R.string.rainbow));
        item.add(getString(R.string.turn_off));
        return item;
    }


    @Override
    public void onClick(View v) {
        //Set the flag
        colors.set(0, Commands.flagSeek);
        switch (v.getId()){
            case R.id.color1ID:
                colors.set(1,255);
                colors.set(2, 147);
                colors.set(3, 41);
                colors = colorBrigthness(colors);
                colorSeekbar.setProgress(1385);
            break;
            case R.id.color2ID:
                colors.set(1,255);
                colors.set(2,197);
                colors.set(3, 143);
                colorSeekbar.setProgress(1334);
            break;
            case R.id.color3ID:
                colors.set(1, 0);
                colors.set(2, 255);
                colors.set(3, 247);
                colorSeekbar.setProgress(767);
                break;
            case R.id.color4ID:
                colors.set(1, 64);
                colors.set(2, 156);
                colors.set(3, 255);
                colorSeekbar.setProgress(610);
                break;
            case R.id.color5ID:
                colors.set(1, 167);
                colors.set(2,0);
                colors.set(3, 255);
                colorSeekbar.setProgress(344);
                break;
        }
        colors = colorBrigthness(colors);
        colors.set(4, Commands.flagSeekEnd);
        myBTService.write(colors);
        int rgb = convertRGB(colors);
        colorSeekbar.getProgressDrawable().setColorFilter(rgb,PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int rgb;
        if (fromUser){
            switch (seekBar.getId()){
                case R.id.colorSeekBarID:
                    colorWheel = colorSeekbar.getProgress();
                    tone = satSeekbar.getProgress();
                    //Get the combination of colors
                    colors = wheel(colorWheel, tone, Commands.flagSeek, Commands.flagSeekEnd);
                    //Update the brightness
                    colorBrigthness(colors);
                    myBTService.write(colors);
                    //Convert the colors to a int value to change the progress bar color
                    rgb = convertRGB(colors);
                    colorSeekbar.getProgressDrawable().setColorFilter(rgb,PorterDuff.Mode.MULTIPLY);
                    tagSend();
                    break;
                case R.id.brightnessID:
                    //Get the brightness value
                    brightness = brightnessSeekbar.getProgress();
                    //Save the original values
                    int redValueTemp =colors.get(1);
                    int greenValueTemp = colors.get(2);
                    int blueValueTemp = colors.get(3);
                    colorBrigthness(colors);
                    myBTService.write(colors);
                    Log.d(TAG, "Red: " + String.valueOf(colors.get(1)) + " Green: " + String.valueOf(colors.get(2)) +
                            " Blue: " + String.valueOf(colors.get(3)));
                    rgb = convertRGB(colors);
                    colorSeekbar.getProgressDrawable().setColorFilter(rgb,PorterDuff.Mode.MULTIPLY);
                    //get the original values
                    colors.set(1, redValueTemp);
                    colors.set(2, greenValueTemp);
                    colors.set(3, blueValueTemp);
                    //tagSend();

                    break;
                case R.id.timeSeekBarID:
                    //Get the time value
                    colors.set(0, Commands.flagTime);
                    colors.set(1, 0);
                    colors.set(2, timeSeekbar.getProgress());
                    colors.set(3, 0);
                    colors.set(4, Commands.flagTimeEnd);
                    myBTService.write(colors);
                    tagSend();
                    break;
                case R.id.saturationID:
                    tone = satSeekbar.getProgress();
                    colors =wheel(colorWheel, tone, Commands.flagSeek, Commands.flagSeekEnd);
                    colorBrigthness(colors);
                    rgb = convertRGB(colors);
                    colorSeekbar.getProgressDrawable().setColorFilter(rgb,PorterDuff.Mode.MULTIPLY);
                    //Add the flags
                    myBTService.write(colors);
                    tagSend();
                    break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    `//Function to get "Rainbow" colors, returns an array of RGB
    //Position- Use it to get diferrent colors  
    //Tono- Get different color tones
    //flags- use them to send different kind of commands
    private ArrayList<Integer> wheel(int position, int tono, int flag, int flagEnd){

        ArrayList<Integer> color = new ArrayList<>();
        int newPosition = position;

        color.add(flag);

        if (position < 256){
                color.add(1,255);
                color.add(2, tono);
                color.add(3, tono + position);
        }else if (position < 511){
            newPosition -= 256;
            color.add(1, 255 - newPosition);
            color.add(2, tono);
            color.add(3, tono + newPosition);
        }else if(position < 766){
            newPosition -= 511;
            color.add(1, tono);
            color.add(2, tono + newPosition);
            color.add(3,255);

        }else if(position < 1021){
            newPosition -= 766;
            color.add(1, tono);
            color.add(2, 255);
            color.add(3,255 - newPosition);

        }else if (position < 1276){
            newPosition -= 1021;
            color.add(1, tono + newPosition);
            color.add(2, 255);
            color.add(3, tono);
        }else if (position == 1530){
            color.add(1,200);
            color.add(2,200);
            color.add(3, 200);
        }else{
            newPosition -= 1276;
            color.add(1, 255);
            color.add(2, 255 - newPosition);
            color.add(3, 255);
        }
        color.add(flagEnd);
        return color;
    }

    //Change the Brigthness of the color 
    private ArrayList<Integer> colorBrigthness(ArrayList<Integer> color){
        color.set(1, (brightness * color.get(1)) / 100);
        color.set(2, (brightness * color.get(2)) / 100);
        color.set(3, (brightness * color.get(3)) / 100);
        return color;
    }

    private int convertRGB(ArrayList<Integer> convert){
        //Start adding the alpha value
        int rgb = 255;
        for (int i = 1; i < convert.size()-1; i++){
            rgb = (rgb << 8) +colors.get(i);
        }
        return rgb;
    }

    private void tagSend(){
        Log.d(TAG, String.valueOf(colorSeekbar.getProgress()));
        Log.d(TAG, String.valueOf(satSeekbar.getProgress()));
        Log.d(TAG, String.valueOf(brightnessSeekbar.getProgress()));
        Log.d(TAG, String.valueOf(timeSeekbar.getProgress()));
    }

    private void setStatus(String subTitle) {
        TextView statusView = findViewById(R.id.statusID);
        statusView.setText(subTitle);
    }
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.connected));
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(getString(R.string.connecting));
                            break;
                        case BluetoothService.STATE_NONE:
                            setStatus(getString(R.string.not_connected));
                            break;
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(MainActivity.this, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
