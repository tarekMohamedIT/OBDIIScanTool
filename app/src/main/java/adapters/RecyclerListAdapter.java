package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tarek on 1/5/18.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ListRecyclerViewHolder> {

    public static final int MODE_FULL = 0;
    public static final int MODE_PART = 1;

    private Context context;
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
        return itemArrayList.get(position).getMode();
    }

    @Override
    public ListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup layout;
        ImageView imageView = new ImageView(context);

        if (viewType == MODE_FULL) {
            layout = new RelativeLayout(context);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            layout.addView(imageView);

        } else {
            layout = new RelativeLayout(context);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            layout.addView(imageView);
        }
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

        return new ListRecyclerViewHolder(layout, viewType);
    }

    @Override
    public void onBindViewHolder(ListRecyclerViewHolder holder, int position) {
        holder.imageView.setBackgroundResource(itemArrayList.get(position).resImageID);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ListRecyclerViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView imageView;

        public ListRecyclerViewHolder(View itemView) {
            super(itemView);
            view = ((FrameLayout) itemView).getChildAt(0);
        }

        public ListRecyclerViewHolder(View itemView, int mode) {
            super(itemView);
            view = ((RelativeLayout) itemView).getChildAt(1);
            imageView = (ImageView) ((RelativeLayout) itemView).getChildAt(0);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public static class ListItem {
        int mode;
        int resImageID;
        int fontSize = 0;

        public ListItem(int mode, int resImageID) {
            this.mode = mode;
            this.resImageID = resImageID;
        }

        public ListItem(int mode, int resImageID, int fontSize) {
            this.mode = mode;
            this.resImageID = resImageID;
            this.fontSize = fontSize;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getResImageID() {
            return resImageID;
        }

        public void setResImageID(int resImageID) {
            this.resImageID = resImageID;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }
    }
}
