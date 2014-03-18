package jp.mmitti.esica;

import java.io.IOException;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import jp.mmitti.esica.common.data.CardData;
import jp.mmitti.esica.common.data.ProgramData;
import jp.mmitti.esica.create.CreateActivity;
import jp.mmitti.esica.edit.EditActivity;
import jp.mmitti.esica.edit.EditPreview;
import jp.mmitti.esica.system.Screen;
import jp.mmitti.esica.system.ScreenManagerActivity;
import jp.mmitti.esica.system.VoidScreen;
import jp.mmitti.sansan.R;

public class MainScreen extends Screen {
	
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private MainDrawer mMainDrawer;
	private RelativeLayout mContent;
	private ProgramData mProgramData;
	@Override
	protected ViewGroup initView(final ScreenManagerActivity activity) {
		mDrawerLayout = (DrawerLayout) DrawerLayout.inflate(activity, R.layout.main, null);
		
		mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout,R.drawable.ic_drawer, R.string.drawer_open,R.string.drawer_close){
			
			
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		activity.getActionBar().setDisplayHomeAsUpEnabled(true);
	    activity.getActionBar().setHomeButtonEnabled(true);
	    mDrawerToggle.syncState();
	    View v = activity.getActionBar().getCustomView();
	    mContent = (RelativeLayout)mDrawerLayout.findViewById(R.id.left_draw);
	    mMainDrawer = (MainDrawer)mDrawerLayout.findViewById(R.id.left_drawer);
	    
	    mProgramData = new ProgramData();
	    update();
	    updateOnAdd();
		return mDrawerLayout;
	}
	
	public void resume(){
	}
	
	private void update(){
		mContent.removeAllViews();

		mProgramData.update();
		mMainDrawer.update(mProgramData.getCurrentID());
		if(mProgramData.getCurrentID() == ProgramData.DEFAULT_INF)mContent.addView(new DummyMainScreen((Activity)mManager, this));
		else{
			mContent.addView(new CurrentMainScreen((Activity)mManager, this, mProgramData.getCurrentID()));
			try {
				CardData.cpCard(mProgramData.getCurrentID(), null);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	
	public void pick(){
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}
	
	private void updateOnAdd(){
		mMainDrawer.init(this, mManager.getHandler());
	}
	
	public void addCard(){
		Activity activity = (Activity)mManager;
		Intent intent = new Intent(activity, CreateActivity.class);
		//	intent.putExtra("SRC", 1);
		activity.startActivityForResult(intent, CreateActivity.ACTIVITY_CODE);
		activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		mDrawerLayout.closeDrawers();
	}
	
	public void edit(int id){
		Activity activity = (Activity)mManager;
		Intent intent = new Intent(activity, EditActivity.class);
		intent.putExtra("SRC", id);
		activity.startActivityForResult(intent, EditActivity.ACTIVITY_CODE);


		activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		mDrawerLayout.closeDrawers();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode == EditActivity.ACTIVITY_CODE && resultCode == Activity.RESULT_OK){
			update();
			if(intent.getBooleanExtra("EDIT", false))updateOnAdd();
		}
		else if(requestCode == CreateActivity.ACTIVITY_CODE && resultCode == Activity.RESULT_OK){
			update();
			updateOnAdd();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem i){
		if(mDrawerToggle.onOptionsItemSelected(i)) return true;
		return false;
	}
	
	

}
