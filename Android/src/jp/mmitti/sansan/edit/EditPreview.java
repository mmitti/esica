package jp.mmitti.sansan.edit;

import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.CardData;
import jp.mmitti.sansan.common.MyAsyncTask;
import jp.mmitti.sansan.common.ProgramData;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EditPreview extends Screen implements OnClickListener{
	private ProgramData mProgramData;
	private CardData mCardData;
	private AlertDialog mRemoveDialog;
	private Remove mRemove;
	private int ID;
	
	public EditPreview(int id){
		ID = id;
	}
	
	@Override
	protected ViewGroup initView(ScreenManagerActivity activity) {
		ViewGroup v = (ViewGroup)ViewGroup.inflate(activity, R.layout.edit_menu, null);
		Button use = (Button)v.findViewById(R.id.use);
		use.setOnClickListener(this);
		Button rm = (Button)v.findViewById(R.id.remove);
		rm.setOnClickListener(this);
		
		
		AlertDialog.Builder b = new Builder(activity);
		
		b.setMessage("この名刺を削除しますか？");
		b.setPositiveButton("はい", new Dialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mRemove.start();
				dialog.dismiss();
			}
		});
		b.setNegativeButton("いいえ", new Dialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		mRemoveDialog = b.create();
		mRemove = new Remove(activity.getHandler());
	
		mCardData = new CardData(ID);
		ImageView img = (ImageView)v.findViewById(R.id.image);
		Bitmap bmp = mCardData.cardImg;
		if(bmp == null){
			img.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.dummycard2));
			TextView t = (TextView)v.findViewById(R.id.text_failed);
			t.setVisibility(View.VISIBLE);
		}
		else img.setImageBitmap(bmp);
		mProgramData = new ProgramData();
		if(mProgramData.getCurrentID() == ID){
			use.setVisibility(View.INVISIBLE);
		}
		
		return v;
	}
	

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.use:
				((EditActivity)mManager).use();
				break;
			case R.id.remove:
				mRemoveDialog.show();
				break;
		}
	}
	
	private class Remove extends MyAsyncTask{
		private ProgressDialog mDlg;
		public Remove(Handler handler) {
			super(handler);
			mDlg = new ProgressDialog((Context)mManager);
		}
		
		protected void preProcessOnUI(){
			mDlg.show();
		}

		@Override
		protected void doBackGround() throws InterruptedException {
			mCardData.remove();
			if(ID == mProgramData.getCurrentID())mProgramData.setCurrentID(ProgramData.DEFAULT_INF);
		}
		
		protected void onFinishOnUI(){
			((EditActivity)mManager).remove();
		}
		
	}

	

}
