package com.android_developer.jaipal.sim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText nameEditText,registrationDesginationEditText, registrationPhoneNumberEditText, registrationOfficialEmailAddressEditText;
    Button RegisterationButton;
    private AwesomeValidation awesomeValidation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_ACTION_BAR);
        setContentView( R.layout.activity_registration );

        initialUISetup();
        applyOnFocusChangeListener(nameEditText);
        applyOnFocusChangeListener(registrationDesginationEditText);
        applyOnFocusChangeListener(registrationPhoneNumberEditText);
        applyOnFocusChangeListenerOnEmail(registrationOfficialEmailAddressEditText);
//        //////////////////////////////////////////////////////////////////////////////////////////////////////////
//        //making login textview clickable
//        TextView login = findViewById( R.id.alreadyRegisteredLoginTextView );
//        String loginText = "Already Registered! Login Me";
//        SpannableString spanString = new SpannableString(loginText);
//        final Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                finish();
//                startActivity(loginIntent);
//            }
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//            }
//        };
//        spanString.setSpan(clickableSpan,20,28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        login.setText(spanString);
//        login.setMovementMethod( LinkMovementMethod.getInstance());
//        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //User Validation
        awesomeValidation.addValidation(this, R.id.nameEditText, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",R.string.namerror);
        awesomeValidation.addValidation(this, R.id.registrationDesginationEditText, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\/\\s\\\\]{0,}$", R.string.desigerror);
        awesomeValidation.addValidation(this, R.id.registrationPhoneNumberEditText, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
//        awesomeValidation.addValidation(this, R.id.registrationOfficialEmailAddressEditText, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void initialUISetup() {
        nameEditText = findViewById( R.id.nameEditText );
        registrationDesginationEditText = findViewById( R.id.registrationDesginationEditText );
        registrationPhoneNumberEditText = findViewById( R.id.registrationPhoneNumberEditText );
        registrationOfficialEmailAddressEditText = findViewById( R.id.registrationOfficialEmailAddressEditText );
        RegisterationButton = findViewById( R.id.RegisterationButton );
        awesomeValidation = new AwesomeValidation( ValidationStyle.BASIC);
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

    private void applyOnFocusChangeListenerOnEmail(final EditText inputEditTxt) {
        inputEditTxt.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!TextUtils.isEmpty( inputEditTxt.getText().toString() ) && !Patterns.EMAIL_ADDRESS.matcher(inputEditTxt.getText().toString()).matches()) {
                        inputEditTxt.setError( "Invalid Email Address" );
                    }
                } else
                    inputEditTxt.setError( null );
            }
        } );
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void submitButton(View view) {
        if (awesomeValidation.validate() && validateEmailAddress(registrationOfficialEmailAddressEditText.getText().toString())) {
//            Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();
            registrationTask task = new registrationTask();
            task.execute();
            //process the data further
        }

    }

    private boolean validateEmailAddress(String s) {
        if(s.isEmpty()){
            return true;
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(s).matches()){
            return true;
        }
        else return false;
    }

    private class registrationTask extends AsyncTask<Void, Void, Void> {
        int success = -2;
        int userCount = -2;
        ProgressDialog pd;
        final GetData getData = new GetData();
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( RegistrationActivity.this );
            pd.setMessage( "Creating Account..." );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String email;
                userCount = getData.findAlreadyRegisteredUser( registrationPhoneNumberEditText.getText().toString() );
                if(TextUtils.isEmpty( registrationOfficialEmailAddressEditText.getText().toString())){
                    email = "-";
                }
                else
                    email = registrationPhoneNumberEditText.getText().toString();
                if(userCount==0) {
                    success = getData.registerUser( nameEditText.getText().toString(), registrationDesginationEditText.getText().toString(), registrationPhoneNumberEditText.getText().toString(), email, AESCrypt.encrypt( registrationPhoneNumberEditText.getText().toString() ) );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd != null) {
                pd.dismiss();
                RegisterationButton.setEnabled( true );
                if(userCount>=1)
                    showAlertDialog( "User Already Exists" );
                if(success==-1)
                    showAlertDialog( "You are Offline. Please connect to Internet." );
            }
            if(userCount==0 && success>=1){
                showAlertDialog( "Account Created" );
                clearAllEditBox();
            }
        }

        private void clearAllEditBox() {
            nameEditText.setText( "" );
            registrationDesginationEditText.setText( "" );
            registrationPhoneNumberEditText.setText( "" );
            registrationOfficialEmailAddressEditText.setText( "" );
        }
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( RegistrationActivity.this );
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
}
