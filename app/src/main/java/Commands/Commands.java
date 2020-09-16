package Commands;

import android.content.res.Resources;

import com.avocado.ws2812bledcontroller.R;

import java.util.ArrayList;

public class Commands {
    public final static int flag = 0x38;
    public final static int flagEnd = 0x30;
    public final static int flagSeek = 0x39;
    public final static int flagSeekEnd = 0x31;
    public final static int flagTime = 0x40;
    public final static int flagTimeEnd = 0x32;
    public final static int flagRainbow = 0x41;
    public final static int flagRainbowEnd = 0x42;
    ArrayList<Integer> data = new ArrayList<>();
    public Commands() {
        for (int i=0; i < 5; i++){
            data.add(0);
        }
    }

    public ArrayList<Integer> getPatternCommand(String pattern){

        data.set(0, flag);
        switch (pattern){
            case "Color Wipe":
                data.set(1, 0x42);
                data.set(2, 0x43);
                data.set(3, 0x44);
                //data = new int[] {flag,0x42,0x43,0x44,flagEnd};
                break;
            case "Fade In":
                data.set(1, 0x45);
                data.set(2, 0x46);
                data.set(3, 0x47);
                //data = new int[] {flag,0x45,0x46,0x47,flagEnd};
                break;
            case "Patron 3":
                data.set(1, 0x48);
                data.set(2, 0x49);
                data.set(3, 0x50);
                //data = new int[] {flag,0x48,0x49,0x50,flagEnd};
                break;
            case "Cyclone Bounce":
                data.set(1, 0x51);
                data.set(2, 0x52);
                data.set(3, 0x53);
                //data = new int[] {flag,0x51,0x52,0x53,flagEnd};
                break;
            case "Twinkle":
                data.set(1, 0x54);
                data.set(2, 0x55);
                data.set(3, 0x56);
                //data = new int[] {flag,0x54,0x55,0x56,flagEnd};
                break;
            case "Running Lights":
                data.set(1, 0x56);
                data.set(2, 0x58);
                data.set(3, 0x59);
                //data = new int[] {flag,0x57,0x58,0x59,flagEnd};
                break;
            case "Theater Chase":
                data.set(1, 0x60);
                data.set(2, 0x61);
                data.set(3, 0x62);
                //data = new int[] {flag,0xFF,0xFF,0xFF,flagEnd};
                break;
            case "Meteor rain":
                data.set(1, 0x63);
                data.set(2, 0x64);
                data.set(3, 0x65);
                //data = new int[] {flag,0x60,0x61,0x62,flagEnd};
                break;
            case "Rainbow":
                data.set(1, 0x66);
                data.set(2, 0x67);
                data.set(3, 0x68);
                //data = new int[] {flag,0x63,0x64,0x65,flagEnd};
                break;
            /*case "Disconnect":
                data = new byte[] {flag,0x45,0x46,0x47,flagEnd};
                break;*/
            case "Turn Off":
                data.set(1, 255);
                data.set(2, 255);
                data.set(3, 255);
                break;
        }
        data.set(4, flagEnd);
        return data;
    }
}
