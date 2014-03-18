package jp.mmitti.esica.system;
import android.view.ViewGroup;

/**
 * 何もしない空のスクリーン
 * @author Masashi
 *
 */
public class VoidScreen extends Screen{

	@Override
	protected ViewGroup initView(ScreenManagerActivity activity){
		return createVerticalLinerLayout(activity);
	}

}
