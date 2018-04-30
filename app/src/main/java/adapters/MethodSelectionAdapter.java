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

public class MethodSelectionAdapter extends RecyclerView.Adapter<MethodSelectionAdapter.MethodViewHolder> {
    private Context context;
    private List<Integer> imagesList;

    private OnItemClickListener onItemClickListener;

    public MethodSelectionAdapter(Context context, List<Integer> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MethodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FrameLayout layout = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        ImageView imageView = new ImageView(context);

        layout.addView(imageView);

        return new MethodViewHolder(layout);
    }

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

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MethodViewHolder extends RecyclerView.ViewHolder {
        ImageView methodImageView;

        public MethodViewHolder(View itemView) {
            super(itemView);

            methodImageView = (ImageView) ((FrameLayout) itemView).getChildAt(0);
        }
    }
}
