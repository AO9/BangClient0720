package com.gto.bang.util.string;

/**
 * Created by shenjialong on 16/7/12 21:50.
 */

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shenjialong on 16/4/25.
 */
public class StringUtil {

    public static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String SIM_DATE = "yyyy-MM-dd";

    public static Date formatDate(String dateStr, String format)
    {
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String formatDate(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getCurrentTime()
    {
        SimpleDateFormat format = new SimpleDateFormat(FMT_DATETIME);
        Date currentTime = new Date(System.currentTimeMillis());
        return format.format(currentTime);
    }

    public static List<String> split(String line, String separator)
    {
        return split(line, separator, false);
    }

    public static List<String> split(String line, String separator, boolean trimSpace)
    {
        if (line == null || separator == null || separator.length() == 0)
            return null;
        ArrayList<String> list = new ArrayList<String>();
        int pos1 = 0;
        int pos2;
        for (;;) {
            pos2 = line.indexOf(separator, pos1);
            if (pos2 < 0) {
                String s = line.substring(pos1);
                list.add(trimSpace ? s.trim() : s);
                break;
            }
            String s = line.substring(pos1, pos2);
            list.add(trimSpace ? s.trim() : s);
            pos1 = pos2 + separator.length();
        }
        for (int i = list.size() - 1; i >= 0 && list.get(i).length() == 0; --i) {
            list.remove(i);
        }
        return list;
    }

    public static long convertLong(String str, long defaults)
    {
        if (StringUtils.isEmpty(str)) {
            return defaults;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaults;
        }
    }

    public static int convertInt(String str, int defaults)
    {
        if (StringUtils.isEmpty(str)) {
            return defaults;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaults;
        }
    }

    public static String convertString(String str, String defaults)
    {
        if (StringUtils.isEmpty(str)) {
            return defaults;
        }
        return str;
    }

    public static boolean convertBoolean(String str)
    {
        return isNotEmpty(str) && (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes"));
    }

    public static List<Long> splitLong(String line, String sep, long def)
    {
        String[] ss = split2Array(line, sep);
        List<Long> r = new ArrayList<Long>();
        for (int i = 0; i < ss.length; ++i) {
            r.add(convertLong(ss[i], def));
        }
        return r;
    }

    public static List<Integer> splitInt(String line, String sep, int def)
    {
        String[] ss = split2Array(line, sep);
        List<Integer> r = new ArrayList<Integer>();
        for (int i = 0; i < ss.length; ++i) {
            r.add(convertInt(ss[i], def));
        }
        return r;
    }

    public static String[] split2Array(String line, String sep)
    {
        List<String> list = split(line, sep);
        if (list != null) {
            return list.toArray(new String[0]);
        }
        return null;
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.isEmpty();
    }

    // 校验一组对象是否为空
    public static boolean isEmpty(Object object)
    {
        return object == null || StringUtils.isEmpty(object.toString());
    }


    public static boolean isEmpty(Object [] objects)
    {
        for(int i=0;i<objects.length;i++){
            if(objects[i]==null|| StringUtils.isEmpty(objects[i].toString())){
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static String filterPhone(String phone)
    {
        if (isEmpty(phone) || phone.length() < 11)
            return phone;
        StringBuilder sb = new StringBuilder();
        sb.append(phone.substring(0, 3)).append("*****").append(phone.substring(8));
        return sb.toString();
    }

    public static String filterEmail(String mail)
    {
        if (isEmpty(mail))
            return mail;
        StringBuilder sb = new StringBuilder();
        if (mail.length() > 3) {
            sb.append(mail.substring(0, 2)).append("***").append(mail.charAt(mail.length() - 1));
        } else {
            sb.append(mail);
        }
        return sb.toString();
    }

    public static String filterName(String name)
    {
        if (isEmpty(name))
            return name;
        StringBuilder sb = new StringBuilder();
        sb.append(name.charAt(0));
        if (name.length() > 1) {
            for (int i = 1; i < name.length(); ++i) {
                sb.append("*");
            }
        }
        return sb.toString();
    }

}
