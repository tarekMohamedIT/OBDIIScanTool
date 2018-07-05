package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import events.OnItemClickListener;

/**
 * Created by r3tr0 on 4/30/18.
 */

/**
 * BluetoothDevicesAdapter class is for showing The 2 methods of input in a {@link RecyclerView} .
 */
public class MethodSelectionAdapter extends RecyclerView.Adapter<MethodSelectionAdapter.MethodViewHolder> {
    private Context context;
    private List<Integer> imagesList;

    private OnItemClickListener onItemClickListener;

    /**
     * The default constructor for the MethodSelectionAdapter class.
     *
     * @param context    The activity on which the RecyclerView is used.
     * @param imagesList The list of images that should be displayed.
     */
    public MethodSelectionAdapter(Context context, List<Integer> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    /**
     * A setter for the event listener.
     * @param onItemClickListener An event for item clicking.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Override method for the event of creating a new view holder to show the image.
     * @param parent The current recyclerView's container.
     * @param viewType The type of the view (Optional usage of {@link #getItemViewType(int)}).
     * @return The created ViewHolder.
     */
    @Override
    public MethodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FrameLayout layout = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        ImageView imageView = new ImageView(context);

        layout.addView(imageView);

        return new MethodViewHolder(layout);
    }

    /**
     * Override method for the event of binding view holder (when the view holder is created and displayed).
     * @param holder The binded view holder.
     * @param position The current position of the created view holder (depends on the {@link #getItemCount()} method).
     */
    @Override
    public void onBindViewHolder(MethodViewHolder holder, int position) {
        holder.methodImageView.setImageResource(imagesList.get(position));
        final int pos = position;

        if (onItemClickListener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(pos);
                }
            });
    }

    /**
     * An override method for getting the items count which will be used in
     * the creating and binding the view holders.
     * @return The number of created views (usually the number of the list of images).
     */
    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    /**
     * The View holder (The container that contains the ImageView that will show the method image).
     */
    public class MethodViewHolder extends RecyclerView.ViewHolder {
        ImageView methodImageView;

        public MethodViewHolder(View itemView) {
            super(itemView);

            methodImageView = (ImageView) ((FrameLayout) itemView).getChildAt(0);
        }
    }
}
