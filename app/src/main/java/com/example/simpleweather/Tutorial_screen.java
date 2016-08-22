package com.example.simpleweather;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viki.simpleweather.R;

public class Tutorial_screen extends FragmentActivity {
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tut_screen);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Editor editor = pref.edit();
		editor.putBoolean("firstime", true);
		editor.putBoolean("secondtime", false);
		editor.commit();
		
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new InstructionHeaderFragment();
			Bundle args = new Bundle();
			args.putInt(InstructionHeaderFragment.ARG_SECTION_NUMBER,
					position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {

			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "REFRESH";
			case 1:
				return "CHANGE CITY";
			case 2:
				return "MORE DETAILS";

			}
			return null;
		}
	}

	public static class InstructionHeaderFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		public InstructionHeaderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tut_screen,
					container, false);
			ImageView Instructions = (ImageView) rootView
					.findViewById(R.id.Instruction);
			ImageView swipe_hand = (ImageView) rootView
					.findViewById(R.id.imageView1);
			Button exit = (Button) rootView.findViewById(R.id.button1);
			exit = (Button) rootView.findViewById(R.id.button1);
			exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			});

			
			TextView instructionHead = (TextView) rootView
					.findViewById(R.id.InstructionHead);
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
			case 1:
				exit.setVisibility(View.GONE);
				instructionHead.setText("Pull down the screen to refresh");
				Instructions.setBackgroundResource(R.drawable.pull_down);
				break;
			case 2:
				exit.setVisibility(View.GONE);
				instructionHead.setText("Click on the city name to change it");
				Instructions.setBackgroundResource(R.drawable.change_city);
				break;
			case 3:
				swipe_hand.setVisibility(View.GONE);
				instructionHead.setText("Click on a day to view more details");
				Instructions.setBackgroundResource(R.drawable.more_details);			
				break;

			default:
				break;
			}
			
			return rootView;
		}
	}

}