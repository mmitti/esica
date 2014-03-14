package jp.mmitti.sansan.edit;

import java.io.IOException;

import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.data.CardData;
import jp.mmitti.sansan.common.data.MetaInfo;
import jp.mmitti.sansan.common.data.ProgramData;
import jp.mmitti.sansan.system.MyAsyncTask;
import jp.mmitti.sansan.system.Screen;
import jp.mmitti.sansan.system.ScreenManagerActivity;
import jp.mmitti.sansan.system.ScreenState;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Activity;
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
	private AlertDialog mRemoveDialog;
	private Remove mRemove;
	private int ID;
	
	public EditPreview(int id){
		ID = id;
		mScreenState.add(ScreenState.NO_FADE_IN);
	}
	
	@Override
	protected ViewGroup initView(final ScreenManagerActivity activity) {
		ViewGroup v = (ViewGroup)ViewGroup.inflate(activity, R.layout.edit_menu, null);
		Button use = (Button)v.findViewById(R.id.use);
		use.setOnClickListener(this);
		Button rm = (Button)v.findViewById(R.id.remove);
		rm.setOnClickListener(this);
		Button e = (Button)v.findViewById(R.id.edit);
		e.setOnClickListener(this);
		
		
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
	
		ImageView img = (ImageView)v.findViewById(R.id.image);
		View img_btn = v.findViewById(R.id.imgbtn);
		Bitmap bmp = CardData.getImage(ID);
		if(bmp == null){
			img.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.dummycard2));
			TextView t = (TextView)v.findViewById(R.id.text_failed);
			t.setVisibility(View.VISIBLE);
			img_btn.setVisibility(View.INVISIBLE);
		}
		else{
			img.setImageBitmap(bmp);
			
			img_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					activity.startActivity(CardData.openAs(ID));
				}
			});
			
		}
		mProgramData = new ProgramData();
		if(mProgramData.getCurrentID() == ID){
			use.setVisibility(View.INVISIBLE);
		}
		MetaInfo meta;
		TextView t = (TextView)v.findViewById(R.id.text);
		try {
			meta = CardData.getMetaInfo(ID);
		
		
		t.setText(meta.getSummary());
		t = (TextView)v.findViewById(R.id.text_detail);
		t.setText(meta.getDetail());
		} catch (IOException e1) {
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
			case R.id.edit:
				((EditActivity)mManager).startEdit();
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
			CardData.remove(ID);
			if(ID == mProgramData.getCurrentID())mProgramData.setCurrentID(ProgramData.DEFAULT_INF);
		}
		
		protected void onFinishOnUI(){
			((EditActivity)mManager).remove();
		}
		
	}

	

}
