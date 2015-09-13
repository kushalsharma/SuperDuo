package in.kushalsharma.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.kushalsharma.models.Book;

/**
 * Created by Kush on 9/13/2015.
 * Database Helper class for book object
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "bookManager";

    public static final String TABLE_BOOK_DETAILS = "bookDetails";

    public static final String KEY_ID = "id";
    public static final String KEY_SELF_LINK = "selfLink";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHORS = "authors";
    public static final String KEY_PUBLISHER = "publisher";
    public static final String KEY_PUBLISH_DATE = "publishDate";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PAGE_COUNT = "pageCount";
    public static final String KEY_CATEGORIES = "categories";
    public static final String KEY_AVERAGE_RATING = "averageRating";
    public static final String KEY_RATINGS_COUNT = "ratingsCount";
    public static final String KEY_SMALL_THUMBNAIL = "smallThumbnail";
    public static final String KEY_THUMBNAIL = "thumbnail";
    public static final String KEY_LANGUAGE = "language";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_BOOK_DETAILS + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_SELF_LINK + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_AUTHORS + " TEXT,"
                + KEY_PUBLISHER + " TEXT,"
                + KEY_PUBLISH_DATE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_PAGE_COUNT + " TEXT,"
                + KEY_CATEGORIES + " TEXT,"
                + KEY_AVERAGE_RATING + " TEXT,"
                + KEY_RATINGS_COUNT + " TEXT,"
                + KEY_SMALL_THUMBNAIL + " TEXT,"
                + KEY_THUMBNAIL + " TEXT,"
                + KEY_LANGUAGE + " TEXT" + ")";
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK_DETAILS);
        onCreate(db);
    }


    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, book.getId());
        values.put(KEY_SELF_LINK, book.getSelfLink());
        values.put(KEY_TITLE, book.getTitle());
        values.put(KEY_AUTHORS, book.getAuthors());
        values.put(KEY_PUBLISHER, book.getPublisher());
        values.put(KEY_PUBLISH_DATE, book.getPublishedDate());
        values.put(KEY_DESCRIPTION, book.getDescription());
        values.put(KEY_PAGE_COUNT, book.getPageCount());
        values.put(KEY_CATEGORIES, book.getCategories());
        values.put(KEY_AVERAGE_RATING, book.getAverageRating());
        values.put(KEY_RATINGS_COUNT, book.getRatingsCount());
        values.put(KEY_SMALL_THUMBNAIL, book.getSmallThumbnail());
        values.put(KEY_THUMBNAIL, book.getThumbnail());
        values.put(KEY_LANGUAGE, book.getLanguage());

        db.insert(TABLE_BOOK_DETAILS, null, values);
        db.close();

    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOK_DETAILS, new String[]{KEY_ID,
                        KEY_SELF_LINK, KEY_TITLE, KEY_AUTHORS, KEY_PUBLISHER,
                        KEY_PUBLISH_DATE, KEY_DESCRIPTION, KEY_PAGE_COUNT, KEY_CATEGORIES,
                        KEY_AVERAGE_RATING, KEY_RATINGS_COUNT, KEY_SMALL_THUMBNAIL, KEY_THUMBNAIL,
                        KEY_LANGUAGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Book book = new Book((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9),
                cursor.getString(10), cursor.getString(11), cursor.getString(12),
                cursor.getString(13));
        cursor.close();
        return book;
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_BOOK_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getString(0));
                book.setSelfLink(cursor.getString(1));
                book.setTitle(cursor.getString(2));
                book.setAuthors(cursor.getString(3));
                book.setPublisher(cursor.getString(4));
                book.setPublishedDate(cursor.getString(5));
                book.setDescription(cursor.getString(6));
                book.setPageCount(cursor.getString(7));
                book.setCategories(cursor.getString(8));
                book.setAverageRating(cursor.getString(9));
                book.setRatingsCount(cursor.getString(10));
                book.setSmallThumbnail(cursor.getString(11));
                book.setThumbnail(cursor.getString(12));
                book.setLanguage(cursor.getString(13));

                bookList.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bookList;
    }

    public int getBookCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BOOK_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOK_DETAILS, KEY_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        db.close();
    }
}
