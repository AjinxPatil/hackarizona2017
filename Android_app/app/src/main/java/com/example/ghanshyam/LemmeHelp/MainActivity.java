package com.example.ghanshyam.LemmeHelp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String accessCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.listView);
        accessCode=getIntent().getStringExtra("access");
        Log.d("accesscodemack",accessCode+"");
        String[] values = new String[]
                {
                       " Want to try out this gym near you?",

                "There is a new Sci Fi movie coming up. Want to check out show timings?",

        "Facts and myths surrounding depression..."

        ,"Checkout A.R. Rahman's new song."
                        ,"Have you recently lost weight?",
                        "How are you feeling?"
                };

        final String[] urls = new String[]
                {
                        "http://www.midvalleyathleticclub.net/",

                        "https://www.google.com/#q=passengers+show+times ",

                        "http://www.webmd.com/depression/ss/slideshow-depression-myths "

                        ,"https://www.youtube.com/watch?v=UObafba-pro "
                };
        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;
                Log.d("pos",position+"");
                if(position==6)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("How are you feeling?");


                    final EditText input = new EditText(MainActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    // alertDialog.setIcon(R.drawable.key);

                    alertDialog.setPositiveButton("Done",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });



                    alertDialog.show();
                }



                if(position==5)
                {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Weight")
                            .setMessage("Have you recently lost weight?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                // ListView Clicked item value
                if(position==1||position==2||position==3||position==4) {
                    String itemValue = (String) listView.getItemAtPosition(position);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position - 1]));
                    startActivity(browserIntent);
                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        StringBuilder stringBuilder=new StringBuilder();
        long now = System.currentTimeMillis();
        long last24 = now - 24*60*60*1000;//24h in millis
        String[] selectionArgs = new String[]{Long.toString(last24)};
        String selection = "date" + ">?";
        String[] projection = new String[]{"date"};
        //Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, selection, selectionArgs,null);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, Telephony.Sms.DATE+" DESC limit 10");

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
                stringBuilder.append(cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY))+";");
                //Log.d("sms",msgData);
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        new UploadAsyncTask("http://ec2-35-167-175-149.us-west-2.compute.amazonaws.com/postFbToken").execute(accessCode);
        new UploadAsyncTask("http://ec2-35-167-175-149.us-west-2.compute.amazonaws.com/postSms").execute(stringBuilder.toString());

    }

    private class UploadAsyncTask extends AsyncTask<String,Void,Void>
    {
        String url;
        UploadAsyncTask(String url)
        {
            this.url=url;
        }

        @Override
        protected Void doInBackground(String... params)
        {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            try {
                httppost.setEntity(new StringEntity(params[0]));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
               HttpResponse response= httpclient.execute(httppost);

            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            MultipartEntity mpEntity = new MultipartEntity(Ht);
            if (params[0] != null) {
                //File file = new File(filePath);
                Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + params[0].length());
                Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + params[0].exists());
                mpEntity.addPart("avatar", new FileBody(file, "application/octet"));
            }
            */

            return null;
        }
    }
}
