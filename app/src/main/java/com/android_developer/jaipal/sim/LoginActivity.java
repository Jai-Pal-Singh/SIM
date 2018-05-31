package com.android_developer.jaipal.sim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    EditText loginPhoneNumberEditText;
    EditText password;
    CheckBox rememberMeCheckBox;
    TextView forgotPasswordTextView;
    Button login;
    private Context context;
    private AwesomeValidation awesomeValidation;
    private ProgressDialog pd;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String task="", changedPassword="", enteredOTP="";
    String forgotPasswordRecipients = "jai.pal.2013@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        context = this;
        initialUISetup();
        applyOnFocusChangeListener( loginPhoneNumberEditText );
        applyOnFocusChangeListener( password );
        makeForgetPasswordClickable();
        awesomeValidation.addValidation( this, R.id.loginPhoneNumberEditText, "^[0-9]{10}$", R.string.mobileerror );

        login.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View view) {
                if(validateUserInput()) {
                    if (awesomeValidation.validate()) {
                        loginTask task = new loginTask();
                        task.execute();
                    }
                }
            }
        });

    }

    private void makeForgetPasswordClickable() {
        TextView login = findViewById( R.id.forgotPasswordTextView );
        String loginText = "Forgot Password?";
        SpannableString spanString = new SpannableString( loginText );
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                showAddItemDialog(context);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState( ds );
            }
        };
        spanString.setSpan( clickableSpan, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        login.setText( spanString );
        login.setMovementMethod( LinkMovementMethod.getInstance() );
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void initialUISetup() {
        loginPhoneNumberEditText = findViewById( R.id.loginPhoneNumberEditText );
        password = findViewById( R.id.password );
        rememberMeCheckBox = findViewById( R.id.rememberMeCheckBox );
        forgotPasswordTextView = findViewById( R.id.forgotPasswordTextView );
        awesomeValidation = new AwesomeValidation( ValidationStyle.BASIC );
        login = findViewById( R.id.phone_login_button );
    }

    private void applyOnFocusChangeListener(final EditText inputEditTxt) {
        inputEditTxt.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty( inputEditTxt.getText().toString() )) {
                        inputEditTxt.setError( "Empty Input Error" );
                    }
                } else
                    inputEditTxt.setError( null );
            }
        } );
    }

    private boolean validateUserInput() {
        Boolean isAnyFieldsEmpty = false;
        if (loginPhoneNumberEditText.getText().toString().isEmpty()) {
            isAnyFieldsEmpty = true;
            loginPhoneNumberEditText.setError( "Empty Input Error" );
        }
        if (password.getText().toString().isEmpty()) {
            isAnyFieldsEmpty = true;
            password.setError( "Empty Input Error" );
        }
        else if (password.getText().toString().length()<5) {
            isAnyFieldsEmpty = true;
            password.setError( "Password should be more than 4 characters long" );
        }
        return !isAnyFieldsEmpty;
    }

    private void rememberMe() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (rememberMeCheckBox.isChecked()) {
            editor.putBoolean( "loggedIn", true );
            editor.apply();
        } else {
            editor.remove( "loggedIn" );
            editor.commit();
        }
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( LoginActivity.this );
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

    private void showAlertDialogForOTP(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( LoginActivity.this );
        alertDialogBuilder.setMessage( msg );
        alertDialogBuilder.setPositiveButton( "Re-enter OTP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//                Toast.makeText( LoginActivity.this, "You clicked ok button", Toast.LENGTH_LONG ).show();
                showEnterOTPDialog( context );
            }
        } );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void changePasswordDialog (final Context c){
        final  EditText pass = new EditText(c);
        final  EditText confPass = new EditText(c);
        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation( LinearLayout.VERTICAL);
        layout.addView( pass );
        layout.addView( confPass );

        pass.setHint( "Enter New Password" );
        pass.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        confPass.setHint( "Confirm Password" );
        confPass.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Update Your Password")
                .setView( layout )
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = String.valueOf(pass.getText());
                        String confirmPassword = String.valueOf(confPass.getText());
                        if (pass.getText().toString().length()<5) {
                            pass.setError( "Password should be more than 4 characters long" );
                        }
                        else if(password.equals( confirmPassword )){
                            changedPassword =  pass.getText().toString();
                            changePasswordTask task = new changePasswordTask();
                            task.execute(  );
                        }
                        else{
                            dialog.cancel();
                            showAlertDialog( "Passwords doesn't match" );
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showEnterOTPDialog(final Context c){
        final EditText taskEditText = new EditText(c);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter One Time Password (OTP)")
                .setView(taskEditText)
                .setPositiveButton("Verify OTP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enteredOTP = taskEditText.getText().toString();
                        VerifyOTP verifyOTP = new VerifyOTP( );
                        verifyOTP.execute();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showAddItemDialog(final Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter Phone number")
                .setView(taskEditText)
                .setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task = String.valueOf(taskEditText.getText());
                        if(isValidPhone( task )){
                            dialog.cancel();
                            try{
                                LongOperation l=new LongOperation();
                                l.execute();  //sends the email in background
//                                Toast.makeText(LoginActivity.this, l.get(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("SendMail", e.getMessage(), e);
                            }
                        }
                        else{
                            dialog.cancel();
                            Log.e( "invalid phone","Not Valid Phone number" );
                            Toast.makeText( c,"Enter valid phone number",Toast.LENGTH_SHORT ).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public boolean isValidPhone(CharSequence phone) {
        boolean check=false;
        if(Pattern.matches("\\d{10}", phone)){
            if(phone.length()==10){
                check = true;
            }
            else {
                check = false;
            }
        }
        else{
            check=false;
        }
        return check;
    }

    private class loginTask extends AsyncTask<Void, Void, Void>{
        int success = -2;
        int userExits = -3;
        final GetData getData = new GetData();
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( context );
            pd.setMessage( "Logging in..." );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            userExits = getData.findAlreadyRegisteredUser(loginPhoneNumberEditText.getText().toString());
            if(userExits>0) {
                sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Map<String, String> hs = getData.getPasswordFromPhoneNumber( loginPhoneNumberEditText.getText().toString() );
                editor.putString( "authName", hs.get( "authName" ) );
                editor.putString( "authDesignation", hs.get( "authDesig" ) );
                editor.putString( "active", hs.get( "active" ) );
                editor.putString( "admin", hs.get( "admin" ) );
                editor.putString( "email", hs.get( "email" ) );
                editor.putString( "phone",loginPhoneNumberEditText.getText().toString() );
                editor.apply();
                try {
                    if (AESCrypt.encrypt( password.getText().toString() ).equals( hs.get( "pass" ) )) {
                        success = 1;
                    } else {
                        success = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                success =-1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            if (pd != null) {
                pd.dismiss();
                login.setEnabled( true );
            }
            if(userExits==-2)
                showAlertDialog( "Check Your Internet Connection!" );
            else if(userExits==-1)
                showAlertDialog( "Could not Connect to server. Try again after sometime" );
            else if(success==-1 )
                showAlertDialog( "User with entered Phone number is not Registered. Please register first." );
            else if(success==0)
                showAlertDialog( "Either Phone number or Password is wrong!" );
            else if(success==1){
                rememberMe();
                if(sharedpreferences.getString( "active","-1" ).equals( "1" )) {
                    Intent myIntent = new Intent( LoginActivity.this, MainActivity.class );
                    finish();
                    startActivity( myIntent );
                }
                else{
                    changePasswordDialog( context );
                }
            }
        }
    }

    private class changePasswordTask extends AsyncTask<Void, Void, Void>{

        final GetData getData = new GetData();
        final int[] count = new int[2];
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( context );
            pd.setMessage( "Updating Password..." );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            count[0] =getData.putActiveStatus( loginPhoneNumberEditText.getText().toString() );
            try {
                count[1] = getData.updatePassword( loginPhoneNumberEditText.getText().toString(), AESCrypt.encrypt(changedPassword ) );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
            if (pd != null) {
                pd.dismiss();
                login.setEnabled( true );
                if(count[0]==-2 ||count[1]==-2){
                    showAlertDialog( "Check Your Internet Connection!" );
                }
                else if(count[0]==-1 ||count[1]==-1){
                    showAlertDialog( "Could not Connect to server. Try again after sometime" );
                }
                else if(count[0]==1 ||count[1]==1){
                    Intent myIntent = new Intent( LoginActivity.this, MainActivity.class );
                    finish();
                    startActivity( myIntent );
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public class LongOperation extends AsyncTask<Void, Void, Void> {
        int userCount = -2;
        final GetData getData = new GetData();

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Sending request mail", true, false);
            pd = new ProgressDialog( LoginActivity.this );
            pd.setTitle( "Please wait" );
            pd.setMessage( "Processing your request..." );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                userCount = getData.findAlreadyRegisteredUser( task );
                if(userCount>0) {
                    Map<String, String> hs = getData.getPasswordFromPhoneNumber( task );
                    sendEmail("Regarding forgot password request","The user " +hs.get( "authName" ) +" ("+hs.get( "authDesig" )+") whose registered phone number is " +task + " has forgotten the password for logging in Signals and Telecom Inspection app.", "sntinspection@gmail.com", forgotPasswordRecipients);
                    String otp = OTP( 5 );
                    String body = "Dear "+hs.get( "authName" ) +" ("+hs.get( "authDesig" )+"),\r\n\nYour One Time Password (OTP) is "+otp+". You will be asked to enter One Time Password (OTP) during login.\r\n\nIf you face any issues with your account, please contact at:\r\n\nPhone - 9001195126, 9001195127\r\n\nEmail - sntinspection@gmail.com\r\n\nRegards,\r\n\nIT Center, NWR";
                    getData.updateOTPforPhone(otp,task);
                    if(!hs.get("email").equals( "-" ))
                        sendEmail( "OTP for "+task+"",body,"sntinspection@gmail.com",hs.get("email") );
                }
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
            if(userCount==-2)
                showAlertDialog( "Check Your Internet Connection!" );
            else if(userCount==-1)
                showAlertDialog( "Could not Connect to server. Try again after sometime" );
            else if(userCount==0)
                showAlertDialog( "User with entered Phone number is not Registered. Please register first." );
            else if(userCount>0){
                showEnterOTPDialog(context);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {


        }
    }

    public class VerifyOTP extends AsyncTask<Void, Void, Void> {
        String userCount = "-3";
        Boolean success = false;
        final GetData getData = new GetData();

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( LoginActivity.this );
            pd.setTitle( "Please wait" );
            pd.setMessage( "Verifying OTP..." );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                userCount = getData.getOTPFromPhone( task );
                if(userCount.equals( enteredOTP ))
                    success = true;
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
            if(userCount.equals( "-2" ))
                showAlertDialog( "Check Your Internet Connection!" );
            else if(userCount.equals( "-1" ))
                showAlertDialog( "Could not Connect to server. Try again after sometime" );
            else {
                if(success)
                    changePasswordDialog(context);
                else
                    showAlertDialogForOTP( "Entered OTP is Incorrect!!!" );
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {


        }
    }

    private void sendEmail(String subject, String body, String sender , String recipents) throws Exception {
        GMailSender send = new GMailSender( "sntinspection@gmail.com", "nwrailway" );
        send.sendMail( subject, body, sender, recipents );
    }

    static String OTP(int len) {
        String numbers = "0123456789";
        Random rndm_method = new Random();
        String otp = "";
        for (int i = 0; i < len; i++) {
            otp += numbers.charAt( rndm_method.nextInt( numbers.length() ) );
        }
        return otp;
    }

}