package fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.r3tr0.obdiiscantool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionFragment extends Fragment {
    int color;
    int resImageID;
    String description;

    public IntroductionFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public IntroductionFragment(int color, int resImageID, String description) {
        this.color = color;
        this.resImageID = resImageID;
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_introduction, container, false);

        view.setBackgroundColor(color);
        ((ImageView) view.findViewById(R.id.descriptionImageView)).setImageResource(resImageID);
        ((TextView) view.findViewById(R.id.descriptionTextView)).setText(description);

        return view;
    }

}
