package jp.mmitti.sansan;

import java.util.LinkedList;
import java.util.List;

import jp.mmitti.sansan.DrawerCard.OnCardSelectedListner;
import jp.mmitti.sansan.common.data.CardData;
import jp.mmitti.sansan.create.CreateActivity;
import jp.mmitti.sansan.system.MyAsyncTask;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainDrawer extends LinearLayout implements OnCardSelectedListner{
	private MainScreen ms;
	private LinearLayout mCards;
	private Update mUpdate;
	private int currentID;
	public MainDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		View v = View.inflate(context, R.layout.main_drawer, null);
		this.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		((Button)v.findViewById(R.id.add)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ms.addCard();
			}
		});
		mCards = (LinearLayout)v.findViewById(R.id.cards);
		
	}
	
	public void update(int cid){
		currentID = cid;
		for(int i = 0; i < mCards.getChildCount(); i++){
			DrawerCard dc = (DrawerCard)mCards.getChildAt(i);
			dc.updateSelecter(cid);
		}
	}
	
	
	public void init(MainScreen s, Handler h){
		ms = s;
		mUpdate = new Update(h);
		mUpdate.start();
	}
	

	public class Update extends MyAsyncTask{
		private boolean isCancel;
		private List<Integer> mCardIDList;
		private List<DrawerCard> mCardList;
		public Update(Handler handler) {
			super(handler);
			
		}
		
		public void preProcess(){
			//画像列挙
			mCardIDList = CardData.getCardList();
			mCardList = new LinkedList<DrawerCard>();
		}
		public void preProcessOnUI(){
			mCards.removeAllViews();
			for(int i : mCardIDList){
				DrawerCard dc = new DrawerCard(getContext(), i);
				mCards.addView(dc);
				dc.preLoad();
				mCardList.add(dc);
				dc.changeSelected(i == currentID);
				dc.onCardSelectedListner = MainDrawer.this;
			}
		}
		
		

		@Override
		protected void doBackGround() throws InterruptedException {
			for(DrawerCard dc : mCardList){
				dc.load(mHandler);
				if(isCancel)dc.cancel();
			}
		}
		
		public void onFinishOnUI(){
			
		}
		
	}


	@Override
	public void onCardSelected(int id) {
		ms.edit(id);
	}

}
