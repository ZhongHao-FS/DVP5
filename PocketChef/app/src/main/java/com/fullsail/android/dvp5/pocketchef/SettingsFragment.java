package com.fullsail.android.dvp5.pocketchef;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    public SettingsFragment() { super(R.layout.fragment_settings); }

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
