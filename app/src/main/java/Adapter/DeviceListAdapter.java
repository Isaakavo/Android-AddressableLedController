package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avocado.ws2812bledcontroller.MainActivity;
import com.avocado.ws2812bledcontroller.R;

import java.util.List;

import Model.ListDevices;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceListAdapter extends RecyclerView.Adapter <DeviceListAdapter.ViewHolder> {

    private Context context;
    private List<ListDevices> devices;

    public DeviceListAdapter(Context context, List<ListDevices> devices) {
        this.context = context;
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_device,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListAdapter.ViewHolder holder, int position) {
        ListDevices item = devices.get(position);
        holder.deviceName.setText(item.getDeviceName());
        holder.deviceAddress.setText(item.getDeviceAddress());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView deviceName;
        public TextView deviceAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            deviceName = itemView.findViewById(R.id.device_name_ID);
            deviceAddress = itemView.findViewById(R.id.device_address_ID);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            ListDevices buttonDevices = devices.get(position);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("Address", buttonDevices.getDeviceAddress());
            intent.putExtra("Name", buttonDevices.getDeviceName());

            context.startActivity(intent);
        }
    }
}
