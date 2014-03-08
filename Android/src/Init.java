import android.view.ViewGroup;
import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;


public class Init extends Screen {

	@Override
	protected ViewGroup initView(ScreenManagerActivity activity) {
		ViewGroup g = (ViewGroup)ViewGroup.inflate(activity, R.layout.c_init, null);
		
		return g;
	}

}
