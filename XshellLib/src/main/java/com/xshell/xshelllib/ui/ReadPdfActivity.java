package com.xshell.xshelllib.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.artifex.mupdf.MuPDFCore;
import com.artifex.mupdf.MuPDFPageAdapter;
import com.xshell.xshelllib.R;

import java.io.File;

public class ReadPdfActivity extends Activity{

	private TextView shareOrOpen;
	private String path;
	private String emailAddr;
	private String emailTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xinyusoft_pdf);
		Intent intent =  getIntent();
		path = intent.getStringExtra("path");
		emailAddr = intent.getStringExtra("emailAddr");
		emailTitle = intent.getStringExtra("emailTitle");
		MuPDFCore core = null;
		try {
			core = new MuPDFCore(this, path);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		ListView mListView = (ListView) findViewById(R.id.lv_pdf);
		MuPDFPageAdapter adapter = new MuPDFPageAdapter(this, core);
		mListView.setAdapter(adapter);


		shareOrOpen = (TextView) findViewById(R.id.share_or_open);
		shareOrOpen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = startSendToEmailIntent();
//				Intent intent = getPdfFileIntent(path);
				startActivity(intent);
			}
		});
	}



	/**
	 * Get PDF file Intent
	 */
	public Intent getPdfFileIntent(String path) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		i.setDataAndType(uri, "application/pdf");
		return i;
	}


	private Intent startSendToEmailIntent(){
		Intent data=new Intent(Intent.ACTION_SEND);
		data.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddr});
		data.putExtra(Intent.EXTRA_SUBJECT, emailTitle);
		data.putExtra(Intent.EXTRA_TEXT, "");
		data.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
		data.setType("text/plain");
		return data;
	}

}
