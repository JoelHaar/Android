package com.JoelH.illanelokuvat;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {
	XMLParser parser;
	ListView lista;
	MovieAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lista = (ListView) findViewById(R.id.Lista);
		ParseXML parsexml = new ParseXML();
		parsexml.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private class ParseXML extends AsyncTask<String,Void,String> {

		@Override
		protected String doInBackground(String... arg0) {
			parser = new XMLParser();
			adapter = new MovieAdapter(MainActivity.this, R.layout.elokuva, parser.GetList());
			try {
				parser.Parse();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String Success = "Success";
			return Success;
		}
		@Override
		protected void onPostExecute(String Success) {
			
			lista.setAdapter(adapter);
			//Add as many movies as needed to listview
			for (int i = 0; i < adapter.GetCount(); i++) {
				adapter.add(new Movie());
			}
			
			
		}
		
	}
}
