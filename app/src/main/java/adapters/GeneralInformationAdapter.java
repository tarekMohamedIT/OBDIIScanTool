package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r3tr0.graphengine.core.circles.CircularProgressBar;
import com.r3tr0.obdiiscantool.R;

import java.util.ArrayList;

import models.GeneralInformationModel;

/**
 * Created by tarek on 3/13/18.
 */

public class GeneralInformationAdapter extends RecyclerView.Adapter<GeneralInformationAdapter.GeneralInformationViewHolder> {

    public static final int MODE_WITH_GAUGE = 0;
    public static final int MODE_WITHOUT_GAUGE = 1;

    private Context context;
    private ArrayList<GeneralInformationModel> generalInformationModelArrayList;

    public GeneralInformationAdapter(Context context, ArrayList<GeneralInformationModel> generalInformationModelArrayList) {
        this.context = context;
        this.generalInformationModelArrayList = generalInformationModelArrayList;
    }

    public void add(GeneralInformationModel model){
        generalInformationModelArrayList.add(model);
        notifyItemInserted(generalInformationModelArrayList.size() - 1);
    }

    public void modifyItem(int position, float value) {
        generalInformationModelArrayList.get(position).setValue(value);
        notifyItemChanged(position);
    }

    @Override
    public GeneralInformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new GeneralInformationViewHolder(inflater.inflate(R.layout.general_information_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GeneralInformationViewHolder holder, int position) {
        holder.headlineTextView.setText(generalInformationModelArrayList.get(position).getHeadline());
        holder.gauge.setTotalProgress(generalInformationModelArrayList.get(position).getMaxValue());
        holder.gauge.setCurrentProgress(generalInformationModelArrayList.get(position).getValue());
        holder.valueTextView.setText(String.format("%s/%s", generalInformationModelArrayList.get(position).getValue(), generalInformationModelArrayList.get(position).getMaxValue()));
    }

    @Override
    public int getItemCount() {
        return generalInformationModelArrayList.size();
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
