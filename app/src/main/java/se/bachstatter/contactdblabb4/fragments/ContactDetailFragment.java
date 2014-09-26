package se.bachstatter.contactdblabb4.fragments;

/**
 * Created by Jocek on 2014-09-17.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import se.bachstatter.contactdblabb4.R;
import se.bachstatter.contactdblabb4.data.ContactDbHelper;
import se.bachstatter.contactdblabb4.models.Contact;

import static se.bachstatter.contactdblabb4.activity.ContactListActivity.*;

public class ContactDetailFragment extends Fragment {
    private Contact contact;
    ContactDbHelper contactDbHelper;
    int contactId;


    /**
     * If arguments contain key CONTACT_ID_CODE
     * get the chosen contact with the help of ContactDBHelper
     * and set the contact to the contact class variable.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(CONTACT_ID_CODE)) {
            contactDbHelper = new ContactDbHelper(getActivity());
            contactId = getArguments().getInt(CONTACT_ID_CODE);
            contact = contactDbHelper.getContact(contactId);
        }
    }

    /**
     * Inflate the layout with the  view
     * if contact is not null
     * Fill the textviews with the contacts get methods.
     * Convert the imgurl to bitmap and set it to imageviewe with the help of picasso.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail,
                container, false);

        if (contact != null) {

            ((TextView) rootView.findViewById(R.id.textViewName))
                    .setText(contact.getName());
            ((TextView) rootView.findViewById(R.id.textViewDescription))
                    .setText(contact.getDescription());
            ((TextView) rootView.findViewById(R.id.textViewAge))
                    .setText(String.valueOf(contact.getAge()));

            ImageView contactImageView = (ImageView)rootView.findViewById(R.id.imageViewContact);

            Picasso.with(getActivity())
                    .load(contact.getImgUrl())
                    .error(R.drawable.person)
                    .placeholder(R.drawable.person)
                    .into(contactImageView);
        }
        return rootView;
    }

}