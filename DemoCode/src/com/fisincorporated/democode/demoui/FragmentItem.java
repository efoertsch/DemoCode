package com.fisincorporated.democode.demoui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Info for either a demo fragment (FragmentList is null) or to drill down to
 * another list (FragmentList is not null)
 */
public class FragmentItem implements Parcelable {
	private String description;
	private String activity;
	private String fragment;
	private String demoListClassName;

	public FragmentItem(String description, String activity, String fragment,
			String demoList) {
		super();
		this.description = description;
		this.activity = activity;
		this.fragment = fragment;
		this.demoListClassName = demoList;
		// this.demoList = demoListClassName;
	}

	@Override
	public String toString() {
		return description;
	}

	public String getDescription() {
		return description;
	}

	public String getActivity() {
		return activity;
	}

	public String getFragment() {
		return fragment;
	}

	public String getDemoListClassName() {
		return demoListClassName;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(description);
		dest.writeString(activity);
		dest.writeString(fragment);
		dest.writeString(demoListClassName);
	}

	public void readFromParcel(Parcel src) {
		description = src.readString();
		activity = src.readString();
		fragment = src.readString();
		demoListClassName = src.readString();

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}