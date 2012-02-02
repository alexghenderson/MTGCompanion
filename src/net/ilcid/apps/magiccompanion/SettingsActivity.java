package net.ilcid.apps.magiccompanion;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preferences_headers, target);
	}
	
	public static class SearchSettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			PreferenceManager.setDefaultValues(getActivity(), R.xml.search_preferences, false);
			addPreferencesFromResource(R.xml.search_preferences);
		}
	}
	
	public static class DataSettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			PreferenceManager.setDefaultValues(getActivity(), R.xml.data_preferences, false);
			addPreferencesFromResource(R.xml.data_preferences);
		}
	}
}
