package com.android_developer.jaipal.sim;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

import static android.content.ContentValues.TAG;

/**
 * Created by hp on 2018-04-01.
 */

public class MainGearsActivity extends AppCompatActivity {
    private int count;

    private Context mContext;
    private Context context;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private ProgressDialog pd;
    Button mainGearsSubmitButton;
    int gtid,pcid,sid,ccipid,biid,acid,psid,lcid,rid,nonsntid,tcid,sinid;
    String lc="",bi="",ac="";
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_gears );
//        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // Get the application context
        mContext = getApplicationContext();
        context = this;
        mainGearsSubmitButton = findViewById( R.id.mainGearsSubmitButton );

        // Get the widgets reference from XML layout
//        mRelativeLayout = (RelativeLayout) findViewById(R.id.rm);
        RecyclerView mRecyclerView = (RecyclerView) findViewById( R.id.recycler_view );
        mRecyclerView.setNestedScrollingEnabled(false);

        //finding count on screen orientation basis
        int value = this.getResources().getConfiguration().orientation;
        if (value == Configuration.ORIENTATION_PORTRAIT){
            count =2;
        }
        if (value == Configuration.ORIENTATION_LANDSCAPE) {
            count =4;
        }


        // Define a layout for RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager( mContext, count );
        mRecyclerView.setLayoutManager( mLayoutManager );

        // Initialize a new instance of RecyclerView Adapter instance
        Resources resources = mContext.getResources();
        String[] name = resources.getStringArray( R.array.gear );
        TypedArray a = resources.obtainTypedArray(R.array.gear_picture);
        Log.d( "debug",""+a.length() );
        Drawable[] picture = new Drawable[a.length()];
        Log.d( "debug",""+ picture.length );
        for (int i = 0; i < picture.length; i++) {
            Log.d("debug",""+i);
            picture[i] = a.getDrawable(i);
        }
        a.recycle();

        RecyclerView.Adapter mAdapter = new GearsAdapter( mContext, name, picture, extras );

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter( mAdapter );
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void submitGenerateInspectionNote(View view) throws SQLException {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedpreferences.getBoolean( "detailActivityComplete",false ) && sharedpreferences.getBoolean( "pointsCrossingActivityComplete",false ) && sharedpreferences.getBoolean( "trackCircuitsActivityComplete",false )  && sharedpreferences.getBoolean( "signalsActivityComplete",false )  && sharedpreferences.getBoolean( "ccipActivityComplete",false ) && sharedpreferences.getBoolean( "biAcActivityComplete",false ) && sharedpreferences.getBoolean( "powerActivityComplete",false ) && sharedpreferences.getBoolean( "levelActivityComplete",false ) && sharedpreferences.getBoolean( "recordsActivityComplete",false ) && sharedpreferences.getBoolean( "nonSntActivityComplete",false )) {
            sendDataToDBTask task = new sendDataToDBTask();
            task.execute();
//            sendSMS sms = new sendSMS();
//            sms.execute(  );
//            if(task.getStatus()==AsyncTask.Status.FINISHED) {
//            generatePdf generate = new generatePdf();
//            generate.execute(  );
//            }
//            GenerateInspectionNote generateInspectionNote = new GenerateInspectionNote( mContext );
//            generateInspectionNote.uploadPdf();
//            generateInspectionNote.previewPdf();
//            removeAllSharedPreferences();
        }
        else{
            Toast.makeText( this, "Please fill all entries and Save them", Toast.LENGTH_SHORT ).show();
        }
    }

    private Boolean sendData() throws SQLException {
        sendGeneralTelecomData();
        sendPointsCrossingsData();
        sendTrackCircuitsData();
        sendSignalsData();
        sendCcipData();
        sendBIData();
        sendAcData();
        sendPowerSupplyData();
        sendLevelCrossingData();
        sendRecordsData();
        sendNonSntData();
        sendMainActivityData();
        return true;
    }

    private void sendGeneralTelecomData() throws SQLException {
        GetData getData = new GetData();
        String vhfActionBy = "", ofcActionBy="",emergencyActionBy="";
        vhfActionBy = getActionBy( sharedpreferences.getString( "vhfSetActionBySpinner","" ), sharedpreferences.getString( "vhfSetActionByEditText","" ) );
        ofcActionBy = getActionBy( sharedpreferences.getString( "digitalEquipActionBySpr","" ), sharedpreferences.getString( "digitalEquipActionByEditText","" ) );
        emergencyActionBy = getActionBy( sharedpreferences.getString( "testedSocketsActionBySpr","" ), sharedpreferences.getString( "testedSocketsActionByEditText","" ) );

        gtid = getData.generalTelecomInsertionQuery(
                sharedpreferences.getString( "SMeditText","" ),
                sharedpreferences.getString( "SMKeyValue","" ),
                sharedpreferences.getString( "VHFsetValue","" ),
                sharedpreferences.getString( "ControlPhoneValue","" ),
                sharedpreferences.getString( "RailwayPhoneValue","" ),
                sharedpreferences.getString( "VHFrepeaterValue","" ),
                sharedpreferences.getString( "PAsystemValue","" ),
                sharedpreferences.getString( "TelecomInstallationEditText","" ),
                vhfActionBy,
                sharedpreferences.getString( "TestingDateEditText","" ),
                sharedpreferences.getString( "TestedPointsEditText","" ),
                sharedpreferences.getString( "TestedCHEditText","" ),
                sharedpreferences.getString( "BatteryVoltageEditText","" ),
                sharedpreferences.getString( "DigitalEquipmentRadioGroupValue","" ),
                sharedpreferences.getString( "BatteryRecordsRadioGroupValue","" ),
                sharedpreferences.getString( "EarthTerminationRadioGroupValue","" ),
                sharedpreferences.getString( "OFCHutEditText","" ),
                ofcActionBy,
                sharedpreferences.getString( "EmergencySocketRadioGroupValue","" ),
                sharedpreferences.getString( "TestedSocketsEditText","" ),
                emergencyActionBy
        );
        Log.e("gtid", "gtid = "+gtid);
    }

    private void sendPointsCrossingsData() throws SQLException {
        GetData getData = new GetData();
        String ActionBy = "", LOCKEDPOINTSACTIONBY="",LASTJOINTACTIONBY="";
        ActionBy = getActionBy( sharedpreferences.getString( "pointsCrossingEnsureActionBySpinner","" ), sharedpreferences.getString( "pointsCrossingEnsureActionByEditText","" ) );
        LOCKEDPOINTSACTIONBY = getActionBy( sharedpreferences.getString( "pointsCrossingDetailsActionBySpinner","" ), sharedpreferences.getString( "pointsCrossingDetailsActionByEditText","" ) );
        LASTJOINTACTIONBY = getActionBy( sharedpreferences.getString( "pointsCrossingLastJointActionBySpinner","" ), sharedpreferences.getString( "pointsCrossingLastJointActionByEditText","" ) );

        pcid = getData.pointsCrossingsInsertionQuery(
                sharedpreferences.getBoolean( getResources().getString( R.string.point_doesn_t_get_operated_when_point_zone_tr_is_dropped ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.point_does_not_stop_when_point_zone_tr_is_dropped_during_point_operation ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.opening_of_point_is_around_115mm_shall_not_be_less_than_95mm_in_any_case ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.filler_gauge_shall_be_in_between_1mm_to_3mm ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.records_of_point_maintenance_were_maintained_and_were_placed_in_respective_point_machines ),false ),
                sharedpreferences.getString( "pointsDeficiencyEditText","" ),
                ActionBy,
                sharedpreferences.getString( "pointsDetailEditText","" ),
                sharedpreferences.getString( "obstructionCurrentEditText","" ),
                sharedpreferences.getString( "lockedPointsEditText","" ),
                LOCKEDPOINTSACTIONBY,
                sharedpreferences.getString( "selectDateEditText","" ),
                sharedpreferences.getString( "lastJointDeficiencyEditText","" ),
                LASTJOINTACTIONBY
        );
        Log.e("pcid", "pcid = "+pcid);
    }

    private String getActionBy (String s1, String s2){
        if(s1.equals( "Other" ))
            return s2;
        else
            return s1;
    }

    private void sendTrackCircuitsData() throws SQLException {
        GetData getData = new GetData();
        String actionBy = "";
        if(sharedpreferences.getString( "trackCircuitsActionBySpinner","" ).equals( "Other" ))
            actionBy = sharedpreferences.getString( "trackCircuitsActionByEditText","" );
        else
            actionBy = sharedpreferences.getString( "trackCircuitsActionBySpinner","" );

        tcid = getData.trackCircuitsInsertionQuery(
                sharedpreferences.getBoolean( getResources().getString( R.string.double_bonding_has_been_done_on_all_the_continuous_rail_joints_and_sejs ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.j_type_pandrol_clip_has_been_used_at_glued_joints_to_avoid_shorting_of_tc ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.when_both_rails_are_shorted_using_tsr_tc_indication_on_panel_is_red_including_track_circuited_sidings ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.specific_gravity_of_tc_battery_is_in_between_1180_1220 ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.relay_end_voltage_is_less_than_3_times_of_pick_up_voltage_of_tr_qt2_qta2_type ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.records_of_tc_parameters_were_maintained_and_placed_in_respective_location_boxes ),false ),
                sharedpreferences.getString( "tracksDeficiencyEditText","" ),
                actionBy
        );
        Log.e("tcid", "tcid = "+tcid);
    }

    private void sendSignalsData() throws SQLException {
        GetData getData = new GetData();
        String ActionBy = "";
        ActionBy = getActionBy( sharedpreferences.getString( "signalsActionBySpinner","" ), sharedpreferences.getString( "signalsActionByEditText","" ) );

        sid = getData.signalsInsertionQuery(
                sharedpreferences.getBoolean( getResources().getString( R.string.signal_lamp_voltae_90_of_rated_value ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.vecr_drops_with_fusing_of_minimum_3_route_leds ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.signals_are_cascaded_for_e_g_fusing_of_a_signal_s_particular_aspect_other_than_red_illuminates_a_more_restrictive_aspect ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.red_lamp_protection_working_e_g_fusing_of_red_aspect_of_signal_should_illuminate_signal_in_rear_with_most_restrictive_aspect_red ),false ),
                sharedpreferences.getString( "signalNo1EditText","" ),
                sharedpreferences.getString( "aspect1EditText","" ),
                sharedpreferences.getString( "voltage1EditText","" ),
                sharedpreferences.getString( "current1EditText","" ),
                sharedpreferences.getString( "signalNo2EditText","" ),
                sharedpreferences.getString( "aspect2EditText","" ),
                sharedpreferences.getString( "voltage2EditText","" ),
                sharedpreferences.getString( "current2EditText","" ),
                sharedpreferences.getString( "signalNo3EditText","" ),
                sharedpreferences.getString( "aspect3EditText","" ),
                sharedpreferences.getString( "voltage3EditText","" ),
                sharedpreferences.getString( "current3EditText","" ),
                sharedpreferences.getString( "signalNo4EditText","" ),
                sharedpreferences.getString( "aspect4EditText","" ),
                sharedpreferences.getString( "voltage4EditText","" ),
                sharedpreferences.getString( "current4EditText","" ),
                sharedpreferences.getString( "signalNo5EditText","" ),
                sharedpreferences.getString( "aspect5EditText","" ),
                sharedpreferences.getString( "voltage5EditText","" ),
                sharedpreferences.getString( "current5EditText","" ),
                sharedpreferences.getString( "signalNo6EditText","" ),
                sharedpreferences.getString( "aspect6EditText","" ),
                sharedpreferences.getString( "voltage6EditText","" ),
                sharedpreferences.getString( "current6EditText","" ),
                sharedpreferences.getString( "signalNo7EditText","" ),
                sharedpreferences.getString( "aspect7EditText","" ),
                sharedpreferences.getString( "voltage7EditText","" ),
                sharedpreferences.getString( "current7EditText","" ),
                sharedpreferences.getString( "signalNo8EditText","" ),
                sharedpreferences.getString( "aspect8EditText","" ),
                sharedpreferences.getString( "voltage8EditText","" ),
                sharedpreferences.getString( "current8EditText","" ),
                sharedpreferences.getString( "signalNo9EditText","" ),
                sharedpreferences.getString( "aspect9EditText","" ),
                sharedpreferences.getString( "voltage9EditText","" ),
                sharedpreferences.getString( "current9EditText","" ),
                sharedpreferences.getString( "signalNo10EditText","" ),
                sharedpreferences.getString( "aspect10EditText","" ),
                sharedpreferences.getString( "voltage10EditText","" ),
                sharedpreferences.getString( "current10EditText","" ),
                ActionBy
        );
        Log.e("sid", "sid = "+sid);
    }

    private void sendCcipData() throws SQLException {
        GetData getData = new GetData();
        String ActionBy = "";
        ActionBy = getActionBy( sharedpreferences.getString( "ccipActionBySpr","" ), sharedpreferences.getString( "ccipActionByEditTxt","" ) );

        ccipid = getData.ccipInsertionQuery(
                sharedpreferences.getString( "typeOfInterlockingSpinner","" ),
                sharedpreferences.getBoolean( getResources().getString( R.string.sip_swrd_is_as_per_the_physical_yard_layout ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.counters_on_panel_vdu_are_same_as_recorded_in_counter_register ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.sample_checking_of_calling_on_signal_coggb_counter_increment ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.sample_checking_of_calling_on_signal_cancellation_cocyn_errb_counter_increment ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.testing_of_emergency_classover_in_double_line_sections ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.sample_check_of_approach_locking_dead_approach_locking ),false ),
                sharedpreferences.getString( "ccipDeficiencyEditText","" ),
                sharedpreferences.getString( "ERRBEditText","" ),
                sharedpreferences.getString( "RRBUEditText","" ),
                sharedpreferences.getString( "COGGNEditText","" ),
                sharedpreferences.getString( "COCYNEditText","" ),
                sharedpreferences.getString( "EBPUEditText","" ),
                sharedpreferences.getString( "ECHEditText","" ),
                sharedpreferences.getString( "lastAnnualDateEditText","" ),
                ActionBy
        );
        Log.e("ccipid", "ccipid = "+ccipid);
    }

    private void sendBIData() throws SQLException {
        GetData getData = new GetData();
        bi="";
        String ActionBy = "";
        ActionBy = getActionBy( sharedpreferences.getString( "bi1ActionBySpr","" ), sharedpreferences.getString( "bi1ActionByEditTxt","" ) );
        biid = getData.blockInstrumentInsertionQuery(
                sharedpreferences.getString( "bi1EditTxt","" ),
                sharedpreferences.getString( "localBI1EditTxt","" ),
                sharedpreferences.getString( "lineBI1EditTxt","" ),
                sharedpreferences.getString( "voltageOutgoingBI1EditTxt","" ),
                sharedpreferences.getString( "currentOutgoingBI1EditTxt","" ),
                sharedpreferences.getString( "voltageIncomingBI1EditTxt","" ),
                sharedpreferences.getString( "currentIncomingBI1EditTxt","" ),
                sharedpreferences.getString( "bi1deficiencyEditTxt","" ),
                sharedpreferences.getString( "lineClearBI1EditTxt","" ),
                sharedpreferences.getString( "recordsBI1Spr","" ),
                ActionBy
        );
        bi+=biid;
        Log.e("biid", "biid = "+biid);

        ActionBy = getActionBy( sharedpreferences.getString( "bi2ActionBySpr","" ), sharedpreferences.getString( "bi2ActionByEditTxt","" ) );
        biid = getData.blockInstrumentInsertionQuery(
                sharedpreferences.getString( "bi2EditTxt","" ),
                sharedpreferences.getString( "localBI2EditTxt","" ),
                sharedpreferences.getString( "lineBI2EditTxt","" ),
                sharedpreferences.getString( "voltageOutgoingBI2EditTxt","" ),
                sharedpreferences.getString( "currentOutgoingBI2EditTxt","" ),
                sharedpreferences.getString( "voltageIncomingBI2EditTxt","" ),
                sharedpreferences.getString( "currentIncomingBI2EditTxt","" ),
                sharedpreferences.getString( "bi2deficiencyEditTxt","" ),
                sharedpreferences.getString( "lineClearBI2EditTxt","" ),
                sharedpreferences.getString( "recordsBI2Spr","" ),
                ActionBy
        );
        bi+=","+biid;
        Log.e("biid", "biid = "+biid);

        for(int biNumber = 0; biNumber <= sharedpreferences.getInt("biCount", 0)-3 ; biNumber++ ) {
            ActionBy = getActionBy( sharedpreferences.getString( "biActionBySpr"+biNumber,"" ), sharedpreferences.getString( "biActionByEditTxt"+biNumber,"" ) );
            biid = getData.blockInstrumentInsertionQuery(
                    sharedpreferences.getString( "biEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "localBIEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "lineBIEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "voltageOutgoingBIEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "currentOutgoingBIEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "voltageIncomingBIEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "currentIncomingBIEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "bideficiencyEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "lineClearBIEditTxt"+biNumber,"" ),
                    sharedpreferences.getString( "recordsBISpr"+biNumber,"" ),
                    ActionBy
            );
            bi+=","+biid;
            Log.e("biid", "biid = "+biid);
        }
        Log.e("bi", "bi = "+bi);
    }

    private void sendAcData() throws SQLException {
        GetData getData = new GetData();
        ac="";
        String ActionBy = "";
        ActionBy = getActionBy( sharedpreferences.getString( "ac1ActionBySpr","" ), sharedpreferences.getString( "ac1ActionByEditTxt","" ) );
        acid = getData.axleCounterInsertionQuery(
                sharedpreferences.getString( "axleCounter1EditTxt","" ),
                sharedpreferences.getString( "workingAC1Spr","" ),
                sharedpreferences.getString( "electricalAC1Spr","" ),
                sharedpreferences.getString( "resetAC1EditTxt","" ),
                sharedpreferences.getString( "ac1deficiencyEditText","" ),
                ActionBy
        );
        ac+=acid;
        Log.e("acid", "acid = "+acid);

        ActionBy = getActionBy( sharedpreferences.getString( "ac2ActionBySpr","" ), sharedpreferences.getString( "ac2ActionByEditTxt","" ) );
        acid = getData.axleCounterInsertionQuery(
                sharedpreferences.getString( "axleCounter2EditTxt","" ),
                sharedpreferences.getString( "workingAC2Spr","" ),
                sharedpreferences.getString( "electricalAC2Spr","" ),
                sharedpreferences.getString( "resetAC2EditTxt","" ),
                sharedpreferences.getString( "ac2deficiencyEditText","" ),
                ActionBy
        );
        ac+=","+acid;
        Log.e("acid", "acid = "+acid);

        for(int acNumber = 0; acNumber <= sharedpreferences.getInt("acCount",0)-13 ; acNumber++ ) {
            ActionBy = getActionBy( sharedpreferences.getString( "acActionBySpr"+acNumber,"" ), sharedpreferences.getString( "aActionByEditTxt"+acNumber,"" ) );
            acid = getData.axleCounterInsertionQuery(
                    sharedpreferences.getString( "axleCounterEditTxt"+acNumber,"" ),
                    sharedpreferences.getString( "workingACSpr"+acNumber,"" ),
                    sharedpreferences.getString( "electricalACSpr"+acNumber,"" ),
                    sharedpreferences.getString( "resetACEditTxt"+acNumber,"" ),
                    sharedpreferences.getString( "acdeficiencyEditText"+acNumber,"" ),
                    ActionBy
            );
            ac+=","+acid;
            Log.e("acid", "acid = "+acid);
        }
        Log.e("ac", "ac = "+ac);
    }

    private void sendPowerSupplyData() throws SQLException {
        GetData getData = new GetData();
        String ActionBy = "",ipsMake="",eiMake="";
        ipsMake = getActionBy( sharedpreferences.getString( "ipsMakespnr","" ), sharedpreferences.getString( "ipsMakeEditTxt","" ) );
        ActionBy = getActionBy( sharedpreferences.getString( "ccipActionBySpr","" ), sharedpreferences.getString( "ccipActionByEditTxt","" ) );
        eiMake = getActionBy( sharedpreferences.getString( "eiMakeSpnr","" ), sharedpreferences.getString( "eiMakeEditTxt","" ) );

        psid = getData.powerSupplyInsertionQuery(
                ipsMake,
                sharedpreferences.getBoolean( getResources().getString( R.string.amc_executed ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.smr_load_sharing_is_working_fine ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.earthing_of_ips_equipment_was_proper ),false ),
                sharedpreferences.getString( "ipsOnEditText","" ),
                sharedpreferences.getString( "ipsOFFEditText","" ),
                sharedpreferences.getString( "ipsAfterEditText","" ),
                sharedpreferences.getString( "specificGravityEditText","" ),
                sharedpreferences.getString( "specificGravityMaxEditText","" ),
                sharedpreferences.getBoolean( getResources().getString( R.string.records_of_battery_readings_were_maintained ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.white_washing_required_in_battery_room ),false ),
                sharedpreferences.getString( "relayRoomOpeningSpinner","" ),
                sharedpreferences.getString( "spareRelaySpinner","" ),
                sharedpreferences.getString( "whiteWashingRelaySpinner","" ),
                sharedpreferences.getString( "electricalGeneralSpinner","" ),
                sharedpreferences.getString( "earthingArrangementsSpinner","" ),
                sharedpreferences.getString( "whetherAMCSpinner","" ),
                sharedpreferences.getString( "maintenanceRecordsSpinner","" ),
                sharedpreferences.getString( "dataLoggerDate","" ),
                sharedpreferences.getString( "lastValidationByEditText","" ),
                eiMake,
                sharedpreferences.getString( "lastSystemSwitchAEditText","" ),
                sharedpreferences.getString( "lastSystemSwitchBEditText","" ),
                sharedpreferences.getString( "eiDate","" ),
                sharedpreferences.getString( "eiRackSpinner","" ),
                sharedpreferences.getString( "voltageParameterSpinner","" ),
                ActionBy
        );
        Log.e("psid", "psid = "+psid);
    }

    private void sendLevelCrossingData() throws SQLException {
        GetData getData = new GetData();
        lc="";
        String ActionBy = "";
        ActionBy = getActionBy( sharedpreferences.getString( "levelCrossingActionBySpr","" ), sharedpreferences.getString( "levelCrossingActionByEditTxt","" ) );

        lcid = getData.levelCrossingInsertionQuery(
                sharedpreferences.getString( "gateNo","" ),
                sharedpreferences.getString( "nameOfGateman","" ),
                sharedpreferences.getString( "gateTypeSpnr","" ),
                sharedpreferences.getBoolean( getResources().getString( R.string.positive_boom_locking_tested ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.booms_were_painted ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.gateman_having_adequate_safety_knowledge ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.gate_telephone_s_found_in_working_order ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.inspection_maintenance_records_were_maintained ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked ),false ),
                sharedpreferences.getString( "anyDeficiencyFound","" ),
                ActionBy
        );
        lc +=lcid;
        Log.e("lcid", "lcid = "+lcid);
        for(int gateNumber = 1; gateNumber <= sharedpreferences.getInt("levelCrossingGateCount", 0) ; gateNumber++ ){
            ActionBy = getActionBy( sharedpreferences.getString( "levelCrossingActionBySpr"+gateNumber,"" ), sharedpreferences.getString( "levelCrossingActionByEditTxt"+gateNumber,"" ) );

            lcid = getData.levelCrossingInsertionQuery(
                    sharedpreferences.getString( "gateNo"+gateNumber,"" ),
                    sharedpreferences.getString( "nameOfGateman"+gateNumber,"" ),
                    sharedpreferences.getString( "gateTypeSpnr"+gateNumber,"" ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.positive_boom_locking_tested )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.booms_were_painted )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.gate_telephone_s_found_in_working_order )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.inspection_maintenance_records_were_maintained )+gateNumber,false ),
                    sharedpreferences.getBoolean( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+gateNumber,false ),
                    sharedpreferences.getString( "anyDeficiencyFound"+gateNumber,"" ),
                    ActionBy
            );
            lc +=","+lcid;
            Log.e("lcid", "lcid = "+lcid);
        }
        Log.e("lc", "lc = "+lc);
    }

    private void sendRecordsData() throws SQLException {
        GetData getData = new GetData();
        String ActionBy = "";
        ActionBy = getActionBy( sharedpreferences.getString( "recordsActionBySpr","" ), sharedpreferences.getString( "recordsActionByEditTxt","" ) );

        rid = getData.recordsInsertionQuery(
                sharedpreferences.getString( "signalPoint1EditText","" ),
                sharedpreferences.getString( "signalTrackCircuit1EditText","" ),
                sharedpreferences.getString( "signalBI1EditText","" ),
                sharedpreferences.getString( "signalPowerSupply1EditText","" ),
                sharedpreferences.getString( "signalOthers1EditText","" ),
                sharedpreferences.getString( "signalPoint2EditText","" ),
                sharedpreferences.getString( "signalTrackCircuit2EditText","" ),
                sharedpreferences.getString( "signalBI2EditText","" ),
                sharedpreferences.getString( "signalPowerSupply2EditText","" ),
                sharedpreferences.getString( "signalOthers2EditText","" ),
                sharedpreferences.getString( "signalPoint3EditText","" ),
                sharedpreferences.getString( "signalTrackCircuit3EditText","" ),
                sharedpreferences.getString( "signalBI3EditText","" ),
                sharedpreferences.getString( "signalPowerSupply3EditText","" ),
                sharedpreferences.getString( "signalOthers3EditText","" ),
                sharedpreferences.getString( "signalFailureRemarkEditText","" ),
                sharedpreferences.getString( "disconnection1DREditText","" ),
                sharedpreferences.getString( "disconnection2DREditText","" ),
                sharedpreferences.getString( "disconnection3DREditText","" ),
                sharedpreferences.getString( "disconnectionReconnectionEditText","" ),
                sharedpreferences.getString( "relayRoom1RREditText","" ),
                sharedpreferences.getString( "relayRoom2RREditText","" ),
                sharedpreferences.getString( "relayRoom3RREditText","" ),
                sharedpreferences.getString( "relayRoomEditText","" ),
                sharedpreferences.getString( "signalInfringementSpinner","" ),
                sharedpreferences.getString( "signalInfringementSpinner","" ),
                sharedpreferences.getString( "cableMeggeringSpinner","" ),
                sharedpreferences.getString( "updatedCktDiagramSpinner","" ),
                sharedpreferences.getString( "cableRouteSpinner","" ),
                sharedpreferences.getString( "cableCoreSpinner","" ),
                sharedpreferences.getString( "signalInterlockingSpinner","" ),
                sharedpreferences.getString( "SMC1Spinner","" ),
                sharedpreferences.getString( "SMC12Spinner","" ),
                sharedpreferences.getString( "signalHistorySpinner","" ),
                ActionBy
        );
        Log.e("rid", "rid = "+rid);
    }

    private void sendNonSntData() throws SQLException {
        GetData getData = new GetData();

        nonsntid = getData.nonSntInsertionQuery(
                sharedpreferences.getString( "engineeringDeficiencyEditText","" ),
                sharedpreferences.getString( "nonSntEngineeringActionByTxtView","" ),
                sharedpreferences.getString( "electricalDeficiencyEditText","" ),
                sharedpreferences.getString( "nonSntElectricalActionByTxtView","" ),
                sharedpreferences.getString( "operatingDeficiencyEditText","" ),
                sharedpreferences.getString( "nonSntOperatingActionByTxtView","" ),
                sharedpreferences.getString( "otherDeficiencyEditText","" ),
                sharedpreferences.getString( "otherDeficiencyActionEditText","" )
        );
        Log.e("nonsntid", "nonsntid = "+nonsntid);
    }

    private void sendMainActivityData() throws SQLException {
        GetData getData = new GetData();

        sinid = getData.mainActivityInsertionQuery(
                sharedpreferences.getString( "division","" ),
                sharedpreferences.getString( "inspectDate","" ),
                sharedpreferences.getString( "station","" ),
                sharedpreferences.getString( "authName","" ),
                sharedpreferences.getString( "authDesignation","" ),
                gtid,pcid,tcid,sid,ccipid,bi,ac,psid,lc,rid,nonsntid
        );
        Log.e("sinid", "sinid = "+sinid);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainGearsActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainGearsActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"Permission is granted");
                return true;
            } else {

                Log.e(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(MainGearsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(MainGearsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void removeAllSharedPreferences() {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        //General Telecom
        editor.remove("SMeditText" );
        editor.remove("SMKey" );
        editor.remove("VHFset" );
        editor.remove("ControlPhone" );
        editor.remove("RailwayPhone" );
        editor.remove("VHFrepeater" );
        editor.remove("PAsystem" );
        editor.remove("DigitalEquipmentRadioGroup" );
        editor.remove("BatteryRecordsRadioGroup" );
        editor.remove("EarthTerminationRadioGroup" );
        editor.remove("EmergencySocketRadioGroup" );
        editor.remove("TelecomInstallationEditText" );
        editor.remove("vhfSetActionByEditText" );
        editor.remove("TestingDateEditText" );
        editor.remove("TestedPointsEditText" );
        editor.remove("TestedCHEditText" );
        editor.remove("BatteryVoltageEditText" );
        editor.remove("OFCHutEditText" );
        editor.remove("digitalEquipActionByEditText" );
        editor.remove("TestedSocketsEditText" );
        editor.remove("testedSocketsActionByEditText" );
        editor.remove("vhfSetActionBySprPosition" );
        editor.remove("digitalEquipActionBySprPosition" );
        editor.remove("testedSocketsActionBySprPosition" );
        editor.remove( "detailActivityComplete" );
//        Points and Crossings
        editor.remove( getResources().getString( R.string.point_doesn_t_get_operated_when_point_zone_tr_is_dropped ) );
        editor.remove( getResources().getString( R.string.point_does_not_stop_when_point_zone_tr_is_dropped_during_point_operation ) );
        editor.remove( getResources().getString( R.string.opening_of_point_is_around_115mm_shall_not_be_less_than_95mm_in_any_case ) );
        editor.remove( getResources().getString( R.string.filler_gauge_shall_be_in_between_1mm_to_3mm ) );
        editor.remove( getResources().getString( R.string.records_of_point_maintenance_were_maintained_and_were_placed_in_respective_point_machines ) );
        editor.remove( "pointsDeficiencyEditText" );
        editor.remove( "pointsCrossingEnsureActionBySpinner" );
        editor.remove( "pointsCrossingEnsureActionByEditText" );
        editor.remove( "pointsDetailEditText" );
        editor.remove( "obstructionCurrentEditText" );
        editor.remove( "lockedPointsEditText" );
        editor.remove( "pointsCrossingDetailsActionBySpinner" );
        editor.remove( "pointsCrossingDetailsActionByEditText" );
        editor.remove( "selectDateEditText" );
        editor.remove( "lastJointDeficiencyEditText" );
        editor.remove( "pointsCrossingLastJointActionBySpinner" );
        editor.remove( "pointsCrossingLastJointActionByEditText" );
        editor.remove( "pointsCrossingActivityComplete" );
        editor.remove( "pointsCrossingEnsureActionBySpinnerPosition" );
        editor.remove( "pointsCrossingDetailsActionBySprPosition" );
        editor.remove( "pointsCrossingLastJointActionBySprPosition" );
        //Track Circuits
        editor.remove( getResources().getString( R.string.double_bonding_has_been_done_on_all_the_continuous_rail_joints_and_sejs ) );
        editor.remove( getResources().getString( R.string.j_type_pandrol_clip_has_been_used_at_glued_joints_to_avoid_shorting_of_tc ) );
        editor.remove( getResources().getString( R.string.when_both_rails_are_shorted_using_tsr_tc_indication_on_panel_is_red_including_track_circuited_sidings ) );
        editor.remove( getResources().getString( R.string.specific_gravity_of_tc_battery_is_in_between_1180_1220 ) );
        editor.remove( getResources().getString( R.string.relay_end_voltage_is_less_than_3_times_of_pick_up_voltage_of_tr_qt2_qta2_type ) );
        editor.remove( getResources().getString( R.string.records_of_tc_parameters_were_maintained_and_placed_in_respective_location_boxes ) );
        editor.remove( "tracksDeficiencyEditText" );
        editor.remove( "trackCircuitsActionByEditText" );
        editor.remove( "trackCircuitsActivityComplete" );
        editor.remove( "trackCircuitsActionBySpinnerPosition" );
        //Signals
        editor.remove( getResources().getString( R.string.signal_lamp_voltae_90_of_rated_value ) );
        editor.remove( getResources().getString( R.string.vecr_drops_with_fusing_of_minimum_3_route_leds ) );
        editor.remove( getResources().getString( R.string.signals_are_cascaded_for_e_g_fusing_of_a_signal_s_particular_aspect_other_than_red_illuminates_a_more_restrictive_aspect ) );
        editor.remove( getResources().getString( R.string.red_lamp_protection_working_e_g_fusing_of_red_aspect_of_signal_should_illuminate_signal_in_rear_with_most_restrictive_aspect_red ) );
        editor.remove( "signalNo1EditText" );
        editor.remove( "aspect1EditText" );
        editor.remove( "voltage1EditText" );
        editor.remove( "current1EditText" );
        editor.remove( "signalNo2EditText" );
        editor.remove( "aspect2EditText" );
        editor.remove( "voltage2EditText" );
        editor.remove( "current2EditText" );
        editor.remove( "signalNo3EditText" );
        editor.remove( "aspect3EditText" );
        editor.remove( "voltage3EditText" );
        editor.remove( "current3EditText" );
        editor.remove( "signalNo4EditText" );
        editor.remove( "aspect4EditText" );
        editor.remove( "voltage4EditText" );
        editor.remove( "current4EditText" );
        editor.remove( "signalNo5EditText" );
        editor.remove( "aspect5EditText" );
        editor.remove( "voltage5EditText" );
        editor.remove( "current5EditText" );
        editor.remove( "signalNo6EditText" );
        editor.remove( "aspect6EditText" );
        editor.remove( "voltage6EditText" );
        editor.remove( "current6EditText" );
        editor.remove( "signalNo7EditText" );
        editor.remove( "aspect7EditText" );
        editor.remove( "voltage7EditText" );
        editor.remove( "current7EditText" );
        editor.remove( "signalNo8EditText" );
        editor.remove( "aspect8EditText" );
        editor.remove( "voltage8EditText" );
        editor.remove( "current8EditText" );
        editor.remove( "signalNo9EditText" );
        editor.remove( "aspect9EditText" );
        editor.remove( "voltage9EditText" );
        editor.remove( "current9EditText" );
        editor.remove( "signalNo10EditText" );
        editor.remove( "aspect10EditText" );
        editor.remove( "voltage10EditText" );
        editor.remove( "current10EditText" );
        editor.remove( "signalsActionBySpinner" );
        editor.remove( "signalsActionBySpinnerPosition" );
        editor.remove( "signalsActionByEditText" );
        editor.remove( "signalsActivityComplete" );
        //CCIP VDU
        editor.remove( "typeOfInterlockingSpinner" );
        editor.remove( getResources().getString( R.string.sip_swrd_is_as_per_the_physical_yard_layout ) );
        editor.remove( getResources().getString( R.string.counters_on_panel_vdu_are_same_as_recorded_in_counter_register ) );
        editor.remove( getResources().getString( R.string.sample_checking_of_calling_on_signal_coggb_counter_increment ) );
        editor.remove( getResources().getString( R.string.sample_checking_of_calling_on_signal_cancellation_cocyn_errb_counter_increment ) );
        editor.remove( getResources().getString( R.string.testing_of_emergency_classover_in_double_line_sections ) );
        editor.remove( getResources().getString( R.string.sample_check_of_approach_locking_dead_approach_locking ) );
        editor.remove( "ccipDeficiencyEditText" );
        editor.remove( "ERRBEditText" );
        editor.remove( "RRBUEditText" );
        editor.remove( "COGGNEditText" );
        editor.remove( "COCYNEditText" );
        editor.remove( "EBPUEditText" );
        editor.remove( "ECHEditText" );
        editor.remove( "lastAnnualDateEditText" );
        editor.remove( "ccipActionBySpr" );
        editor.remove( "ccipActionByEditTxt" );
        editor.remove( "ccipActivityComplete" );
        editor.remove( "ccipActionBySprPosition" );
        editor.remove( "typeOfInterlockingSpinnerPosition" );
        //BlockInstruments and Axle Counter
        editor.remove( "bi1EditTxt" );
        editor.remove( "localBI1EditTxt" );
        editor.remove( "lineBI1EditTxt" );
        editor.remove( "voltageOutgoingBI1EditTxt" );
        editor.remove( "currentOutgoingBI1EditTxt" );
        editor.remove( "voltageIncomingBI1EditTxt" );
        editor.remove( "currentIncomingBI1EditTxt" );
        editor.remove( "bi1deficiencyEditTxt" );
        editor.remove( "lineClearBI1EditTxt" );
        editor.remove( "recordsBI1Spr" );
        editor.remove( "bi1ActionBySpr" );
        editor.remove( "bi1ActionByEditTxt" );
        editor.remove( "axleCounter1EditTxt" );
        editor.remove( "workingAC1Spr" );
        editor.remove( "electricalAC1Spr" );
        editor.remove( "resetAC1EditTxt" );
        editor.remove( "ac1deficiencyEditText" );
        editor.remove( "ac1ActionBySpr" );
        editor.remove( "ac1ActionByEditTxt" );
        editor.remove( "bi2EditTxt" );
        editor.remove( "localBI2EditTxt" );
        editor.remove( "lineBI2EditTxt" );
        editor.remove( "voltageOutgoingBI2EditTxt" );
        editor.remove( "currentOutgoingBI2EditTxt" );
        editor.remove( "voltageIncomingBI2EditTxt" );
        editor.remove( "currentIncomingBI2EditTxt" );
        editor.remove( "bi2deficiencyEditTxt" );
        editor.remove( "lineClearBI2EditTxt" );
        editor.remove( "recordsBI2Spr" );
        editor.remove( "bi2ActionBySpr" );
        editor.remove( "bi2ActionByEditTxt" );
        editor.remove( "axleCounter2EditTxt" );
        editor.remove( "workingAC2Spr" );
        editor.remove( "electricalAC2Spr" );
        editor.remove( "resetAC2EditTxt" );
        editor.remove( "ac2deficiencyEditText" );
        editor.remove( "ac2ActionBySpr" );
        editor.remove( "ac2ActionByEditTxt" );
        removeBiReplicatedGates(sharedpreferences.getInt("biCount",0));
        removeAcReplicatedGates(sharedpreferences.getInt("acCount",0));
        editor.remove( "biAcActivityComplete" );
        //Power Supply
        editor.remove( "ipsMakespnr" );
        editor.remove( "ipsMakeEditTxt" );
        editor.remove( getResources().getString( R.string.amc_executed ) );
        editor.remove( getResources().getString( R.string.smr_load_sharing_is_working_fine ) );
        editor.remove( getResources().getString( R.string.earthing_of_ips_equipment_was_proper ) );
        editor.remove( "ipsOnEditText" );
        editor.remove( "ipsOFFEditText" );
        editor.remove( "ipsAfterEditText" );
        editor.remove( "specificGravityEditText" );
        editor.remove( "specificGravityMaxEditText" );
        editor.remove( getResources().getString( R.string.records_of_battery_readings_were_maintained ) );
        editor.remove( getResources().getString( R.string.white_washing_required_in_battery_room ) );
        editor.remove( "relayRoomOpeningSpinner" );
        editor.remove( "spareRelaySpinner" );
        editor.remove( "whiteWashingRelaySpinner" );
        editor.remove( "electricalGeneralSpinner" );
        editor.remove( "earthingArrangementsSpinner" );
        editor.remove( "whetherAMCSpinner" );
        editor.remove( "maintenanceRecordsSpinner" );
        editor.remove( "dataLoggerDate" );
        editor.remove( "lastValidationByEditText" );
        editor.remove( "eiMakeSpnr" );
        editor.remove( "eiMakeEditTxt" );
        editor.remove( "lastSystemSwitchAEditText" );
        editor.remove( "lastSystemSwitchBEditText" );
        editor.remove( "eiDate" );
        editor.remove( "eiRackSpinner" );
        editor.remove( "voltageParameterSpinner" );
        editor.remove( "powerSupplyActionBySpr" );
        editor.remove( "powerSupplyActionByEditTxt" );
        editor.remove( "powerActivityComplete" );
        editor.remove( "ipsMakespnrPosition" );
        editor.remove( "relayRoomOpeningSpinnerPosition" );
        editor.remove( "spareRelaySpinnerPosition" );
        editor.remove( "whiteWashingRelaySpinnerPosition" );
        editor.remove( "electricalGeneralSpinnerPosition" );
        editor.remove( "earthingArrangementsSpinnerPosition" );
        editor.remove( "whetherAMCSpinnerPosition" );
        editor.remove( "maintenanceRecordsSpinnerPosition" );
        editor.remove( "eiMakeSpnrPosition" );
        editor.remove( "eiRackSpinnerPosition" );
        editor.remove( "voltageParameterSpinnerPosition" );
        editor.remove( "powerSupplyActionBySprPosition" );
        //Level Crossing
        editor.remove( "gateNo" );
        editor.remove( "nameOfGateman" );
        editor.remove( "gateTypeSpnr" );
        editor.remove( "gateTypeSpnrPosition" );
        editor.remove( getResources().getString( R.string.positive_boom_locking_tested ) );
        editor.remove( getResources().getString( R.string.booms_were_painted ) );
        editor.remove( getResources().getString( R.string.gateman_having_adequate_safety_knowledge ) );
        editor.remove( getResources().getString( R.string.gate_telephone_s_found_in_working_order ) );
        editor.remove( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule ) );
        editor.remove( getResources().getString( R.string.inspection_maintenance_records_were_maintained ) );
        editor.remove( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked ) );
        editor.remove( "anyDeficiencyFound" );
        editor.remove( "levelCrossingActionBySpr" );
        editor.remove( "levelCrossingActionBySprPosition" );
        editor.remove( "levelCrossingActionByEditTxt" );
        removeLevelCrossingReplicatedGates(sharedpreferences.getInt("levelCrossingGateCount",0));
        editor.remove( "levelActivityComplete" );
        //Records
        editor.remove( "signalPoint1EditText" );
        editor.remove( "signalTrackCircuit1EditText" );
        editor.remove( "signalBI1EditText" );
        editor.remove( "signalPowerSupply1EditText" );
        editor.remove( "signalOthers1EditText" );
        editor.remove( "signalPoint2EditText" );
        editor.remove( "signalTrackCircuit2EditText" );
        editor.remove( "signalBI2EditText" );
        editor.remove( "signalPowerSupply2EditText" );
        editor.remove( "signalOthers2EditText" );
        editor.remove( "signalPoint3EditText" );
        editor.remove( "signalTrackCircuit3EditText" );
        editor.remove( "signalBI3EditText" );
        editor.remove( "signalPowerSupply3EditText" );
        editor.remove( "signalOthers3EditText" );
        editor.remove( "signalFailureRemarkEditText" );
        editor.remove( "disconnection1DREditText" );
        editor.remove( "disconnection2DREditText" );
        editor.remove( "disconnection3DREditText" );
        editor.remove( "disconnectionReconnectionEditText" );
        editor.remove( "relayRoom1RREditText" );
        editor.remove( "relayRoom2RREditText" );
        editor.remove( "relayRoom3RREditText" );
        editor.remove( "relayRoomEditText" );
        editor.remove( "signalInfringementSpinner" );
        editor.remove( "earthTestingSpinner" );
        editor.remove( "cableMeggeringSpinner" );
        editor.remove( "updatedCktDiagramSpinner" );
        editor.remove( "cableRouteSpinner" );
        editor.remove( "cableCoreSpinner" );
        editor.remove( "signalInterlockingSpinner" );
        editor.remove( "SMC1Spinner" );
        editor.remove( "SMC12Spinner" );
        editor.remove( "signalHistorySpinner" );
        editor.remove( "recordsActionBySpr" );
        editor.remove( "recordsActionByEditTxt" );
        editor.remove( "recordsActivityComplete" );
        editor.remove( "signalInfringementSpinnerPosition" );
        editor.remove( "earthTestingSpinnerPosition" );
        editor.remove( "cableMeggeringSpinnerPosition" );
        editor.remove( "cableRouteSpinnerPosition" );
        editor.remove( "cableCoreSpinnerPosition" );
        editor.remove( "SMC1SpinnerPosition" );
        editor.remove( "SMC12SpinnerPosition" );
        editor.remove( "signalHistorySpinnerPosition" );
        editor.remove( "updatedCktDiagramSpinnerPosition" );
        editor.remove( "signalInterlockingSpinnerPosition" );
        editor.remove( "recordsActionBySprPosition" );
        //Non SNT
        editor.remove( "engineeringDeficiencyEditText" );
        editor.remove( "electricalDeficiencyEditText" );
        editor.remove( "operatingDeficiencyEditText" );
        editor.remove( "otherDeficiencyActionEditText" );
        editor.remove( "nonSntActivityComplete" );

        editor.apply();
    }

    private void removeBiReplicatedGates(int biCount) {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for(int biNumber = 0; biNumber <= biCount-3 ; biNumber++ ) {
            editor.remove( "biEditTxt"+biNumber );
            editor.remove( "localBIEditTxt"+biNumber );
            editor.remove( "lineBIEditTxt"+biNumber );
            editor.remove( "voltageOutgoingBIEditTxt"+biNumber );
            editor.remove( "currentOutgoingBIEditTxt"+biNumber );
            editor.remove( "voltageIncomingBIEditTxt"+biNumber );
            editor.remove( "currentIncomingBIEditTxt"+biNumber );
            editor.remove( "bideficiencyEditTxt"+biNumber );
            editor.remove( "lineClearBIEditTxt"+biNumber );
            editor.remove( "recordsBISpr"+biNumber );
            editor.remove( "biActionBySpr"+biNumber );
            editor.remove( "biActionByEditTxt"+biNumber );
            editor.apply();
        }
    }

    private void removeAcReplicatedGates(int acCount) {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for(int acNumber = 0; acNumber <= acCount-13 ; acNumber++ ) {
            editor.remove( "axleCounterEditTxt"+acNumber );
            editor.remove( "workingACSpr"+acNumber );
            editor.remove( "electricalACSpr"+acNumber );
            editor.remove( "resetACEditTxt"+acNumber );
            editor.remove( "acdeficiencyEditText"+acNumber );
            editor.remove( "acActionBySpr"+acNumber );
            editor.remove( "acActionByEditTxt"+acNumber );
            editor.apply();
        }
    }

    private void removeLevelCrossingReplicatedGates(int levelCrossingGateCount) {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for(int gateNumber = 1; gateNumber <= levelCrossingGateCount ; gateNumber++ ){
            editor.remove( "gateNo"+gateNumber );
            editor.remove( "nameOfGateman"+gateNumber );
            editor.remove( "gateTypeSpnr"+gateNumber );
            editor.remove( "gateTypeSpnrPosition"+gateNumber );
            editor.remove( getResources().getString( R.string.positive_boom_locking_tested )+gateNumber );
            editor.remove( getResources().getString( R.string.booms_were_painted )+gateNumber );
            editor.remove( getResources().getString( R.string.gateman_having_adequate_safety_knowledge )+gateNumber );
            editor.remove( getResources().getString( R.string.gate_telephone_s_found_in_working_order )+gateNumber );
            editor.remove( getResources().getString( R.string.sse_je_inspections_are_as_per_their_maintenance_schedule )+gateNumber );
            editor.remove( getResources().getString( R.string.inspection_maintenance_records_were_maintained )+gateNumber );
            editor.remove( getResources().getString( R.string.other_electrical_and_mechanical_parameter_of_gates_were_checked )+gateNumber );
            editor.remove( "anyDeficiencyFound"+gateNumber );
            editor.remove( "levelCrossingActionBySpr"+gateNumber );
            editor.remove( "levelCrossingActionBySprForReplicate"+gateNumber );
            editor.remove( "levelCrossingActionByEditTxt"+gateNumber );
            editor.apply();
        }
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( MainGearsActivity.this );
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

    private class sendDataToDBTask extends AsyncTask<Void, Void, Void> {
        int success = 0;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( context );
            pd.setMessage( "Submitting... " );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Boolean dataSent = null;
            try {
                dataSent = sendData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dataSent) {
                success =1;
            } else {
                success = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd != null) {
                pd.dismiss();
                mainGearsSubmitButton.setEnabled( true );
                if(success==0)
                    showAlertDialog( "Couldn't send the data. Please check your internet connection!" );
            }
            generatePdf generate = new generatePdf();
            generate.execute(  );
        }
    }

    private class generatePdf extends AsyncTask<Void, Void, Void> {
        int success = 0;
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( context );
            progressDialog.setMessage( "Generating Inspection Note... " );
            progressDialog.setCancelable( false );
            progressDialog.setIndeterminate( true );
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (isStoragePermissionGranted() ) {
                Looper.prepare();
                GenerateInspectionNote generateInspectionNote = new GenerateInspectionNote( mContext );
                generateInspectionNote.uploadPdf();
                generateInspectionNote.previewPdf();
                removeAllSharedPreferences();

                success =1;
            } else {
                success = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                mainGearsSubmitButton.setEnabled( true );
                if(success==0)
                    showAlertDialog( "Couldn't generate Inspection Note. Please review Storage Permission!" );
            }
        }
    }

    private class sendSMS extends AsyncTask<Void, Void, Void> {
        int success = 0;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( context );
            pd.setMessage( "Generating Inspection Note... " );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (isStoragePermissionGranted() ) {
//                generateInspectionNote = new GenerateInspectionNote( mContext );
//                generateInspectionNote.previewPdf();
                success =1;
            } else {
                success = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd != null) {
                pd.dismiss();
                mainGearsSubmitButton.setEnabled( true );
                if(success==0)
                    showAlertDialog( "Couldn't generate Inspection Note. Please review Storage Permission!" );
            }
        }
    }
}
