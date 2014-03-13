package jp.mmitti.sansan;

import java.util.LinkedList;
import java.util.List;

import jp.mmitti.sansan.common.CardData;
import jp.mmitti.sansan.common.MyAsyncTask;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DrawerCard extends LinearLayout{
	private ImageView mImage;
	private ProgressBar mLoadProgress;
	private TextView mText;
	private int mID;
	public OnCardSelectedListner onCardSelectedListner;
	private boolean mIsSelected;
	private LinearLayout mCardFrame;
	private TextView mSelectText;
	public DrawerCard(Context context, int id) {
		super(context);
		View v = View.inflate(context, R.layout.drawer_card, null);
		this.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		
		mImage = (ImageView)v.findViewById(R.id.image);
		
		mText = (TextView)v.findViewById(R.id.text);
		mLoadProgress = (ProgressBar)v.findViewById(R.id.loading);
		mText.setVisibility(INVISIBLE);
		mLoadProgress.setVisibility(INVISIBLE);
		mID = id;
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onCardSelectedListner != null)onCardSelectedListner.onCardSelected(mID);
			}
		});
		mCardFrame = (LinearLayout)v.findViewById(R.id.frame);
		mSelectText = (TextView)v.findViewById(R.id.text_select);
		changeSelected(false);
	}
	
	public void updateSelecter(int selectedID){
		changeSelected(selectedID == mID);
	}
	
	public void changeSelected(boolean b){
		mIsSelected = b;
		if(b){
			mCardFrame.setBackgroundResource(R.drawable.drawer_card_selected);
			mSelectText.setVisibility(VISIBLE);
		}
		else{
			mCardFrame.setBackgroundResource(0);
			mSelectText.setVisibility(INVISIBLE);
		}
	}
	
	public void preLoad(){
		mLoadProgress.setVisibility(VISIBLE);

		mText.setVisibility(VISIBLE);
		mText.setText("読み込み中です。");
	}
	
	public void load(){
		Load l = new Load(getHandler());
		l.startSync();
	}
	
	public void cancel(){
		mLoadProgress.setVisibility(INVISIBLE);
		mText.setVisibility(VISIBLE);
		mText.setText("読み込みに失敗しました。");
	}
	
	public class Load extends MyAsyncTask{
		private Bitmap b;
		public Load(Handler handler) {
			super(handler);
			
		}
		
		

		@Override
		protected void doBackGround() throws InterruptedException {
			b = CardData.getImage(mID);
		}
		
		public void onFinishOnUI(){
			mLoadProgress.setVisibility(INVISIBLE);
			if(b == null)cancel();
			else{
				mText.setVisibility(INVISIBLE);
				mImage.setImageBitmap(b);
			}
		}
		
	}
	
	public interface OnCardSelectedListner{
		public void onCardSelected(int id);
	}
	
}
