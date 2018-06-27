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

public class GeneralInformationAdapter extends RecyclerView.Adapter<GeneralInformationAdapter.GeneralInformationViewHolder> {

    public static final int MODE_WITH_GAUGE = 0;
    public static final int MODE_WITHOUT_GAUGE = 1;


    private Context context;
    private ArrayList<GeneralInformation> generalInformationArrayList;

    public GeneralInformationAdapter(Context context, ArrayList<GeneralInformation> generalInformationArrayList) {
        this.context = context;
        this.generalInformationArrayList = generalInformationArrayList;
    }

    public void add(GeneralInformation model) {
        generalInformationArrayList.add(model);
        notifyItemInserted(generalInformationArrayList.size() - 1);
    }

    public void replaceAll(ArrayList<GeneralInformation> models) {
        generalInformationArrayList.clear();
        generalInformationArrayList.addAll(models);
        modifyItems();
    }

    public void modifyItems() {
        notifyDataSetChanged();
    }

    public void modifyItemAt(int position, GeneralInformation newItem) {
        this.generalInformationArrayList.set(position, newItem);
        notifyItemChanged(position);
    }

    public ArrayList<GeneralInformation> getGeneralInformationArrayList() {
        return generalInformationArrayList;
    }

    @Override
    public GeneralInformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new GeneralInformationViewHolder(inflater.inflate(R.layout.general_information_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GeneralInformationViewHolder holder, int position) {
        final GeneralInformationViewHolder tmp = holder;

        if (!ObdService.isReadingRealData) {
            holder.headlineTextView.setText(generalInformationArrayList.get(position).getHeadline());
            holder.gauge.setCurrentProgress(0);
            Log.e("onBind", "max : " + generalInformationArrayList.get(position).getMaxValue() + ", Min : 0");
            Log.e("onBind2", "items : " + generalInformationArrayList.get(position).getValues().toString());

            holder.gauge.setTotalProgress(generalInformationArrayList.get(position).getMaxValue());
            float val = generalInformationArrayList.get(position).generateValue();
            holder.gauge.setCurrentProgress(val);
            holder.valueTextView.setText(String.format("%s/%s", val, generalInformationArrayList.get(position).getMaxValue()));
        } else {
            holder.headlineTextView.setText(generalInformationArrayList.get(position).getHeadline());
            holder.gauge.setCurrentProgress(0);
            holder.gauge.setTotalProgress(generalInformationArrayList.get(position).getUsedValue());
            holder.gauge.setCurrentProgress(generalInformationArrayList.get(position).getUsedValue());
            holder.valueTextView.setText(String.format("%s", generalInformationArrayList.get(position).getUsedValue()));
        }
    }

    @Override
    public int getItemCount() {
        return generalInformationArrayList.size();
    }

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
