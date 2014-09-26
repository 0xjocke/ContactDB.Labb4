package se.bachstatter.contactdblabb4.fragments;

/**
 * Created by Jocek on 2014-09-17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import se.bachstatter.contactdblabb4.R;
import se.bachstatter.contactdblabb4.adapters.ContactAdapter;
import se.bachstatter.contactdblabb4.data.ContactDbHelper;
import se.bachstatter.contactdblabb4.models.Contact;

import static se.bachstatter.contactdblabb4.activity.ContactListActivity.*;

public class ContactListFragment extends ListFragment implements AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener {
    ContactDbHelper mDbHelper;
    Cursor contactCursor;
    public ContactAdapter contactAdapter;
    private static final int FIRST_ITEM = 0;
    private Callbacks mCallbacks;
    int currentIdForDialog;
    int mActivatedId = ListView.NO_ID;

    /**
     * Create an interface that will be used to call parent activity
     * OnItemSelected with teh chosen position.
     */
    public interface Callbacks {
        public void onItemSelected(int id);
    }

    /**
     * Create a static contact list and save it to Contacs staticContactList variable.
     * It needs to be static since we will use it in static callback function.
     * Add some content.
     * Create a new Contactadapter with the staticContactList
     * then run setListadapter with the contactAdpater
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new ContactDbHelper(getActivity());
        contactCursor = mDbHelper.get();
        contactAdapter = new ContactAdapter(getActivity(), contactCursor, false);
        setListAdapter(contactAdapter);
    }


    /**
     * If savedInstance is null and we are in landscape: show first item
     *
     * if savedInstance is null and it containsKey CONTACT_ID_CODE:
     * run setActivatedPosition  CONTACT_ID_CODE
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLongClickListener();
        if (savedInstanceState != null && savedInstanceState.containsKey(CONTACT_ID_CODE)) {
            setActivatedPosition(savedInstanceState.getInt(CONTACT_ID_CODE));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        contactCursor = mDbHelper.get();
        contactAdapter.changeCursor(contactCursor);
        contactAdapter.notifyDataSetChanged();
    }

    public void updateList(){
        contactCursor = mDbHelper.get();
        contactAdapter.changeCursor(contactCursor);
        contactAdapter.notifyDataSetChanged();
    }

    /**
     * Sets OnItemLongClickListener on listview.
     *
     */
    private void setLongClickListener() {
        ListView listView = getListView();
        listView.setOnItemLongClickListener(this);
    }

    /**
     * Check that parent activity implements Callbacks else throw error.
     * then sets mCallsback to the parents activitys callback.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    /**
     * OnDetach set mCallback to null since the event wont be relevant anymore and can cause
     * unexpected problems.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * On ListItemClick send the position to parent activity with the help of our callback interface.
     * @param listView
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);
        mActivatedId = (int)id;
        mCallbacks.onItemSelected((int)id);
    }

    /**
     * if mActivated not is Invalid
     * give outstate mActivatedPosition with putInt
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mActivatedId != ListView.NO_ID) {
            outState.putInt(CONTACT_ID_CODE, mActivatedId);
        }
        super.onSaveInstanceState(outState);

    }

    /**
     * Sets the checked state of ListView the specified position.
     * Also saves teh position to mActivatedPosition
     * @param id
     */
    private void setActivatedPosition(int id) {
       //TODO might need position here
       // getListView().setItemChecked(position, true);
        mActivatedId = id;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        currentIdForDialog = (int)id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, this);
        builder.setNegativeButton(R.string.cancel, this);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == AlertDialog.BUTTON_POSITIVE){
            mDbHelper.delete(currentIdForDialog);
            contactCursor = mDbHelper.get();
            contactAdapter.changeCursor(contactCursor);
            contactAdapter.notifyDataSetChanged();
        }
        dialog.dismiss();
    }
}