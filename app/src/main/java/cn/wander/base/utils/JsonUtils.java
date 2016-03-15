package cn.wander.base.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {

	@SuppressWarnings("unchecked")
	public static Map<String, String> jsonToMap(String json) {
//		String jsonStr = "{\"age\":23,\"id\":123,\"name\":\"tt_2009\"," +  
//	            "\"province\":\"上海\",\"sex\":\"男\"}";  
	    JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e1) {
			return null;
		}    
		Map<String, String> result = new HashMap<String, String>();
		Iterator<String> iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			try {
				value = jsonObject.getString(key);
				result.put(key, value);
			} catch (JSONException e) {
				
			}
		}
 		return result;
	}
	
	public static ArrayList<String> jsonToList(String json) {
		JSONArray array;
		try {
			array = new JSONArray(json);
		} catch (Exception e) {
			return null;
		}

		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			try {
				result.add(array.getString(i));
			} catch (JSONException e) {

			}
		}
		return result;
	}

	public static void main(String[] args) {
		String json = "{\"result\":\"succ\",\"uid\":\"6508300\",\"sid\":\"1689263805\",\"lev\":\"41\",\"vip_lev\":\"0\",\"vip_type\":\"-1\",\"head\":\"http://img1.kuwo.cn/star/userhead/0/40/1350465710281_6508300s.jpg\",\"nick\":\"yangfan700\",\"dcinfo\":\"mp3-0;ape-0;mkv-0\",\"vip_expire\":\"704\",\"next_avail_date\":\"\",\"mp3\":\"0\",\"ape\":\"0\",\"mkv\":\"0\",\"msg\":\"登录成功\"}";
		Map<String, String> m = jsonToMap(json);
		System.out.println(m.get("result"));
	}
}
