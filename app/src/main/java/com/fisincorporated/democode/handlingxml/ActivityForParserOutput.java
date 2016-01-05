package com.fisincorporated.democode.handlingxml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fisincorporated.democode.R;
import com.fisincorporated.interfaces.ICallbackTemplate;


public class ActivityForParserOutput extends AppCompatActivity implements ICallbackTemplate {
	private TextView textView;
	private XMLPullParserTemplate xmlPullParser;
	private String lineSeparator = System.getProperty("line.separator");
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// display screen
		setContentView(R.layout.activity_for_xml_parser);
		getWidgetsSetListeners();
		xmlPullParser = new XMLPullParserTemplate(this);
		
		}
	
	private void getWidgetsSetListeners(){
		textView = (TextView) findViewById(R.id.textView_for_xml_output);
		Button btn = (Button) findViewById(R.id.btn_start_parsing);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				textView.setText("");
				// using Sample XML
				xmlPullParser.startParsing(); 
				
			}});
	}

	@Override
	public void callbackInfo(String information) {
		if (textView != null){
			textView.append(information + lineSeparator);
		}
		
	}
}
