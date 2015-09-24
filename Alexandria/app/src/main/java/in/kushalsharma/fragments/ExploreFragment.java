package in.kushalsharma.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.kushalsharma.adapters.BookDataAdapter;
import in.kushalsharma.alexandria.R;
import in.kushalsharma.models.Book;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.BookContentProvider;
import in.kushalsharma.utils.ContentProviderHelperMethods;
import in.kushalsharma.utils.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {

    private Toolbar toolbar;
    private MenuCallback menuCallback;
    private EditText searchText;
    private ImageButton scanButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private FloatingActionButton fab;

    private Book book;

    private ProgressBar progressBar;

    private Context context;

    private String startText = "";

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragmentDemo = new ExploreFragment();
        Bundle args = new Bundle();
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(context.getString(R.string.key_isbn), searchText.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startText = savedInstanceState.getString(context.getString(R.string.key_isbn));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.add_book));

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuCallback != null) {
                    menuCallback.menuPressed();
                }
            }
        });


        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.hide();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_explore);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        searchText = (EditText) v.findViewById(R.id.isbn_input);
        searchText.setText(startText);

        scanButton = (ImageButton) v.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ean = s.toString();
                //catch isbn10 numbers
                if (ean.length() == 10 && !ean.startsWith("978")) {
                    ean = "978" + ean;
                }
                if (ean.length() == 13) {
                    getBookData(context.getString(R.string.key_google_api_url) + ean);
                }
            }
        });

        return v;
    }

    private void getBookData(final String bookUrl) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest mRequest = new JsonObjectRequest(bookUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressBar.setVisibility(View.INVISIBLE);

                book = new Book();
                JSONArray itemArray;
                JSONObject itemObject;
                String id = null;
                String selfLink = null;
                JSONObject volumeInfoObject = null;
                try {
                    itemArray = response.getJSONArray(context.getString(R.string.key_items));
                    itemObject = itemArray.getJSONObject(0);
                    id = itemObject.getString(context.getString(R.string.key_id));
                    selfLink = itemObject.getString(context.getString(R.string.key_self_link));
                    volumeInfoObject = itemObject.getJSONObject(context.getString(R.string.key_volume_info));
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                    showSnackBar(context.getString(R.string.book_not_found_text));
                }

                if (volumeInfoObject != null) {
                    try {
                        book.setTitle(volumeInfoObject.getString(context.getString(R.string.key_title)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setTitle(context.getString(R.string.not_found_text));
                    }

                    String authors = "";
                    try {
                        JSONArray authorsArray = volumeInfoObject.getJSONArray(context.getString(R.string.key_authors));
                        if (authorsArray.length() == 1) {
                            authors = authorsArray.getString(0);
                        } else {
                            for (int i = 0; i < authorsArray.length(); i++) {
                                if (i != authorsArray.length())
                                    authors += authorsArray.getString(i) + ", ";
                                else authors += authorsArray.getString(i) + ".";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        authors = context.getString(R.string.not_found_text);
                    } finally {
                        book.setAuthors(authors);
                    }

                    try {
                        book.setPublisher(volumeInfoObject.getString(context.getString(R.string.key_publisher)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setPublisher(context.getString(R.string.not_found_text));
                    }


                    try {
                        book.setPublishedDate(volumeInfoObject.getString(context.getString(R.string.key_publish_date)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setPublishedDate(context.getString(R.string.not_found_text));
                    }

                    try {
                        book.setDescription(volumeInfoObject.getString(context.getString(R.string.key_description)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setDescription(context.getString(R.string.not_found_text));
                    }

                    try {
                        book.setPageCount(volumeInfoObject.getString(context.getString(R.string.key_page_count)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setPageCount(context.getString(R.string.not_found_text));
                    }


                    String categories = "";
                    try {
                        JSONArray categoriesArray = volumeInfoObject.getJSONArray(context.getString(R.string.key_categories));

                        if (categoriesArray.length() == 1) {
                            categories = categoriesArray.getString(0);
                        } else {
                            for (int i = 0; i < categoriesArray.length(); i++) {
                                if (i != categoriesArray.length())
                                    categories += categoriesArray.getString(i) + ", ";
                                else categories += categoriesArray.getString(i) + ".";
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        categories = context.getString(R.string.not_found_text);
                    } finally {
                        book.setCategories(categories);
                    }

                    try {
                        book.setAverageRating(volumeInfoObject.getString(context.getString(R.string.key_avg_rating)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setAverageRating(context.getString(R.string.not_found_text));
                    }
                    try {
                        book.setRatingsCount(volumeInfoObject.getString(context.getString(R.string.key_ratings_count)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setRatingsCount(context.getString(R.string.not_found_text));
                    }


                    JSONObject imageLinksObject;
                    try {
                        imageLinksObject = volumeInfoObject.getJSONObject(context.getString(R.string.key_image_links));
                        book.setSmallThumbnail(imageLinksObject.getString(context.getString(R.string.key_small_thumbnail)));
                        book.setThumbnail(imageLinksObject.getString(context.getString(R.string.key_thumbnail)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setSmallThumbnail(context.getString(R.string.empty));
                        book.setThumbnail(context.getString(R.string.empty));
                    }

                    try {
                        book.setLanguage(volumeInfoObject.getString(context.getString(R.string.key_language)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        book.setLanguage(context.getString(R.string.not_found_text));
                    }

                }
                book.setId(id);
                book.setSelfLink(selfLink);

                mAdapter = new BookDataAdapter(book, context);
                mLayoutManager = new LinearLayoutManager(context);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setVisibility(View.VISIBLE);

                boolean isBookInDB = ContentProviderHelperMethods
                        .isBookInDatabase(context,
                                String.valueOf(book.getId()));

                if (isBookInDB) {
                    fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like));
                } else {
                    fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_outline));
                }

                fab.show();

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isBookInDB = ContentProviderHelperMethods
                                .isBookInDatabase(context,
                                        String.valueOf(book.getId()));
                        if (isBookInDB) {
                            Uri contentUri = BookContentProvider.CONTENT_URI;
                            context.getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(book.getId())});
                            Snackbar.make(view, R.string.book_remove_text, Snackbar.LENGTH_LONG)
                                    .setAction(context.getString(R.string.action), null).show();
                            fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_outline));

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

                            context.getContentResolver().insert(BookContentProvider.CONTENT_URI, values);

                            Snackbar.make(view, context.getString(R.string.book_added_text), Snackbar.LENGTH_LONG)
                                    .setAction(context.getString(R.string.action), null).show();

                            fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like));
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                fab.hide();
                showSnackBar(context.getString(R.string.network_error_text));
            }
        });

        AppController.getInstance().addToRequestQueue(mRequest);
    }


    public void showSnackBar(String message) {
        final Snackbar mSnackBar = Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_INDEFINITE);
        mSnackBar.setActionTextColor(Color.WHITE);
        mSnackBar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackBar.dismiss();
            }
        });
        mSnackBar.show();
    }

    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                searchText.setText(result.getContents());
            }
        }

    }

    public void setMenuCallBack(MenuCallback callback) {
        this.menuCallback = callback;
    }

    public interface MenuCallback {
        void menuPressed();
    }
}
