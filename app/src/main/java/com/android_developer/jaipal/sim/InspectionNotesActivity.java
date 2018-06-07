package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class InspectionNotesActivity extends AppCompatActivity {
    private Context mContext;
    private ProgressBar spinner;
    List<InspectionNoteDataModel> datamodel;
    RecyclerView mRecyclerView;
    String serverUrl, fileUrl="";

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.inspection_notes_activity );
        mContext = getApplicationContext();

        spinner = findViewById( R.id.progressBar );
        mRecyclerView = findViewById( R.id.inspection_notes_recycler_view );
        mRecyclerView.setNestedScrollingEnabled(false);
        serverUrl = getResources().getString( R.string.fileServerUrl );

        GetInspectionNoteData getInspectionNoteData = new GetInspectionNoteData();
        getInspectionNoteData.execute(  );
    }

    private class GetInspectionNoteData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility( View.VISIBLE );
            mRecyclerView.setVisibility( View.GONE );}

        @Override
        protected Void doInBackground(Void... arg0) {
            GetData getData;
            datamodel =new ArrayList<InspectionNoteDataModel>();
            getData = new GetData();
            datamodel = getData.getInpectionNoteData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (datamodel != null) {
                spinner.setVisibility( View.GONE );
                mRecyclerView.setVisibility( View.VISIBLE );
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mRecyclerView.setLayoutManager( mLayoutManager );
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                RecyclerView.Adapter mAdapter = new InspectionNotesAdapter( mContext, datamodel );
                mRecyclerView.setAdapter( mAdapter );
                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        InspectionNoteDataModel data = datamodel.get(position);
                        fileUrl = serverUrl+"/"+data.getFilename();
                        openURLinBrowser( view, fileUrl );
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

            } else {
                spinner.setVisibility( View.GONE );
                showAlertDialog( "Please check your internet connection!" );
            }
        }
    }

    public void openURLinBrowser(View view, String fileUrl){
        Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( InspectionNotesActivity.this );
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
