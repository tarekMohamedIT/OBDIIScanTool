package dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.r3tr0.obdiiscantool.R;

import java.util.ArrayList;
import java.util.List;

import adapters.MethodSelectionAdapter;
import events.OnItemClickListener;

/**
 * Created by r3tr0 on 4/30/18.
 */

public class MethodSelectionDialog {
    private Context context;
    private AlertDialog dialog;
    private RecyclerView recyclerView;

    public MethodSelectionDialog(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;

        FrameLayout frameLayout = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);

        recyclerView = new RecyclerView(context);

        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        frameLayout.addView(recyclerView);

        dialog = new AlertDialog.Builder(context).setView(frameLayout).create();

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_json_select);
        images.add(R.drawable.ic_bluetooth_select);

        MethodSelectionAdapter adapter = new MethodSelectionAdapter(context, images);
        if (onItemClickListener != null)
            adapter.setOnItemClickListener(onItemClickListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }


}
