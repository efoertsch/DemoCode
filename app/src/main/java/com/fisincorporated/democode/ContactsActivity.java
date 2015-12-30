package com.fisincorporated.democode;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

// from http://examples.javacodegeeks.com/android/core/provider/android-contacts-example/
// with some modifications
public class ContactsActivity extends AppCompatActivity {

	private TextView outputText;
	private ListView listView;
	private ProgressBar pb;
	private ArrayList<ContactSummary> contactSummaryList = new ArrayList<ContactSummary>();
	private ContactListAdapter contactListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_list);
		listView = (ListView) findViewById(R.id.listView1);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		fetchContacts();
		contactListAdapter = new ContactListAdapter(this, contactSummaryList);
		listView.setAdapter(contactListAdapter);
		
		
	}

	public void fetchContacts() {
		long contactRow = 0l;
		StringBuilder sbSummary = new StringBuilder();
		String phoneNumber = null;
		String email = null;

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver = getContentResolver();

		Cursor cursor = contentResolver
				.query(CONTENT_URI, null, null, null, null);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {
			pb.setMax(cursor.getCount());

			while (cursor.moveToNext()) {
				sbSummary.setLength(0);
				String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0) {

					sbSummary.append("\n First Name:" + name);

					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI,
							null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));
						sbSummary.append("\n Phone number:" + phoneNumber);

					}

					phoneCursor.close();

					// Query and loop for every email of the contact
					Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,
							null, EmailCONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (emailCursor.moveToNext()) {

						email = emailCursor.getString(emailCursor
								.getColumnIndex(DATA));

						sbSummary.append("\nEmail:" + email);

					}

					emailCursor.close();
				}
				contactSummaryList.add(new ContactSummary( contactRow, sbSummary.toString()));
				output.append("\n");
				pb.setProgress((int) ++contactRow);
			}
		}
	}
	
	public class ContactSummary{
		long _id = 0l;
		String summary="";
		public ContactSummary(Long id, String summary){
			_id = id;
			this.summary = summary;
		}
		public String getSummary(){
			return summary;
		}
		
	}
	
	private class ContactListAdapter extends ArrayAdapter<ContactSummary> {
		public class ContactListHolder {
			public TextView tvTextView;
		}
	 
		public ContactListAdapter(Context context, ArrayList<ContactSummary> contactSummary) {
			super(context, android.R.layout.simple_list_item_single_choice,
					contactSummary);
		}

			@Override
		// see http://www.vogella.com/tutorials/AndroidListView/article.html for
		// optimization of inflate/findViewById
		// do minimal stuff here, do most in interface callback
		// needs cleanup
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			ContactListAdapter.ContactListHolder viewHolder;
			if (convertView == null) {
				// for fragment
				//LayoutInflater inflater = (LayoutInflater) getActivity()
				//		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				viewHolder = new ContactListHolder();
				rowView = getLayoutInflater().inflate(R.layout.contact_summary_row, parent, false);
				
				viewHolder.tvTextView = (TextView)rowView;
				rowView.setTag(viewHolder);
			}
			ContactSummary contactSummary = this.getItem(position);
			viewHolder = (ContactListAdapter.ContactListHolder) rowView.getTag();
			// viewHolder.tvLessonNumber.setText((position + 1) + "");
			viewHolder.tvTextView.setText(contactSummary.getSummary());
			
			return rowView;
		}

	}

}
