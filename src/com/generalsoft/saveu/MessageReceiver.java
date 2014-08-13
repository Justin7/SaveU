package com.generalsoft.saveu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {
	Context mContext;

	public static final String TAG = Config.TAG;
	//public static final String INTENT_BT_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
	public static final String INTENT_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		String action = intent.getAction();
		Log.d(TAG , "onReceive()");
		/*if (action.equals(INTENT_BT_CHANGED)) {
			Toast.makeText(context, "Blutooth Change Received",  Toast.LENGTH_SHORT).show();
			mContext.startService(new Intent(mContext, RingtoneService.class));
		} else*/ if (action.equals(INTENT_SMS_RECEIVED)) {
			Toast.makeText(context, "SMS Received",  Toast.LENGTH_SHORT).show();

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
			Toast.makeText(context, msg,  Toast.LENGTH_SHORT).show();
			
			if (checkMessage(msg)) {
				mContext.startService(new Intent(mContext, RingtoneService.class));
			}
		}
	}

	private boolean checkMessage(String msg) {
		String message = msg.toLowerCase();
		if (!message.startsWith(Config.KEYWORD)) {
			return false;
		}
		
		if (message.endsWith(Config.RING)) {
			return true;
		}
		return false;
	}
}
