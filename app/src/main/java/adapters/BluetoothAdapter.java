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

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothViewHolder> {

    private Context context;
    private ArrayList<String> strings;
    private OnItemClickListener onItemClickListener;

    public BluetoothAdapter(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(String string) {
        strings.add(string);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addAll(Collection<String> strings) {
        this.strings.addAll(strings);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.strings.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        this.strings.clear();
        notifyDataSetChanged();
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public String getItem(int position) {
        return this.strings.get(position);
    }

    @Override
    public BluetoothViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new BluetoothViewHolder(inflater.inflate(R.layout.bluetooth_device_card, parent, false));
    }

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

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public interface OnItemClickListener {
        public void onClick(int position, View view);
    }

    public class BluetoothViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public BluetoothViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.DeviceNameTextView);
        }
    }
}
