package jp.mmitti.sansan.create;

import net.arnx.jsonic.JSON;
import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.BitmapUtil;
import jp.mmitti.sansan.common.HTTPConnection;
import jp.mmitti.sansan.common.MyAsyncTask;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;

public class Preview extends CreateScreen {
	private Post mPost;
	@Override
	protected ViewGroup initView(ScreenManagerActivity activity) {
		ViewGroup g = (ViewGroup)ViewGroup.inflate(activity, R.layout.c_preview, null);
		mPost = new Post(activity.getHandler());
		mPost.start();
		
		return g;
	}
	
	
	private class Post extends MyAsyncTask{
		ProgressDialog mDlg;
		public Post(Handler handler) {
			super(handler);
			mDlg = new ProgressDialog((Context)mManager);
		}
		
		
		protected void preProcessOnUI(){
			mDlg.show();
		}
		
		@Override
		protected void doBackGround() throws InterruptedException {
			String json = JSON.encode(mManager.getData());
			String ret = HTTPConnection.Post("pass", json);
			Bitmap b = BitmapUtil.jsonToBitmap(ret);
		}
		
		
		protected void onFinishOnUI(){
			mDlg.dismiss();
			//TODO set
		}
		
		
	}
}
