package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * The IntroductionPagerAdapter is a class that is used to display
 * {@link Fragment} items in a {@link android.support.v4.view.ViewPager}
 */
public class IntroductionPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;

    /**
     * The default constructor for the BluetoothAdapter class.
     *
     * @param fm        The FragmantManager that should be used.
     * @param fragments The list of fragments that should be displayed.
     */
    public IntroductionPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /**
     * A method for getting the fragment at a given position (Used for showing the fragment in the ViewPager).
     * @param position The position of the fragment.
     * @return The required fragment.
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * An override method for getting the items count which will be used in
     * the creating The fragments.
     * @return The number of created views (usually the number of the list of Fragments).
     */
    @Override
    public int getCount() {
        return fragments.size();
    }
}
