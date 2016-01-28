
package com.fisincorporated.democode.threads;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fisincorporated.democode.R;

public abstract class ThreadDemoFragment extends Fragment implements
		OnClickListener {

	protected static final String sLineSeparator = System
			.getProperty("line.separator");
	protected ProgressBar mProgressBar;
	protected TextView mTvProgressMsg;
	protected Button mStartButton;
	protected Button mCancelButton;
	protected Button mClearButton;
	protected ScrollView mScrollViewStatus;
	protected TextView mTvStatusArea;
	protected TextView mTvDemoTitle;


	public ThreadDemoFragment() {
		super();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.thread_demo, container, false);
		mTvProgressMsg = (TextView) v.findViewById(R.id.tvProgressMsg);
		mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		mProgressBar.setMax(100);
		mStartButton = (Button) v.findViewById(R.id.btnStart);
		mStartButton.setOnClickListener(this);
		mCancelButton = (Button) v.findViewById(R.id.btnCancel);
		mCancelButton.setOnClickListener(this);
		mClearButton = (Button) v.findViewById(R.id.btnClear);
		mClearButton.setOnClickListener(this);
		mTvStatusArea = (TextView) v.findViewById(R.id.tvStatusArea);
		mScrollViewStatus = (ScrollView) v.findViewById(R.id.scrollViewStatus);
		mTvDemoTitle = (TextView) v.findViewById(R.id.tvDemoTitle);
        mTvDemoTitle.setText(getFragmentTitle());
		return v;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:
			doStart();
			break;
		case R.id.btnCancel:
			doCancel();
			break;
		case R.id.btnClear:
			doClear();
			break;
		}
	}


    protected abstract String getFragmentTitle();

    protected abstract void doStart();

	protected abstract void doCancel();

	protected void doClear() {
		mTvStatusArea.setText("");
		mTvProgressMsg.setText("");

	}
	
	protected void scrollToBottom() {
		mScrollViewStatus.post(new Runnable() {
			public void run() {
				mScrollViewStatus.fullScroll(View.FOCUS_DOWN);
			}
		});
	}
}

