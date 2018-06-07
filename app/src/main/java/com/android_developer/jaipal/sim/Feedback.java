package com.android_developer.jaipal.sim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;


public class Feedback extends AppCompatActivity {
    EditText editText;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String recipients;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.feedback_activity );

        initUISetUp();
        recipients = getResources().getString( R.string.feedbackRecipients );
    }

    private void initUISetUp() {
        editText = findViewById( R.id.feedbackEditText );
    }

    public void saveFeedbackData(View view) {
        if(validateInput()){
            SendFeedback task = new SendFeedback();
            task.execute();
        }
    }

    private boolean validateInput() {
        Boolean isAnyFieldsEmpty = false;
        if (editText.getText().toString().isEmpty()) {
            isAnyFieldsEmpty = true;
            editText.setError( "Empty Input Error" );
        }
        return !isAnyFieldsEmpty;
    }

    private int sendEmail(String subject, String body, String sender , String recipents) throws Exception {
        int success = 0;
        try {
            GMailSender send = new GMailSender( "sntinspection@gmail.com", "nwrailway" );
            send.sendMail( subject, body, sender, recipents );
            success = 1;
        }
        catch (Exception e) {
            Log.e("error", e.getMessage(), e);
            success = -1;
        }
        return success;
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( Feedback.this );
        alertDialogBuilder.setMessage( msg );
        alertDialogBuilder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//                Toast.makeText( LoginActivity.this, "You clicked ok button", Toast.LENGTH_LONG ).show();
            }
        } );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public class SendFeedback extends AsyncTask<Void, Void, Void> {
        int userCount = -2;
        ProgressDialog pd;
        final GetData getData = new GetData();

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( Feedback.this );
            pd.setMessage( "Sending Feedback..." );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(getData.isInternetConnected()) {
                    sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
                    String body = "The user " + sharedpreferences.getString( "authName", "" ) + " (" + sharedpreferences.getString( "authDesignation", "" ) + ") whose registered phone number is " + sharedpreferences.getString( "phone", "" ) + " has given following feedback :\r\n\n" + editText.getText().toString() + "";
                    userCount = sendEmail( "Feedback by " + sharedpreferences.getString( "phone", "" ), body, sharedpreferences.getString( "email", "" ), recipients );
                }
                else userCount = -1;
            } catch (Exception e) {
                Log.e("error", e.getMessage(), e);
//                return "Email Not Sent";
            }
//            return "Email Sent";
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
            if (pd != null)
                pd.dismiss();
            if(userCount==-1)
                showAlertDialog( "Check Your Internet Connection!" );
            else if(userCount==1){
//                showAlertDialog("Feedback Sent!!!");
                Toast.makeText(Feedback.this,"Feedback Sent",Toast.LENGTH_SHORT).show();
                editText.setText( "" );
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {


        }
    }
}
