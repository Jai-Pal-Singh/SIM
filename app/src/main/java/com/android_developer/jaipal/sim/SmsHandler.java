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
//        String urlToVisit="http://172.21.7.55:80/SnTInspection/Sms.php";
    String urlToVisit="http://192.168.43.22:80/SnTInspection/Sms.php";
//    String urlToVisit="http://172.21.5.81:8080/SnTServlet/sendSms.jsp";
    //    String urlToVisit="http://172.21.5.81:80/SnTInspection/Sms.php";
//String url="https://google.com";
    String phoneNo, message;

    public SmsHandler(Context mContext) {
        this.mContext = mContext;
        try{

            // CALL GetText method to make post method call
            GetText();
        }
        catch(Exception ex)
        {
            Log.e("Response Error : ","Url Exception");
        }
    }

    // Create GetText Metod
    public  void  GetText()  throws UnsupportedEncodingException
    {
        // Get user defined values
        phoneNo = "8947850652";
        message   = "asdf";


        // Create data variable for sent values to server

        String data = URLEncoder.encode("phoneNo", "UTF-8")
                + "=" + URLEncoder.encode(phoneNo, "UTF-8");

        data += "&" + URLEncoder.encode("message", "UTF-8") + "="
                + URLEncoder.encode(message, "UTF-8");


        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

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


            text = sb.toString();
        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        // Show response on activity

        Log.e("Response: ","Response : "+text);

    }

}
