package com.library.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

public class IntentUtil {

    public static void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextManager.getContext().startActivity(intent);
    }


    /**
     * 发送短信
     *
     * @param phone
     * @param content
     */
    public static void sendSmsMessage(String phone, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
        intent.putExtra("sms_body", content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextManager.getContext().startActivity(intent);
    }


    public static void pickContact(Activity activity) {
        Intent jumpIntent = new Intent(Intent.ACTION_PICK);
        //从有电话号码的联系人中选取
        jumpIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        activity.startActivityForResult(jumpIntent, 100);
    }
}
