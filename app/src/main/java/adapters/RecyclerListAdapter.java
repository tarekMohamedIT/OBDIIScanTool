package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.r3tr0.obdiiscantool.R;

import java.util.ArrayList;
import java.util.Arrays;

import enums.RecyclerListAdapterMode;
import events.OnItemClickListener;

/**
 * Created by tarek on 1/5/18.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ListRecyclerViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ListItem> itemArrayList;
    private OnItemClickListener onItemClickListener;

    public RecyclerListAdapter(Context context, ArrayList<ListItem> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    public RecyclerListAdapter(Context context, ListItem[] strings) {
        this.context = context;
        this.itemArrayList = new ArrayList<>(Arrays.asList(strings));
    }

    public ListItem getItem(int position) {
        return itemArrayList.get(position);
    }

    public void addItem(ListItem item) {
        itemArrayList.add(item);
        notifyItemInserted(itemArrayList.size());
    }

    public void addItem(ListItem item, int position) {
        itemArrayList.add(position, item);
        notifyItemInserted(position);
    }

    public void changeImage(int position, int imageResID) {
        this.itemArrayList.get(position).setResImageID(imageResID);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        itemArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearItems() {
        itemArrayList.clear();
        notifyDataSetChanged();
    }


    public ArrayList<ListItem> getItemArrayList() {
        return itemArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return itemArrayList.get(position).getMode().getValue();
    }

    @NonNull
    @Override
    public ListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        if (viewType == 0) {//full
            view = inflater.inflate(R.layout.item_full, parent, false);
        } else if (viewType == 1) {//full with title and sub title
            view = inflater.inflate(R.layout.item_full_title_subtitle, parent, false);
        } else {//part
            view = inflater.inflate(R.layout.item_part, parent, false);
        }

        return new ListRecyclerViewHolder(view, viewType);

/*
        else {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10, 0,10);
            layout.setLayoutParams(params);

            ((LinearLayout)layout).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout)layout).setWeightSum(8f);

            view = new EditText(context);
            view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7f));
            layout.addView(view);

            imageView = new ImageView(context);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            imageView.setImageResource(R.drawable.ic_delete_vector);
            layout.addView(imageView);
        }*/

    }

    @Override
    public void onBindViewHolder(ListRecyclerViewHolder holder, int position) {

        holder.imageView.setBackgroundResource(itemArrayList.get(position).resImageID);
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) onItemClickListener.onClick(pos);
            }
        });

        if (itemArrayList.get(position).mode == RecyclerListAdapterMode.fullWithTitleAndSubTitleMode) {
            holder.title.setText(itemArrayList.get(pos).title);
            holder.subtitle.setText(itemArrayList.get(pos).subTitle);
        }
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class ListItem {
        String title;
        String subTitle;
        RecyclerListAdapterMode mode;
        int resImageID;

        private ListItem() {

        }

        public ListItem(String title, String subTitle, RecyclerListAdapterMode mode, int resImageID) {
            this.title = title;
            this.subTitle = subTitle;
            this.mode = mode;
            this.resImageID = resImageID;
        }

        public ListItem(RecyclerListAdapterMode mode, int resImageID) {
            this.title = "";
            this.subTitle = "";
            this.mode = mode;
            this.resImageID = resImageID;
        }

        public RecyclerListAdapterMode getMode() {
            return mode;
        }

        public void setMode(RecyclerListAdapterMode mode) {
            this.mode = mode;
        }

        public int getResImageID() {
            return resImageID;
        }

        public void setResImageID(int resImageID) {
            this.resImageID = resImageID;
        }

        public String getTitle() {
            return title;
        }

        public String getSubTitle() {
            return subTitle;
        }
    }

    public class ListRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView subtitle;


        public ListRecyclerViewHolder(View itemView, int mode) {
            super(itemView);
            imageView = (ImageView) ((ViewGroup) itemView).getChildAt(0);

            if (mode == RecyclerListAdapterMode.fullWithTitleAndSubTitleMode.getValue()) {
                title = itemView.findViewById(R.id.titleTextview);
                subtitle = itemView.findViewById(R.id.subtitleTextView);
            }
        }
    }
}
