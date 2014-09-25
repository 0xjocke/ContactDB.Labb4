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

    LayoutInflater inflater;
    ViewHolder viewHolder;

    public ContactAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        viewHolder = new ViewHolder();
        View view = inflater.inflate(R.layout.list_row_contact, parent, false);
        viewHolder.imageHolder = (ImageView)view.findViewById(R.id.imageViewContact);
        viewHolder.nameHolder = (TextView)view.findViewById(R.id.textViewContact);
        view.setTag(viewHolder);
        return view;
    }

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

    static class ViewHolder{
        public TextView nameHolder;
        public ImageView imageHolder;
    }
}
