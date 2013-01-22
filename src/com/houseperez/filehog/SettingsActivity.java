package com.houseperez.filehog;

import java.io.File;
import java.util.ArrayList;

import com.houseperez.util.Constants;
import com.houseperez.util.FileIO;
import com.houseperez.util.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;

public class SettingsActivity extends Activity {

	// Constants
	private static final String TAG = "SettingsActivity";

	// Globals
	private Settings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Log.i(TAG, "onCreate(): " + TAG);

		// Log.i(TAG, "settings: " + settings);

		// Bundle b = this.getIntent().getExtras();
		// if (b != null)
		// settings = (Settings) b.getParcelable(Constants.STR_STRING);
	}

	@Override
	protected void onStart() {
		super.onStop();
		Log.i(TAG, "onStart(): " + TAG);

		settings = (Settings) FileIO.readObject(getApplicationContext(),
				Constants.SETTINGS_FILE);

		if (settings == null)
			settings = new Settings(new ArrayList<File>(0),
					new ArrayList<File>(0), new ArrayList<File>(0),
					new ArrayList<File>(0), Constants.STARTING_FILE_COUNT,
					Settings.EXTERNAL_DIRECTORY, true, Settings.DAY_IN_MILLI,
					false, Settings.DAILY);

		// File Count
		Log.i(TAG, "settings.getIntFileCount(): " + settings.getIntFileCount());
		SeekBar bar = (SeekBar) findViewById(R.id.seekFileCount);
		bar.setOnSeekBarChangeListener(new FileCountSeekBar());
		bar.setMax(Constants.MAX_SEEK_BAR);
		bar.setProgress(settings.getIntFileCount() - 1);
		TextView textview = ((TextView) findViewById(R.id.txtFileCountNumber));
		textview.setText(Integer.toString(settings.getIntFileCount()));

		// Research Frequency
		Spinner spinner = (Spinner) findViewById(R.id.spinResearchFrequency);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.ResearchFrequencyItems,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new ResearchFrequencySpinner());
		spinner.setSelection(settings.getResearchFrequency());

		switch (settings.getSelectedSearchDirectory()) {
		case Settings.EXTERNAL_DIRECTORY:
			((RadioButton) findViewById(R.id.rdoExternal)).setChecked(true);
			break;
		case Settings.ROOT_DIRECTORY:
			((RadioButton) findViewById(R.id.rdoRoot)).setChecked(true);
			break;
		}

		if (settings.isFindBiggestFiles())
			((RadioButton) findViewById(R.id.rdoLargest)).setChecked(true);
		else
			((RadioButton) findViewById(R.id.rdoSmallest)).setChecked(true);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private class FileCountSeekBar implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				Log.i(TAG, "IntFileCount: " + progress);
				settings.setIntFileCount(progress + 1);
				((TextView) findViewById(R.id.txtFileCountNumber))
						.setText(Integer.toString(progress + 1));
				FileIO.writeObject(settings, getApplicationContext(),
						Constants.SETTINGS_FILE);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	private class ResearchFrequencySpinner implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			switch (pos) {
			case Settings.ALWAYS:
				settings.setTimeToDelayRefresh(-(Long.MAX_VALUE / 2));
				settings.setResearchFrequency(Settings.ALWAYS);
				break;
			case Settings.HOURLY:
				settings.setTimeToDelayRefresh(Settings.HOUR_IN_MILLI);
				settings.setResearchFrequency(Settings.HOURLY);
				break;
			case Settings.DAILY:
				settings.setTimeToDelayRefresh(Settings.DAY_IN_MILLI);
				settings.setResearchFrequency(Settings.DAILY);
				break;
			case Settings.WEEKLY:
				settings.setTimeToDelayRefresh(Settings.WEEK_IN_MILLI);
				settings.setResearchFrequency(Settings.WEEKLY);
				break;
			case Settings.BI_WEEKLY:
				settings.setTimeToDelayRefresh(Settings.BI_WEEK_IN_MILLI);
				settings.setResearchFrequency(Settings.BI_WEEKLY);
				break;
			}
			FileIO.writeObject(settings, getApplicationContext(),
					Constants.SETTINGS_FILE);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Another interface callback
		}

	}

	public void onDirectoryRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.rdoExternal:
			if (checked) {
				settings.setSelectedSearchDirectory(Settings.EXTERNAL_DIRECTORY);
				Log.i(TAG,
						"settings.getSelectedSearchDirectory(): "
								+ settings.getSelectedSearchDirectory());
			}
			break;
		case R.id.rdoRoot:
			if (checked) {
				settings.setSelectedSearchDirectory(Settings.ROOT_DIRECTORY);
				Log.i(TAG,
						"settings.getSelectedSearchDirectory(): "
								+ settings.getSelectedSearchDirectory());
			}
			break;
		}
		FileIO.writeObject(settings, getApplicationContext(),
				Constants.SETTINGS_FILE);
	}

	public void onFileSortRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.rdoLargest:
			if (checked)
				settings.setFindBiggestFiles(Settings.BIGGEST_FILES);
			break;
		case R.id.rdoSmallest:
			if (checked)
				settings.setFindBiggestFiles(Settings.SMALLEST_FILES);
			break;
		}
		FileIO.writeObject(settings, getApplicationContext(),
				Constants.SETTINGS_FILE);
	}

}
