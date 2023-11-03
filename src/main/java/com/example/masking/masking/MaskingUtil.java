package com.example.masking.masking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingUtil {
    public static String MaskingOf(MaskingType maskType, String value){
        return switch(maskType){
            case NAME -> nameMaskOf(value);
            case PHONE_NUMBER -> phoneNumberMaskOf(value);
            case EMAIL -> emailMaskOf(value);
        };
    }
    private static String nameMaskOf(String value){
        // 홍*동 마스킹
        String regex = "(?<=.{1})(.*)(?=.$)";
        String maskedValue = value.replaceFirst(regex, "*".repeat(value.length() - 2));
        return maskedValue;
    }

    private static String phoneNumberMaskOf(String value){
        // 010-****-1234
        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";
        Matcher matcher = Pattern.compile(regex).matcher(value);
        if(matcher.find()) {
            String maskedValue =  matcher.group(0).replaceAll(matcher.group(2),"****");
            return maskedValue;
        }
        return value;
    }

    private static String emailMaskOf(String value){
        // abc****@gmail.com
        return value.replaceAll("(?<=.{3}).(?=[^@]*?@)", "*");
    }
}
