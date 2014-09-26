package se.bachstatter.contactdblabb4.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import se.bachstatter.contactdblabb4.R;
import se.bachstatter.contactdblabb4.data.ContactDbHelper;
import se.bachstatter.contactdblabb4.models.Contact;

import static se.bachstatter.contactdblabb4.activity.ContactDetailActivity.EDIT_CONTACT_REQUEST_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.NEW_CONTACT_REQUEST_CODE;
import static se.bachstatter.contactdblabb4.activity.ContactListActivity.REQUEST_CODE;


public class EditContactActivity extends Activity implements View.OnClickListener {
    /**
     * Classvariables
     */
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText descriptionEditText;
    private EditText imgUrlEditText;
    private Contact contact;
    private int contactId;
    ContactDbHelper mDbHelper;

    /**
     * Errormessage constant
     */
    private static final String ERROR_EMPTY = "Please fill all fields";

    /**
     * Set layout initializeVariables
     * if requestCode is EDIT_CONTACT_REQUEST_CODE
     * get chosen contact, run fillTextFields() and setEditBtnListener()
     *
     * if requestCode is NEW_CONTACT_REQUEST_CODE: setAddBtnListener();
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        initializeVariables();
        mDbHelper = new ContactDbHelper(this);
        int requestCode = getIntent().getExtras().getInt(REQUEST_CODE);
        if (requestCode == EDIT_CONTACT_REQUEST_CODE) {

            contactId = getIntent().getIntExtra(ContactListActivity.CONTACT_ID_CODE, 0);
            contact = mDbHelper.getItem(contactId);
            fillTextFields();
            setEditBtnListener();
        }
        if (requestCode == NEW_CONTACT_REQUEST_CODE) {
            setAddBtnListener();
        }
    }

    /**
     * Find add btn, set it visible and add onClickListener
     */
    private void setAddBtnListener() {
        Button addContactBtn = (Button) findViewById(R.id.addContactBtn);
        addContactBtn.setVisibility(View.VISIBLE);
        addContactBtn.setOnClickListener(this);
    }
    /**
     * Find edit btn, set it visible and add onClickListener
     */
    private void setEditBtnListener() {
        Button editContactBtn = (Button) findViewById(R.id.editContactBtn);
        editContactBtn.setVisibility(View.VISIBLE);
        editContactBtn.setOnClickListener(this);
    }

    /**
     * find edittexts with findbyid and set class variables.
     */
    private void initializeVariables() {
        nameEditText = (EditText) findViewById(R.id.editTextName);
        descriptionEditText = (EditText) findViewById(R.id.editTextDescription);
        ageEditText = (EditText) findViewById(R.id.editTextAge);
        imgUrlEditText = (EditText) findViewById(R.id.editTextImgUrl);
    }

    /**
     * Fill all edit text with the values from contact.
     */
    private void fillTextFields() {
        nameEditText.setText(contact.getName());
        ageEditText.setText(String.valueOf(contact.getAge()));
        imgUrlEditText.setText(contact.getImgUrl());
        descriptionEditText.setText(contact.getDescription());
    }

    /**
     * Check what btn was clicked an run addBtnClicked or editBtnClicked
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addContactBtn:
                addBtnClicked();
                break;
            case R.id.editContactBtn:
                editBtnClicked();
                break;
        }
    }

    /**
     * Take the values from edittext
     * If validateFields return true
     * update the contact with the new values using set methods.
     * getintent and send the new contact with putExtra
     * set result ot RESULT_OK and run finish()
     *
     */
    private void editBtnClicked() {
        String name = String.valueOf(nameEditText.getText());
        String description = String.valueOf(descriptionEditText.getText());
        String age = String.valueOf(ageEditText.getText());
        String imgUrl = String.valueOf(imgUrlEditText.getText());
        if (validateFields(name, description, age, imgUrl)) {
            int intAge = Integer.parseInt(age);
            contact.setName(name);
            contact.setAge(intAge);
            contact.setImgUrl(imgUrl);
            contact.setDescription(description);
            mDbHelper.update(contact, contactId);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    /**
     * Take the values from edittext
     * If validateFields return true
     * Create a contact with the string from edittext..
     * getintent and send the new contact with putExtra
     * set result ot RESULT_OK and run finish()
     *
     */
    private void addBtnClicked() {
        String name = String.valueOf(nameEditText.getText());
        String description = String.valueOf(descriptionEditText.getText());
        String age = String.valueOf(ageEditText.getText());
        String imgUrl = String.valueOf(imgUrlEditText.getText());
        if (validateFields(name, description, age, imgUrl)) {
            int intAge = Integer.parseInt(age);
            Contact contact = new Contact(imgUrl, name, description, intAge);
            mDbHelper.insert(contact);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    /**
     * Checks if a Edittext is empty and if it is setError on that Edittext.
     * @param name
     * @param description
     * @param age
     * @param imgUrl
     * @return
     */
    private boolean validateFields(String name, String description, String age, String imgUrl) {
        if (name.isEmpty()) {
            nameEditText.setError(ERROR_EMPTY);
            return false;
        }
        if (age.isEmpty()) {
            ageEditText.setError(ERROR_EMPTY);
            return false;
        }
        if (imgUrl.isEmpty()) {
            imgUrlEditText.setError(ERROR_EMPTY);
            return false;
        }
        if (description.isEmpty()) {
            descriptionEditText.setError(ERROR_EMPTY);
            return false;
        }
        return true;
    }
}

