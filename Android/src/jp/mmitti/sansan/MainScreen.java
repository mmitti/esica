package jp.mmitti.sansan;

import android.app.Activity;
import android.content.Intent;
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
import jp.mmitti.sansan.create.CreateActivity;

public class MainScreen extends Screen {
	
	private ActionBarDrawerToggle mDrawerToggle;
	@Override
	protected ViewGroup initView(final ScreenManagerActivity activity) {
		final DrawerLayout d = (DrawerLayout) DrawerLayout.inflate(activity, R.layout.main, null);
		
		mDrawerToggle = new ActionBarDrawerToggle(activity, d,R.drawable.ic_drawer, R.string.drawer_open,R.string.drawer_close){
			
			
		};
		d.setDrawerListener(mDrawerToggle);
		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
	    activity.getActionBar().setHomeButtonEnabled(true);
	    mDrawerToggle.syncState();
	    View v = activity.getActionBar().getCustomView();
	    ((Button)d.findViewById(R.id.add)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity, CreateActivity.class);
			//	intent.putExtra("SRC", 1);
				activity.startActivity(intent);
				activity.overridePendingTransition(0, 0);
				d.closeDrawers();
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
