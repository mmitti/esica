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
import android.content.res.Resources;
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
		mUse = (Button)v.findViewById(R.id.use);
		mUse.setOnClickListener(this);
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
	
		mImg = (ImageView)v.findViewById(R.id.image);
		mImgButon = v.findViewById(R.id.imgbtn);
		mRes = activity.getResources();
		mProgramData = new ProgramData();
		mTextFailed = (TextView)v.findViewById(R.id.text_failed);
		mTitle = (TextView)v.findViewById(R.id.text);
		mMessage = (TextView)v.findViewById(R.id.text_detail);
		
		return v;
	}
	
	public void resume(){
		updateView();
	}
	private Button mUse;
	private Resources mRes;
	private ImageView mImg;
	private View mImgButon;
	private TextView mTextFailed;
	private TextView mTitle;
	private TextView mMessage;
	
	private void updateView(){
		mProgramData.update();
		Bitmap bmp = CardData.getImage(ID);
		if(bmp == null){
			mImg.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.dummycard2));
			mTextFailed.setVisibility(View.VISIBLE);
			mImgButon.setVisibility(View.INVISIBLE);
		}
		else{
			mImg.setImageBitmap(bmp);
			
			mImgButon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					((Activity)mManager).startActivity(CardData.openAs(ID));
				}
			});
			
		}
		if(mProgramData.getCurrentID() == ID){
			mUse.setVisibility(View.INVISIBLE);
		}
		MetaInfo meta;
		
		try {
			meta = CardData.getMetaInfo(ID);
		
		
		mTitle.setText(meta.getSummary());
		mMessage.setText(meta.getDetail());
		} catch (IOException e1) {
		}
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
