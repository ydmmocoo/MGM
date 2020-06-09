package com.fjx.mg.utils;

import java.util.regex.Pattern;

public class RegUtil {
    public static boolean checkPassword(String password) {
        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(password).find();

    }
}
