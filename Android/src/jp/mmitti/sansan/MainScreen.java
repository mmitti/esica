package jp.mmitti.sansan;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import jp.mmitti.sansan.common.VoidScreen;

public class MainScreen extends Screen {
	
	private ActionBarDrawerToggle mDrawerToggle;
	@Override
	protected ViewGroup initView(final ScreenManagerActivity activity) {
		DrawerLayout d = (DrawerLayout) DrawerLayout.inflate(activity, R.layout.main, null);
		
		mDrawerToggle = new ActionBarDrawerToggle(activity, d,R.drawable.ic_drawer, R.string.drawer_open,R.string.drawer_close){
			
			
		};
		d.setDrawerListener(mDrawerToggle);
		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
	    activity.getActionBar().setHomeButtonEnabled(true);
	    mDrawerToggle.syncState();
	    ((Button)d.findViewById(R.id.drawer_button)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				activity.getActionBar().setDisplayHomeAsUpEnabled (true);
				mManager.changeScreen(new VoidScreen());
			}
		});
		return d;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem i){
		if(mDrawerToggle.onOptionsItemSelected(i)) return true;
		return false;
	}

}
