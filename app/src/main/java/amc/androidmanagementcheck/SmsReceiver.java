package amc.androidmanagementcheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSBroadcastReceiver";


    SharedPreferences sharedpreferences;
    String encrypted = "";
    String key;

    Processor proc;


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction() == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Bundle bundle = intent.getExtras();

            String info = intent.getStringExtra("format");

            if (bundle != null) {

                Object[] pdus = (Object[]) bundle.get("pdus");

                final SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], info);
                }

                if (messages.length > -1) {
                    Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
                    Toast.makeText(context, messages[0].getMessageBody(), Toast.LENGTH_LONG).show();
                    checkSMS(messages[0].getMessageBody(), context);
                }
            }
        }
    }


    void checkSMS(String SMS, Context context)
    {
        if(!SMS.contains(" ") && SMS.contains(":")){
            String[] instructions = SMS.split(":");
            if(instructions.length == 2) {

                sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);

                String defaultSting = "hello";
                try {
                    encrypted = AESUtils.encrypt(defaultSting);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.key = this.sharedpreferences.getString("key", encrypted);
                String pass = "";
                try {
                    pass = AESUtils.encrypt(instructions[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(this.key.equals(pass)){
                    this.abortBroadcast();
                    Processor.process(instructions[1]);
                }
            }

        }
    }

}
