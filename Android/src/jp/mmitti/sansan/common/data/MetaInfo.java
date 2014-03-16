package jp.mmitti.sansan.common.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jp.mmitti.sansan.common.data.ArgData.PicMode;
import net.arnx.jsonic.JSONHint;

public class MetaInfo {
	
	public String name;
	public String school;
	public String department;
	public Calendar date;
	public PicMode PicMode;
	
	public MetaInfo(){
		name = "";
		school = "";
		department = "";
		date = null;
		PicMode = PicMode.NONE;
	}
	
	@JSONHint(ignore=true)
	public String getSummary(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm 作成");
		return sdf.format(date.getTime())+"\n"+name;
	}
	
	public String getDetail(){
		return school +"\n"+department;
	}
}
