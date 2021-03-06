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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String lc="",bi="",ac="",strid="";
    Bundle extras;
    GenerateInspectionNote generateInspectionNote;
    Map<String, List<String>> actionByMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature( Window.FEATURE_ACTION_BAR );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_gears );

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
            createHashMapActionBy();
            sendDataToDBTask task = new sendDataToDBTask();
            task.execute();
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
        strid = "";
        ActionBy = getActionBy( sharedpreferences.getString( "signalsActionBySpinner","" ), sharedpreferences.getString( "signalsActionByEditText","" ) );

        for(int gateNumber = 0; gateNumber <= sharedpreferences.getInt("signalTableRowCount", 0) ; gateNumber++ ){
            lcid = getData.signalTableRowInsertionQuery(
                    sharedpreferences.getString( "signalNoEditText"+gateNumber,"" ),
                    sharedpreferences.getString( "aspectEditText"+gateNumber,"" ),
                    sharedpreferences.getString( "voltageEditText"+gateNumber,"" ),
                    sharedpreferences.getString( "currentEditText"+gateNumber,"" )
            );
            strid +=lcid+" ";
            Log.e("str", "str = "+lcid);
        }

        sid = getData.signalsInsertionQuery(
                sharedpreferences.getBoolean( getResources().getString( R.string.signal_lamp_voltae_90_of_rated_value ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.vecr_drops_with_fusing_of_minimum_3_route_leds ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.signals_are_cascaded_for_e_g_fusing_of_a_signal_s_particular_aspect_other_than_red_illuminates_a_more_restrictive_aspect ),false ),
                sharedpreferences.getBoolean( getResources().getString( R.string.red_lamp_protection_working_e_g_fusing_of_red_aspect_of_signal_should_illuminate_signal_in_rear_with_most_restrictive_aspect_red ),false ),
                strid,
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
        removeSignalTableRows(sharedpreferences.getInt("signalTableRowCount",0));
        editor.remove( "signalsActionBySpinner" );
        editor.remove( "signalsActionBySpinnerPosition" );
        editor.remove( "signalsActionByEditText" );
        editor.remove( "signalsActivityComplete" );
        editor.remove( "signalTableRowCount" );
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
        editor.remove( "recordsBI1SprPosition" );
        editor.remove( "bi1ActionBySprPosition" );
        editor.remove( "recordsBI2SprPosition" );
        editor.remove( "bi2ActionBySprPosition" );
        editor.remove( "workingAC1SprPosition" );
        editor.remove( "electricalAC1SprPosition" );
        editor.remove( "ac1ActionBySprPosition" );
        editor.remove( "workingAC2SprPosition" );
        editor.remove( "electricalAC2SprPosition" );
        editor.remove( "ac2ActionBySprPosition" );
        removeBiReplicatedGates(sharedpreferences.getInt("biCount",0));
        removeAcReplicatedGates(sharedpreferences.getInt("acCount",0));
        editor.remove( "biAcActivityComplete" );
        editor.remove( "biCount" );
        editor.remove( "acCount" );
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
        editor.remove( "levelCrossingGateCount" );
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

    private void removeSignalTableRows(int rowCount) {
        sharedpreferences = getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for(int gateNumber = 0; gateNumber <= rowCount ; gateNumber++ ) {
            editor.remove( "signalNoEditText"+gateNumber );
            editor.remove( "aspectEditText"+gateNumber );
            editor.remove( "voltageEditText"+gateNumber );
            editor.remove( "currentEditText"+gateNumber );
            editor.apply();
        }
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
            editor.remove( "recordsBISprPosition"+biNumber );
            editor.remove( "biActionBySprPosition"+biNumber );
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
            editor.remove( "workingACSprPosition"+acNumber );
            editor.remove( "electricalACSprPosition"+acNumber );
            editor.remove( "acActionBySprPosition"+acNumber );
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

    public void createHashMapActionBy(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        List<String> stringList = new ArrayList<String>();
        actionByMap = new HashMap<String, List<String>>();
        //General Telecom
        if(isActionRequired( sharedpreferences.getString("vhfSetActionBySpinner", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "vhfSetActionBySpinner", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "vhfSetActionBySpinner", "" ), Arrays.asList("General/Telecom") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "vhfSetActionBySpinner", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "General/Telecom" );
                actionByMap.put( sharedpreferences.getString( "vhfSetActionBySpinner", "" ), stringList );
            }
        }
        if(isActionRequired( sharedpreferences.getString("digitalEquipActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "digitalEquipActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "digitalEquipActionBySpr", "" ), Collections.singletonList( "General/Telecom" ) );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "digitalEquipActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "General/Telecom" );
                actionByMap.put( sharedpreferences.getString( "digitalEquipActionBySpr", "" ),stringList );
            }
        }
        if(isActionRequired( sharedpreferences.getString("testedSocketsActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "testedSocketsActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "testedSocketsActionBySpr", "" ), Arrays.asList("General/Telecom") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "testedSocketsActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "General/Telecom" );
                actionByMap.put( sharedpreferences.getString( "testedSocketsActionBySpr", "" ),stringList );
            }
        }
        //Points and Crossing
        if(isActionRequired( sharedpreferences.getString("pointsCrossingEnsureActionBySpinner", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "pointsCrossingEnsureActionBySpinner", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "pointsCrossingEnsureActionBySpinner", "" ), Arrays.asList("Points and Crossings") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "pointsCrossingEnsureActionBySpinner", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Points and Crossings" );
                actionByMap.put( sharedpreferences.getString( "pointsCrossingEnsureActionBySpinner", "" ),stringList );
            }
        }
        if(isActionRequired( sharedpreferences.getString("pointsCrossingDetailsActionBySpinner", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "pointsCrossingDetailsActionBySpinner", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "pointsCrossingDetailsActionBySpinner", "" ), Arrays.asList("Points and Crossings") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "pointsCrossingDetailsActionBySpinner", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Points and Crossings" );
                actionByMap.put( sharedpreferences.getString( "pointsCrossingDetailsActionBySpinner", "" ),stringList );
            }
        }
        if(isActionRequired( sharedpreferences.getString("pointsCrossingLastJointActionBySpinner", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "pointsCrossingLastJointActionBySpinner", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "pointsCrossingLastJointActionBySpinner", "" ), Arrays.asList("Points and Crossings") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "pointsCrossingLastJointActionBySpinner", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Points and Crossings" );
                actionByMap.put( sharedpreferences.getString( "pointsCrossingLastJointActionBySpinner", "" ),stringList );
            }
        }
        //Track Circuits
        if(isActionRequired( sharedpreferences.getString("trackCircuitsActionBySpinner", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "trackCircuitsActionBySpinner", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "trackCircuitsActionBySpinner", "" ), Arrays.asList("Track Circuits") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "trackCircuitsActionBySpinner", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Track Circuits" );
                actionByMap.put( sharedpreferences.getString( "trackCircuitsActionBySpinner", "" ),stringList );
            }
        }
        //Signals
        if(isActionRequired( sharedpreferences.getString("signalsActionBySpinner", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "signalsActionBySpinner", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "signalsActionBySpinner", "" ), Arrays.asList("Signals") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "signalsActionBySpinner", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Signals" );
                actionByMap.put( sharedpreferences.getString( "signalsActionBySpinner", "" ),stringList );
            }
        }
        //CCIP/VDU
        if(isActionRequired( sharedpreferences.getString("ccipActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "ccipActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "ccipActionBySpr", "" ), Arrays.asList("CCIP/VDU") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "ccipActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "CCIP/VDU" );
                actionByMap.put( sharedpreferences.getString( "ccipActionBySpr", "" ),stringList );
            }

        }
        //Block Instruments and Axle Counters
        if(isActionRequired( sharedpreferences.getString("bi1ActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "bi1ActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "bi1ActionBySpr", "" ), Arrays.asList("Block Instruments") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "bi1ActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Block Instruments" );
                actionByMap.put( sharedpreferences.getString( "bi1ActionBySpr", "" ),stringList );
            }
        }
        if(isActionRequired( sharedpreferences.getString("bi2ActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "bi2ActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "bi2ActionBySpr", "" ), Arrays.asList("Block Instruments") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "bi2ActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Block Instruments" );
                actionByMap.put( sharedpreferences.getString( "bi2ActionBySpr", "" ),stringList );
            }
        }
        for(int biNumber = 0; biNumber <= sharedpreferences.getInt("biCount", 3)-3 ; biNumber++){
            if(isActionRequired( sharedpreferences.getString("biActionBySpr"+biNumber, ""))) {
                if(actionByMap.get( sharedpreferences.getString( "biActionBySpr" + biNumber, "" ) )==null){
                    actionByMap.put( sharedpreferences.getString( "biActionBySpr" + biNumber, "" ), Arrays.asList("Block Instruments") );
                }
                else {
                    stringList = actionByMap.get( sharedpreferences.getString( "biActionBySpr" + biNumber, "" ) );
                    stringList = new ArrayList<String>(stringList);
                    stringList.add( "Block Instruments" );
                    actionByMap.put( sharedpreferences.getString( "biActionBySpr" + biNumber, "" ), stringList );
                }
            }
        }
        if(isActionRequired( sharedpreferences.getString("ac1ActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "ac1ActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "ac1ActionBySpr", "" ), Arrays.asList("Axle Counters") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "ac1ActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Axle Counters" );
                actionByMap.put( sharedpreferences.getString( "ac1ActionBySpr", "" ),stringList );
            }
        }
        if(isActionRequired( sharedpreferences.getString("ac2ActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "ac2ActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "ac2ActionBySpr", "" ), Arrays.asList("Axle Counters") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "ac2ActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Axle Counters" );
                actionByMap.put( sharedpreferences.getString( "ac2ActionBySpr", "" ),stringList );
            }
        }
        for(int acNumber = 0; acNumber <= sharedpreferences.getInt("acCount", 13)-13 ; acNumber++  ){
            if(isActionRequired( sharedpreferences.getString("acActionBySpr"+acNumber, ""))) {
                if(actionByMap.get( sharedpreferences.getString( "acActionBySpr" + acNumber, "" ) )==null){
                    actionByMap.put( sharedpreferences.getString( "acActionBySpr" + acNumber, "" ), Arrays.asList("Axle Counters") );
                }
                else {
                    stringList = actionByMap.get( sharedpreferences.getString( "acActionBySpr" + acNumber, "" ) );
                    stringList = new ArrayList<String>(stringList);
                    stringList.add( "Axle Counters" );
                    actionByMap.put( sharedpreferences.getString( "acActionBySpr" + acNumber, "" ), stringList );
                }
            }
        }
        //Power Supply
        if(isActionRequired( sharedpreferences.getString("powerSupplyActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "powerSupplyActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "powerSupplyActionBySpr", "" ), Arrays.asList("Power Supply") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "powerSupplyActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Power Supply" );
                actionByMap.put( sharedpreferences.getString( "powerSupplyActionBySpr", "" ),stringList );
            }
        }
        //Level Crossing
        if(isActionRequired( sharedpreferences.getString("levelCrossingActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "levelCrossingActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "levelCrossingActionBySpr", "" ), Arrays.asList("Level Crossing") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "levelCrossingActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Level Crossing" );
                actionByMap.put( sharedpreferences.getString( "levelCrossingActionBySpr", "" ),stringList );
            }
        }
        for(int gateNumber = 1; gateNumber <= sharedpreferences.getInt( "levelCrossingGateCount",0 ) ; gateNumber++ ){
            if(isActionRequired( sharedpreferences.getString("levelCrossingActionBySpr"+gateNumber, ""))) {
                if(actionByMap.get( sharedpreferences.getString( "levelCrossingActionBySpr" + gateNumber, "" ) )==null){
                    actionByMap.put( sharedpreferences.getString( "levelCrossingActionBySpr" + gateNumber, "" ), Arrays.asList("Level Crossing") );
                }
                else {
                    stringList = actionByMap.get( sharedpreferences.getString( "levelCrossingActionBySpr" + gateNumber, "" ) );
                    stringList = new ArrayList<String>(stringList);
                    stringList.add( "Level Crossing" );
                    actionByMap.put( sharedpreferences.getString( "levelCrossingActionBySpr" + gateNumber, "" ), stringList );
                }
            }
        }
        //Records
        if(isActionRequired( sharedpreferences.getString("recordsActionBySpr", ""))) {
            if(actionByMap.get( sharedpreferences.getString( "recordsActionBySpr", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "recordsActionBySpr", "" ), Arrays.asList("Records") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "recordsActionBySpr", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Records" );
                actionByMap.put( sharedpreferences.getString( "recordsActionBySpr", "" ),stringList );
            }
        }
        //Non SnT
        if(!sharedpreferences.getString("engineeringDeficiencyEditText", "").isEmpty()){
            if(actionByMap.get( sharedpreferences.getString( "nonSntEngineeringActionByTxtView", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "nonSntEngineeringActionByTxtView", "" ), Arrays.asList("Non-SNT Deficiency") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "nonSntEngineeringActionByTxtView", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Non-SNT Deficiency" );
                actionByMap.put( sharedpreferences.getString( "nonSntEngineeringActionByTxtView", "" ),stringList );
            }
        }
        if(!sharedpreferences.getString("electricalDeficiencyEditText", "").isEmpty()){
            if(actionByMap.get( sharedpreferences.getString( "nonSntElectricalActionByTxtView", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "nonSntElectricalActionByTxtView", "" ), Arrays.asList("Non-SNT Deficiency") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "nonSntElectricalActionByTxtView", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Non-SNT Deficiency" );
                actionByMap.put( sharedpreferences.getString( "nonSntElectricalActionByTxtView", "" ),stringList );
            }
        }
        if(!sharedpreferences.getString("operatingDeficiencyEditText", "").isEmpty()){
            if(actionByMap.get( sharedpreferences.getString( "nonSntOperatingActionByTxtView", "" ) )==null){
                actionByMap.put( sharedpreferences.getString( "nonSntOperatingActionByTxtView", "" ), Arrays.asList("Non-SNT Deficiency") );
            }
            else {
                stringList = actionByMap.get( sharedpreferences.getString( "nonSntOperatingActionByTxtView", "" ) );
                stringList = new ArrayList<String>(stringList);
                stringList.add( "Non-SNT Deficiency" );
                actionByMap.put( sharedpreferences.getString( "nonSntOperatingActionByTxtView", "" ),stringList );
            }
        }
    }

    public ArrayList<String> getPhoneNumberForHashMapEntries(){
        GetData getData = new GetData();
        ArrayList<String> phoneNumberList = new ArrayList<String>(  );
        String phoneNo = "";
        for (Map.Entry<String, List<String>> entry : actionByMap.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (key.equals( "SSE/Sig/" + sharedpreferences.getString( "stationCode", "" ) )) {
                //getting phone number for monthly and quarterly SSE/Sig
                Map<String, String> hs = getData.getSSESigPhoneNoFromStation( sharedpreferences.getString( "stationCode", "" ) );
                phoneNo = hs.get( "MonthlyInspectionBy" );
                if(!phoneNo.equals( "Vacant" )&&!phoneNo.equals( null ))
                    phoneNumberList.add( phoneNo );
                phoneNo = hs.get( "QuaterlyInspectionBy" );
                if(!phoneNo.equals( "Vacant" )&&!phoneNo.equals( null ))
                    phoneNumberList.add( phoneNo );
            }
            else if (key.equals( "SSE/Tele/" + sharedpreferences.getString( "stationCode", "" ) )) {
                //getting phone number for monthly and quarterly SSE/Tele
                Map<String, String> hs = getData.getSSETelePhoneNoFromStation( sharedpreferences.getString( "stationCode", "" ) );
                phoneNo = hs.get( "MonthlyInspectionBy" );
                if(!phoneNo.equals( "Vacant" )&&!phoneNo.equals( null ))
                    phoneNumberList.add( phoneNo );
                phoneNo = hs.get( "QuaterlyInspectionBy" );
                if(!phoneNo.equals( "Vacant" )&&!phoneNo.equals( null ))
                    phoneNumberList.add( phoneNo );
            }
            else{
                if(value.contains( "General/Telecom" )) {
                    if (!actionByMap.containsKey( "SSE/Tele/" + sharedpreferences.getString( "stationCode", "" ) )) {
                        //getting phone number for monthly and quarterly SSE/Sig
                        Map<String, String> hs = getData.getSSETelePhoneNoFromStation( sharedpreferences.getString( "stationCode", "" ) );
                        phoneNo = hs.get( "MonthlyInspectionBy" );
                        if (!phoneNo.equals( "Vacant" ) && !phoneNo.equals( null ))
                            phoneNumberList.add( phoneNo );
                        phoneNo = hs.get( "QuaterlyInspectionBy" );
                        if (!phoneNo.equals( "Vacant" ) && !phoneNo.equals( null ))
                            phoneNumberList.add( phoneNo );
                    }
                }
                else {
                    if (!actionByMap.containsKey( "SSE/Sig/" + sharedpreferences.getString( "stationCode", "" ) )) {
                        //getting phone number for monthly and quarterly SSE/Sig
                        Map<String, String> hs = getData.getSSESigPhoneNoFromStation( sharedpreferences.getString( "stationCode", "" ) );
                        phoneNo = hs.get( "MonthlyInspectionBy" );
                        if (!phoneNo.equals( "Vacant" ) && !phoneNo.equals( null ))
                            phoneNumberList.add( phoneNo );
                        phoneNo = hs.get( "QuaterlyInspectionBy" );
                        if (!phoneNo.equals( "Vacant" ) && !phoneNo.equals( null ))
                            phoneNumberList.add( phoneNo );
                    }
                }
                //getting phone number for other Designation
//                if(!actionByMap.containsKey( key )) {
                    phoneNo = getData.getPhoneNoFromDesignation( key );
//                    if (!phoneNo.equals( null ) || !phoneNo.equals( "-1" ) || !phoneNo.equals( "-2" )||!phoneNo.equals( "0" ))
                    if(phoneNo.length()==10)
                        phoneNumberList.add( phoneNo );
//                }
            }
        }
        return phoneNumberList;
    }

    public boolean isActionRequired(String s){
        return !s.equals( "None" ) && !s.equals( "Other" );
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
//            SendSMS sendSMS = new SendSMS();
//            sendSMS.execute(  );
        }
    }

    private class SendSMS extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        int success = 0;
        SmsHandler smsHandler;
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog( MainGearsActivity.this );
            pd.setMessage( "Sending SMS Notification... " );
            pd.setCancelable( false );
            pd.setIndeterminate( true );
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //message for Level Crossing
            String message = "There are pending items  for you at the station " + sharedpreferences.getString( "stationCode", "" ) + ", as per inspection performed by " + sharedpreferences.getString( "authDesignation", "" ) + " on " + sharedpreferences.getString( "inspectDate", "" ) + ". Visit the following link for more details : \r\n"+getResources().getString(R.string.fileServerUrl)+"/"+generateInspectionNote.returnFileName();

            //Sending Sms
            ArrayList<String> phoneNoList = getPhoneNumberForHashMapEntries();
            if(phoneNoList.isEmpty()){
                success = 1;
            }
            else {
                for (int i = 0; i < phoneNoList.size(); i++) {
                    smsHandler = new SmsHandler( MainGearsActivity.this, phoneNoList.get( i ), message );
                    success = smsHandler.sendSmsStatus();
                }
            }
//            if(isActionRequired(sharedpreferences.getString("levelCrossingActionBySpr", "")  )) {
//                //getting phone number for monthly and quarterly SSE/Sig
//                Map<String, String> hs = getData.getSSESigPhoneNoFromStation( sharedpreferences.getString( "stationCode", "" ) );
//                phoneNo = hs.get( "MonthlyInspectionBy" );
//
//                if(!phoneNo.equals( "Vacant" ))
//                    smsHandler = new SmsHandler( LevelCrossingActivity.this, phoneNo, message );
//                phoneNo = hs.get( "QuaterlyInspectionBy" );
//                if(!phoneNo.equals( "Vacant" ))
//                    smsHandler = new SmsHandler( LevelCrossingActivity.this, phoneNo, message );
//                //getting phone no and sending sms to other Inspecting officer except SSE/Sig and SSE/Tele
//                for (Map.Entry<String, String> entry : actionByMap.entrySet()) {
//                    String key = entry.getKey();
//                    if (!key.equals( "SSE/Sig/" + sharedpreferences.getString( "stationCode", "" ) )) {
//                        //code to get phone no from database and then send msg
//                        phoneNo = getData.getPhoneNoFromDesignation(sharedpreferences.getString( "authDesignation", "" ));
//                        smsHandler = new SmsHandler( LevelCrossingActivity.this, phoneNo, message );
//                    }
//                }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd != null) {
                pd.dismiss();

                if(success==0)
                    showAlertDialog( "Failed to send SMS." );
                else if(success==-1){
                    showAlertDialog( "Failed to send SMS. Url Exception" );
                }
                else if(success==-2){
                    showAlertDialog( "Couldn't send sms notification. Please check your internet connection!" );
                }
                else{
                    generateInspectionNote.previewPdf();
                    removeAllSharedPreferences();
//                    generatePdf generate = new generatePdf();
//                    generate.execute(  );
                }
            }
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
                generateInspectionNote = new GenerateInspectionNote( mContext );
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
                else{
                    UploadGeneratedPdf uploadGeneratedPdf = new UploadGeneratedPdf();
                    uploadGeneratedPdf.execute(  );
                }
            }
        }
    }

    private class UploadGeneratedPdf extends AsyncTask<Void, Void, Void> {
        int success = 0;
        int dataUpload = -3;
        private ProgressDialog progressDialog;
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog( MainGearsActivity.this );
            progressDialog.setMessage( "Uploading Inspection Note... " );
            progressDialog.setCancelable( false );
            progressDialog.setIndeterminate( true );
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            GetData getData = new GetData();
            if (isStoragePermissionGranted() ) {
//                Looper.prepare();
                generateInspectionNote.uploadPdf();
                dataUpload = getData.inspectionNoteInsertionQuery(generateInspectionNote.returnFileName(),sharedpreferences.getString("authDesignation", ""),sharedpreferences.getString("stationCode", ""),generateInspectionNote.returnDateTime(),sharedpreferences.getString("division", ""));
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
                if(dataUpload==-2 || success==0)
                    showAlertDialog( "Couldn't upload the Inspection Note. Please check your internet connection!" );
                else if(dataUpload==-1)
                    showAlertDialog( "Could not Connect to server. Try again after sometime" );
                else{
                    SendSMS sendSMS = new SendSMS();
                    sendSMS.execute(  );
//                    generateInspectionNote.previewPdf();
//                    removeAllSharedPreferences();
                }
            }
        }
    }
}
