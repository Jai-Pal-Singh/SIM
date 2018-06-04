package com.android_developer.jaipal.sim;

import android.provider.ContactsContract;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;

public class GetData {
    private Connection connect;
    private String connectionResult ="";

    public boolean isInternetConnected(){
        ConnectToOracle connectToOracle = new ConnectToOracle();
        connect = connectToOracle.connections();
        if (connect==null){
            connectionResult = "Check Your Internet Connection!";
            Log.e( "error : ",connectionResult );
            return false;
        }
        else
            return true;
    }
    public int mainActivityInsertionQuery(String DIVISION, String INSPECTDATE, String STATIONCODE, String AUTHNAME, String AUTHDESIGNATION, int GTID, int PCID, int GTID2, int GTID3, int GTID4, String GTID5, String GTID6, int GTID7, String GTID8, int GTID9,int GTID10) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String query = "INSERT INTO USERNAME.MAINTABLE (DIVISION, INSPECTDATE, STATIONCODE, AUTHNAME, AUTHDESIGNATION, GTID, PCID, GTID2, GTID3, GTID4, GTID5, GTID6, GTID7, GTID8, GTID9, GTID10) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING SINID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,DIVISION );
                st.setString( 2,INSPECTDATE );
                st.setString( 3,STATIONCODE );
                st.setString( 4,AUTHNAME );
                st.setString( 5,AUTHDESIGNATION );
                st.setInt( 6,GTID );
                st.setInt( 7,PCID );
                st.setInt( 8,GTID2 );
                st.setInt( 9,GTID3 );
                st.setInt( 10,GTID4 );
                st.setString( 11,GTID5 );
                st.setString( 12,GTID6 );
                st.setInt( 13,GTID7 );
                st.setString( 14,GTID8 );
                st.setInt( 15,GTID9 );
                st.setInt( 16,GTID10 );
                st.registerReturnParameter(17, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
        connectionResult = ex.getMessage();
        Log.e( "Connection Result : ",connectionResult );
        tcid  =-1;
    }
        return tcid;
    }

    public HashMap getPasswordFromPhoneNumber(String Phone) {
        ResultSet result = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String query = "SELECT NAME, DESIGNATION, PASSWORD, ACTIVE, ADMIN, EMAIL FROM USERS WHERE PHONE = ?";
                PreparedStatement st = connect.prepareStatement( query );
                st.setString( 1,Phone );

                result =st.executeQuery();
                while (result.next()) {
                    hm.put( "authName",result.getString( "NAME" ) );
                    hm.put( "authDesig",result.getString( "DESIGNATION" ) );
                    hm.put( "pass",result.getString( "PASSWORD" ) );
                    hm.put( "active",result.getString( "ACTIVE" ) );
                    hm.put( "admin",result.getString( "ADMIN" ) );
                    hm.put( "email",result.getString( "EMAIL" ) );
                }
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
        }
        return hm;
    }

    public int registerUser(String NAME ,String DESIGNATION, String PHONE,String EMAIL, String PASSWORD){
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String query = "INSERT INTO USERNAME.USERS (NAME, DESIGNATION, PHONE, EMAIL, ACTIVE, PASSWORD, ADMIN, OTP) VALUES (?,?,?,?, '0', ?, '0', '0') RETURNING USERID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,NAME );
                st.setString( 2,DESIGNATION );
                st.setString( 3,PHONE );
                st.setString( 4,EMAIL );
                st.setString( 5,PASSWORD );
                st.registerReturnParameter(6, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int findAlreadyRegisteredUser(String Phone){
        ResultSet result = null ;
        int count=0;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
                count = -2;
            }
            else {
                String query = "SELECT * FROM USERS WHERE PHONE = ?";
                PreparedStatement st = connect.prepareStatement( query );
                st.setString( 1,Phone );
                result =st.executeQuery();
                while(result.next())
                    count++;
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            count  =-1;
        }
        return count;
    }

    public int putActiveStatus(String Phone){
        ResultSet result = null ;
        int count=0;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
                count = -2;
            }
            else {
                String query = "UPDATE USERNAME.USERS SET ACTIVE = '1' WHERE PHONE = ?";
                PreparedStatement st = connect.prepareStatement( query );
                st.setString( 1,Phone );
                result =st.executeQuery();
                count = 1;
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            count  =-1;
        }
        return count;
    }

    public int generalTelecomInsertionQuery( String STATIONMASTER, String SMKEY, String VHFSET, String CONTROLPHONE, String RAILWAYPHONE, String VHFREPEATER, String PASYSTEM, String VHFDEFICIENCY, String VHFACTIONBY, String TESTINGDATE, String POINTSTESTED, String CHTESTED, String BATTERYVOLTAGE, String DIGITALEQIPMENT, String BATTERYRECORDS, String EARTHTERMINATION, String OFCHUTDEFICIENCY, String OFCACTIONBY, String EMERGENCYSOCKETS, String EMERGENCYDEFICIENCY, String EMERGENCYACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.GENERALTELCOM (STATIONMASTER, SMKEY, VHFSET, CONTROLPHONE, RAILWAYPHONE, VHFREPEATER, PASYSTEM, VHFDEFICIENCY, VHFACTIONBY, TESTINGDATE, POINTSTESTED, CHTESTED, BATTERYVOLTAGE, DIGITALEQIPMENT, BATTERYRECORDS, EARTHTERMINATION,OFCHUTDEFICIENCY, OFCACTIONBY, EMERGENCYSOCKETS,EMERGENCYDEFICIENCY, EMERGENCYACTIONBY) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING GTID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,STATIONMASTER );
                st.setString( 2,SMKEY );
                st.setString( 3,VHFSET );
                st.setString( 4,CONTROLPHONE );
                st.setString( 5,RAILWAYPHONE );
                st.setString( 6,VHFREPEATER );
                st.setString( 7,PASYSTEM );
                st.setString( 8,VHFDEFICIENCY );
                st.setString( 9,VHFACTIONBY );
                st.setString( 10,TESTINGDATE );
                st.setString( 11,POINTSTESTED );
                st.setString( 12,CHTESTED );
                st.setString( 13,BATTERYVOLTAGE );
                st.setString( 14,DIGITALEQIPMENT );
                st.setString( 15,BATTERYRECORDS );
                st.setString( 16,EARTHTERMINATION );
                st.setString( 17,OFCHUTDEFICIENCY );
                st.setString( 18,OFCACTIONBY );
                st.setString( 19,EMERGENCYSOCKETS );
                st.setString( 20,EMERGENCYDEFICIENCY );
                st.setString( 21,EMERGENCYACTIONBY );
                st.registerReturnParameter(22, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int pointsCrossingsInsertionQuery(Boolean POINTOPERATED, Boolean TRDROPPED, Boolean OPENINGPOINT, Boolean FILLERGAUGE, Boolean POINTMAINTAINENCE, String DEFICIENCY, String ACTIONBY, String POINTDETAILS, String OBSTRUCTIONCURRENT, String LOCKEDPOINTS, String LOCKEDPOINTSACTIONBY, String LASTJOINTDATE, String LASTJOINTDEFICIENCY, String LASTJOINTACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.POINTSCROSSING (POINTOPERATED, TRDROPPED, OPENINGPOINT, FILLERGAUGE, POINTMAINTAINENCE, DEFICIENCY, ACTIONBY, POINTDETAILS, OBSTRUCTIONCURRENT, LOCKEDPOINTS, LOCKEDPOINTSACTIONBY, LASTJOINTDATE, LASTJOINTDEFICIENCY, LASTJOINTACTIONBY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING PCID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setBoolean( 1,POINTOPERATED );
                st.setBoolean( 2,TRDROPPED );
                st.setBoolean( 3,OPENINGPOINT );
                st.setBoolean( 4,FILLERGAUGE );
                st.setBoolean( 5,POINTMAINTAINENCE );
                st.setString( 6,DEFICIENCY );
                st.setString( 7,ACTIONBY );
                st.setString( 8,POINTDETAILS );
                st.setString( 9,OBSTRUCTIONCURRENT );
                st.setString( 10,LOCKEDPOINTS );
                st.setString( 11,LOCKEDPOINTSACTIONBY );
                st.setString( 12,LASTJOINTDATE );
                st.setString( 13,LASTJOINTDEFICIENCY );
                st.setString( 14,LASTJOINTACTIONBY );
                st.registerReturnParameter(15, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int trackCircuitsInsertionQuery(Boolean DOUBLEBONDING, Boolean JTYPE, Boolean BOTHRAILS, Boolean SPECIFICGRAVITY, Boolean RELAYEND,Boolean RECORDSTC, String DEFICIENCY, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.TRACKCIRCUITS (DOUBLEBONDING, JTYPE, BOTHRAILS, SPECIFICGRAVITY, RELAYEND, RECORDSTC,DEFICIENCY, ACTIONBY) VALUES(?,?,?,?,?,?,?,?) RETURNING TCID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setBoolean( 1,DOUBLEBONDING );
                st.setBoolean( 2,JTYPE );
                st.setBoolean( 3,BOTHRAILS );
                st.setBoolean( 4,SPECIFICGRAVITY );
                st.setBoolean( 5,RELAYEND );
                st.setBoolean( 6,RECORDSTC );
                st.setString( 7,DEFICIENCY );
                st.setString( 8,ACTIONBY );
                st.registerReturnParameter(9, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int signalsInsertionQuery(Boolean SIGNALLAMP, Boolean UECR, Boolean CASCADED, Boolean REDLAMP, String SIGNALNO1,String ASPECT1, String VOLTAGE1, String CURRENT1, String SIGNALNO2,String ASPECT2, String VOLTAGE2, String CURRENT2, String SIGNALNO3,String ASPECT3, String VOLTAGE3, String CURRENT3, String SIGNALNO4,String ASPECT4, String VOLTAGE4, String CURRENT4, String SIGNALNO5,String ASPECT5, String VOLTAGE5, String CURRENT5, String SIGNALNO6,String ASPECT6, String VOLTAGE6, String CURRENT6, String SIGNALNO7,String ASPECT7, String VOLTAGE7, String CURRENT7, String SIGNALNO8,String ASPECT8, String VOLTAGE8, String CURRENT8, String SIGNALNO9,String ASPECT9, String VOLTAGE9, String CURRENT9, String SIGNALNO10,String ASPECT10, String VOLTAGE10, String CURRENT10, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.SIGNALS (SIGNALLAMP, UECR, CASCADED, REDLAMP, SIGNALNO1, ASPECT1, VOLTAGE1, CURRENT1, SIGNALNO2, ASPECT2, VOLTAGE2, CURRENT2, SIGNALNO3, ASPECT3, VOLTAGE3, CURRENT3, SIGNALNO4, ASPECT4, VOLTAGE4, CURRENT4, SIGNALNO5, ASPECT5, VOLTAGE5, CURRENT5, SIGNALNO6, ASPECT6, VOLTAGE6, CURRENT6, SIGNALNO7,ASPECT7, VOLTAGE7, CURRENT7, SIGNALNO8, ASPECT8, VOLTAGE8, CURRENT8, SIGNALNO9, ASPECT9, VOLTAGE9, CURRENT9, SIGNALNO10, ASPECT10, VOLTAGE10, CURRENT10, ACTIONBY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING SID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setBoolean( 1,SIGNALLAMP );
                st.setBoolean( 2,UECR );
                st.setBoolean( 3,CASCADED );
                st.setBoolean( 4,REDLAMP );
                st.setString( 5,SIGNALNO1 );st.setString( 6,ASPECT1 );st.setString( 7,VOLTAGE1 );st.setString( 8,CURRENT1 );
                st.setString( 9,SIGNALNO2 );st.setString( 10,ASPECT2 );st.setString( 11,VOLTAGE2 );st.setString( 12,CURRENT2 );
                st.setString( 13,SIGNALNO3 );st.setString( 14,ASPECT3 );st.setString( 15,VOLTAGE3 );st.setString( 16,CURRENT3 );
                st.setString( 17,SIGNALNO4 );st.setString( 18,ASPECT4 );st.setString( 19,VOLTAGE4 );st.setString( 20,CURRENT4 );
                st.setString( 21,SIGNALNO5 );st.setString( 22,ASPECT5 );st.setString( 23,VOLTAGE5 );st.setString( 24,CURRENT5 );
                st.setString( 25,SIGNALNO6 );st.setString( 26,ASPECT6 );st.setString( 27,VOLTAGE6 );st.setString( 28,CURRENT6 );
                st.setString( 29,SIGNALNO7 );st.setString( 30,ASPECT7 );st.setString( 31,VOLTAGE7 );st.setString( 32,CURRENT7 );
                st.setString( 33,SIGNALNO8 );st.setString( 34,ASPECT8 );st.setString( 35,VOLTAGE8 );st.setString( 36,CURRENT8 );
                st.setString( 37,SIGNALNO9 );st.setString( 38,ASPECT9 );st.setString( 39,VOLTAGE9 );st.setString( 40,CURRENT9 );
                st.setString( 41,SIGNALNO10 );st.setString( 42,ASPECT10 );st.setString( 43,VOLTAGE10 );st.setString( 44,CURRENT10 );
                st.setString( 45,ACTIONBY );
                st.registerReturnParameter(46, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int ccipInsertionQuery(String INTERLOCKING,Boolean SIP, Boolean COUNTERVDU, Boolean COGGBCOUNTER, Boolean COCYNCOUNTER, Boolean EMERGENCYCROSSOVER,Boolean SAMPLECHECK, String DEFICIENCY,String ERRB, String RRBU, String COGGN, String COCYN, String EBPU,String ECH,String TESTINGDATE, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.CCIP (INTERLOCKING, SIP, COUNTERVDU, COGGBCOUNTER, COCYNCOUNTER, EMERGENCYCROSSOVER, SAMPLECHECK, DEFICIENCY, ERRB, RRBU, COGGN, COCYN, EBPU, ECH, TESTINGDATE, ACTIONBY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING CCIPID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,INTERLOCKING );
                st.setBoolean( 2,SIP );
                st.setBoolean( 3,COUNTERVDU );
                st.setBoolean( 4,COGGBCOUNTER );
                st.setBoolean( 5,COCYNCOUNTER );
                st.setBoolean( 6,EMERGENCYCROSSOVER );
                st.setBoolean( 7,SAMPLECHECK );
                st.setString( 8,DEFICIENCY );
                st.setString( 9,ERRB );
                st.setString( 10,RRBU );
                st.setString( 11,COGGN );
                st.setString( 12,COCYN );
                st.setString( 13,EBPU );
                st.setString( 14,ECH );
                st.setString( 15,TESTINGDATE );
                st.setString( 16,ACTIONBY );
                st.registerReturnParameter(17, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int powerSupplyInsertionQuery(String IPSMAKE,Boolean AMC, Boolean SMR, Boolean EARTHINGIPS, String IPSON,String IPSOFF, String AFTER, String MINIMUM, String MAXIMUM, Boolean BATTERYREADINGREORDS, Boolean WASHINGBATTERY, String OPENING,String SPARERELAY, String WASHINGRELAY, String ELECTRICAL, String EARTHINGCTR, String AMCEXECUTED,String MAINTAINENCERECOREDS, String LASTVALIDATION,String VALIDATIONBY, String EIMAKE, String LASTSWITCHOVERFROM, String LASTSWITCHOVERTO, String LASTSWITCHOVERON,String EIRACK,String VOLTAGE, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.POWERSUPPLY (IPSMAKE, AMC, SMR, EARTHINGIPS, IPSON, IPSOFF, AFTER, MINIMUM, MAXIMUM, BATTERYREADINGREORDS, WASHINGBATTERY, OPENING, SPARERELAY, WASHINGRELAY, ELECTRICAL, EARTHINGCTR, AMCEXECUTED, MAINTAINENCERECOREDS, LASTVALIDATION, VALIDATIONBY, EIMAKE, LASTSWITCHOVERFROM, LASTSWITCHOVERTO, LASTSWITCHOVERON, EIRACK, VOLTAGE, ACTIONBY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING PSID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,IPSMAKE );
                st.setBoolean( 2,AMC );
                st.setBoolean( 3,SMR );
                st.setBoolean( 4,EARTHINGIPS );
                st.setString( 5,IPSON );
                st.setString( 6,IPSOFF );
                st.setString( 7,AFTER );
                st.setString( 8,MINIMUM );
                st.setString( 9,MAXIMUM );
                st.setBoolean( 10,BATTERYREADINGREORDS );
                st.setBoolean( 11,WASHINGBATTERY );
                st.setString( 12,OPENING );
                st.setString( 13,SPARERELAY );
                st.setString( 14,WASHINGRELAY );
                st.setString( 15,ELECTRICAL );
                st.setString( 16,EARTHINGCTR );
                st.setString( 17,AMCEXECUTED );
                st.setString( 18,MAINTAINENCERECOREDS );
                st.setString( 19,LASTVALIDATION );
                st.setString( 20,VALIDATIONBY );
                st.setString( 21,EIMAKE );
                st.setString( 22,LASTSWITCHOVERFROM );
                st.setString( 23,LASTSWITCHOVERTO );
                st.setString( 24,LASTSWITCHOVERON );
                st.setString( 25,EIRACK );
                st.setString( 26,VOLTAGE );
                st.setString( 27,ACTIONBY );
                st.registerReturnParameter(28, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int recordsInsertionQuery(String POINT1,String TRACK1, String BI1, String POWER1, String OTHERS1, String POINT2,String TRACK2, String BI2,String POWER2, String OTHERS2, String POINTS3, String TRACK3, String BI3,String POWER3,String OTHER3, String SIGNALREMARK,String DR1,String DR2, String DR3, String DRREMARK, String RR1, String RR2,String RR3, String RRREMARK,String SIGNALINFRINGE, String EARTHTESTING, String CABLEMEGGERING, String CIRCUIT, String CRP,String CCC,String SIP, String SMC,String SMC12,String SHB, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.RECORDS (POINT1, TRACK1, BI1, POWER1, OTHERS1, POINT2, TRACK2, BI2, POWER2, OTHERS2, POINTS3, TRACK3, BI3, POWER3, OTHER3, SIGNALREMARK, DR1, DR2, DR3, DRREMARK, RR1, RR2, RR3, RRREMARK, SIGNALINFRINGE, EARTHTESTING, CABLEMEGGERING, CIRCUIT, CRP, CCC, SIP, SMC, SMC12, SHB, ACTIONBY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING RID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,POINT1 );st.setString( 2,TRACK1 ); st.setString( 3,BI1 );
                st.setString( 4,POWER1 );st.setString( 5,OTHERS1 );
                st.setString( 6,POINT2 );st.setString( 7,TRACK2 );st.setString( 8,BI2 );
                st.setString( 9,POWER2 );st.setString( 10,OTHERS2 );
                st.setString( 11,POINTS3 );st.setString( 12,TRACK3 );st.setString( 13,BI3 );
                st.setString( 14,POWER3 );st.setString( 15,OTHER3 );
                st.setString( 16,SIGNALREMARK );
                st.setString( 17,DR1 );st.setString( 18,DR2 );st.setString( 19,DR3 );
                st.setString( 20,DRREMARK );
                st.setString( 21,RR1 );st.setString( 22,RR2 ); st.setString( 23,RR3 );
                st.setString( 24,RRREMARK );
                st.setString( 25,SIGNALINFRINGE );
                st.setString( 26,EARTHTESTING );
                st.setString( 27,CABLEMEGGERING );
                st.setString( 28,CIRCUIT );
                st.setString( 29,CRP );
                st.setString( 30,CCC );
                st.setString( 31,SIP );
                st.setString( 32,SMC );
                st.setString( 33,SMC12 );
                st.setString( 34,SHB );
                st.setString( 35,ACTIONBY );
                st.registerReturnParameter(36, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int nonSntInsertionQuery(String ENGINEERINGDEFICIENCY,String ENGINEERINGACTIONBY, String ENGINEERINGDEFICIENCY1, String ENGINEERINGACTIONBY1, String ENGINEERINGDEFICIENCY2, String ENGINEERINGACTIONBY2,String OTHERDEFICIENCY, String ENGINEERINGACTIONBY3) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.NONSNT (ENGINEERINGDEFICIENCY, ENGINEERINGACTIONBY, ENGINEERINGDEFICIENCY1, ENGINEERINGACTIONBY1, ENGINEERINGDEFICIENCY2, ENGINEERINGACTIONBY2, OTHERDEFICIENCY, ENGINEERINGACTIONBY3)VALUES(?,?,?,?,?,?,?,?) RETURNING NONSNTID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,ENGINEERINGDEFICIENCY );st.setString( 2,ENGINEERINGACTIONBY );
                st.setString( 3,ENGINEERINGDEFICIENCY1 );st.setString( 4,ENGINEERINGACTIONBY1 );
                st.setString( 5,ENGINEERINGDEFICIENCY2 );st.setString( 6,ENGINEERINGACTIONBY2 );
                st.setString( 7,OTHERDEFICIENCY );st.setString( 8,ENGINEERINGACTIONBY3 );
                st.registerReturnParameter(9, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int levelCrossingInsertionQuery(String GATENO,String GATEMAN, String GATETYPE, Boolean POSITIVEBOOM, Boolean BOOM, Boolean SAFETY,Boolean TELEPHONE, Boolean SSE,Boolean INSPECTION, Boolean OTHER, String DEFICIENCY, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.LEVELCROSSING (GATENO, GATEMAN, GATETYPE, POSITIVEBOOM, BOOM, SAFETY, TELEPHONE, SSE, INSPECTION, OTHER, DEFICIENCY, ACTIONBY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?) RETURNING LCID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,GATENO );
                st.setString( 2,GATEMAN );
                st.setString( 3,GATETYPE );
                st.setBoolean( 4,POSITIVEBOOM );
                st.setBoolean( 5,BOOM );
                st.setBoolean( 6,SAFETY );
                st.setBoolean( 7,TELEPHONE );
                st.setBoolean( 8,SSE );
                st.setBoolean( 9,INSPECTION );
                st.setBoolean( 10,OTHER );
                st.setString( 11,DEFICIENCY );
                st.setString( 12,ACTIONBY );
                st.registerReturnParameter(13, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int blockInstrumentInsertionQuery(String NAME,String BVLOCAL, String BVLINE, String OVOLT, String OCURR, String IVOLT,String ICURR, String DEFICIENCY, String BILINE,String RECORDS, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.BLOCKINSTRUMENTS (NAME, BVLOCAL, BVLINE, OVOLT, OCURR, IVOLT, ICURR, DEFICIENCY, BILINE, RECORDS, ACTIONBY) VALUES(?,?,?,?,?,?,?,?,?,?,?) RETURNING BIID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,NAME );st.setString( 2,BVLOCAL );
                st.setString( 3,BVLINE );st.setString( 4,OVOLT );
                st.setString( 5,OCURR );st.setString( 6,IVOLT );
                st.setString( 7,ICURR );st.setString( 8,DEFICIENCY );
                st.setString( 9,BILINE );
                st.setString( 10,RECORDS );st.setString( 11,ACTIONBY );
                st.registerReturnParameter(12, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int axleCounterInsertionQuery(String NAME,String WORKINGON, String ELECTRICAL, String COUNTER, String DEFICIENCY, String ACTIONBY) throws SQLException {
        ResultSet result = null ;
        int tcid = -1;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.AXLECOUNTERS (NAME, WORKINGON, ELECTRICAL, COUNTER, DEFICIENCY, ACTIONBY) VALUES(?,?,?,?,?,?) RETURNING AXID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,NAME );st.setString( 2,WORKINGON );
                st.setString( 3,ELECTRICAL );st.setString( 4,COUNTER );
                st.setString( 5,DEFICIENCY );st.setString( 6,ACTIONBY );
                st.registerReturnParameter(7, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public int updatePassword(String PHONE, String PASSWORD) {
        ResultSet result = null ;
        int count=0;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
                count = -2;
            }
            else {
                String query = "UPDATE USERNAME.USERS SET PASSWORD = ? WHERE PHONE = ?";
                PreparedStatement st = connect.prepareStatement( query );
                st.setString( 1,PASSWORD );
                st.setString( 2,PHONE );
                result =st.executeQuery();
                count = 1;
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            count  =-1;
        }
        return count;
    }

    public void updateOTPforPhone(String otp, String PHONE) {
        ResultSet result = null ;
        int count=0;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
                count = -2;
            }
            else {
                String query = "UPDATE USERNAME.USERS SET OTP = ? WHERE PHONE = ?";
                PreparedStatement st = connect.prepareStatement( query );
                st.setString( 1,otp );
                st.setString( 2,PHONE );
                result =st.executeQuery();
                count = 1;
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            count  =-1;
        }
    }

    public String getOTPFromPhone(String PHONE) {
        ResultSet result = null ;
        String count="";
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
                count = "-2";
            }
            else {
                String query = "SELECT OTP FROM USERNAME.USERS WHERE PHONE = ?";
                PreparedStatement st = connect.prepareStatement( query );
                st.setString( 1,PHONE );
                result =st.executeQuery();
                while (result.next()) {
                     count=result.getString( "OTP" );
                }
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            count  ="-1";
        }
        return count;
    }

    public int inspectionNoteInsertionQuery(String filename, String authDesignation, String stationCode, String dateTime, String division) {
        ResultSet result = null ;
        int tcid = 0;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
                tcid = -2;
            }
            else {
                String cols[] = {"TCID"};
                String query = "INSERT INTO USERNAME.INSPECTIONNOTES (FILENAME, INSPECTEDBY, STATION, TIME, DIVISION) VALUES(?,?,?,?,?) RETURNING INID INTO ?";
                OraclePreparedStatement st = (OraclePreparedStatement) connect.prepareStatement( query  );
                st.setString( 1,filename );st.setString( 2,authDesignation );
                st.setString( 3,stationCode );st.setString( 4,dateTime );
                st.setString( 5,division );
                st.registerReturnParameter(6, Types.NUMERIC);
                st.executeUpdate();
                result = st.getReturnResultSet();
                result.next();
                tcid = result.getInt( 1 );
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            tcid  =-1;
        }
        return tcid;
    }

    public List<InspectionNoteDataModel> getInpectionNoteData(){
        List<InspectionNoteDataModel> data=new ArrayList<>();
        ResultSet result = null ;
        try {
            ConnectToOracle connectToOracle = new ConnectToOracle();
            connect = connectToOracle.connections();
            if (connect==null){
                connectionResult = "Check Your Internet Connection!";
                Log.e( "error : ",connectionResult );
                data = null;
            }
            else {
                String query = "select * from USERNAME.INSPECTIONNOTES ORDER BY INID DESC";
                PreparedStatement st = connect.prepareStatement( query );

                result =st.executeQuery();

                StringBuffer stringBuffer = new StringBuffer();
                InspectionNoteDataModel dataModel = null;
                while (result.next()) {
                    dataModel= new InspectionNoteDataModel();
                    dataModel.setFilename(result.getString( "FILENAME" ));
                    dataModel.setInspectedBy(result.getString( "INSPECTEDBY" ));
                    dataModel.setStationCode(result.getString( "STATION" ));
                    dataModel.setTime(result.getString( "TIME" ));
                    dataModel.setDivision(result.getString( "DIVISION" ));

//                    stringBuffer.append(dataModel);
                    // stringBuffer.append(dataModel);
                    data.add(dataModel);
                }
                st.close();
                connectionResult = "Suceessful";
                Log.e( "Connection Result : ",connectionResult );
                connect.close();
            }
        }
        catch (Exception ex){
            connectionResult = ex.getMessage();
            Log.e( "Connection Result : ",connectionResult );
            data  = null;
        }
        return data;
    }
}