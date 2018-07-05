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

/**
 * BluetoothDevicesAdapter class is for showing Bluetooth devices names in a {@link RecyclerView} .
 */
public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.BluetoothDeviceViewHolder> {

    private Activity activity;
    private List<BluetoothDevice> deviceList;

    private OnItemClickListener onItemClickListener;

    /**
     * The default constructor for the BluetoothDevicesAdapter class.
     *
     * @param activity   The activity on which the RecyclerView is used.
     * @param deviceList The list of devices that should be displayed.
     */
    public BluetoothDevicesAdapter(Activity activity, List<BluetoothDevice> deviceList) {
        this.activity = activity;
        if (deviceList.size() == 0) Toast.makeText(activity, "THis list is empty", Toast.LENGTH_LONG).show();
        this.deviceList = deviceList;
    }

    /**
     * A setter for the event listener.
     * @param onItemClickListener An event for item clicking.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Adds new {@link BluetoothDevice} to the list and notifies the RecyclerView that a new device should be displayed.
     * @param device THe newly added string.
     */
    public void addDevice(BluetoothDevice device){
        deviceList.add(device);
        notifyDataSetChanged();
    }

    /**
     * Adds new list of devices to the list and notifies the RecyclerView that there are new devices should be displayed.
     * @param devices The list of devices that should be added.
     */
    public void addDevices(Collection<BluetoothDevice> devices){
        deviceList.addAll(devices);
        notifyDataSetChanged();
    }

    /**
     * Removes all the devices from the recycler view then displays a new list of devices.
     * @param devices The list of devices that should be added.
     */
    public void replaceDevicesList(Collection<BluetoothDevice> devices){
        deviceList.clear();
        deviceList.addAll(devices);
        notifyDataSetChanged();
    }

    /**
     * A method for getting the device at a given position.
     * @param position The position of the device.
     * @return The required device.
     */
    public BluetoothDevice get(int position){
        return deviceList.get(position);
    }

    /**
     * Override method for the event of creating a new view holder to show the string.
     * @param parent The current recyclerView's container.
     * @param viewType The type of the view (Optional usage of {@link #getItemViewType(int)}).
     * @return The created ViewHolder.
     */
    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new BluetoothDeviceViewHolder(inflater.inflate(R.layout.bluetooth_device_card, parent, false));
    }

    /**
     * Override method for the event of binding view holder (when the view holder is created and displayed).
     * @param holder The binded view holder.
     * @param position The current position of the created view holder (depends on the {@link #getItemCount()} method).
     */
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
    }

    /**
     * An override method for getting the items count which will be used in
     * the creating and binding the view holders.
     * @return The number of created views (usually the number of the list of strings).
     */
    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    /**
     * An interface for the on click event.
     */
    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    /**
     * The View holder (The container that contains the TextView that will show the device name).
     */
    public class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder{

        TextView deviceName;

        public BluetoothDeviceViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.DeviceNameTextView);
        }
    }
}
