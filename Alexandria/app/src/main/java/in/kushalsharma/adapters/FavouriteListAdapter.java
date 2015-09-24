package in.kushalsharma.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.kushalsharma.alexandria.BookDetailActivity;
import in.kushalsharma.alexandria.R;
import in.kushalsharma.models.Book;
import in.kushalsharma.utils.AppController;

public class FavouriteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Book> mBookList = new ArrayList<>();
    private Activity mAct;
    private LayoutInflater mInflater;

    public FavouriteListAdapter(ArrayList<Book> mBookList, Activity activity) {
        this.mBookList = mBookList;
        this.mAct = activity;

        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == 2) {
            View v = mInflater.inflate(R.layout.layout_holder_book_small, parent, false);
            vh = new ViewHolderSmall(v);
        } else {
            View v = mInflater.inflate(R.layout.layout_holder_book_large, parent, false);
            vh = new ViewHolderLarge(v);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 4 == 0)
            return 1;
        else
            return 2;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 2:
                ((ViewHolderSmall) holder).getImageView().setImageUrl(mBookList.get(position).getThumbnail(), AppController.getInstance().getImageLoader());
                ((ViewHolderSmall) holder).getTitleView().setText(mBookList.get(position).getTitle());

//                ((ViewHolderSmall) holder).getToolbar().getMenu().clear();
//                ((ViewHolderSmall) holder).getToolbar().inflateMenu(R.menu.card_toolbar_menu);

                ((ViewHolderSmall) holder).getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mBookDetailIntent = new Intent(mAct, BookDetailActivity.class);
                        mBookDetailIntent.putExtra(mAct.getString(R.string.key_id), mBookList.get(position).getId());
                        mAct.startActivity(mBookDetailIntent);
                    }
                });

                break;
            case 1:
                ((ViewHolderLarge) holder).getImageView().setImageUrl(mBookList.get(position).getThumbnail(), AppController.getInstance().getImageLoader());
                ((ViewHolderLarge) holder).getTitleView().setText(mBookList.get(position).getTitle());
                ((ViewHolderLarge) holder).getOverviewView().setText(mBookList.get(position).getDescription());
                ((ViewHolderLarge) holder).getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mBookDetailIntent = new Intent(mAct, BookDetailActivity.class);
                        mBookDetailIntent.putExtra(mAct.getString(R.string.key_id), mBookList.get(position).getId());
                        mAct.startActivity(mBookDetailIntent);
                    }
                });
                ((ViewHolderLarge) holder).getReadMoreView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mBookDetailIntent = new Intent(mAct, BookDetailActivity.class);
                        mBookDetailIntent.putExtra(mAct.getString(R.string.key_id), mBookList.get(position).getId());
                        mAct.startActivity(mBookDetailIntent);
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public static class ViewHolderSmall extends RecyclerView.ViewHolder {

        private NetworkImageView imageView;
        private TextView titleView;
//        private Toolbar toolbar;

        public ViewHolderSmall(View v) {
            super(v);
            imageView = (NetworkImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
//            toolbar = (Toolbar) v.findViewById(R.id.card_toolbar);
        }

        public NetworkImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

//        public Toolbar getToolbar() {
//            return toolbar;
//        }
    }

    public static class ViewHolderLarge extends RecyclerView.ViewHolder {

        private NetworkImageView imageView;
        private TextView titleView, overviewView, readMoreView;

        public ViewHolderLarge(View v) {
            super(v);
            imageView = (NetworkImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
            overviewView = (TextView) v.findViewById(R.id.overview);
            readMoreView = (TextView) v.findViewById(R.id.read_more);
        }

        public NetworkImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getOverviewView() {
            return overviewView;
        }

        public TextView getReadMoreView() {
            return readMoreView;
        }
    }
}
