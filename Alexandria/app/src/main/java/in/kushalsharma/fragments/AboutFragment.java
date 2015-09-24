package in.kushalsharma.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.kushalsharma.alexandria.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private MenuCallback menuCallback;
    private Toolbar toolbar;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        AboutFragment fragmentDemo = new AboutFragment();
        Bundle args = new Bundle();
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getResources().getString(R.string.about));
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuCallback != null) {
                    menuCallback.aboutMenuPressed();
                }
            }
        });

        return v;
    }

    public void setMenuCallBack(MenuCallback callback) {
        this.menuCallback = callback;
    }

    public interface MenuCallback {
        void aboutMenuPressed();
    }
}
