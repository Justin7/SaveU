package com.gs.saveu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {
	Context mContext;

	public static final String TAG = Config.TAG;
	//public static final String INTENT_BT_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
	public static final String INTENT_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		String action = intent.getAction();
		/*Log.d(TAG , "onReceive()");
		if (action.equals(INTENT_BT_CHANGED)) {
			String msg = "save@1234@ring";
			if (checkMessage(msg)) {
				mContext.startService(new Intent(mContext, RingtoneService.class));
			}
		} else */if (action.equals(INTENT_SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				return;
			}
			
			Object[] pdusObj = (Object[]) bundle.get("pdus");
			if (pdusObj == null) {
				return;
			}
			
			String msg = "";
			SmsMessage[] messages = new SmsMessage[pdusObj.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu( (byte[])pdusObj[i] );
				msg = messages[i].getMessageBody();
			}
			if (checkMessage(msg)) {
				mContext.startService(new Intent(mContext, RingtoneService.class));
			}
		}
	}

	private boolean checkMessage(String msg) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		String password = sharedPrefs.getString("prefUserPassword", "1234");
		
		String message = msg.toLowerCase();
		if (!message.startsWith(Config.KEYWORD)) {
			return false;
		}
		
		StringBuilder sb = new StringBuilder()
		.append(Config.KEYWORD)
		.append(password)
		.append("@")
		.append(Config.RING);

		if (message.equals(sb.toString())) {
			return true;
		}
		return false;
	}
}
