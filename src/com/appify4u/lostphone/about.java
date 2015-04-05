package com.appify4u.lostphone;

import com.appify4u.lostphone.core.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class about extends Activity implements View.OnClickListener{

	Button vis,feed;
	Intent browserIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		vis = (Button) findViewById(R.id.vis);
		feed = (Button) findViewById(R.id.feed);
		
		vis.setOnClickListener(this);
		feed.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.vis :
			browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.appify4u.com"));
			startActivity(browserIntent);
			break;
		case R.id.feed:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@appify4u.com"});
			i.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
			i.putExtra(Intent.EXTRA_TEXT   , "body of email");
			try {
			    startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
			    Toast.makeText(about.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
