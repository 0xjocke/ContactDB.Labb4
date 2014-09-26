package se.bachstatter.contactdblabb4.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import se.bachstatter.contactdblabb4.R;
import se.bachstatter.contactdblabb4.data.ContactDbHelper;
import se.bachstatter.contactdblabb4.fragments.ContactDetailFragment;
import se.bachstatter.contactdblabb4.fragments.ContactListFragment;

public class ContactListActivity extends Activity implements
        ContactListFragment.Callbacks {
    /**
     * Variable for checking if two pane layout is active.
     */
    private boolean mTwoPane;
    ContactDbHelper contactDbHelper;


    /**
     * Request and Code Constants
     */
    public static final String CONTACT_ID_CODE = "id" ;
    public static final String MODE_CODE = "mode";
    public static String REQUEST_CODE = "requestCode";
    public static final int NEW_CONTACT_REQUEST_CODE = 123 ;
    private static final int DETAIL_REQUEST_CODE = 234;
    public static final int LANDSCAPE_CODE = 857;


    /**
     * Check if contact detail is not null/ visible then set mTwoPane to true.
     * It will be visible is screen is larger than 500dp
     * Either case run ContactListFragment setActivateOnItemClick function.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contactDbHelper = new ContactDbHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        if (findViewById(R.id.contact_detail_container) != null) {
            mTwoPane = true;
        }
    }

    /**
     * We can use this method cause we implements ContactListFragment.Callbacks
     * When the ListFragments gets a click onListItemClick will run this function and send the position.
     *
     * if mTwoPane is true:
     * Create new Bundle named arguments,
     * save the id with putString
     * Create a new DetailFragment and call setArgument with the bundle.
     * Then call fragmentmanager, begintransaction and replace.
     * We call replace cause we want to show the current id.
     * And finally call commit()
     *
     * Else create a new detailIntent
     * and send id with put extra
     *
     * @param id
     */
    public void onItemSelected(int id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(CONTACT_ID_CODE, id);
            ContactDetailFragment fragment = new ContactDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.contact_detail_container, fragment).commit();
        } else {
            Intent detailIntent = new Intent(this, ContactDetailActivity.class);
            detailIntent.putExtra(CONTACT_ID_CODE, id);
            startActivityForResult(detailIntent, DETAIL_REQUEST_CODE);
        }
    }

    /**
     * Inflate the menu layout with the actionbar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * If user clicks on add contact start a new EditContactActivity
     * Send requestCode NEW_CONTACT_REQUEST_CODE with put extra.
     * and startActivityForResult.
     *
     * If user clicks on sortName btn run toggleSorting on our dbHelper
     * check is we are in twoPane mode. run update list on with the
     * help of thefragment manager on the shown fragment.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addContact:
                Intent addContactIntent = new Intent(this, EditContactActivity.class);
                addContactIntent.putExtra(REQUEST_CODE, NEW_CONTACT_REQUEST_CODE);
                startActivityForResult(addContactIntent, NEW_CONTACT_REQUEST_CODE);
                break;
            case R.id.sortName:
                contactDbHelper.toggleSorting();
                ContactListFragment fragment;
                if (!mTwoPane){
                    fragment = (ContactListFragment) getFragmentManager().
                            findFragmentById(R.id.contact_single_list);

                }else{
                    fragment = (ContactListFragment) getFragmentManager().
                            findFragmentById(R.id.contact_list);
                }
                fragment.updateList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * If the requestcode is DETAIL_REQUEST_CODE and  ResultCode == Result_OK
     * Check the MODE_CODE sent with putExtra
     * If mode is LANDSCAPE:
     * Create new Bundle named arguments,
     * save the id with putString
     * Create a new DetailFragment and call setArgument with the bundle.
     * Then call fragmentmanager, begintransaction and replace.
     * We call replace cause we want to show the current contact.
     * And finally call commit()
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DETAIL_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                switch (data.getIntExtra(MODE_CODE, 0)){
                    case LANDSCAPE_CODE:
                        Bundle arguments = new Bundle();
                        arguments.putInt(CONTACT_ID_CODE, data.getIntExtra(CONTACT_ID_CODE,0));
                        ContactDetailFragment fragment = new ContactDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.contact_detail_container, fragment).commit();
                        break;
                }
            }
        }
    }
}