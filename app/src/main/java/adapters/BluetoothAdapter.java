package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r3tr0.obdiiscantool.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by tarek on 3/13/18.
 */

/**
 * Bluetooth adapter class is for showing strings in a {@link RecyclerView} .
 */
public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothViewHolder> {

    private Context context; //The activity on which the RecyclerView is used.
    private ArrayList<String> strings; //The list of strings that should be displayed.
    private OnItemClickListener onItemClickListener; //An event for item clicking.

    /**
     * The default constructor for the BluetoothAdapter class.
     *
     * @param context The activity on which the RecyclerView is used.
     * @param strings The list of strings that should be displayed.
     */
    public BluetoothAdapter(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    /**
     * A setter for the event listener.
     * @param onItemClickListener An event for item clicking.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Adds new string to the list and notifies the RecyclerView that a new string should be displayed.
     * @param string THe newly added string.
     */
    public void add(String string) {
        strings.add(string);
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * Adds new list of strings to the list and notifies the RecyclerView that there are new strings should be displayed.
     * @param strings The list of strings that should be added.
     */
    public void addAll(Collection<String> strings) {
        this.strings.addAll(strings);
        notifyDataSetChanged();
    }

    /**
     * Removes a string at a given position.
     * @param position The position of the string.
     */
    public void remove(int position) {
        this.strings.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Clears the whole List of strings
     */
    public void clear() {
        this.strings.clear();
        notifyDataSetChanged();
    }

    /**
     * A method for getting the list of strings.
     * @return The current List of strings.
     */
    public ArrayList<String> getStrings() {
        return strings;
    }

    /**
     * A method for getting the string at a given position.
     * @param position The position of the string.
     * @return The required string.
     */
    public String getItem(int position) {
        return this.strings.get(position);
    }

    /**
     * An override method for getting the items count which will be used in
     * the creating and binding the view holders.
     *
     * @return The number of created views (usually the number of the list of strings).
     */
    @Override
    public int getItemCount() {
        return strings.size();
    }

    /**
     * Override method for the event of creating a new view holder to show the string.
     * @param parent The current recyclerView's container.
     * @param viewType The type of the view (Optional usage of {@link #getItemViewType(int)}).
     * @return The created ViewHolder.
     */
    @Override
    public BluetoothViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new BluetoothViewHolder(inflater.inflate(R.layout.bluetooth_device_card, parent, false));
    }

    /**
     * Override method for the event of binding view holder (when the view holder is created and displayed).
     * @param holder The binded view holder.
     * @param position The current position of the created view holder (depends on the {@link #getItemCount()} method).
     */
    @Override
    public void onBindViewHolder(BluetoothViewHolder holder, int position) {
        final int pos = position;
        holder.textView.setText(strings.get(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(pos, view);
                }
            });
        }
    }

    /**
     * An interface for the on click event.
     */
    public interface OnItemClickListener {
        void onClick(int position, View view);
    }

    /**
     * The View holder (The container that contains the TextView that will show the String).
     */
    public class BluetoothViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public BluetoothViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.DeviceNameTextView);
        }
    }
}
