package jp.mmitti.sansan.common;

import android.os.Handler;

public interface ScreenManager{

	/**
	* Screenを切り替えるときに呼ぶ<BR>
	* これを読んだ場合Stackに積まれる
	* @param s
	*/
	public void changeScreen(final Screen s);
	
	/**
	* Screenのスタックを消す<BR>
	* 戻りたくないスクリーン向け
	*/
	public void clearScreen();
	
	/**
	* Stackに積まずにシーンを移動する
	* @param s
	*/
	public void moveScreen(final Screen s);
	
	/**
	* 前の画面に戻る<BR>
	* @return 失敗時はfalseを返す
	*/
	public boolean popScreen();
	
	/**
	* TasQServiceを取得する<BR>
	* まだサービができてない場合はnullが帰る（ただ、Screenができるときにはサービスができてることが保証される）<BR>
	* ただし、サービスが切断ということも有る
	* @return Service
	*/
	/*public TasQService getService(){
	return mService;
	}*/
	
	/**
	* ハンドラーを取得する<BR>
	* これ経由でUIを操作する（UIスレッド以外の場合）
	* @return
	*/
	public Handler getHandler();

	public void setTitle(String string);
}