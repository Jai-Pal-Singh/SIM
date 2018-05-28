package com.android_developer.jaipal.sim;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConnectToOracle {

    @SuppressLint( "newApi" )
    public  Connection connections(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy( policy );
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String serverName = "sim.cbmhy1ronxyp.ap-south-1.rds.amazonaws.com";
            String serverPort = "1521";
            String sid = "SIMDB";
            String url = "jdbc:oracle:thin:@" + serverName + ":" + serverPort + ":" + sid;

            String username = "username";
            String password = "mypassword";

            connection = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException se){
            Log.e( "error from sql",se.getMessage() );
        }
        catch (ClassNotFoundException e){Log.e( "error from class",e.getMessage() );}
        catch (Exception ex){Log.e( "error from Exception",ex.getMessage() );}
        return connection;
    }
}
