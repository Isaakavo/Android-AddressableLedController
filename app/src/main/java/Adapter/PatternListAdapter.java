package Adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.avocado.ws2812bledcontroller.R;


import java.util.List;

import Bluetooth.BluetoothService;
import Model.ListPatterns;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatternListAdapter extends RecyclerView.Adapter <PatternListAdapter.ViewHolder> {

    private Context context;
    private List<ListPatterns> patterns;

    private BluetoothAdapter bluetoothAdapter;

    public PatternListAdapter(Context context, List<ListPatterns> patterns) {
        this.context = context;
        this.patterns = patterns;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pattern_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatternListAdapter.ViewHolder holder, int position) {
        ListPatterns item = patterns.get(position);
        holder.patternView.setText(item.getPatternName());
    }

    @Override
    public int getItemCount() {
        return patterns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView patternView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            patternView = itemView.findViewById(R.id.patternID);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            ListPatterns patternButton = patterns.get(position);

        }
    }
}
