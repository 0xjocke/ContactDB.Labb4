package se.bachstatter.contactdblabb4.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import se.bachstatter.contactdblabb4.R;
import se.bachstatter.contactdblabb4.data.ContactDbHelper;
import se.bachstatter.contactdblabb4.fragments.ContactDetailFragment;

import static se.bachstatter.contactdblabb4.activity.ContactListActivity.*;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.CONTACT_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.CONTACT_POSITION_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.EDIT_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.LANDSCAPE_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.MODE_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.REMOVE_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.REQUEST_CODE;

/**
 * ContactDetailActivitu class
 * Will hold a detailfragment
 *
 */
public class ContactDetailActivity extends Activity implements DialogInterface.OnClickListener {
    /**
     * Code constants
     */
    public static final int EDIT_CONTACT_REQUEST_CODE = 234 ;

    /**
     * If orientation is landscape:
     * Get the intent send back MODE_CODE with LANDSCAPE_CODE
     * and CONTACT_POSITION_CODE to the current CONTACT.
     * Set result to RESULT_OK
     * and finish the intent.
     *
     * If savedInstance is null:
     * Create new Bundle named agruments,
     * putString/send the position
     * Create a new DetailFragment and call setArgument with the bundle.
     * Then call fragmentmanager, begintransaction,
     * Run add since this is the first time and put in right layout with the new fragment.
     * And finally call commit()
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            Intent landscapeIntent = getIntent();
            landscapeIntent.putExtra(MODE_CODE, LANDSCAPE_CODE);
            landscapeIntent.putExtra(CONTACT_POSITION_CODE, getIntent().getIntExtra(CONTACT_POSITION_CODE, 0));
            landscapeIntent.putExtra(CONTACT_ID_CODE, getIntent().getIntExtra(CONTACT_ID_CODE, 0));
            setResult(RESULT_OK, landscapeIntent);
            finish();
            return;
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(CONTACT_POSITION_CODE, getIntent().getIntExtra(CONTACT_POSITION_CODE, 0));
            arguments.putInt(CONTACT_ID_CODE, getIntent().getIntExtra(CONTACT_ID_CODE, 0));
            ContactDetailFragment fragment = new ContactDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.contact_detail_container, fragment).commit();
        }
    }

    /**
     * Inflate menu with activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_detail, menu);
        return true;
    }

    /**
     * if user clicked editcontact:
     * create new editContactintent. Send the position and the requestCode with putExtra.
     * Then run startactivity with the intent and the requestcode
     *
     * If user clicked remove button:
     * Create a alertdialog, set message, set title, create positiveBtn and negativeBtn with
     * onClickListener and finally show the dialog.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.editContact) {
            Intent editContactIntent = new Intent(this, EditContactActivity.class);
            editContactIntent.putExtra(CONTACT_ID_CODE, getIntent().getIntExtra(CONTACT_ID_CODE,0));
            editContactIntent.putExtra(REQUEST_CODE, EDIT_CONTACT_REQUEST_CODE);
            startActivityForResult(editContactIntent, EDIT_CONTACT_REQUEST_CODE);
            return true;
        }
        if (id == R.id.removeContact) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.ok, this);
            builder.setNegativeButton(R.string.cancel, this);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *If user clicked on positive button create a goToMainIntent.
     * Send CONTACT_POSITION_CODE with putExtra
     * also Send MODE_CODE REMOVE_CODE
     * SetResult to OK and finish
     *
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == AlertDialog.BUTTON_POSITIVE){
            ContactDbHelper mDbHelper = new ContactDbHelper(this);
            mDbHelper.delete(getIntent().getIntExtra(CONTACT_ID_CODE, 0));
            setResult(RESULT_OK, getIntent());
            finish();
        }
        dialog.dismiss();
    }

    /**
     * If requestcode is EDIT_CONTACT_REQUEST_CODE and RESULT_OK
     * create a goToMainIntent. Send CONTACT_POSITION_CODE with putExtra
     * Send chosen contact with string CONTACT_CODE
     * also Send MODE_CODE REMOVE_CODE
     * SetResult to OK and finish
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_CONTACT_REQUEST_CODE){
            if(RESULT_OK == resultCode){
//                Intent goToMainIntent = getIntent();
//                int contactPositionInList = goToMainIntent.getIntExtra(CONTACT_POSITION_CODE, 0);
//                goToMainIntent.putExtra(CONTACT_POSITION_CODE, contactPositionInList);
//                goToMainIntent.putExtra(CONTACT_CODE, data.getParcelableExtra(CONTACT_CODE) );
//                goToMainIntent.putExtra(MODE_CODE, EDIT_CODE);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        }
    }
}