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
    ContactDbHelper mDbHelper;
    int contactId;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(CONTACT_POSITION_CODE) &&
                getArguments().containsKey(CONTACT_ID_CODE)) {
            mDbHelper = new ContactDbHelper(getActivity());
            contactId = getArguments().getInt(CONTACT_ID_CODE);
            contact = mDbHelper.getItem(contactId);
        }
    }

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