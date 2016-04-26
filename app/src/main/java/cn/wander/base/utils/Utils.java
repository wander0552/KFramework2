package cn.wander.base.utils;

/**
 * Created by wander on 16/4/26.
 */
public class Utils {
    /**
     * safe to int
     * @param value
     * @param defaultValue
     * @return
     */
    public final static int converToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try{
            return Integer.valueOf(value.toString());

        }catch (Exception e){
            try{
                return Double.valueOf(value.toString()).intValue();
            }catch (Exception e1){
                return defaultValue;
            }
        }
    }
}
