package adapters;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.r3tr0.obdiiscantool.R;

import java.util.Collection;
import java.util.List;

/**
 * Created by tarek on 12/18/17.
 */

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.BluetoothDeviceViewHolder> {

    private Activity activity;
    private List<BluetoothDevice> deviceList;

    private OnItemClickListener onItemClickListener;

    public BluetoothDevicesAdapter(Activity activity, List<BluetoothDevice> deviceList) {
        this.activity = activity;
        if (deviceList.size() == 0) Toast.makeText(activity, "THis list is empty", Toast.LENGTH_LONG).show();
        this.deviceList = deviceList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addDevice(BluetoothDevice device){
        deviceList.add(device);
        notifyDataSetChanged();
    }

    public void addDevices(Collection<BluetoothDevice> devices){
        deviceList.addAll(devices);
        notifyDataSetChanged();
    }

    public void replaceDevicesList(Collection<BluetoothDevice> devices){
        deviceList.clear();
        deviceList.addAll(devices);
        notifyDataSetChanged();
    }

    public BluetoothDevice get(int position){
        return deviceList.get(position);
    }

    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new BluetoothDeviceViewHolder(inflater.inflate(R.layout.bluetooth_device_card, parent, false));
    }

    @Override
    public void onBindViewHolder(BluetoothDeviceViewHolder holder, int position) {

        if (onItemClickListener != null){
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, pos);
                }
            });
        }
        holder.deviceName.setText(deviceList.get(position).getName());
        holder.macAddressName.setText(deviceList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder{

        TextView deviceName;
        TextView macAddressName;

        public BluetoothDeviceViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.DeviceNameTextView);
            macAddressName = itemView.findViewById(R.id.DeviceMacTextView);
        }
    }

    public interface OnItemClickListener{
        void onClick(View view, int position);
    }
}
