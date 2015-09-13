package in.kushalsharma.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import in.kushalsharma.alexandria.R;
import in.kushalsharma.models.Book;
import in.kushalsharma.utils.AppController;

/**
 * Created by kushal on 06/09/15.
 * Adapter for book data
 */

public class BookDataAdapter extends RecyclerView.Adapter<BookDataAdapter.BookViewHolder> {

    private Book book;
    private Activity mAct;
    private LayoutInflater mInflater;

    public BookDataAdapter(Book book, Activity mActivity) {
        this.book = book;
        this.mAct = mActivity;

        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mInflater.inflate(R.layout.layout_book_details, viewGroup, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, int i) {
        bookViewHolder.getThumbnailImageView().setImageUrl(book.getThumbnail(), AppController.getInstance().getImageLoader());
        bookViewHolder.getTitleView().setText(book.getTitle());
        bookViewHolder.getAuthorView().setText("by " + book.getAuthors());
        bookViewHolder.getPublisherView().setText("Publisher - " + book.getPublisher());
        bookViewHolder.getPublishDateView().setText("Publish Date - " + book.getPublishedDate());
        bookViewHolder.getPageCountView().setText("Pages - " + book.getPageCount());
        bookViewHolder.getDescriptionView().setText("Overview - " + book.getDescription());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView thumbnailImageView;
        private TextView titleView, authorView, publisherView, publishDateView, pageCountView, descriptionView;

        public BookViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = (NetworkImageView) itemView.findViewById(R.id.thumbnail_image);
            titleView = (TextView) itemView.findViewById(R.id.title);
            authorView = (TextView) itemView.findViewById(R.id.author);
            publisherView = (TextView) itemView.findViewById(R.id.publisher);
            publishDateView = (TextView) itemView.findViewById(R.id.publish_date);
            pageCountView = (TextView) itemView.findViewById(R.id.page_count);
            descriptionView = (TextView) itemView.findViewById(R.id.description);
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getAuthorView() {
            return authorView;
        }

        public NetworkImageView getThumbnailImageView() {
            return thumbnailImageView;
        }

        public TextView getPublisherView() {
            return publisherView;
        }

        public TextView getPublishDateView() {
            return publishDateView;
        }

        public TextView getPageCountView() {
            return pageCountView;
        }

        public TextView getDescriptionView() {
            return descriptionView;
        }
    }
}
