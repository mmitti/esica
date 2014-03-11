package jp.mmitti.sansan.create;

import android.content.Intent;
import jp.mmitti.sansan.common.Data;
import jp.mmitti.sansan.common.ImageSelectDialog.OnBitmapRecvdListner;
import jp.mmitti.sansan.common.ScreenManager;

public interface CreateManager extends ScreenManager {
	public Data getData();
	
	public void requestGetImage(OnBitmapRecvdListner recver, int x, int y);
	public void onActivityResult(int requestCode, int resultCode, Intent data);
}
