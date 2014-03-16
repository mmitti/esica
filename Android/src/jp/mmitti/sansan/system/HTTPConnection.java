package jp.mmitti.sansan.system;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



import java.net.URLEncoder;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.DropBoxManager.Entry;

public class HTTPConnection{
	private final static String URI = "misansan2.cloudapp.net";
	private final static int PORT = 4567;

	/*public static String PostJsonArgToParams(String path, String jsonarg){
		try{
			Map<String, String> map = JSON.decode(jsonarg);
			path+="?";
			int s = map.size();
			for(Map.Entry<String, String> e : map.entrySet()){
				s--;
				path+=e.getKey()+"="+URLEncoder.encode(e.getValue(),"UTF-8");
				if(s>0)path+="&";
			}
		}catch(Exception e){}
		
		return Post(path, "");
	}*/
	
	/**
	 * 
	 * @param path http://URI/path
	 * @param jsonarg
	 * @return
	 */
	public static String Post(String path, String jsonarg){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = null;
		try{
			post = new HttpPost(new URI("http://"+URI+":"+PORT+"/"+path));
		}catch(URISyntaxException e1){
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
 
        HttpResponse res = null;
        try {

	        StringEntity body = new StringEntity(jsonarg, "UTF-8");
            post.setEntity(body);
	        post.addHeader("Content-type", "application/json");
            res = httpClient.execute(post);
            String ret = EntityUtils.toString(res.getEntity(), "UTF-8");
			httpClient.getConnectionManager().shutdown();
			return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
	}
	
	
}
