package com.coeus.weatherforecast;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.coeus.weatherforecast.adapters.AllTabsFragmentAdapter;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements
ActionBar.TabListener{

	private ViewPager mainViewPager;
	private AllTabsFragmentAdapter allTabsFragmentAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Dashboard", "Major Cities", "Map Location" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		mainViewPager = (ViewPager) findViewById(R.id.mainViewPager); 
		actionBar = getActionBar(); // Initilization of action bar
		actionBar.setDisplayShowTitleEnabled(false); // Hiding title bar
		actionBar.setDisplayShowHomeEnabled(false);
		allTabsFragmentAdapter = new AllTabsFragmentAdapter(getSupportFragmentManager()); // Tabs fragment adapter for view pager

		mainViewPager.setAdapter(allTabsFragmentAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position); //Navigation
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mainViewPager.setCurrentItem(tab.getPosition()); // Selection of tabs
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	
}
