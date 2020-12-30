package com.ccrental.composite.common.utillity;

import com.ccrental.composite.common.vos.UserVo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Converter {
    private Converter(){}

    public static void setUserVo(HttpServletRequest request, UserVo userVo) {
        request.getSession().setAttribute("UserVo", userVo);
    }

    public static UserVo getUserVo(HttpServletRequest request) {
        Object userVoObject = request.getSession().getAttribute("UserVo");
        UserVo userVo = null;
        if (userVoObject instanceof UserVo) {
            userVo = (UserVo) userVoObject;
        }
        return userVo;
    }

    public static String imageToString(MultipartFile image) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("data:image/png;base64,");
        stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(image.getBytes())));       // apache commons codec (mvn dependency add)
        return stringBuilder.toString();
    }

    public static int stringToInt(String idText, int fallback) {
        try {
            return Integer.parseInt(idText);
        } catch (Exception ignored) {
            return fallback;
        }
    }

    public static Date dateParsing(String str) {
        return Date.valueOf(str);
    }

    public static Time timeParsing(String str) throws ParseException {
//        return  Time.valueOf(str);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        long longTime = simpleDateFormat.parse(str).getTime();
        return new Time (longTime);
    }
}