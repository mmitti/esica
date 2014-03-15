package jp.mmitti.sansan.create;

import java.util.EnumSet;

import net.arnx.jsonic.JSON;
import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.Utils;
import jp.mmitti.sansan.common.data.ArgData;
import jp.mmitti.sansan.system.HTTPConnection;
import jp.mmitti.sansan.system.MyAsyncTask;
import jp.mmitti.sansan.system.ScreenManagerActivity;
import jp.mmitti.sansan.system.ScreenState;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Preview extends CreateScreen {
	private Post mPost;
	private ImageView mImg;
	private AlertDialog mCancelDialog;
	private Save mSave;
	
	public Preview() {
		mScreenState = EnumSet.of(ScreenState.NO_BACK, ScreenState.NO_PUSH_STACK);
	}
	@Override
	protected ViewGroup initView(final ScreenManagerActivity activity) {
		
		ViewGroup g = (ViewGroup)ViewGroup.inflate(activity, R.layout.c_preview, null);
		mImg = (ImageView)g.findViewById(R.id.image);
		mPost = new Post(activity.getHandler());
		mPost.start();
		mSave = new Save(activity.getHandler());
		mManager.clearScreen();
		activity.getActionBar().setDisplayHomeAsUpEnabled(false);
		AlertDialog.Builder b = new AlertDialog.Builder(activity);
		b.setTitle("データを破棄しますか?");
		b.setPositiveButton("はい", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				activity.setResult(Activity.RESULT_CANCELED);
				activity.finish();
			}
		});
		b.setNegativeButton("いいえ", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		mCancelDialog = b.create();
		
		Button cancel = (Button)g.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				mCancelDialog.show();
			}
		});
		
		Button save = (Button)g.findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mSave.start();
			}
		});
		
		return g;
	}
	
	
	private class Post extends MyAsyncTask{
		ProgressDialog mDlg;
		private Bitmap bitmap;
		public Post(Handler handler) {
			super(handler);
			mDlg = new ProgressDialog((Context)mManager);
			res = ((Activity)mManager).getResources();
			mDlg.setMessage("名刺を作成しています。");
		}
		
		private Resources res;
		protected void preProcessOnUI(){
			mDlg.show();
		}
		
		@Override
		protected void doBackGround() throws InterruptedException {
			ArgData d = mManager.getData();
	if(d.back.trim().equals(""))
		d.back = Utils.bitmapToBase64(BitmapFactory.decodeResource(res, R.drawable.dummy));
	if(d.pic.trim().equals(""))
		d.pic = Utils.bitmapToBase64(BitmapFactory.decodeResource(res, R.drawable.dummy_s));
			//TODO 将来的に不要になる 
			String json = JSON.encode(d);
			String ret = HTTPConnection.PostJsonArgToParams("pass", json);
			Thread.sleep(1000);
			bitmap = Utils.base64ToBitmap(mManager.getData().back);
			mManager.make(bitmap);
		}
		
		
		protected void onFinishOnUI(){
			mImg.setImageBitmap(bitmap);
			mDlg.dismiss();

		}
		
		
	}
	
	private class Save extends MyAsyncTask{
		ProgressDialog mDlg;
		public Save(Handler handler) {
			super(handler);
			mDlg = new ProgressDialog((Context)mManager);
			mDlg.setMessage("保存しています。");
		}
		
		
		protected void preProcessOnUI(){
			mDlg.show();
		}
		
		@Override
		protected void doBackGround() throws InterruptedException {
			mManager.save();
		}
		
		
		protected void onFinishOnUI(){
			mDlg.dismiss();
			((Activity)mManager).setResult(Activity.RESULT_OK);
			((Activity)mManager).finish();
		}
		
		
	}

}
