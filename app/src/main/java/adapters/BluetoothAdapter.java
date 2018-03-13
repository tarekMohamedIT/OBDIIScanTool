package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r3tr0.obdiiscantool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarek on 3/13/18.
 */

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothViewHolder> {

    Context context;
    ArrayList<String> strings;

    public BluetoothAdapter(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public BluetoothViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new BluetoothViewHolder(inflater.inflate(R.layout.bluetooth_device_card, parent, false));
    }

    @Override
    public void onBindViewHolder(BluetoothViewHolder holder, int position) {
        Log.e("test", strings.get(position));
        holder.textView.setText(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class BluetoothViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public BluetoothViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.DeviceNameTextView);
        }
    }
}
