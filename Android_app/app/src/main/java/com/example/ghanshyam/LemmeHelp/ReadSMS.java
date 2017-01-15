package com.example.ghanshyam.LemmeHelp;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadSMS extends Service {
    String email;
    public ReadSMS() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        email=intent.getStringExtra("email");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

       // Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, Telephony.Sms.DATE+" DESC limit 3");
        //Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, "datetime(date/1000, 'unixepoch') between date('now', '-1 day') and date('now')", null, null);
        StringBuilder stringBuilder=new StringBuilder();
        long now = System.currentTimeMillis();
        long last24 = now - 24*60*60*1000;//24h in millis
        String[] selectionArgs = new String[]{Long.toString(last24)};
        String selection = "date" + ">?";
        String[] projection = new String[]{"date"};
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, selection, selectionArgs,null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                /*
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    //msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    Log.d("name")
                }*/

                Log.d("date",""+new SimpleDateFormat("MM/dd/yyyy").format(new Date(cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE)))));
                Log.d("body",""+cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)));
                stringBuilder.append(cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)));
                //Log.d("sms",msgData);
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        //Intent i=new Intent(ReadSMS.this,Tr)
    }
}
