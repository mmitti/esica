package jp.mmitti.sansan.create;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.Screen;
import jp.mmitti.sansan.common.ScreenManagerActivity;


public class Init extends CreateScreen {

	@Override
	protected ViewGroup initView(ScreenManagerActivity activity) {
		ViewGroup g = (ViewGroup)ViewGroup.inflate(activity, R.layout.c_init, null);
		
		Button next = (Button)g.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mManager.changeScreen(new Input());
			}
		});
		
		return g;
	}

}
