package com.android_developer.jaipal.sim;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class SmsHandler {


    Context mContext;
    String urlToVisit;
    String phone, msg;
    int status = 0;

    public SmsHandler(Context mContext,String phoneNo,String message ) {
        this.mContext = mContext;
        this.phone = phoneNo;
        this.msg = message;
        urlToVisit = mContext.getResources().getString(R.string.urlToVisit);
        try{
            // CALL GetText method to make post method call
            GetText();
        }
        catch(Exception ex)
        {
            Log.e("Response Error : ","Url Exception");
            status = -1;
        }
    }

    // Create GetText Method
    public  void  GetText()  throws UnsupportedEncodingException{
        // Create data variable for sent values to server
        String data = URLEncoder.encode("phone", "UTF-8")
                + "=" + URLEncoder.encode(phone, "UTF-8");

        data += "&" + URLEncoder.encode("msg", "UTF-8") + "="
                + URLEncoder.encode(msg, "UTF-8");
        String text = "";
        BufferedReader reader=null;
        // Send data
        try{
            // Defined URL  where to send data
            URL url = new URL(urlToVisit);
            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }

            status = 1;
            text = sb.toString();
        }
        catch(Exception ex)
        {
            status = -2;
        }
        finally
        {
            try
            {
                reader.close();
            }

            catch(Exception ex) {
                status = -2;
            }
        }

        // Show response on activity

        Log.e("Response: ","Response : "+text);

    }

    public int sendSmsStatus(){
        return status;
    }

}
