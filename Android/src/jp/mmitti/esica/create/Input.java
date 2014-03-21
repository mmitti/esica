package jp.mmitti.esica.create;

import jp.mmitti.esica.common.data.ArgData;
import jp.mmitti.esica.system.ScreenManagerActivity;
import jp.mmitti.sansan.R;
import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Input extends CreateScreen implements OnClickListener {
	
	private InputFilter[] romaFilter = {new RomaInputFilter(), new InputFilter.LengthFilter(20)};
	private InputFilter[] kanjiFilter = {new KanjiInputFilter(), new InputFilter.LengthFilter(5)};
	private InputFilter[] telFilter = {new TelInputFilter(), new InputFilter.LengthFilter(15)};
	private InputFilter[] emailFilter = {new EmailInputFilter(),new InputFilter.LengthFilter(100)};
	private InputFilter[] schootFilter = {new InputFilter.LengthFilter(20)};

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
		School.setFilters(schootFilter);
		Department.setFilters(schootFilter);
		
		Email = (EditText)g.findViewById(R.id.email);
		Email.setFilters(emailFilter);
		
		Tel = (EditText)g.findViewById(R.id.tel);
		
		Tel.setFilters(telFilter);
		setData();
		
		TextView step = (TextView)g.findViewById(R.id.text_step);
		
		if(mManager.isEditMode()){
			back.setVisibility(View.INVISIBLE);
			step.setText("ステップ1/3");//TODO 治したい
		}
		else{
			step.setText("ステップ2/4");
		}
		return g;
	}
	
	@Override
	public void onClick(View v) {
		InputMethodManager inputMethodManager =   
	            (InputMethodManager)((Activity)mManager).getSystemService(Context.INPUT_METHOD_SERVICE);  
	      inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  
		switch(v.getId()){
		
		case R.id.next:
			if(check((Activity)mManager))mManager.changeScreen(new SelectPic());
			break;
		case R.id.back:
			mManager.popScreen();
			break;
		
		}
	}
	
	private boolean check(Activity a){
		//TODO 必須項目及び検証
		if(!Email.getEditableText().toString().matches("[a-zA-Z0-9_\\.\\-]+@[A-Za-z0-9_\\.\\-]+\\.[A-Za-z0-9_\\.\\-]+")){
			Toast.makeText(a, "メールアドレスの形式が不正です", Toast.LENGTH_LONG).show();
			return false;
		}
		if(!Tel.getEditableText().toString().matches("^((\\d+)-?)*")){
			Toast.makeText(a, "電話番号の形式が不正です", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void resume(){
		super.resume();
		setData();
	}
	
	private void setData(){
		ArgData d = mManager.getData();
		Family.setText(d.family.trim());
		Name.setText(d.name.trim());
		FamilyRuby.setText(d.rubi_family.trim());
		NameRuby.setText(d.rubi_name.trim());
		
		School.setText(d.school.trim());
		Department.setText(d.department.trim());
		
		Email.setText(d.mail.trim());
		Tel.setText(d.tel.trim());
	}
	
	@Override
	public void pause(){
		super.pause();
		ArgData d = mManager.getData();
		d.family = Family.getEditableText().toString().trim();
		d.name = Name.getEditableText().toString().trim();
		d.rubi_family = FamilyRuby.getEditableText().toString().trim();
		d.rubi_name = NameRuby.getEditableText().toString().trim();
		
		d.school = School.getEditableText().toString().trim();
		d.department = Department.getEditableText().toString().trim();
		
		d.mail = Email.getEditableText().toString().trim();
		d.tel = Tel.getEditableText().toString().trim();
		
	}
	
	private class RomaInputFilter implements android.text.InputFilter{
		@Override
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dest, int dstart, int dend){
			if(dest instanceof SpannableStringBuilder){
				SpannableStringBuilder t = (SpannableStringBuilder) dest;
				if(src.toString().matches("[0-9a-zA-Z@¥.¥_\\-]*")){
					return src;
				}
			}
			if(dest instanceof SpannedString)return src;
			
			return "";
		}
		
	}
	
	private class TelInputFilter implements android.text.InputFilter{
		@Override
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dest, int dstart, int dend){
			if(dest instanceof SpannableStringBuilder){
				StringBuilder sb = new StringBuilder();
				sb.append(dest.toString());
				sb.insert(dstart, src.toString().subSequence(start, end));
				String result = sb.toString();
				if(result.matches("^((\\d+)-?)*")){
					return src;
				}
			}
			if(dest instanceof SpannedString)return src;
			
			return "";
		}
		
	}
	//[a-zA-Z0-9_\\.\\-]+@[A-Za-z0-9_\\.\\-]+\\.[A-Za-z0-9_\\.\\-]+
	//最終チェック用
	private class EmailInputFilter implements android.text.InputFilter{
		@Override
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dest, int dstart, int dend){
			if(dest instanceof SpannableStringBuilder){
				if(src.toString().matches("[a-zA-Z0-9_@\\.\\-]*")){
					return src;
				}
			}
			if(dest instanceof SpannedString)return src;
			
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
