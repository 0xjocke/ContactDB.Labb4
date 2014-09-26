package se.bachstatter.contactdblabb4.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import se.bachstatter.contactdblabb4.R;
import se.bachstatter.contactdblabb4.data.contracts.ContactContract;


/**
 * Created by Jocek on 2014-09-14.
 */
public class ContactAdapter extends CursorAdapter {

    /**
     * Class variables
     */
    LayoutInflater inflater;
    ViewHolder viewHolder;

    /**
     * Constructor that saves a Layoutinfaltor to our classvariable
     * @param context
     * @param cursor
     * @param autoRequery
     */
    public ContactAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
        inflater = LayoutInflater.from(context);
    }

    /**
     * Inflates the layout with the view.
     * Creates a new View holder and saves the Edittext and ImageView in its class variables.
     * set a tag on the view so we cana find it later.
     *
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        viewHolder = new ViewHolder();
        View view = inflater.inflate(R.layout.list_row_contact, parent, false);
        viewHolder.imageHolder = (ImageView)view.findViewById(R.id.imageViewContact);
        viewHolder.nameHolder = (TextView)view.findViewById(R.id.textViewContact);
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Get the our viewholder.
     * Get our edittext from out viewholder and settext with the text from the cursor.
     * With the help of picasso set our create a bitmap from our imgURL and set it to imageview.
     * Picasso also sets a placeholder picture and an error picture.
     *
     *
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewHolder = (ViewHolder)view.getTag();
        viewHolder.nameHolder.setText(cursor.getString((cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_NAME_NAME))));
        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_NAME_IMG_URL)))
                .error(R.drawable.person)
                .placeholder(R.drawable.person)
                .into(viewHolder.imageHolder);
    }

    /**
     * Viewholder class for saving edittext and imageview.
     */
    static class ViewHolder{
        public TextView nameHolder;
        public ImageView imageHolder;
    }
}
