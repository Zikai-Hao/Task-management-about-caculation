package com.wugroup.calmanage.demo.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Haozk on 2019/11/28
 */
public class UrlUtil {
    public static String isUrl(String contents){
        if(contents.length()==0) return "";
        String regex = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contents);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String urlStr=matcher.group();
            for(int i=0;i<urlStr.length()-1;i++){
                if(contents.charAt(i)=='-'&&urlStr.charAt(i+1)=='-') {
                    urlStr=urlStr.substring(0,i);
                    break;
                }
            }
            StringBuffer replace = new StringBuffer();
            replace.append("<a href=\"").append(urlStr);
            replace.append("\" target=\"_blank\" style=\"color:#0000ff\">"+urlStr+"</a>");
            matcher.appendReplacement(result, replace.toString());
        }
        matcher.appendTail(result);
        //System.out.println(resultContents);
        return result.toString();

    }
}
