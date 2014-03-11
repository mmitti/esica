package jp.mmitti.sansan.common;

import java.util.EnumSet;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

public abstract class ScreenManagerActivity extends Activity implements ScreenManager{
	/**
	 * 現在表示しているScreen
	 */
	private Screen mActiveScreen;
	/**
	 * 現在表示しているScreenのView
	 */
	private View mActiveView;
	/**
	 * ScreenやそのViewが格納されるStack<BR>
	 * ここに格納されるのは現在表示していないもの<BR>
	 * 表示しているものは{@link #mActiveScreen}や{@link #mActiveView}に入る
	 */
	private Stack<Pair<Screen, View>> mScreenStack;
	/**
	 * 外部から処理を要求するためのハンドラー
	 */
	private Handler mHandler;
	
//	private TasQService;

	public ScreenManagerActivity(){
		mScreenStack = new Stack<Pair<Screen, View>>();
		mScreenStack.clear();
		mActiveScreen = null;
		mActiveView = null;
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(mActiveScreen != null) return;

		moveScreen(new VoidScreen());

//		startService(new Intent(this, SokutanService.class));
//		if(mService == null) bindService(new Intent(this, SokutanService.class), this, BIND_AUTO_CREATE);

		mHandler = new Handler();
	}

	@Override
	public void changeScreen(Screen s){
		if(!(mActiveScreen == null || mActiveScreen.getState().contains(ScreenState.NO_PUSH_STACK))){
			mScreenStack.push(Pair.create(mActiveScreen, mActiveView));
			//mActiveScreen.pause();//bug?
		}
		moveScreen(s);
	}

	@Override
	public void clearScreen(){
		mScreenStack.clear();
	}

	@Override
	public void moveScreen(Screen s){
		if(mActiveScreen != null){
			mActiveScreen.pause();//もしくはfinish
			AlphaAnimation a = new AlphaAnimation(1.0f, 0);
			a.setDuration(400);
			mActiveView.startAnimation(a);
			mActiveView = null;
		}
		mActiveScreen = s;
		View v = mActiveScreen.init(this);
		mActiveView = v;
		AlphaAnimation a = new AlphaAnimation(0, 1.0f);
		a.setDuration(400);

		acceptScreenState(mActiveScreen);
		setContentView(v);
		mActiveScreen.onCreate();
		v.startAnimation(a);
	}

	@Override
	public boolean popScreen(){
		if(mScreenStack.size() > 0){
			mActiveScreen.pause();//もしくはfinish
			// anim
			AlphaAnimation a = new AlphaAnimation(1.0f, 0);
			a.setDuration(400);
			mActiveView.startAnimation(a);
			AlphaAnimation b = new AlphaAnimation(0, 1.0f);
			b.setDuration(400);
			Pair<Screen, View> buf = mScreenStack.pop();
			mActiveView = buf.second;
			mActiveScreen = buf.first;
			
			setContentView(mActiveView);
			
			acceptScreenState(mActiveScreen);
			mActiveView.startAnimation(b);
			mActiveScreen.resume();
			return true;
		}
		return false;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		mActiveScreen.resume();
	}

	@Override
	public void onPause(){
		super.onPause();
		mActiveScreen.pause();
	}
	
	/**
	 * ScreenStateを反映する
	 * @param s 反映元のScreen
	 */
	private void acceptScreenState(final Screen s){
		EnumSet<ScreenState> flags = s.getState();
	}

	@Override
	public void onBackPressed(){
		if(mActiveScreen.getState().contains(ScreenState.NO_BACK)){
			return;
		}
		if(!popScreen()){
			super.onBackPressed();
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem i){
		if(mActiveScreen != null)
			if(mActiveScreen.onOptionsItemSelected(i))return true;
		return false;
	}

	@Override
	public Handler getHandler(){
		return mHandler;
	}

	public void setTitle(String s){
		super.setTitle(s);
	}
}
