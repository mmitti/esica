package jp.mmitti.esica.create;

import android.content.Intent;
import android.graphics.Bitmap;
import jp.mmitti.esica.common.ImageSelectDialog.OnBitmapRecvdListner;
import jp.mmitti.esica.common.data.ArgData;
import jp.mmitti.esica.system.ScreenManager;

public interface CreateManager extends ScreenManager {
	public ArgData getData();
	
	public void requestGetImage(OnBitmapRecvdListner recver, int x, int y);
	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public void save();
	
	public void make(Bitmap img);
	public boolean isEditMode();
}
