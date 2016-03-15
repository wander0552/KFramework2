/**
 * 
 */
package cn.wander.base.utils;

import java.io.UnsupportedEncodingException;


/**
 * @author 李建衡：jianheng.li@kuwo.cn
 *
 */
public class TextUtils {
	
	/**
     * 将bytes转换成十六进制字符串
     * @param infos byte 数组
     * @param separator 分隔符
     */
    public static String byte2HexStr(byte[] infos, String separator) {
    	String stmp = "";
        StringBuilder sb = new StringBuilder();
        
        for (byte info : infos) {
        	stmp = Integer.toHexString(info & 0xFF);
        	sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
        	sb.append(separator);
        }
            
        return sb.toString().toUpperCase().trim();   
    }
    
    public static String byte2HexStr(byte[] infos) {
    	return byte2HexStr(infos, " ");
    }
    
    /**
     * 将bytes转换成十六进制字符串
     * @param infos byte 数组
     * @param separator 分隔符
     */
    public static String String2HexStr(CharSequence str, String separator) {
    	String stmp = "";
        StringBuilder sb = new StringBuilder();
        
        final int len = str.length();
    	
    	for (int i = 0; i < len; ++i) {
    		stmp = Integer.toHexString(str.charAt(i) >>> 8 & 0xFF);
    		sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
        	sb.append(separator);
        	
        	stmp = Integer.toHexString(str.charAt(i) & 0xFF);
        	sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
        	sb.append(separator);
        }
            
        return sb.toString().toUpperCase().trim();   
    }
    
    public static String String2HexStr(CharSequence str) {
    	return String2HexStr(str, " ");
    }
    
    
    
    public static final String SPEC_CHAR = "~$\\/:,;*?|～&";

    /**
     * 返回真，如果给定的字符串是个乱码
     */
    public static boolean isGarbled(CharSequence str) {
    	if (android.text.TextUtils.isEmpty(str)) {
    		return false;
    	}
 		
    	final int len = str.length();
    	
    	for (int i = 0; i < len; ++i) {
    		if (SPEC_CHAR.indexOf(str.charAt(i)) == -1) {
    			return false;
    		}
        }
    	
        return true;
    }

    /**
     * 返回真，如果给定的字符串可以用指定的编码格式转换
     * @throws UnsupportedEncodingException
     */
    public static boolean isEncodable(String s, String encoding) throws UnsupportedEncodingException {
    	String s2;
    	
		try {
			s2 = new String(s.getBytes(encoding), encoding);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
    	
    	return s2.equals(s);
    }
    

    /**
     * 强制转码，不具有通用性
     */
    public static String changeEncode(String string) {
    	if (android.text.TextUtils.isEmpty(string)) {
    		return string;
    	}
    	
    	try {
    		if (!isEncodable(string, "GBK")) {
    			String temp =  new String(string.getBytes("ISO-8859-1"), "GBK");
    			
    			if (!isGarbled(temp)) {
    				return temp;
    			} else {
    				temp =  new String(string.getBytes("ISO-8859-1"), "UTF-8");
    				return temp;
    			}
			}   
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
		return string;
	}  
}
