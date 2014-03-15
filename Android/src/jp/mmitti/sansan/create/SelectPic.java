package jp.mmitti.sansan.create;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.MyRadioGroup;
import jp.mmitti.sansan.common.MyRadioGroup.OnRadioCheckedListner;
import jp.mmitti.sansan.common.data.ArgData.PicMode;
import jp.mmitti.sansan.system.MyAsyncTask;
import jp.mmitti.sansan.system.Screen;
import jp.mmitti.sansan.system.ScreenManagerActivity;

public class SelectPic extends CreateScreen implements OnClickListener {
	private MyRadioGroup mMode;
	private FacePictureSelect mFacePictureSelect;
	private QRCreate mQRCreate;
	private Save mSave;
	@Override
	protected ViewGroup initView(ScreenManagerActivity activity) {
		ViewGroup g = (ViewGroup)ViewGroup.inflate(activity, R.layout.c_pic, null);
		mSave = new Save(activity.getHandler());
		Button next = (Button)g.findViewById(R.id.next);
		next.setOnClickListener(this);
		Button back = (Button)g.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		mFacePictureSelect = (FacePictureSelect)g.findViewById(R.id.facePictureSelect);
		mFacePictureSelect.init(mManager);
		mQRCreate = (QRCreate)g.findViewById(R.id.qRCreate);
		mQRCreate.init(mManager);
		
		mMode = new MyRadioGroup();
		mMode.addRadio((RadioButton)g.findViewById(R.id.facepic));
		mMode.addRadio((RadioButton)g.findViewById(R.id.qrcode));
		mMode.addRadio((RadioButton)g.findViewById(R.id.none));
		mMode.setListner(new OnRadioCheckedListner() {
			
			@Override
			public void onChecked(CompoundButton c) {
				changeMode(IdToPicMode(c.getId()));
			}
		});
		
		mMode.checkByID(picModeToId(mManager.getData().PicMode));
		restore();
		TextView step = (TextView)g.findViewById(R.id.text_step);
		
		if(mManager.isEditMode()){
			step.setText("ステップ2/3");//TODO 治したい
		}
		else{
			step.setText("ステップ3/4");
		}
		return g;
	}
	
	private void changeMode(PicMode m){
		
		mManager.getData().PicMode = m;
		
		if(m != PicMode.FACE)mFacePictureSelect.setVisibility(View.INVISIBLE);
		else mFacePictureSelect.setVisibility(View.VISIBLE);
		if(m != PicMode.QR)mQRCreate.setVisibility(View.INVISIBLE);
		else mQRCreate.setVisibility(View.VISIBLE);
		
	}
	
	
	
	private void restore(){
		
		switch(mManager.getData().PicMode){
			case FACE:
				mFacePictureSelect.setImage(mManager.getData().pic);
				break;
			case QR:
				mQRCreate.setData();
				break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.next:
			mManager.changeScreen(new SelectBackground());
			
			break;
		case R.id.back:
			mManager.popScreen();
			break;
		
		}
	}
	
	public void pause(){
		mSave.start();
	}

	public int picModeToId(PicMode p){
		switch(p){
			case QR:
				return R.id.qrcode;
			case FACE:
				return R.id.facepic;
		}
		return R.id.none;
	}
	
	public PicMode IdToPicMode(int i){
		switch(i){
			case R.id.qrcode:
				return PicMode.QR;
			case R.id.facepic:
				return PicMode.FACE;
		}
		return PicMode.NONE;
	}
	
	private class Save extends MyAsyncTask{
		private ProgressDialog mDlg;
		public Save(Handler handler) {
			super(handler);
			mDlg = new ProgressDialog((Context)mManager);
			mDlg.setMessage("しばらくお待ちください");
		}
		
		protected void preProcessOnUI(){
			mDlg.show();
		}

		@Override
		protected void doBackGround() throws InterruptedException {
			switch(mManager.getData().PicMode){
				case FACE:
					//img sump
					mManager.getData().pic = mFacePictureSelect.dumpImg();
					break;
				case QR:
					//QR create
					mManager.getData().pic = mQRCreate.getQRImage();
					break;
				default:
					mManager.getData().pic = "";	
			}
		}
		
		protected void onFinishOnUI(){
			mDlg.dismiss();
		}
		
	}
	
}
