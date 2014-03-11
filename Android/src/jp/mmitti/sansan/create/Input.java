package jp.mmitti.sansan.create;

import jp.mmitti.sansan.R;
import jp.mmitti.sansan.common.ScreenManagerActivity;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Input extends CreateScreen implements OnClickListener {
	
	private InputFilter[] romaFilter = {new RomaInputFilter()};
	private InputFilter[] kanjiFilter = {new KanjiInputFilter()};

	private EditText FamilyRuby;
	private EditText NameRuby;
	private EditText Family;
	private EditText Name;
	private EditText School;
	private EditText Department;
	private EditText Email;
	private EditText Tel;
	
	@Override
	protected ViewGroup initView(ScreenManagerActivity activity) {
		ViewGroup g = (ViewGroup)ViewGroup.inflate(activity, R.layout.c_input, null);
		
		Button next = (Button)g.findViewById(R.id.next);
		next.setOnClickListener(this);
		Button back = (Button)g.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		FamilyRuby = (EditText)g.findViewById(R.id.last_name_ruby);
		NameRuby = (EditText)g.findViewById(R.id.first_name_ruby);
		Family = (EditText)g.findViewById(R.id.last_name);
		Name = (EditText)g.findViewById(R.id.first_name);
		
		Family.setFilters(kanjiFilter);
		Name.setFilters(kanjiFilter);
		FamilyRuby.setFilters(romaFilter);
		NameRuby.setFilters(romaFilter);
		
		School = (EditText)g.findViewById(R.id.school);
		
		Department = (EditText)g.findViewById(R.id.dep);
		
		Email = (EditText)g.findViewById(R.id.email);
		
		Tel = (EditText)g.findViewById(R.id.tel);
		return g;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.next:
			mManager.changeScreen(new Preview());
			break;
		case R.id.back:
			mManager.popScreen();
			break;
		
		}
	}
	
	private class RomaInputFilter implements android.text.InputFilter{
		@Override
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dest, int dstart, int dend){
			SpannableStringBuilder t = (SpannableStringBuilder) dest;
			if(src.toString().matches("[0-9a-zA-Z@¥.¥_\\-]*")){
				return src;
			}
			
			
			return "";
		}
		
	}
	
	private class KanjiInputFilter implements InputFilter{
		@Override
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dest, int dstart, int dend){
			if(!src.toString().equals(" ")) {
				return src;
			}
			return "";
		}
		
	}
}
