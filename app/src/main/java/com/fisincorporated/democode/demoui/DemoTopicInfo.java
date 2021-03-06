package com.fisincorporated.democode.demoui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Info for either a demo fragment (FragmentList is null) or to drill down to
 * another list (demoListClassName is not null)
 */
public class DemoTopicInfo implements Parcelable {
	/**
	 * General description of this topic. May be used as subheader text
	 */
	private String description;
	/**
	 * Activity used to display the topic. Assign only if you want app to create this activity
	 * and display the topic fragment. If not assigned the fragment will be displayed with existing
	 * activity
	 */
	private String activity;
	/**
	 * Fragment to be created to show demo
	 */
	private String fragment;
	/**
	 * A DemoTopicList. Assigned if the demo topic is another list of topics
	 *
	 */
	private String demoListClassName;

	public DemoTopicInfo(String description, String activity, String fragment,
						 String demoList) {
		super();
		this.description = description;
		this.activity = activity;
		this.fragment = fragment;
		this.demoListClassName = demoList;
	}

	private DemoTopicInfo(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<DemoTopicInfo> CREATOR
			= new Parcelable.Creator<DemoTopicInfo>() {
		public DemoTopicInfo createFromParcel(Parcel in) {
			return new DemoTopicInfo(in);
		}

		public DemoTopicInfo[] newArray(int size) {
			return new DemoTopicInfo[size];
		}
	};

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
		return 0;
	}
}