package jp.mmitti.sansan.create;

import android.content.Intent;
import android.graphics.Bitmap;
import jp.mmitti.sansan.common.ImageSelectDialog.OnBitmapRecvdListner;
import jp.mmitti.sansan.common.data.ArgData;
import jp.mmitti.sansan.system.ScreenManager;

public interface CreateManager extends ScreenManager {
	public ArgData getData();
	
	public void requestGetImage(OnBitmapRecvdListner recver, int x, int y);
	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public void save();
	
	public void make(Bitmap img);
}
