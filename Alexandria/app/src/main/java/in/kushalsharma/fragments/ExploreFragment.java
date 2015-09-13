package in.kushalsharma.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

    private Book book;

    private ProgressBar progressBar;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuCallback != null) {
                    menuCallback.menuPressed();
                }
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_explore);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        searchText = (EditText) v.findViewById(R.id.isbn_input);
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
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
                String ean = s.toString();
                //catch isbn10 numbers
                if (ean.length() == 10 && !ean.startsWith("978")) {
                    ean = "978" + ean;
                }
                if (ean.length() == 13) {
                    getBookData("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ean);
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

                try {
                    JSONArray itemArray = response.getJSONArray("items");
                    JSONObject itemObject = itemArray.getJSONObject(0);

                    String id = itemObject.getString("id");
                    String selfLink = itemObject.getString("selfLink");

                    JSONObject volumeInfoObject = itemObject.getJSONObject("volumeInfo");
                    String title = volumeInfoObject.getString("title");

                    JSONArray authorsArray = volumeInfoObject.getJSONArray("authors");
                    String authors = "";

                    if (authorsArray.length() == 1) {
                        authors = authorsArray.getString(0);
                    } else {
                        for (int i = 0; i < authorsArray.length(); i++) {
                            if (i != authorsArray.length())
                                authors += authorsArray.getString(i) + ", ";
                            else authors += authorsArray.getString(i) + ".";
                        }
                    }

                    String publisher = volumeInfoObject.getString("publisher");
                    String publishDate = volumeInfoObject.getString("publishedDate");
                    String description = volumeInfoObject.getString("description");
                    String pageCount = volumeInfoObject.getString("pageCount");

                    JSONArray categoriesArray = volumeInfoObject.getJSONArray("categories");
                    String categories = "";

                    if (categoriesArray.length() == 1) {
                        categories = categoriesArray.getString(0);
                    } else {
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            if (i != categoriesArray.length())
                                categories += categoriesArray.getString(i) + ", ";
                            else categories += categoriesArray.getString(i) + ".";
                        }
                    }

                    String averageRating = volumeInfoObject.getString("averageRating");
                    String ratingsCount = volumeInfoObject.getString("ratingsCount");

                    JSONObject imageLinksObject = volumeInfoObject.getJSONObject("imageLinks");

                    String smallThumbnail = imageLinksObject.getString("smallThumbnail");
                    String thumbnail = imageLinksObject.getString("thumbnail");

                    String language = volumeInfoObject.getString("language");

                    book = new Book();
                    book.setId(id);
                    book.setSelfLink(selfLink);
                    book.setTitle(title);
                    book.setAuthors(authors);
                    book.setPublisher(publisher);
                    book.setPublishedDate(publishDate);
                    book.setDescription(description);
                    book.setPageCount(pageCount);
                    book.setCategories(categories);
                    book.setAverageRating(averageRating);
                    book.setRatingsCount(ratingsCount);
                    book.setSmallThumbnail(smallThumbnail);
                    book.setThumbnail(thumbnail);
                    book.setLanguage(language);

                    mAdapter = new BookDataAdapter(book, getActivity());
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                    showSnackBar("Something went wrong! Book not found.");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                showSnackBar("Something went wrong! Please check the internet connection and try again.");
            }
        });

        AppController.getInstance().addToRequestQueue(mRequest);
    }


    public void showSnackBar(String message) {
        final Snackbar mSnackBar = Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_INDEFINITE);
        mSnackBar.setActionTextColor(Color.parseColor("#FFFFFF"));
        mSnackBar.setAction("Okay", new View.OnClickListener() {
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
                Log.e("Scanned from fragment: ", "cancel");
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
