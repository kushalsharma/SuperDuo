package in.kushalsharma.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kushal on 06/09/15.
 * Book Class
 */

public class Book implements Parcelable {

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private String id, selfLink, title, authors, publisher, publishedDate, description, pageCount,
            categories, averageRating, ratingsCount, smallThumbnail, thumbnail, language;

    public Book() {
    }

    public Book(String id, String selfLink, String title, String authors, String publisher,
                String publishedDate, String description, String pageCount, String categories,
                String averageRating, String ratingsCount, String smallThumbnail, String thumbnail,
                String language) {
        this.id = id;
        this.selfLink = selfLink;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.categories = categories;
        this.averageRating = averageRating;
        this.ratingsCount = ratingsCount;
        this.smallThumbnail = smallThumbnail;
        this.thumbnail = thumbnail;
        this.language = language;
    }

    protected Book(Parcel in) {
        String[] data = new String[14];

        in.readStringArray(data);
        this.id = data[0];
        this.selfLink = data[1];
        this.title = data[2];
        this.authors = data[3];
        this.publisher = data[4];
        this.publishedDate = data[5];
        this.description = data[6];
        this.pageCount = data[7];
        this.categories = data[8];
        this.averageRating = data[9];
        this.ratingsCount = data[10];
        this.smallThumbnail = data[11];
        this.thumbnail = data[12];
        this.language = data[13];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id,
                this.selfLink,
                this.title,
                this.authors,
                this.publisher,
                this.publishedDate,
                this.description,
                this.pageCount,
                this.categories,
                this.averageRating,
                this.ratingsCount,
                this.smallThumbnail,
                this.thumbnail,
                this.language});
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(String ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
