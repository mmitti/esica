package jp.mmitti.esica.create;

import android.view.ViewGroup;
import jp.mmitti.esica.system.Screen;
import jp.mmitti.esica.system.ScreenManagerActivity;

public abstract class CreateScreen extends Screen {

	protected CreateManager mManager;
	@Override
	public ViewGroup init(ScreenManagerActivity activity) {
		// TODO 自動生成されたメソッド・スタブ
		mManager = (CreateManager)activity;
		return super.init(activity);
	}

}
