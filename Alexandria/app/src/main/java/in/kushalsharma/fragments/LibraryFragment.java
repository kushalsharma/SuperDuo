package in.kushalsharma.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.kushalsharma.adapters.FavouriteListAdapter;
import in.kushalsharma.alexandria.R;
import in.kushalsharma.models.Book;
import in.kushalsharma.utils.ContentProviderHelperMethods;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    private MenuCallback menuCallback;
    private Toolbar toolbar;
    private ArrayList<Book> mBookList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mGridLayoutManager;

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_library, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getResources().getString(R.string.my_library));
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuCallback != null) {
                    menuCallback.libraryMenuPressed();
                }
            }
        });


        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_library);
        mAdapter = new FavouriteListAdapter(mBookList, getActivity());
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);

        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((position + 1) % 4 == 0)
                    return 3;
                else
                    return 1;
            }
        });

        getBooksList();
        setupRecyclerView(mRecyclerView);
        return v;
    }

    private void getBooksList() {

        ArrayList<Book> list = new ArrayList<>(ContentProviderHelperMethods
                .getBookListFromDatabase(getActivity()));
        mBookList.clear();
        for (Book book : list) {
            mBookList.add(book);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getBooksList();
        mAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
    }

    public void setMenuCallBack(MenuCallback callback) {
        this.menuCallback = callback;
    }

    public interface MenuCallback {
        void libraryMenuPressed();
    }

}
