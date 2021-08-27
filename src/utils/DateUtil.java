package utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    //1.将字符串转换为util.Date对象
    public static Date strToUd(String str){
        Date date=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            date=sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    //2.将util.date转换为sql.date
    public static java.sql.Date udToSd(Date ud){
        return new java.sql.Date(ud.getTime());
    }
    //3.将util.date转换为字符串
    public static String udToStr(Date ud){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str=sdf.format(ud);
        return str;
    }
}
