<<<<<<< HEAD:app/src/main/java/com/fisincorporated/democode/threads/ThreadDemoFragment.java
package com.fisincorporated.democode.threads;

import com.fisincorporated.democode.R;

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

public abstract class ThreadDemoFragment extends Fragment implements
		OnClickListener {

	protected static final String lineSeparator = System
			.getProperty("line.separator");
	protected ProgressBar progressBar;
	protected TextView tvProgressMsg;
	protected Button startButton;
	protected Button cancelButton;
	protected Button clearButton;
	protected ScrollView scrollViewStatus;
	protected TextView tvStatusArea;

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
		tvProgressMsg = (TextView) v.findViewById(R.id.tvProgressMsg);
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		progressBar.setMax(100);
		startButton = (Button) v.findViewById(R.id.btnStart);
		startButton.setOnClickListener(this);
		cancelButton = (Button) v.findViewById(R.id.btnCancel);
		cancelButton.setOnClickListener(this);
		clearButton = (Button) v.findViewById(R.id.btnClear);
		clearButton.setOnClickListener(this);
		tvStatusArea = (TextView) v.findViewById(R.id.tvStatusArea);
		scrollViewStatus = (ScrollView) v.findViewById(R.id.scrollViewStatus);
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

	protected abstract void doStart();

	protected abstract void doCancel();

	protected void doClear() {
		tvStatusArea.setText("");
		tvProgressMsg.setText("");

	}
	
	protected void scrollToBottom() {
		scrollViewStatus.post(new Runnable() { 
		     public void run() {
		         scrollViewStatus.fullScroll(View.FOCUS_DOWN);
		     }
		 });
	}
}
=======
package com.fisincorporated.democode.threads;

import com.fisincorporated.democode.R;

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

public abstract class ThreadDemoFragment extends Fragment implements
		OnClickListener {

	protected static final String lineSeparator = System
			.getProperty("line.separator");
	protected ProgressBar progressBar;
	protected TextView tvProgressMsg;
	protected Button startButton;
	protected Button cancelButton;
	protected Button clearButton;
	protected ScrollView scrollViewStatus;
	protected TextView tvStatusArea;

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
		tvProgressMsg = (TextView) v.findViewById(R.id.tvProgressMsg);
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		progressBar.setMax(100);
		startButton = (Button) v.findViewById(R.id.btnStart);
		startButton.setOnClickListener(this);
		cancelButton = (Button) v.findViewById(R.id.btnCancel);
		cancelButton.setOnClickListener(this);
		clearButton = (Button) v.findViewById(R.id.btnClear);
		clearButton.setOnClickListener(this);
		tvStatusArea = (TextView) v.findViewById(R.id.tvStatusArea);
		scrollViewStatus = (ScrollView) v.findViewById(R.id.scrollViewStatus);
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

	protected abstract void doStart();

	protected abstract void doCancel();

	protected void doClear() {
		tvStatusArea.setText("");
		tvProgressMsg.setText("");

	}
	
	protected void scrollToBottom() {
		scrollViewStatus.post(new Runnable() { 
		     public void run() {
		         scrollViewStatus.fullScroll(View.FOCUS_DOWN);
		     }
		 });
	}
}
>>>>>>> origin/master:DemoCode/src/com/fisincorporated/democode/threads/ThreadDemoFragment.java
