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

/**
 * RecyclerListAdapter class is for showing The {@link ListItem} in a {@link RecyclerView} .
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ListRecyclerViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ListItem> itemArrayList;
    private OnItemClickListener onItemClickListener;

    /**
     * The default constructor for the RecyclerListAdapter class.
     *
     * @param context       The activity on which the RecyclerView is used.
     * @param itemArrayList The list of ListItems that should be displayed.
     */
    public RecyclerListAdapter(Context context, ArrayList<ListItem> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    /**
     * The alternate constructor for the RecyclerListAdapter class.
     *
     * @param context The activity on which the RecyclerView is used.
     * @param items   The list of ListItems that should be displayed.
     */
    public RecyclerListAdapter(Context context, ListItem[] items) {
        this.context = context;
        this.itemArrayList = new ArrayList<>(Arrays.asList(items));
    }

    /**
     * A method for getting the Item at a given position.
     * @param position The position of the item.
     * @return The required device.
     */
    public ListItem getItem(int position) {
        return itemArrayList.get(position);
    }

    /**
     * Adds new {@link ListItem} to the list and notifies the RecyclerView that a new item should be displayed.
     * @param item THe newly added item.
     */
    public void addItem(ListItem item) {
        itemArrayList.add(item);
        notifyItemInserted(itemArrayList.size());
    }

    /**
     * Changes the item's image resource id then shows the new image.
     * @param position The position of the item
     * @param imageResID The new image resource ID.
     */
    public void changeImage(int position, int imageResID) {
        this.itemArrayList.get(position).setResImageID(imageResID);
        notifyItemChanged(position);
    }

    /**
     * Changes the item's color then shows the new color.
     * used with :
     * {@link RecyclerListAdapterMode#fullWithTitleAndSubTitleMode}.
     * {@link RecyclerListAdapterMode#partMode}.
     * @param position The position of the item
     * @param color The new color.
     */
    public void changeColor(int position, Integer color) {
        this.itemArrayList.get(position).setColor(color);
        notifyItemChanged(position);
    }

    /**
     * Changes the item's title then shows the title.
     * used with :
     * {@link RecyclerListAdapterMode#fullWithTitleAndSubTitleMode}.
     * @param position The position of the item
     * @param title The new title.
     */
    public void changeTitle(int position, String title) {
        this.itemArrayList.get(position).title = title;
        notifyItemChanged(position);
    }

    /**
     * Changes the item's sub title then shows the new sub title.
     * used with :
     * {@link RecyclerListAdapterMode#fullWithTitleAndSubTitleMode}.
     * @param position The position of the item
     * @param subtitle The new sub title.
     */
    public void changeSubTitle(int position, String subtitle) {
        this.itemArrayList.get(position).subTitle = subtitle;
        notifyItemChanged(position);
    }

    /**
     * Removes an Item at a given position.
     * @param position The position of the string.
     */
    public void removeItem(int position) {
        itemArrayList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Clears the whole List of items
     */
    public void clearItems() {
        itemArrayList.clear();
        notifyDataSetChanged();
    }


    /**
     * A method for getting the list of items.
     * @return The current List of items.
     */
    public ArrayList<ListItem> getItemArrayList() {
        return itemArrayList;
    }

    /**
     * A setter for the event listener.
     * @param onItemClickListener An event for item clicking.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Method for getting an integer indicating which view should be used.
     * @param position The position of the currently view to be created.
     * @return an integer indicating which view should be used.
     *
     * 0 => {@link RecyclerListAdapterMode#fullMode}.
     * 1 => {@link RecyclerListAdapterMode#fullWithTitleAndSubTitleMode}.
     * 2 => {@link RecyclerListAdapterMode#partMode}.
     */
    @Override
    public int getItemViewType(int position) {
        return itemArrayList.get(position).getMode().getValue();
    }

    /**
     * Override method for the event of creating a new view holder to show the item.
     * @param parent The current recyclerView's container.
     * @param viewType The type of the view (Optional usage of {@link #getItemViewType(int)}).
     * @return The created ViewHolder.
     */
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

    /**
     * Override method for the event of binding view holder (when the view holder is created and displayed).
     * @param holder The binded view holder.
     * @param position The current position of the created view holder (depends on the {@link #getItemCount()} method).
     */
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

        if (itemArrayList.get(position).getColor() != null)
            holder.itemView.setBackgroundColor(itemArrayList.get(position).color);
    }

    /**
     * An override method for getting the items count which will be used in
     * the creating and binding the view holders.
     * @return The number of created views (usually the number of the list of strings).
     */
    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    /**
     * The ListItem class that contains the item's information to be displayed.
     */
    public static class ListItem {
        String title;
        String subTitle;
        RecyclerListAdapterMode mode;
        int resImageID;
        Integer color;

        private ListItem() {
        }

        /**
         * A constructor for the Item.
         * Best used with :
         * {@link RecyclerListAdapterMode#fullMode}.
         *
         * @param mode The mode of the item.
         * @param resImageID The image to be displayed
         */
        public ListItem(RecyclerListAdapterMode mode, int resImageID) {
            this.title = "";
            this.subTitle = "";
            this.mode = mode;
            this.resImageID = resImageID;
            this.color = null;
        }

        /**
         * A constructor for the Item.
         * Best used with :
         * {@link RecyclerListAdapterMode#fullMode}.
         * {@link RecyclerListAdapterMode#partMode}.
         *
         * @param mode The mode of the item.
         * @param resImageID The image to be displayed
         * @param color The background color of the view
         */
        public ListItem(RecyclerListAdapterMode mode, int resImageID, Integer color) {
            this.mode = mode;
            this.resImageID = resImageID;
            this.color = color;
        }

        /**
         * A constructor for the Item.
         * Best used with :
         * {@link RecyclerListAdapterMode#fullWithTitleAndSubTitleMode}.
         * {@link RecyclerListAdapterMode#partMode}.
         *
         * @param title The title of the item.
         * @param subTitle The sub title of the item.
         * @param mode The mode of the item.
         * @param resImageID The image to be displayed.
         */
        public ListItem(String title, String subTitle, RecyclerListAdapterMode mode, int resImageID) {
            this.title = title;
            this.subTitle = subTitle;
            this.mode = mode;
            this.resImageID = resImageID;
            this.color = null;
        }

        /**
         * A constructor for the Item.
         * Best used with :
         * {@link RecyclerListAdapterMode#fullWithTitleAndSubTitleMode}.
         * {@link RecyclerListAdapterMode#partMode}.
         *
         * @param title The title of the item.
         * @param subTitle The sub title of the item.
         * @param mode The mode of the item.
         * @param resImageID The image to be displayed.
         * @param color The background color of the view.
         */
        public ListItem(String title, String subTitle, RecyclerListAdapterMode mode, int resImageID, Integer color) {
            this.title = title;
            this.subTitle = subTitle;
            this.mode = mode;
            this.resImageID = resImageID;
            this.color = color;
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

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public Integer getColor() {
            return color;
        }

        public void setColor(Integer color) {
            this.color = color;
        }
    }

    /**
     * The View holder (The container that contains the views that will show the item's data).
     */
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
