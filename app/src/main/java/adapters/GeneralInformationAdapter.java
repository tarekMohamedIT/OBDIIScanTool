package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r3tr0.graphengine.core.circles.CircularProgressBar;
import com.r3tr0.obdiiscantool.R;

import java.util.ArrayList;

import communications.ObdService;
import models.GeneralInformation;

/**
 * Created by tarek on 3/13/18.
 */

/**
 * GeneralInformationAdapter class is for showing The general information names and values in a {@link RecyclerView} .
 */
public class GeneralInformationAdapter extends RecyclerView.Adapter<GeneralInformationAdapter.GeneralInformationViewHolder> {

    private Context context;
    private ArrayList<GeneralInformation> generalInformationArrayList;

    /**
     * The default constructor for the GeneralInformationAdapter class.
     *
     * @param context                     The activity on which the RecyclerView is used.
     * @param generalInformationArrayList The list of general information items that should be displayed.
     */
    public GeneralInformationAdapter(Context context, ArrayList<GeneralInformation> generalInformationArrayList) {
        this.context = context;
        this.generalInformationArrayList = generalInformationArrayList;
    }

    /**
     * Adds new {@link GeneralInformation} to the list and notifies the RecyclerView that a new item should be displayed.
     * @param model THe newly added item.
     */
    public void add(GeneralInformation model) {
        generalInformationArrayList.add(model);
        notifyItemInserted(generalInformationArrayList.size() - 1);
    }

    /**
     * Removes all the items from the recycler view then displays a new list of items.
     * @param models The list of items that should be added.
     */
    public void replaceAll(ArrayList<GeneralInformation> models) {
        generalInformationArrayList.clear();
        generalInformationArrayList.addAll(models);
        modifyItems();
    }

    /**
     *A method used in simulation mode to regenerate the random values.
     */
    public void modifyItems() {
        notifyDataSetChanged();
    }

    /**
     * Replaces the item in a position with another
     * @param position The position of the item that you want to change.
     * @param newItem The item that should be replaced with.
     */
    public void modifyItemAt(int position, GeneralInformation newItem) {
        this.generalInformationArrayList.set(position, newItem);
        notifyItemChanged(position);
    }

    /**
     * A method for getting the list of items.
     * @return The current List of items.
     */
    public ArrayList<GeneralInformation> getGeneralInformationArrayList() {
        return generalInformationArrayList;
    }

    /**
     * Override method for the event of creating a new view holder to show the item.
     * @param parent The current recyclerView's container.
     * @param viewType The type of the view (Optional usage of {@link #getItemViewType(int)}).
     * @return The created ViewHolder.
     */
    @Override
    public GeneralInformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new GeneralInformationViewHolder(inflater.inflate(R.layout.general_information_item, parent, false));
    }

    /**
     * Override method for the event of binding view holder (when the view holder is created and displayed).
     * @param holder The binded view holder.
     * @param position The current position of the created view holder (depends on the {@link #getItemCount()} method).
     */
    @Override
    public void onBindViewHolder(GeneralInformationViewHolder holder, int position) {
        final GeneralInformationViewHolder tmp = holder;

        if (!ObdService.isReadingRealData) { //Simulation mode.
            holder.headlineTextView.setText(generalInformationArrayList.get(position).getHeadline());
            holder.gauge.setCurrentProgress(0);
            Log.e("onBind", "max : " + generalInformationArrayList.get(position).getMaxValue() + ", Min : 0");
            Log.e("onBind2", "items : " + generalInformationArrayList.get(position).getValues().toString());

            holder.gauge.setTotalProgress(generalInformationArrayList.get(position).getMaxValue());
            float val = generalInformationArrayList.get(position).generateValue();
            holder.gauge.setCurrentProgress(val);
            holder.valueTextView.setText(String.format("%s/%s", val, generalInformationArrayList.get(position).getMaxValue()));
        } else { // real-time mode
            holder.headlineTextView.setText(generalInformationArrayList.get(position).getHeadline());
            holder.gauge.setCurrentProgress(0);
            holder.gauge.setTotalProgress(generalInformationArrayList.get(position).getUsedValue());
            holder.gauge.setCurrentProgress(generalInformationArrayList.get(position).getUsedValue());
            holder.valueTextView.setText(String.format("%s", generalInformationArrayList.get(position).getUsedValue()));
        }
    }

    /**
     * An override method for getting the items count which will be used in
     * the creating and binding the view holders.
     * @return The number of created views (usually the number of the list of strings).
     */
    @Override
    public int getItemCount() {
        return generalInformationArrayList.size();
    }

    /**
     * The View holder (The container that contains the TextView that will show the item's data).
     */
    public class GeneralInformationViewHolder extends RecyclerView.ViewHolder{

        TextView headlineTextView;
        CircularProgressBar gauge;
        TextView valueTextView;

        public GeneralInformationViewHolder(View itemView) {
            super(itemView);
            headlineTextView = itemView.findViewById(R.id.headlineTextView);
            gauge = itemView.findViewById(R.id.gaugeProgressBar);
            valueTextView = itemView.findViewById(R.id.ValueTextView);
        }
    }
}
