package in.kushalsharma.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import in.kushalsharma.models.Book;

/**
 * Created by Kush on 9/13/2015.
 * Content Provider Helper Methods Class
 */

public class ContentProviderHelperMethods {

    public static ArrayList<Book> getBookListFromDatabase(Activity mAct) {

        ArrayList<Book> mBookList = new ArrayList<>();
        Uri contentUri = BookContentProvider.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {

                Book book = new Book(c.getString(c.getColumnIndex(DatabaseHelper.KEY_ID)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_SELF_LINK)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_TITLE)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_AUTHORS)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_PUBLISHER)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_PUBLISH_DATE)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_DESCRIPTION)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_PAGE_COUNT)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_CATEGORIES)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_AVERAGE_RATING)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_RATINGS_COUNT)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_SMALL_THUMBNAIL)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_THUMBNAIL)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_LANGUAGE)));

                mBookList.add(book);
            } while (c.moveToNext());
        }
        c.close();
        return mBookList;
    }

    public static boolean isBookInDatabase(Activity mAct, String id) {

        ArrayList<Book> list = new ArrayList<>(ContentProviderHelperMethods
                .getBookListFromDatabase(mAct));
        for (Book listItem : list) {
            if (listItem.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static Book getBookFromDatabase(Activity mAct, String ID) {
        Book book = null;
        Uri contentUri = BookContentProvider.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                if (ID.equals(c.getString(c.getColumnIndex(DatabaseHelper.KEY_ID)))) {
                    book = new Book(c.getString(c.getColumnIndex(DatabaseHelper.KEY_ID)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_SELF_LINK)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_TITLE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_AUTHORS)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_PUBLISHER)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_PUBLISH_DATE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_DESCRIPTION)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_PAGE_COUNT)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_CATEGORIES)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_AVERAGE_RATING)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_RATINGS_COUNT)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_SMALL_THUMBNAIL)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_THUMBNAIL)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_LANGUAGE)));
                    break;
                }

            } while (c.moveToNext());
        }
        c.close();
        return book;
    }
}
