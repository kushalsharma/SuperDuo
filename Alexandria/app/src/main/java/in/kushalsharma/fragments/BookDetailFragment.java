package in.kushalsharma.fragments;

import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.kushalsharma.adapters.BookDataAdapter;
import in.kushalsharma.alexandria.R;
import in.kushalsharma.models.Book;
import in.kushalsharma.utils.BookContentProvider;
import in.kushalsharma.utils.ContentProviderHelperMethods;
import in.kushalsharma.utils.DatabaseHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookDetailFragment extends Fragment {

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private CoordinatorLayout coordinatorLayout;

    private FloatingActionButton fab;

    private Book book;
    private String bookId;

    public BookDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book_detail, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        bookId = getActivity().getIntent().getStringExtra(getActivity().getString(R.string.key_id));

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_book_detail);
        mLayoutManager = new LinearLayoutManager(getActivity());
        getBookDataFromID(bookId);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    private void getBookDataFromID(final String id) {
        book = ContentProviderHelperMethods.getBookFromDatabase(getActivity(), id);
        mAdapter = new BookDataAdapter(book, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        toolbar.setTitle(book.getTitle());
        toolbar.setTitleTextColor(Color.WHITE);

        boolean isMovieInDB = ContentProviderHelperMethods
                .isBookInDatabase(getActivity(), book.getId());
        if (isMovieInDB) {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isBookInDB = ContentProviderHelperMethods
                        .isBookInDatabase(getActivity(), book.getId());
                if (isBookInDB) {
                    Uri contentUri = BookContentProvider.CONTENT_URI;
                    getActivity().getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(book.getId())});
                    Snackbar.make(coordinatorLayout, R.string.book_removed_text, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));

                } else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.KEY_ID, book.getId());
                    values.put(DatabaseHelper.KEY_SELF_LINK, book.getTitle());
                    values.put(DatabaseHelper.KEY_TITLE, book.getTitle());
                    values.put(DatabaseHelper.KEY_AUTHORS, book.getAuthors());
                    values.put(DatabaseHelper.KEY_PUBLISHER, book.getPublisher());
                    values.put(DatabaseHelper.KEY_PUBLISH_DATE, book.getPublishedDate());
                    values.put(DatabaseHelper.KEY_DESCRIPTION, book.getDescription());
                    values.put(DatabaseHelper.KEY_PAGE_COUNT, book.getPageCount());
                    values.put(DatabaseHelper.KEY_CATEGORIES, book.getCategories());
                    values.put(DatabaseHelper.KEY_AVERAGE_RATING, book.getAverageRating());
                    values.put(DatabaseHelper.KEY_RATINGS_COUNT, book.getRatingsCount());
                    values.put(DatabaseHelper.KEY_SMALL_THUMBNAIL, book.getSmallThumbnail());
                    values.put(DatabaseHelper.KEY_THUMBNAIL, book.getThumbnail());
                    values.put(DatabaseHelper.KEY_LANGUAGE, book.getLanguage());

                    getActivity().getContentResolver().insert(BookContentProvider.CONTENT_URI, values);

                    Snackbar.make(coordinatorLayout, R.string.book_added_text, Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.action), null).show();

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
                }
            }
        });

    }
}
