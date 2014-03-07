package jp.mmitti.sansan.common;

import java.util.EnumSet;

import android.content.Context;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * スクリーンクラス<BR>
 * initでViewを作る。ここで返したものがsetContextViewにせっとされる<BR>
 * 大幅にViewをあとから書き換えるならinitで返したものを持っておく
 * @author Masashi
 */
public abstract class Screen{
	protected ScreenManager mManager;
	/**
	 * Screenの様々な属性を保持する
	 */
	protected EnumSet<ScreenState> mScreenState;

	public Screen(){
		mScreenState = EnumSet.noneOf(ScreenState.class);
	}

	/**
	 * タッチイベント<BR>
	 * 今のところ未使用
	 * @param event
	 * @return trueならsuperの処理を続ける
	 */
	public boolean onTouchEvent(final MotionEvent event){
		return false;
	}

	/**
	 * 初期化時に呼ばれる。<BR>
	 * ここで作ったものが適用される。
	 * @param activity
	 * @return ViewGroup これがsetContextViewされる
	 */
	public ViewGroup init(final ScreenManagerActivity activity){
		mManager = activity;
		return initView(activity);
	}
	
	protected abstract ViewGroup initView(final ScreenManagerActivity activity);

	/**
	 * Viewがセットされた後適用される。（本来なら最初の描画時らしいが
	 */
	public void onCreate(){

	}

	/**
	 * pushされた時、アプリケーション自体のpauseが呼ばれたときに呼ばれる<BR>
	 * なお、これが呼ばれた後復帰するかどうかの保証はない
	 */
	public void pause(){

	}

	/**
	 * popされた時、アプリケーション自体のresumeが呼ばれたときに呼ばれる
	 */
	public void resume(){

	}

	/**
	 * ScreenStateのcloneを取得する
	 * @return
	 */
	public EnumSet<ScreenState> getState(){
		return mScreenState.clone();
	}
	
	/**
	 * メニューアイテムが選択された時に呼ばれる
	 * @param item
	 * @return
	 */
	public boolean onOptionsItemSelected(MenuItem item){
		return false;
	}

	/**
	 * LinerLayout(Vertical)を作る関数。<BR>
	 * 基本的にinitで使うから関数化した。
	 * @return
	 */
	protected LinearLayout createVerticalLinerLayout(Context context){
		LinearLayout r = new LinearLayout(context);
		r.setOrientation(LinearLayout.VERTICAL);
		r.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		return r;
	}
}