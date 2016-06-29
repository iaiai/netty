package org.iaiai.netty.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * <br/>
 * Title: DateUtil.java<br/>
 * E-Mail: 176291935@qq.com<br/>
 * QQ: 176291935<br/>
 * Http: iaiai.iteye.com<br/>
 * Create time: 2013-5-24 下午5:23:30<br/>
 * <br/>
 *
 * @author 丸子
 * @version 0.0.1
 */
public class DateUtil {

    private DateUtil(){
        /** cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 将字符串转为日期类型,format 为null时,按照 yyyy-MM-dd HH:mm:ss.SSS 格式转换
     * @param strDate 字符串型日期
     * @param format 转换格式
     * @return
     */
    public static Date stringDateFormat(String strDate,String format){
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try{
            SimpleDateFormat sdf = (format==null)?new SimpleDateFormat(DEFAULT_FORMAT):new SimpleDateFormat(format);
            return sdf.parse(strDate);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 将字符串转为日期类型,format 为null时,按照 yyyy-MM-dd HH:mm:ss.SSS 格式转换
     * @param strDate 字符串型日期
     * @return
     */
    public static Date stringDateFormat(String strDate){
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try{
            return new SimpleDateFormat(DEFAULT_FORMAT).parse(strDate);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 将日期类型转为字符串,按照 yyyy-MM-dd HH:mm:ss.SSS 格式转换
     * @param date
     * @param format
     * @return
     */
    public static String dateStringFormat(Date date,String format){
        if(date==null)
            return null;
        try {
            SimpleDateFormat sdf = TextUtils.isEmpty(format)?new SimpleDateFormat(DEFAULT_FORMAT):new SimpleDateFormat(format);
            return sdf.format(date);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 将日期类型转为字符串,按照 yyyy-MM-dd HH:mm:ss.SSS 格式转换
     * @param date  字符串型日期
     * @return
     */
    public static String dateStringFormat(Date date){
        if(date==null)
            return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
            return sdf.format(date);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 获得参考日期前一天日期.
     *
     * @param date 参考日期
     * @return Date 参考日期前一天日期
     */
    public static Date getYesterday(Date date){
        return addDays(date, Calendar.DAY_OF_MONTH, -1);
    }

    /**
     * 获得参考日期后一天的日期.
     *
     * @param date 参考日期
     * @return Date 参考日期后一天日期
     */
    public static Date getTomorrow(Date date){
        return addDays(date,Calendar.DAY_OF_MONTH,1);
    }

    /**
     * 获得参考日期前两天的日期.
     *
     * @param date 参考日期
     * @return Date 参考日期前两天日期
     */
    public static Date getBeforeYesterday(Date date){
        return addDays(date,Calendar.DAY_OF_MONTH,-2);
    }

    /**
     * 获得参考日期后两天的日期.
     *
     * @param date 参考日期
     * @return Date 参考日期后两天日期
     */
    public static Date getAfertTomorrow(Date date){
        return addDays(date,Calendar.DAY_OF_MONTH,2);
    }

    public static Date addDays(Date date,int calendarField,int index) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(calendarField,index);

            return cal.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 获得参考日期月初的日期.
     *
     * @param date 参考日期
     * @return Date 参考日期月初日期
     */
    public static Date getMonthFistDay(Date date){
        return set(date,Calendar.DAY_OF_MONTH,1);
    }

    /**
     * 获得参考日期月末日期.
     *
     * @param date 参考日期
     * @return Date 参考日期月末日期
     */
    public static Date getMonthLastDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int actualMaximum = calendar.getActualMaximum(Calendar.DATE);
        return set(date,Calendar.DAY_OF_MONTH, actualMaximum);
    }

    private static Date set(Date date, int calendarField, int amount) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(calendarField, amount);
        return c.getTime();
    }

    /**
     * 返回两个时间相减的分钟数
     * @param start
     * @param end
     * @return
     */
    public static long dateReduction(Date start,Date end){
        long l = end.getTime()-start.getTime();
        return l/1000/60;
    }

    /**
     * 取得参考时间（若为空则取当前时间）加上指定秒
     * @param date
     * @param second
     * @return date
     */
    public static Date getAddSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 取得当前时间之前分钟的时间
     * @param time
     * @return
     */
    public static Date getBeforeDate(int time){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, (-1*time));
        return c.getTime();
    }

    /**
     * 比较两个日期是否相同，不包含时间
     *
     * @param start
     *            日期
     * @param end
     *            日期
     * @return 负数：start<end；0：start=end；整数：start>end
     */
    public static int compare(Date start, Date end) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = formatter.parse(formatter.format(start));
        Date dt2 = formatter.parse(formatter.format(end));

        long l = dt1.getTime() - dt2.getTime();
        return (int) (l / (24 * 3600 * 1000));
    }

    /**
     * 返回星期几
     *
     * @return String 返回星期几
     */
    public static String getWeek() {
        String s = null;
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("E");
            s = simpledateformat.format(new Date());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }

}
