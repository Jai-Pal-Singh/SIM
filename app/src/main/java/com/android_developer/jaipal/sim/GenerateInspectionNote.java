package com.android_developer.jaipal.sim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.itextpdf.text.html.HtmlTags.FONT;

public class GenerateInspectionNote extends Activity {

    Document document;
    File file;
    String path;
    String fileName;
    FileOutputStream fOut;
    Context mContext;

    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static Font boldSubFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static Font normalSubFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    public GenerateInspectionNote(Context mcontext){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mContext = mcontext;
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            document = new Document( PageSize.A4, 25, 25, 25, 25);
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SIM";
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();
            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss") ;
            String authDesig = sharedpreferences.getString("authDesignation", "");
            String newAuthDesig = authDesig.replaceAll( "/","_" );

            fileName = sharedpreferences.getString("stationCode", "")+"_"+newAuthDesig+"_"+dateFormat.format(date)+".pdf";
//            file = new File(dir, sharedpreferences.getString("stationCode", "")+"_"+newAuthDesig+"_"+dateFormat.format(date)+".pdf");
            file = new File( dir, fileName );
            fOut = new FileOutputStream(file);
            PdfWriter writer =PdfWriter.getInstance(document, fOut);
            Header event = new Header();
            writer.setPageEvent(event);
            document.open();
            addContent(document);
            document.close();
//            previewPdf();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error Instance : ",e.getMessage());
        }
    }

    private void addContent(Document document) throws DocumentException, IOException {
        addMainActivityData( document );
        addGeneralTelecomData (document);
        addPointsCrossingData(document);
        addTrackCircuitsData(document);
        addSignalsData(document);
        addCcipVduData(document);
        addBiAcData(document);
        addPowerSupplyData(document);
        addLevelCrossingData(document);
        addRecordsData(document);
        addNonSntData( document );
        addDocumentEndContent(document);
    }

    private void addMainActivityData(Document document) throws DocumentException, IOException {
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );

        Drawable d = mContext.getResources().getDrawable(R.drawable.unnamed);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image img = Image.getInstance(stream.toByteArray());
        img.scaleAbsolute( 80,80);
        PdfPCell emptyCell1 = new PdfPCell(img);
        emptyCell1.setBorder( PdfPCell.NO_BORDER );
        emptyCell1.setColspan( 1 );
        emptyCell1.setPaddingLeft( 240 );
        emptyCell1.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        docHeadingTable.addCell(emptyCell1);

        PdfPCell cell = new PdfPCell(new Phrase("NORTH WESTERN RAILWAY", catFont));
        cell.setBorder( PdfPCell.NO_BORDER );
        cell.setColspan( 1 );
        cell.setPaddingLeft( 165 );
        cell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        docHeadingTable.addCell(cell);

        PdfPCell signalCell = new PdfPCell(new Phrase("Signal & Telecom Department", subFont));
        signalCell.setBorder( PdfPCell.NO_BORDER );
//        signalCell.setCellEvent(new SolidBorder(PdfPCell.RIGHT));
        signalCell.setColspan( 1 );
        signalCell.setPaddingLeft( 190 );
//        signalCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        signalCell.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE );
        docHeadingTable.addCell(signalCell);

        PdfPCell noteCell = new PdfPCell(new Phrase("Officer's Inspection Note", subFont));
        noteCell.setBorder( PdfPCell.NO_BORDER );
//        noteCell.setCellEvent(new SolidBorder(PdfPCell.RIGHT));
        noteCell.setColspan( 1 );
        noteCell.setPaddingLeft( 206 );
//        noteCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        noteCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        docHeadingTable.addCell(noteCell);

        document.add( docHeadingTable );

        Paragraph preface1 = new Paragraph();
        addEmptyLine(preface1, 1);
        document.add( preface1 );

        PdfPTable docTitleTable = new PdfPTable( 10 );
        docTitleTable.setWidthPercentage( 100 );
        String div = getDivision(sharedpreferences.getString("division", ""));
        PdfPCell divisionCell = new PdfPCell(new Phrase(div+" DIVISION", subFont));
        divisionCell.setBorder( PdfPCell.NO_BORDER );
        divisionCell.setCellEvent(new SolidBorder(PdfPCell.RIGHT));
        divisionCell.setPaddingLeft( 20 );
        divisionCell.setColspan( 3 );
        divisionCell.setRowspan( 2 );
        divisionCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        divisionCell.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE );
        divisionCell.setHorizontalAlignment( PdfPCell.ALIGN_MIDDLE );
        docTitleTable.addCell(divisionCell);

        PdfPCell dateCell = new PdfPCell(new Phrase("Date", boldSubFont));
        dateCell.setBorder( PdfPCell.NO_BORDER );
        dateCell.setColspan( 1 );
        dateCell.setPaddingLeft( 10 );
        dateCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        dateCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        docTitleTable.addCell(dateCell);

        PdfPCell dateEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("inspectDate", null),normalSubFont));
        dateEntryCell.setBorder( PdfPCell.NO_BORDER );
        dateEntryCell.setCellEvent(new SolidBorder(PdfPCell.RIGHT));
        dateEntryCell.setColspan( 3 );
        dateEntryCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        dateEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        docTitleTable.addCell(dateEntryCell);

        PdfPCell nameCell = new PdfPCell(new Phrase("Name", boldSubFont));
        nameCell.setBorder( PdfPCell.NO_BORDER );
        nameCell.setPaddingLeft( 10 );
        nameCell.setColspan( 1 );
        nameCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        nameCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        docTitleTable.addCell(nameCell);

        PdfPCell nameEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("authName", null),normalSubFont));
        nameEntryCell.setBorder( PdfPCell.NO_BORDER );
        nameEntryCell.setColspan( 2 );
        nameEntryCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        nameEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        docTitleTable.addCell(nameEntryCell);

        PdfPCell stationCell = new PdfPCell(new Phrase("Station", boldSubFont));
        stationCell.setBorder( PdfPCell.NO_BORDER );
        stationCell.setPaddingLeft( 10 );
        stationCell.setColspan( 1 );
        stationCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        stationCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        docTitleTable.addCell(stationCell);

        PdfPCell stationEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("station", null),normalSubFont));
        stationEntryCell.setBorder( PdfPCell.NO_BORDER );
        stationEntryCell.setColspan( 3 );
        stationEntryCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        stationEntryCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        docTitleTable.addCell(stationEntryCell);

        PdfPCell desigCell = new PdfPCell(new Phrase("Desig.", boldSubFont));
        desigCell.setBorder( PdfPCell.NO_BORDER );
        desigCell.setCellEvent(new SolidBorder(PdfPCell.LEFT));
        desigCell.setPaddingLeft( 10 );
        desigCell.setColspan( 1 );
        desigCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        desigCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        docTitleTable.addCell(desigCell);

        PdfPCell desigEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("authDesignation", null),normalSubFont));
        desigEntryCell.setBorder( PdfPCell.NO_BORDER );
        desigEntryCell.setColspan( 2 );
        desigEntryCell.setBackgroundColor( BaseColor.LIGHT_GRAY );
        desigEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        docTitleTable.addCell(desigEntryCell);

        document.add( docTitleTable );

        Paragraph preface2 = new Paragraph();
        addEmptyLine(preface2, 1);
        document.add( preface2 );
    }

    private void addGeneralTelecomData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine(emptyLine1, 1);
        document.add( emptyLine1 );

        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );

        PdfPCell headingCell1 = new PdfPCell(new Phrase("General/Telecom", subFont));
        headingCell1.setBorder( PdfPCell.NO_BORDER );
        headingCell1.setColspan( 1 );
        headingCell1.setBackgroundColor( BaseColor.LIGHT_GRAY );
        headingCell1.setVerticalAlignment( PdfPCell.ALIGN_LEFT );
        docHeadingTable.addCell(headingCell1);
        document.add( docHeadingTable );

        PdfPTable stationMaster = new PdfPTable( 5 );
        stationMaster.setWidthPercentage( 100 );

        PdfPCell stationMasterCell = new PdfPCell(new Phrase("On-duty Station Master", boldSubFont));
        stationMasterCell.setBorder( PdfPCell.NO_BORDER );
        stationMasterCell.setColspan( 3 );
        stationMasterCell.setIndent( 20 );
        stationMasterCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(stationMasterCell);

        PdfPCell dateEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("SMeditText", ""),normalSubFont));
        dateEntryCell.setBorder( PdfPCell.NO_BORDER );
        dateEntryCell.setColspan( 2 );
        dateEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(dateEntryCell);

        PdfPCell smKeyCell = new PdfPCell(new Phrase("SM's Key was found in working order", boldSubFont));
        smKeyCell.setBorder( PdfPCell.NO_BORDER );
        smKeyCell.setColspan( 3 );
        smKeyCell.setIndent( 20 );
        smKeyCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(smKeyCell);

        PdfPCell smKeyEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("SMKeyValue", ""),normalSubFont));
        smKeyEntryCell.setBorder( PdfPCell.NO_BORDER );
        smKeyEntryCell.setColspan( 2 );
        smKeyEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(smKeyEntryCell);

        PdfPCell checkingTelecomCell = new PdfPCell(new Phrase("Checking the Telecom Installation in SM's Room", boldSubFont));
        checkingTelecomCell.setBorder( PdfPCell.NO_BORDER );
        checkingTelecomCell.setColspan( 5 );
        checkingTelecomCell.setIndent( 20 );
        checkingTelecomCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(checkingTelecomCell);

        PdfPCell vhfSetCell = new PdfPCell(new Phrase("VHF Set", boldSubFont));
        vhfSetCell.setBorder( PdfPCell.NO_BORDER );
        vhfSetCell.setColspan( 3 );
        vhfSetCell.setIndent( 40 );
        vhfSetCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(vhfSetCell);

        PdfPCell vhfSetEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("VHFsetValue", ""),normalSubFont));
        vhfSetEntryCell.setBorder( PdfPCell.NO_BORDER );
        vhfSetEntryCell.setColspan( 2 );
        vhfSetEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(vhfSetEntryCell);

        PdfPCell controlPhoneCell = new PdfPCell(new Phrase("Control Phone(Both OFC and Quad)", boldSubFont));
        controlPhoneCell.setBorder( PdfPCell.NO_BORDER );
        controlPhoneCell.setColspan( 3 );
        controlPhoneCell.setIndent( 40 );
        controlPhoneCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(controlPhoneCell);

        PdfPCell controlPhoneEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("ControlPhoneValue", ""),normalSubFont));
        controlPhoneEntryCell.setBorder( PdfPCell.NO_BORDER );
        controlPhoneEntryCell.setColspan( 2 );
        controlPhoneEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(controlPhoneEntryCell);

        PdfPCell railwayPhoneCell = new PdfPCell(new Phrase("Railway/ CUG/ BSNL phone", boldSubFont));
        railwayPhoneCell.setBorder( PdfPCell.NO_BORDER );
        railwayPhoneCell.setColspan( 3 );
        railwayPhoneCell.setIndent( 40 );
        railwayPhoneCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(railwayPhoneCell);

        PdfPCell railwayPhoneEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("RailwayPhoneValue", ""),normalSubFont));
        railwayPhoneEntryCell.setBorder( PdfPCell.NO_BORDER );
        railwayPhoneEntryCell.setColspan( 2 );
        railwayPhoneEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(railwayPhoneEntryCell);

        PdfPCell vhfRepeaterCell = new PdfPCell(new Phrase("VHF Repeater", boldSubFont));
        vhfRepeaterCell.setBorder( PdfPCell.NO_BORDER );
        vhfRepeaterCell.setColspan( 3 );
        vhfRepeaterCell.setIndent( 40 );
        vhfRepeaterCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(vhfRepeaterCell);

        PdfPCell vhfRepeaterEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("VHFrepeaterValue", ""),normalSubFont));
        vhfRepeaterEntryCell.setBorder( PdfPCell.NO_BORDER );
        vhfRepeaterEntryCell.setColspan( 2 );
        vhfRepeaterEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(vhfRepeaterEntryCell);

        PdfPCell paSystemCell = new PdfPCell(new Phrase("PA System", boldSubFont));
        paSystemCell.setBorder( PdfPCell.NO_BORDER );
        paSystemCell.setColspan( 3 );
        paSystemCell.setIndent( 40 );
        paSystemCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(paSystemCell);

        PdfPCell paSystemEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("PAsystemValue", ""),normalSubFont));
        paSystemEntryCell.setBorder( PdfPCell.NO_BORDER );
        paSystemEntryCell.setColspan( 2 );
        paSystemEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(paSystemEntryCell);

        PdfPCell telecomInstallDefCell = new PdfPCell(new Phrase("Deficiency Found", boldSubFont));
        telecomInstallDefCell.setBorder( PdfPCell.NO_BORDER );
        telecomInstallDefCell.setColspan( 3 );
        telecomInstallDefCell.setIndent( 40 );
        telecomInstallDefCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(telecomInstallDefCell);

        PdfPCell telecomInstallDefEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("TelecomInstallationEditText", "-"),normalSubFont));
        telecomInstallDefEntryCell.setBorder( PdfPCell.NO_BORDER );
        telecomInstallDefEntryCell.setColspan( 2 );
        telecomInstallDefEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(telecomInstallDefEntryCell);

        PdfPCell telecomInstallActionByCell = new PdfPCell(new Phrase("Action By", boldSubFont));
        telecomInstallActionByCell.setBorder( PdfPCell.NO_BORDER );
        telecomInstallActionByCell.setColspan( 3 );
        telecomInstallActionByCell.setIndent( 40 );
        telecomInstallActionByCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(telecomInstallActionByCell);

        PdfPCell telecomInstallActionByEntryCell;
        String actionBy = sharedpreferences.getString("vhfSetActionBySpinner", "-");
        if(actionBy.equals( "Other" ))
        telecomInstallActionByEntryCell = new PdfPCell(new Phrase(sharedpreferences.getString("vhfSetActionByEditText", "-"),normalSubFont));
        else
            telecomInstallActionByEntryCell = new PdfPCell(new Phrase(actionBy,normalSubFont));
        telecomInstallActionByEntryCell.setBorder( PdfPCell.NO_BORDER );
        telecomInstallActionByEntryCell.setColspan( 2 );
        telecomInstallActionByEntryCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(telecomInstallActionByEntryCell);

        createCell(stationMaster,"Crank Handle Testing by SM and Pointsman", boldSubFont,5,20);
        createCell(stationMaster,"Date of Testing", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("TestingDateEditText", "-"), normalSubFont,2,0);
        createCell(stationMaster,"Points Tested", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("TestedPointsEditText", "-"), normalSubFont,2,0);
        createCell(stationMaster,"CH Tested", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("TestedCHEditText", "-"), normalSubFont,2,0);

        createCell(stationMaster,"OFC Hut", boldSubFont,5,20);
        createCell(stationMaster,"Total Battery Voltage", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("BatteryVoltageEditText", "-")+"V", normalSubFont,2,0);
        createCell(stationMaster,"Status of Digital Equipments", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("DigitalEquipmentRadioGroupValue", "-"), normalSubFont,2,0);
        createCell(stationMaster,"Maintained Battery Readings Record", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("BatteryRecordsRadioGroupValue", "-"), normalSubFont,2,0);
        createCell(stationMaster,"Earth Termination", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("EarthTerminationRadioGroupValue", "-"), normalSubFont,2,0);
        createCell(stationMaster,"Deficiency Found", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("OFCHutEditText", "-"), normalSubFont,2,0);
        createCell(stationMaster,"Action By", boldSubFont,3,40);
        createCellForActionBy(stationMaster,sharedpreferences.getString("digitalEquipActionBySpr", "-"),sharedpreferences.getString("digitalEquipActionByEditText", "-"),normalSubFont,2,0 );

        createCell(stationMaster,"Emergency Sockets Tested (If any)", boldSubFont,3,20);
        createCell(stationMaster,sharedpreferences.getString("EmergencySocketRadioGroupValue", "-"), normalSubFont,2,0);
        createCell(stationMaster,"KM No. of Socket and Deficiency (if any)", boldSubFont,3,40);
        createCell(stationMaster,sharedpreferences.getString("TestedSocketsEditText", "-"), normalSubFont,2,0);
        createCell(stationMaster,"Action By", boldSubFont,3,40);
        createCellForActionBy(stationMaster,sharedpreferences.getString("testedSocketsActionBySpr", "-"),sharedpreferences.getString("testedSocketsActionByEditText", "-"),normalSubFont,2,0 );

        document.add( stationMaster );
    }

    private void addPointsCrossingData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine(emptyLine1, 1);
        document.add( emptyLine1 );

        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );

        createCellWithColor( docHeadingTable, "Points and Crossings", subFont,1,0, BaseColor.LIGHT_GRAY  );

        document.add( docHeadingTable );

        PdfPTable pointsCrossing = new PdfPTable( 5 );
        pointsCrossing.setWidthPercentage( 100 );

        createCell( pointsCrossing,"Point doesn't get operated when point zone TR is dropped",boldSubFont,4,20 );
        createCellForCheckbox(pointsCrossing, sharedpreferences.getBoolean( "Point doesn't get operated when point zone TR is dropped", false ),normalSubFont,1,0);
        createCellwithPadding( pointsCrossing,"Point does not stop when point zone TR is dropped during point operation",boldSubFont,4,0, 20 );
        createCellForCheckbox(pointsCrossing, sharedpreferences.getBoolean( "Point does not stop when point zone TR is dropped during point operation", false ),normalSubFont,1,0);
        createCellwithPadding( pointsCrossing,"Opening of point is around 115mm (shall not be less than 95mm in any case)",boldSubFont,4,0 ,20);
        createCellForCheckbox(pointsCrossing, sharedpreferences.getBoolean( "Opening of point is around 115mm (shall not be less than 95mm in any case)", false ),normalSubFont,1,0);
        createCellwithPadding( pointsCrossing,"Filler gauge (shall be in between 1mm to 3mm)",boldSubFont,4,0 ,20);
        createCellForCheckbox(pointsCrossing, sharedpreferences.getBoolean( "Filler gauge (shall be in between 1mm to 3mm)", false ),normalSubFont,1,0);
        createCellwithPadding( pointsCrossing,"Records of point maintenance were maintained and were placed in respective Point machines",boldSubFont,4,0 ,20);
        createCellForCheckbox(pointsCrossing, sharedpreferences.getBoolean( "Records of point maintenance were maintained and were placed in respective Point machines", false ),normalSubFont,1,0);
        createCell(pointsCrossing,"Deficiency Found", boldSubFont,3,40);
        createCell(pointsCrossing,sharedpreferences.getString("pointsDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(pointsCrossing,"Action By", boldSubFont,3,40);
        createCellForActionBy(pointsCrossing,sharedpreferences.getString("pointsCrossingEnsureActionBySpinner", "-"),sharedpreferences.getString("pointsCrossingEnsureActionByEditText", "-"),normalSubFont,2,0 );

        createCellwithPadding(pointsCrossing,"Details of points with obstruction voltage less than 80 V", boldSubFont,5,0, 20);
        createCell(pointsCrossing,sharedpreferences.getString("pointsDetailEditText", "-"), normalSubFont,5,40);
        createCellwithPadding(pointsCrossing,"Obstruction current should be 1.5 to 2 times of the normal operating current or difference of both currents shall be less than 0.5 A. If not, Name such points", boldSubFont,5,0,20);
        createCell(pointsCrossing,sharedpreferences.getString("obstructionCurrentEditText", "-"), normalSubFont,5,40);
        createCellwithPadding(pointsCrossing,"Points which either gets locked or detection contacts make during 5mm obstruction test", boldSubFont,5,0, 20);
        createCell(pointsCrossing,sharedpreferences.getString("lockedPointsEditText", "-"), normalSubFont,5,40);
        createCell(pointsCrossing,"Action By", boldSubFont,3,40);
        createCellForActionBy(pointsCrossing,sharedpreferences.getString("pointsCrossingDetailsActionBySpinner", "-"),sharedpreferences.getString("pointsCrossingDetailsActionByEditText", "-"),normalSubFont,2,0 );

        createCell(pointsCrossing,"Last Joint Point and Crossings inspection", boldSubFont,5,20);
        createCell(pointsCrossing,"Date", boldSubFont,3,40);
        createCell(pointsCrossing,sharedpreferences.getString("selectDateEditText", "-"), normalSubFont,2,0);
        createCell(pointsCrossing,"Deficiency Found", boldSubFont,3,40);
        createCell(pointsCrossing,sharedpreferences.getString("lastJointDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(pointsCrossing,"Action By", boldSubFont,3,40);
        createCellForActionBy(pointsCrossing,sharedpreferences.getString("pointsCrossingLastJointActionBySpinner", "-"),sharedpreferences.getString("pointsCrossingLastJointActionByEditText", "-"),normalSubFont,2,0 );

        document.add( pointsCrossing );
    }

    private void addTrackCircuitsData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 1 );
        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "Track Circuits", subFont, 1, 0,BaseColor.LIGHT_GRAY );
        document.add( docHeadingTable );

        PdfPTable trackCircuits = new PdfPTable( 5 );
        trackCircuits.setWidthPercentage( 100 );

        createCellwithPadding( trackCircuits,"Double Bonding has been done on all the Continuous Rail Joints and SEJs",boldSubFont,4,0,20 );
        createCellForCheckbox(trackCircuits, sharedpreferences.getBoolean( "Double Bonding has been done on all the Continuous Rail Joints and SEJs", false ),normalSubFont,1,0);
        createCellwithPadding( trackCircuits,"'J' type pandrol clip has been used at Glued Joints to avoid Shorting of TC.",boldSubFont,4,0,20 );
        createCellForCheckbox(trackCircuits, sharedpreferences.getBoolean( "'J' type pandrol clip has been used at Glued Joints to avoid Shorting of TC.", false ),normalSubFont,1,0);
        createCellwithPadding( trackCircuits,"When Both rails are shorted using TSR, TC indication on panel is red including track circuited sidings.",boldSubFont,4,0,20 );
        createCellForCheckbox(trackCircuits, sharedpreferences.getBoolean( "When Both rails are shorted using TSR, TC indication on panel is red including track circuited sidings.", false ),normalSubFont,1,0);
        createCellwithPadding( trackCircuits,"Specific Gravity of TC battery is in between 1180–1220",boldSubFont,4,0,20 );
        createCellForCheckbox(trackCircuits, sharedpreferences.getBoolean( "Specific Gravity of TC battery is in between 1180–1220", false ),normalSubFont,1,0);
        createCellwithPadding( trackCircuits,"Relay End Voltage is less than 3 times of Pick Up voltage of TR (QT2/QTA2 Type)",boldSubFont,4,0,20 );
        createCellForCheckbox(trackCircuits, sharedpreferences.getBoolean( "Relay End Voltage is less than 3 times of Pick Up voltage of TR (QT2/QTA2 Type)", false ),normalSubFont,1,0);
        createCellwithPadding( trackCircuits,"Records of TC parameters were maintained and placed in respective location boxes",boldSubFont,4,0,20 );
        createCellForCheckbox(trackCircuits, sharedpreferences.getBoolean( "Records of TC parameters were maintained and placed in respective location boxes", false ),normalSubFont,1,0);
        createCell(trackCircuits,"Deficiency Found", boldSubFont,3,20);
        createCell(trackCircuits,sharedpreferences.getString("tracksDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(trackCircuits,"Action By", boldSubFont,3,20);
        createCellForActionBy(trackCircuits,sharedpreferences.getString("trackCircuitsActionBySpinner", "-"),sharedpreferences.getString("trackCircuitsActionByEditText", "-"),normalSubFont,2,0 );

        document.add( trackCircuits );
    }

    private void addSignalsData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 1 );
        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "Signals", subFont, 1, 0,BaseColor.LIGHT_GRAY  );
        document.add( docHeadingTable );

        PdfPTable signals = new PdfPTable( 5 );
        signals.setWidthPercentage( 100 );

        createCellwithPadding( signals,"Signal lamp voltage < 90% of rated value",boldSubFont,4,0,20 );
        createCellForCheckbox(signals, sharedpreferences.getBoolean( "Signal lamp voltage < 90% of rated value", false ),normalSubFont,1,0);
        createCellwithPadding( signals,"UECR drops with fusing of minimum 3 route LEDS",boldSubFont,4,0,20 );
        createCellForCheckbox(signals, sharedpreferences.getBoolean( "UECR drops with fusing of minimum 3 route LEDS", false ),normalSubFont,1,0);
        createCellwithPadding( signals,"Signals are cascaded (e.g. - fusing of a signal's particular aspect (other than Red) illuminates a more restrictive aspect)",boldSubFont,4,0,20 );
        createCellForCheckbox(signals, sharedpreferences.getBoolean( "Signals are cascaded (e.g. - fusing of a signal's particular aspect (other than Red) illuminates a more restrictive aspect)", false ),normalSubFont,1,0);
        createCellwithPadding( signals,"Red Lamp Protection working (e.g. - fusing of Red aspect of signal should illuminate signal in rear with most restrictive aspect (Red).)",boldSubFont,4,0,20 );
        createCellForCheckbox(signals, sharedpreferences.getBoolean( "Red Lamp Protection working (e.g. - fusing of Red aspect of signal should illuminate signal in rear with most restrictive aspect (Red).)", false ),normalSubFont,1,0);
        createCell(signals,"Voltage/Current Parameters of Signals", boldSubFont,5,20);
        document.add(signals);

        PdfPTable signalsTable = new PdfPTable( 4 );
        signalsTable.setWidthPercentage( 90 );

        addTableCellwithPadding( signalsTable,"Signal No.", boldSubFont,1,0,30);
        addTableCellwithPadding( signalsTable,"Aspect", boldSubFont,1,0,30);
        addTableCellwithPadding( signalsTable,"Voltage", boldSubFont,1,0,30);
        addTableCellwithPadding( signalsTable,"Current", boldSubFont,1,0,30);
        for(int i= 1; i<11; i++){
            if(!sharedpreferences.getString( "signalNo"+i+"EditText", "" ).isEmpty()){
                addTableCellwithPadding( signalsTable,sharedpreferences.getString( "signalNo"+i+"EditText", "" ), normalSubFont,1,0,30);
                addTableCellwithPadding( signalsTable,sharedpreferences.getString( "aspect"+i+"EditText", "" ), normalSubFont,1,0,30);
                addTableCellwithPadding( signalsTable,sharedpreferences.getString( "voltage"+i+"EditText", "" ), normalSubFont,1,0,30);
                addTableCellwithPadding( signalsTable,sharedpreferences.getString( "current"+i+"EditText", "" ), normalSubFont,1,0,30);
            }
        }
        document.add( signalsTable );

        PdfPTable signalsActionBy = new PdfPTable( 5 );
        signalsActionBy.setWidthPercentage( 100 );
        createCell(signalsActionBy,"Action By", boldSubFont,4,20);
        createCellForActionBy(signalsActionBy,sharedpreferences.getString("signalsActionBySpinner", "-"),sharedpreferences.getString("signalsActionByEditText", "-"),normalSubFont,1,0 );

        document.add( signalsActionBy );
    }

    private void addCcipVduData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 1 );
        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "CCIP/VDU (SM's Panel)", subFont, 1, 0,BaseColor.LIGHT_GRAY  );
        document.add( docHeadingTable );

        PdfPTable ccip = new PdfPTable( 5 );
        ccip.setWidthPercentage( 100 );

        createCell(ccip,"Type of Interlocking", boldSubFont,4,20);
        createCell(ccip,sharedpreferences.getString("typeOfInterlockingSpinner", "-"), normalSubFont,1,0);
        createCellwithPadding( ccip,"SIP/SWRD is as per the physical yard layout",boldSubFont,4,0,20 );
        createCellForCheckbox(ccip, sharedpreferences.getBoolean( "SIP/SWRD is as per the physical yard layout", false ),normalSubFont,1,0);
        createCellwithPadding( ccip,"Counters on Panel/VDU are same as recorded in Counter Register",boldSubFont,4,0,20 );
        createCellForCheckbox(ccip, sharedpreferences.getBoolean( "Counters on Panel/VDU are same as recorded in Counter Register", false ),normalSubFont,1,0);
        createCellwithPadding( ccip,"Sample checking of Calling ON Signal (COGGB Counter increment)",boldSubFont,4,0,20 );
        createCellForCheckbox(ccip, sharedpreferences.getBoolean( "Sample checking of Calling ON Signal (COGGB Counter increment)", false ),normalSubFont,1,0);
        createCellwithPadding( ccip,"Sample checking of Calling ON Signal Cancellation (COCYN/ERRB Counter Increment)",boldSubFont,4,0,20 );
        createCellForCheckbox(ccip, sharedpreferences.getBoolean( "Sample checking of Calling ON Signal Cancellation (COCYN/ERRB Counter Increment)", false ),normalSubFont,1,0);
        createCellwithPadding( ccip,"Testing of Emergency Crossover (in double line sections)",boldSubFont,4,0,20 );
        createCellForCheckbox(ccip, sharedpreferences.getBoolean( "Testing of Emergency Classover (in double line sections)", false ),normalSubFont,1,0);
        createCellwithPadding( ccip,"Sample check of Approach Locking/Dead Approach Locking",boldSubFont,4,0,20 );
        createCellForCheckbox(ccip, sharedpreferences.getBoolean( "Sample check of Approach Locking/Dead Approach Locking", false ),normalSubFont,1,0);
        createCell(ccip,"Deficiency Found", boldSubFont,3,20);
        createCell(ccip,sharedpreferences.getString("ccipDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"Counter Details", boldSubFont,5,20);
        createCell(ccip,"ERRB", boldSubFont,3,40);
        createCell(ccip,sharedpreferences.getString("ERRBEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"RRBU", boldSubFont,3,40);
        createCell(ccip,sharedpreferences.getString("RRBUEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"COGGN", boldSubFont,3,40);
        createCell(ccip,sharedpreferences.getString("COGGNEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"COCYN", boldSubFont,3,40);
        createCell(ccip,sharedpreferences.getString("COCYNEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"EBPU", boldSubFont,3,40);
        createCell(ccip,sharedpreferences.getString("EBPUEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"ECH", boldSubFont,3,40);
        createCell(ccip,sharedpreferences.getString("ECHEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"Last Annual Panel testing Date", boldSubFont,3,20);
        createCell(ccip,sharedpreferences.getString("lastAnnualDateEditText", "-"), normalSubFont,2,0);
        createCell(ccip,"Action By", boldSubFont,3,20);
        createCellForActionBy(ccip,sharedpreferences.getString("ccipActionBySpr", "-"),sharedpreferences.getString("ccipActionByEditTxt", "-"),normalSubFont,2,0 );

        document.add( ccip );
    }

    private void addBiAcData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 1 );
        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "Block Instruments and Axle Counters", subFont, 1, 0 ,BaseColor.LIGHT_GRAY );
        document.add( docHeadingTable );

        PdfPTable blockInstrument = new PdfPTable( 5 );
        blockInstrument.setWidthPercentage( 100 );
        createCell( blockInstrument, "Block Instruments", boldSubFont, 5, 20 );
        document.add( blockInstrument );

        for(int bi =1 ;bi <=2;bi++){
            PdfPTable blockInstruments = new PdfPTable( 5 );
            blockInstruments.setWidthPercentage( 100 );
            createCell( blockInstruments, "Block Instrument "+bi, boldSubFont, 3, 40 );
            createCell( blockInstruments, sharedpreferences.getString("bi"+bi+"EditTxt", "-"), normalSubFont, 2, 0 );
            document.add( blockInstruments );

            PdfPTable blockInstrumentsTable = new PdfPTable( 6 );
            blockInstrumentsTable.setWidthPercentage( 85 );
            addTableCellwithPadding(blockInstrumentsTable,"Battery Voltage", boldSubFont,2,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Outgoing", boldSubFont,2,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Incoming", boldSubFont,2,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Local", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Line", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Voltage", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Current", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Voltage", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Current", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("localBI"+bi+"EditTxt", "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("lineBI"+bi+"EditTxt", "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("voltageOutgoingBI"+bi+"EditTxt", "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("currentOutgoingBI"+bi+"EditTxt", "-")+"A", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("voltageIncomingBI"+bi+"EditTxt", "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("currentIncomingBI"+bi+"EditTxt", "-")+"A", normalSubFont,1,0,25);
            document.add( blockInstrumentsTable );

            PdfPTable blockInstrumentsRemaining = new PdfPTable( 5 );
            blockInstrumentsRemaining.setWidthPercentage( 100 );
            createCell( blockInstrumentsRemaining, "Deficiency Found", boldSubFont, 3, 40 );
            createCell( blockInstrumentsRemaining, sharedpreferences.getString("bi"+bi+"deficiencyEditTxt", "-"), normalSubFont, 2, 0 );
            createCellwithPadding( blockInstrumentsRemaining,"BI line clear cancellation counter",boldSubFont,3,0,40 );
            createCell( blockInstrumentsRemaining, sharedpreferences.getString("lineClearBI"+bi+"EditTxt", "-"), normalSubFont, 2, 0 );
            createCellwithPadding( blockInstrumentsRemaining,"Records of BI parameters were maintained",boldSubFont,3,0,40 );
            createCell( blockInstrumentsRemaining, sharedpreferences.getString("recordsBI"+bi+"Spr", "-"), normalSubFont, 2, 0 );
            createCell(blockInstrumentsRemaining,"Action By", boldSubFont,3,40);
            createCellForActionBy(blockInstrumentsRemaining,sharedpreferences.getString("bi"+bi+"ActionBySpr", "-"),sharedpreferences.getString("bi"+bi+"ActionByEditTxt", "-"),normalSubFont,2,0 );
            document.add( blockInstrumentsRemaining );
            Paragraph emptyLine2 = new Paragraph();
            addEmptyLine( emptyLine2, 1 );
            document.add( emptyLine2 );
        }

        for(int biNumber = 0; biNumber <= sharedpreferences.getInt("biCount", 0)-3 ; biNumber++ ) {
            PdfPTable blockInstruments = new PdfPTable( 5 );
            blockInstruments.setWidthPercentage( 100 );
            int bi = biNumber+3;
            createCell( blockInstruments, "Block Instrument "+bi, boldSubFont, 3, 40 );
            createCell( blockInstruments, sharedpreferences.getString("biEditTxt"+biNumber, "-"), normalSubFont, 2, 0 );
            document.add( blockInstruments );

            PdfPTable blockInstrumentsTable = new PdfPTable( 6 );
            blockInstrumentsTable.setWidthPercentage( 85 );
            addTableCellwithPadding(blockInstrumentsTable,"Battery Voltage", boldSubFont,2,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Outgoing", boldSubFont,2,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Incoming", boldSubFont,2,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Local", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Line", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Voltage", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Current", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Voltage", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,"Current", boldSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("localBIEditTxt"+biNumber, "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("lineBIEditTxt"+biNumber, "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("voltageOutgoingBIEditTxt"+biNumber, "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("currentOutgoingBIEditTxt"+biNumber, "-")+"A", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("voltageIncomingBIEditTxt"+biNumber, "-")+"V", normalSubFont,1,0,25);
            addTableCellwithPadding(blockInstrumentsTable,sharedpreferences.getString("currentIncomingBIEditTxt"+biNumber, "-")+"A", normalSubFont,1,0,25);
            document.add( blockInstrumentsTable );

            PdfPTable blockInstrumentsRemaining = new PdfPTable( 5 );
            blockInstrumentsRemaining.setWidthPercentage( 100 );
            createCell( blockInstrumentsRemaining, "Deficiency Found", boldSubFont, 3, 40 );
            createCell( blockInstrumentsRemaining, sharedpreferences.getString("bideficiencyEditTxt"+biNumber, "-"), normalSubFont, 2, 0 );
            createCellwithPadding( blockInstrumentsRemaining,"BI line clear cancellation counter",boldSubFont,3,0,40 );
            createCell( blockInstrumentsRemaining, sharedpreferences.getString("lineClearBIEditTxt"+biNumber, "-"), normalSubFont, 2, 0 );
            createCellwithPadding( blockInstrumentsRemaining,"Records of BI parameters were maintained",boldSubFont,3,0,40 );
            createCell( blockInstrumentsRemaining, sharedpreferences.getString("recordsBISpr"+biNumber, "-"), normalSubFont, 2, 0 );
            createCell(blockInstrumentsRemaining,"Action By", boldSubFont,3,40);
            createCellForActionBy(blockInstrumentsRemaining,sharedpreferences.getString("biActionBySpr"+biNumber, "-"),sharedpreferences.getString("biActionByEditTxt"+biNumber, "-"),normalSubFont,2,0 );
            document.add( blockInstrumentsRemaining );
            Paragraph emptyLine2 = new Paragraph();
            addEmptyLine( emptyLine2, 1 );
            document.add( emptyLine2 );
        }

        PdfPTable axleCount = new PdfPTable( 5 );
        axleCount.setWidthPercentage( 100 );
        createCell( axleCount, "Axle Counters", boldSubFont, 5, 20 );
        document.add( axleCount );

        for(int ac = 1; ac <=2;ac++){
            PdfPTable axleCounter = new PdfPTable( 5 );
            axleCounter.setWidthPercentage( 100 );
            createCell( axleCounter, "Axle Counter "+ac, boldSubFont, 3, 40 );
            createCell( axleCounter, sharedpreferences.getString("axleCounter"+ac+"EditTxt", "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Working on",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("workingAC"+ac+"Spr", "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Electrical parameter records were maintained",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("electricalAC"+ac+"Spr", "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Axle Counter Reset Counter",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("resetAC"+ac+"EditTxt", "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Deficiency Found",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("ac"+ac+"deficiencyEditText", "-"), normalSubFont, 2, 0 );
            createCell(axleCounter,"Action By", boldSubFont,3,40);
            createCellForActionBy(axleCounter,sharedpreferences.getString("ac"+ac+"ActionBySpr", "-"),sharedpreferences.getString("ac"+ac+"ActionByEditTxt", "-"),normalSubFont,2,0 );
            document.add( axleCounter );
            Paragraph emptyLine2 = new Paragraph();
            addEmptyLine( emptyLine2, 1 );
            document.add( emptyLine2 );
        }

        for(int acNumber = 0; acNumber <= sharedpreferences.getInt("acCount",0)-13 ; acNumber++ ) {
            PdfPTable axleCounter = new PdfPTable( 5 );
            axleCounter.setWidthPercentage( 100 );
            int ac = acNumber+3;
            createCell( axleCounter, "Axle Counter "+ac, boldSubFont, 3, 40 );
            createCell( axleCounter, sharedpreferences.getString("axleCounterEditTxt"+acNumber, "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Working on",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("workingACSpr"+acNumber, "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Electrical parameter records were maintained",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("electricalACSpr"+acNumber, "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Axle Counter Reset Counter",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("resetACEditTxt"+acNumber, "-"), normalSubFont, 2, 0 );
            createCellwithPadding( axleCounter,"Deficiency Found",boldSubFont,3,0,40 );
            createCell( axleCounter, sharedpreferences.getString("acdeficiencyEditText"+acNumber, "-"), normalSubFont, 2, 0 );
            createCell(axleCounter,"Action By", boldSubFont,3,40);
            createCellForActionBy(axleCounter,sharedpreferences.getString("acActionBySpr"+acNumber, "-"),sharedpreferences.getString("acActionByEditTxt"+acNumber, "-"),normalSubFont,2,0 );
            document.add( axleCounter );
            Paragraph emptyLine2 = new Paragraph();
            addEmptyLine( emptyLine2, 1 );
            document.add( emptyLine2 );
        }
    }

    private void addPowerSupplyData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
//        Paragraph emptyLine1 = new Paragraph();
//        addEmptyLine( emptyLine1, 1 );
//        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "Power Supply and Relay Room", subFont, 1, 0 ,BaseColor.LIGHT_GRAY );
        document.add( docHeadingTable );

        PdfPTable powerSupply = new PdfPTable( 5 );
        powerSupply.setWidthPercentage( 100 );
        createCell( powerSupply, "Power Supply", boldSubFont, 5, 20 );
        createCell( powerSupply, "IPS Make", boldSubFont, 3, 40 );
        createCellForActionBy( powerSupply, sharedpreferences.getString("ipsMakespnr", "-"),sharedpreferences.getString("ipsMakeEditTxt", "-"), normalSubFont, 2, 0 );
        createCellwithPadding( powerSupply,"Whether AMC executed",boldSubFont,4,0,40 );
        createCellForCheckbox(powerSupply, sharedpreferences.getBoolean( "Whether AMC executed", false ),normalSubFont,1,0);
        createCellwithPadding( powerSupply,"SMR load sharing is working fine",boldSubFont,4,0,40 );
        createCellForCheckbox(powerSupply, sharedpreferences.getBoolean( "SMR load sharing is working fine", false ),normalSubFont,1,0);
        createCellwithPadding( powerSupply,"Earthing of IPS equipment was proper",boldSubFont,4,0,40 );
        createCellForCheckbox(powerSupply, sharedpreferences.getBoolean( "Earthing of IPS equipment was proper", false ),normalSubFont,1,0);
        createCell( powerSupply, "Total IPS Battery Voltage", boldSubFont, 5, 40 );
        createCell( powerSupply, "IPS ON", boldSubFont, 3, 60 );
        createCell( powerSupply, sharedpreferences.getString("ipsOnEditText", "-")+"V", normalSubFont, 2, 0 );
        createCell( powerSupply, "IPS OFF", boldSubFont, 3, 60 );
        createCell( powerSupply, sharedpreferences.getString("ipsOFFEditText", "-")+"V", normalSubFont, 2, 0 );
        createCell( powerSupply, "After", boldSubFont, 3, 60 );
        createCell( powerSupply, sharedpreferences.getString("ipsAfterEditText", "-")+"Hrs", normalSubFont, 2, 0 );
        createCell( powerSupply, "Specific Gravity of Battery Cells", boldSubFont, 5, 40 );
        createCell( powerSupply, "Minimum", boldSubFont, 3, 60 );
        createCell( powerSupply, sharedpreferences.getString("specificGravityEditText", "-"), normalSubFont, 2, 0 );
        createCell( powerSupply, "Maximum", boldSubFont, 3, 60 );
        createCell( powerSupply, sharedpreferences.getString("specificGravityMaxEditText", "-"), normalSubFont, 2, 0 );
        createCellwithPadding( powerSupply,"Records of Battery Readings were maintained",boldSubFont,4,0,40 );
        createCellForCheckbox(powerSupply, sharedpreferences.getBoolean( "Records of Battery Readings were maintained", false ),normalSubFont,1,0);
        createCellwithPadding( powerSupply,"White washing required in Battery Room",boldSubFont,4,0,40 );
        createCellForCheckbox(powerSupply, sharedpreferences.getBoolean( "White washing required in Battery Room", false ),normalSubFont,1,0);
        createCell( powerSupply, "Relay Room", boldSubFont, 5, 20 );
        createCellwithPadding( powerSupply,"Relay Room Opening was not more than once in a week",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("relayRoomOpeningSpinner", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"Spare Relays/Tools",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("spareRelaySpinner", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"White washing required in Relay Room",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("whiteWashingRelaySpinner", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"Electrical general fitting was found",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("electricalGeneralSpinner", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"Earthing arrangements of Relay Rack/CTR Rack/ET Module were proper",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("earthingArrangementsSpinner", "-"), normalSubFont, 1, 0 );

        createCell( powerSupply, "Data Logger", boldSubFont, 5, 20 );
        createCellwithPadding( powerSupply,"Whether AMC executed",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("whetherAMCSpinner", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"Maintenance Records were maintained",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("maintenanceRecordsSpinner", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"Last Validation done on",boldSubFont,2,0,40 );
        createCell( powerSupply, sharedpreferences.getString("dataLoggerDate", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"By",boldSubFont,1,0,40 );
        createCell( powerSupply, sharedpreferences.getString("lastValidationByEditText", "-"), normalSubFont, 1, 0 );

        createCell( powerSupply, "EI/RRI", boldSubFont, 5, 20 );
        createCell( powerSupply, "EI Make", boldSubFont, 3, 40 );
        createCellForActionBy( powerSupply, sharedpreferences.getString("eiMakeSpnr", "-"),sharedpreferences.getString("eiMakeEditTxt", "-"), normalSubFont, 2, 0 );
        createCell( powerSupply, "Last system switchover from", boldSubFont, 3, 40 );
        createCell( powerSupply, sharedpreferences.getString("lastSystemSwitchAEditText", "-"), normalSubFont, 2, 0 );
        createCell( powerSupply, " to", boldSubFont, 1, 40 );
        createCell( powerSupply, sharedpreferences.getString("lastSystemSwitchBEditText", "-"), normalSubFont, 2, 0 );
        createCell( powerSupply, "on", boldSubFont, 1, 40 );
        createCell( powerSupply, sharedpreferences.getString("eiDate", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"EI Rack was properly earthen",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("eiRackSpinner", "-"), normalSubFont, 1, 0 );
        createCellwithPadding( powerSupply,"Voltage Parameter Records of major relays were maintained",boldSubFont,4,0,40 );
        createCell( powerSupply, sharedpreferences.getString("voltageParameterSpinner", "-"), normalSubFont, 1, 0 );
        createCell( powerSupply, "Action By", boldSubFont, 3, 20 );
        createCellForActionBy( powerSupply, sharedpreferences.getString("powerSupplyActionBySpr", "-"),sharedpreferences.getString("powerSupplyActionByEditTxt", "-"), normalSubFont, 2, 0 );

        document.add( powerSupply );
    }

    private void addLevelCrossingData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 1 );
        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "Level Crossing", subFont, 1, 0 ,BaseColor.LIGHT_GRAY );
        document.add( docHeadingTable );

        PdfPTable levelCrossing = new PdfPTable( 5 );
        levelCrossing.setWidthPercentage( 100 );
        createCell( levelCrossing, "Gate No.", boldSubFont, 3, 20 );
        createCell( levelCrossing, sharedpreferences.getString("gateNo", "-"), normalSubFont, 2, 0 );
        createCell( levelCrossing, "Gateman", boldSubFont, 3, 20 );
        createCell( levelCrossing, sharedpreferences.getString("nameOfGateman", "-"), normalSubFont, 2, 0 );
        createCell( levelCrossing, "Gate Type", boldSubFont, 3, 20 );
        createCell( levelCrossing, sharedpreferences.getString("gateTypeSpnr", "-"), normalSubFont, 2, 0 );
        createCellwithPadding( levelCrossing,"Positive Boom Locking tested",boldSubFont,4,0,20 );
        createCellForCheckbox(levelCrossing, sharedpreferences.getBoolean( "Positive Boom Locking tested", false ),normalSubFont,1,0);
        createCellwithPadding( levelCrossing,"Booms were painted",boldSubFont,4,0,20 );
        createCellForCheckbox(levelCrossing, sharedpreferences.getBoolean( "Booms were painted", false ),normalSubFont,1,0);
        createCellwithPadding( levelCrossing,"Gateman having adequate safety knowledge",boldSubFont,4,0,20 );
        createCellForCheckbox(levelCrossing, sharedpreferences.getBoolean( "Gateman having adequate safety knowledge", false ),normalSubFont,1,0);
        createCellwithPadding( levelCrossing,"Gate telephone(s) found in working order",boldSubFont,4,0,20 );
        createCellForCheckbox(levelCrossing, sharedpreferences.getBoolean( "Gate telephone(s) found in working order", false ),normalSubFont,1,0);
        createCellwithPadding( levelCrossing,"SSE/JE inspections are as per their maintenance schedule",boldSubFont,4,0,20 );
        createCellForCheckbox(levelCrossing, sharedpreferences.getBoolean( "SSE/JE inspections are as per their maintenance schedule", false ),normalSubFont,1,0);
        createCellwithPadding( levelCrossing,"Inspection/Maintenance Records were maintained",boldSubFont,4,0,20 );
        createCellForCheckbox(levelCrossing, sharedpreferences.getBoolean( "Inspection/Maintenance Records were maintained", false ),normalSubFont,1,0);
        createCellwithPadding( levelCrossing,"Other Electrical and Mechanical parameter of gates were checked",boldSubFont,4,0,20 );
        createCellForCheckbox(levelCrossing, sharedpreferences.getBoolean( "Other Electrical and Mechanical parameter of gates were checked", false ),normalSubFont,1,0);
        createCell(levelCrossing,"Deficiency Found", boldSubFont,3,20);
        createCell(levelCrossing,sharedpreferences.getString("anyDeficiencyFound", "-"), normalSubFont,2,0);
        createCell(levelCrossing,"Action By", boldSubFont,3,20);
        createCellForActionBy(levelCrossing,sharedpreferences.getString("levelCrossingActionBySpr", "-"),sharedpreferences.getString("levelCrossingActionByEditTxt", "-"),normalSubFont,2,0 );
        document.add( levelCrossing );

        for(int gateNumber = 1; gateNumber <= sharedpreferences.getInt("levelCrossingGateCount", 0) ; gateNumber++ ){
            Paragraph emptyLine2 = new Paragraph();
            addEmptyLine( emptyLine2, 1 );
            document.add( emptyLine2 );

            PdfPTable level = new PdfPTable( 5 );
            level.setWidthPercentage( 100 );
            createCell( level, "Gate No.", boldSubFont, 3, 20 );
            createCell( level, sharedpreferences.getString("gateNo"+gateNumber, "-"), normalSubFont, 2, 0 );
            createCell( level, "Gateman", boldSubFont, 3, 20 );
            createCell( level, sharedpreferences.getString("nameOfGateman"+gateNumber, "-"), normalSubFont, 2, 0 );
            createCell( level, "Gate Type", boldSubFont, 3, 20 );
            createCell( level, sharedpreferences.getString("gateTypeSpnr"+gateNumber, "-"), normalSubFont, 2, 0 );
            createCellwithPadding( level,"Positive Boom Locking tested",boldSubFont,4,0,20 );
            createCellForCheckbox(level, sharedpreferences.getBoolean( "Positive Boom Locking tested"+gateNumber, false ),normalSubFont,1,0);
            createCellwithPadding( level,"Booms were painted",boldSubFont,4,0,20 );
            createCellForCheckbox(level, sharedpreferences.getBoolean( "Booms were painted"+gateNumber, false ),normalSubFont,1,0);
            createCellwithPadding( level,"Gateman having adequate safety knowledge",boldSubFont,4,0,20 );
            createCellForCheckbox(level, sharedpreferences.getBoolean( "Gateman having adequate safety knowledge"+gateNumber, false ),normalSubFont,1,0);
            createCellwithPadding( level,"Gate telephone(s) found in working order",boldSubFont,4,0,20 );
            createCellForCheckbox(level, sharedpreferences.getBoolean( "Gate telephone(s) found in working order"+gateNumber, false ),normalSubFont,1,0);
            createCellwithPadding( level,"SSE/JE inspections are as per their maintenance schedule",boldSubFont,4,0,20 );
            createCellForCheckbox(level, sharedpreferences.getBoolean( "SSE/JE inspections are as per their maintenance schedule"+gateNumber, false ),normalSubFont,1,0);
            createCellwithPadding( level,"Inspection/Maintenance Records were maintained",boldSubFont,4,0,20 );
            createCellForCheckbox(level, sharedpreferences.getBoolean( "Inspection/Maintenance Records were maintained"+gateNumber, false ),normalSubFont,1,0);
            createCellwithPadding( level,"Other Electrical and Mechanical parameter of gates were checked",boldSubFont,4,0,20 );
            createCellForCheckbox(level, sharedpreferences.getBoolean( "Other Electrical and Mechanical parameter of gates were checked"+gateNumber, false ),normalSubFont,1,0);
            createCell(level,"Deficiency Found", boldSubFont,3,20);
            createCell(level,sharedpreferences.getString("anyDeficiencyFound"+gateNumber, "-"), normalSubFont,2,0);
            createCell(level,"Action By", boldSubFont,3,20);
            createCellForActionBy(level,sharedpreferences.getString("levelCrossingActionBySpr"+gateNumber, "-"),sharedpreferences.getString("levelCrossingActionByEditTxt"+gateNumber, "-"),normalSubFont,2,0 );
            document.add( level );
        }
    }

    private void addRecordsData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 1 );
        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "Records", subFont, 1, 0,BaseColor.LIGHT_GRAY  );
        document.add( docHeadingTable );

        PdfPTable SignalFailure = new PdfPTable( 5 );
        SignalFailure.setWidthPercentage( 100 );
        createCell(SignalFailure,"Signal Failure Register (last 3 months failures)", boldSubFont,5,20);
        document.add( SignalFailure );

        PdfPTable SignalFailureTable = new PdfPTable( 6 );
        SignalFailureTable.setWidthPercentage( 92 );
        addTableCellwithPadding(SignalFailureTable,"Month", boldSubFont,1,0,25);
        addTableCellwithPadding(SignalFailureTable,"Point", boldSubFont,1,0,30);
        addTableCellwithPadding(SignalFailureTable,"Track Circuit", boldSubFont,1,0,10);
        addTableCellwithPadding(SignalFailureTable,"BI/BPAC", boldSubFont,1,0,20);
        addTableCellwithPadding(SignalFailureTable,"Power Supply", boldSubFont,1,0,5);
        addTableCellwithPadding(SignalFailureTable,"Others", boldSubFont,1,0,25);
        for(int i =1; i<4; i++){
            addTableCellwithPadding(SignalFailureTable,sharedpreferences.getString("month"+i, "-"), boldSubFont,1,0,30);
            addTableCellwithPadding(SignalFailureTable,sharedpreferences.getString("signalPoint"+i+"EditText", "-"), normalSubFont,1,0,30);
            addTableCellwithPadding(SignalFailureTable,sharedpreferences.getString("signalTrackCircuit"+i+"EditText", "-"), normalSubFont,1,0,30);
            addTableCellwithPadding(SignalFailureTable,sharedpreferences.getString("signalBI"+i+"EditText", "-"), normalSubFont,1,0,30);
            addTableCellwithPadding(SignalFailureTable,sharedpreferences.getString("signalPowerSupply"+i+"EditText", "-"), normalSubFont,1,0,30);
            addTableCellwithPadding(SignalFailureTable,sharedpreferences.getString("signalOthers"+i+"EditText", "-"), normalSubFont,1,0,30);
        }
        document.add( SignalFailureTable );

        PdfPTable disconnection = new PdfPTable( 5 );
        disconnection.setWidthPercentage( 100 );
        createCell(disconnection,"Remarks", boldSubFont,3,20);
        createCell(disconnection,sharedpreferences.getString("signalFailureRemarkEditText", "-"), normalSubFont,2,0);
        createCell(disconnection,"Disconnection and Reconnection Register (last 3 months)", boldSubFont,5,20);
        document.add( disconnection );

        PdfPTable disconnectionTable = new PdfPTable( 4 );
        disconnectionTable.setWidthPercentage( 92 );
        addTableCellwithPadding(disconnectionTable,"Month", boldSubFont,1,0,30);
        addTableCellwithPadding(disconnectionTable,sharedpreferences.getString("month1", "-"), boldSubFont,1,0,30);
        addTableCellwithPadding(disconnectionTable,sharedpreferences.getString("month2", "-"), boldSubFont,1,0,30);
        addTableCellwithPadding(disconnectionTable,sharedpreferences.getString("month3", "-"), boldSubFont,1,0,30);
        addTableCellwithPadding(disconnectionTable,"D/R", boldSubFont,1,0,30);
        addTableCellwithPadding(disconnectionTable,sharedpreferences.getString("disconnection1DREditText", "-"), normalSubFont,1,0,30);
        addTableCellwithPadding(disconnectionTable,sharedpreferences.getString("disconnection2DREditText", "-"), normalSubFont,1,0,30);
        addTableCellwithPadding(disconnectionTable,sharedpreferences.getString("disconnection3DREditText", "-"), normalSubFont,1,0,30);
        document.add( disconnectionTable );

        PdfPTable relayRoom = new PdfPTable( 5 );
        relayRoom.setWidthPercentage( 100 );
        createCell(relayRoom,"Remarks", boldSubFont,3,20);
        createCell(relayRoom,sharedpreferences.getString("disconnectionReconnectionEditText", "-"), normalSubFont,2,0);
        createCell(relayRoom,"Relay Room Opening Register (last 3 months)", boldSubFont,5,20);
        document.add( relayRoom );

        PdfPTable relayRoomTable = new PdfPTable( 4 );
        relayRoomTable.setWidthPercentage( 92 );
        addTableCellwithPadding(relayRoomTable,"Month", boldSubFont,1,0,30);
        addTableCellwithPadding(relayRoomTable,sharedpreferences.getString("month1", "-"), boldSubFont,1,0,30);
        addTableCellwithPadding(relayRoomTable,sharedpreferences.getString("month2", "-"), boldSubFont,1,0,30);
        addTableCellwithPadding(relayRoomTable,sharedpreferences.getString("month3", "-"), boldSubFont,1,0,30);
        addTableCellwithPadding(relayRoomTable,"RR Opening", boldSubFont,1,0,30);
        addTableCellwithPadding(relayRoomTable,sharedpreferences.getString("relayRoom1RREditText", "-"), normalSubFont,1,0,30);
        addTableCellwithPadding(relayRoomTable,sharedpreferences.getString("relayRoom2RREditText", "-"), normalSubFont,1,0,30);
        addTableCellwithPadding(relayRoomTable,sharedpreferences.getString("relayRoom3RREditText", "-"), normalSubFont,1,0,30);
        document.add( relayRoomTable );

        PdfPTable remaining = new PdfPTable( 5 );
        remaining.setWidthPercentage( 100 );
        createCell(remaining,"Remarks", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("relayRoomEditText", "-"), normalSubFont,2,0);
        createCell(remaining,"Signal Infringement Register", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("signalInfringementSpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Earth Testing Record", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("earthTestingSpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Cable Meggering Record", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("cableMeggeringSpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Updated Circuit Diagrams were available", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("updatedCktDiagramSpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Cable Route Plan", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("cableRouteSpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Cable Core Chart", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("cableCoreSpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Signal Interlocking Plan (SIP)", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("signalInterlockingSpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"SMC 1 – 11", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("SMC1Spinner", "-"), normalSubFont,2,0);
        createCell(remaining,"SMC 12", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("SMC12Spinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Signal History Book", boldSubFont,3,20);
        createCell(remaining,sharedpreferences.getString("signalHistorySpinner", "-"), normalSubFont,2,0);
        createCell(remaining,"Action By", boldSubFont,3,20);
        createCellForActionBy(remaining,sharedpreferences.getString("recordsActionBySpr", "-"),sharedpreferences.getString("recordsActionByEditTxt", "-"),normalSubFont,2,0 );
        document.add( remaining );
    }

    private void addNonSntData(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 1 );
        document.add( emptyLine1 );
        PdfPTable docHeadingTable = new PdfPTable( 1 );
        docHeadingTable.setWidthPercentage( 100 );
        createCellWithColor( docHeadingTable, "Non SNT Deficiencies", subFont, 1, 0,BaseColor.LIGHT_GRAY  );
        document.add( docHeadingTable );

        PdfPTable nonSnt = new PdfPTable( 5 );
        nonSnt.setWidthPercentage( 100 );

        createCell(nonSnt,"Engineering Deficiency", boldSubFont,5,20);
        createCell(nonSnt,"Deficiency Found", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("engineeringDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(nonSnt,"Action By", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("nonSntEngineeringActionByTxtView", "-"), normalSubFont,2,0);
        createCell(nonSnt,"Electrical Deficiency", boldSubFont,5,20);
        createCell(nonSnt,"Deficiency Found", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("electricalDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(nonSnt,"Action By", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("nonSntElectricalActionByTxtView", "-"), normalSubFont,2,0);
        createCell(nonSnt,"Operating Deficiency", boldSubFont,5,20);
        createCell(nonSnt,"Deficiency Found", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("operatingDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(nonSnt,"Action By", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("nonSntOperatingActionByTxtView", "-"), normalSubFont,2,0);
        createCell(nonSnt,"Deficiency of Other Department", boldSubFont,5,20);
        createCell(nonSnt,"Deficiency Found", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("otherDeficiencyEditText", "-"), normalSubFont,2,0);
        createCell(nonSnt,"Action By", boldSubFont,3,40);
        createCell(nonSnt,sharedpreferences.getString("otherDeficiencyActionEditText", "-"), normalSubFont,2,0);

        document.add( nonSnt );
    }

    private void addDocumentEndContent(Document document) throws DocumentException {
        sharedpreferences = mContext.getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE );
        Paragraph emptyLine1 = new Paragraph();
        addEmptyLine( emptyLine1, 2 );
        document.add( emptyLine1 );

        PdfPTable docEnd = new PdfPTable( 5 );
        docEnd.setWidthPercentage( 100 );
        createCell( docEnd,"",normalSubFont,4,0 );
        createCell( docEnd,sharedpreferences.getString("authName", null),boldSubFont,1,0 );
        createCell( docEnd,"",normalSubFont,4,0 );
        createCell( docEnd,sharedpreferences.getString("authDesignation", null),boldSubFont,1,0 );

        document.add( docEnd );
    }

    private void createCell( PdfPTable stationMaster, String s, Font font, int colSpan, int indent) {
        PdfPCell inputCell = new PdfPCell(new Phrase(s, font));
        inputCell.setBorder( PdfPCell.NO_BORDER );
        inputCell.setColspan( colSpan );
        inputCell.setIndent( indent );
        inputCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(inputCell);
    }

    private void createCellWithColor(PdfPTable stationMaster, String s, Font font, int colSpan, int indent, BaseColor green) {
        PdfPCell inputCell = new PdfPCell(new Phrase(s, font));
        inputCell.setBorder( PdfPCell.NO_BORDER );
        inputCell.setColspan( colSpan );
        inputCell.setIndent( indent );
        inputCell.setBackgroundColor( green );
        inputCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(inputCell);
    }

    private void createCellwithPadding( PdfPTable stationMaster, String s, Font font, int colSpan, int indent, int padding) {
        PdfPCell inputCell = new PdfPCell(new Phrase(s, font));
        inputCell.setBorder( PdfPCell.NO_BORDER );
        inputCell.setColspan( colSpan );
        inputCell.setIndent( indent );
        inputCell.setPaddingLeft( padding );
        inputCell.setVerticalAlignment( PdfPCell.ALIGN_RIGHT );
        stationMaster.addCell(inputCell);
    }

    private void addTableCellwithPadding( PdfPTable stationMaster, String s, Font font, int colSpan, int indent, int padding) {
        PdfPCell inputCell = new PdfPCell(new Phrase(s, font));
        inputCell.setBorder( PdfPCell.BOX );
        inputCell.setColspan( colSpan );
        inputCell.setIndent( indent );
        inputCell.setPaddingLeft( padding );
        inputCell.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE );
        stationMaster.addCell(inputCell);
    }

    private void createCellForActionBy(PdfPTable stationMaster, String actionBySpinner, String actionByEditText, Font normalSubFont, int colspan, int indent) {
        PdfPCell inputCell;
        if(actionBySpinner.equals( "Other" ))
            inputCell = new PdfPCell(new Phrase(actionByEditText,normalSubFont));
        else
            inputCell = new PdfPCell(new Phrase(actionBySpinner,normalSubFont));
        inputCell.setBorder( PdfPCell.NO_BORDER );
        inputCell.setColspan( colspan );
        inputCell.setIndent( indent );
        inputCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        stationMaster.addCell(inputCell);
    }

    private void createCellForCheckbox(PdfPTable pointsCrossing, boolean aBoolean, Font normalSubFont, int colspan, int indent) {
        PdfPCell inputCell;
        if(aBoolean)
            inputCell = new PdfPCell(new Phrase("Yes",normalSubFont));
        else
            inputCell = new PdfPCell(new Phrase("No",normalSubFont));
        inputCell.setBorder( PdfPCell.NO_BORDER );
        inputCell.setColspan( colspan );
        inputCell.setIndent( indent );
        inputCell.setVerticalAlignment( PdfPCell.ALIGN_CENTER );
        pointsCrossing.addCell(inputCell);
    }

    private String getDivision(String division) {
        switch (division) {
            case "JP":    return "JAIPUR";
            case "JU":    return "JODHPUR";
            case "AII":   return "AJMER";
            case "BKN":   return "BIKANER";
            default:      return "";
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void previewPdf() {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    abstract class CustomBorder implements PdfPCellEvent {
        private int border = 0;
        public CustomBorder(int border) {
            this.border = border;
        }
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.saveState();
            setLineDash(canvas);
            if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }
            if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }
            if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            canvas.stroke();
            canvas.restoreState();
        }

        public abstract void setLineDash(PdfContentByte canvas);
    }

    class SolidBorder extends CustomBorder {
        public SolidBorder(int border) { super(border); }
        public void setLineDash(PdfContentByte canvas) {}
    }

    class Header extends PdfPageEventHelper {
        Font font;
        PdfTemplate t;
        Image total;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole( PdfName.ARTIFACT);
                font =  normalSubFont;
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(3);
            try {
                table.setPaddingTop( 10 );
                table.setWidths(new int[]{24, 24, 1});
                table.setTotalWidth(520);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                table.addCell(new Phrase("", font));
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
                PdfPCell cell = new PdfPCell(total);
                cell.setBorder(Rectangle.BOTTOM);
                table.addCell(cell);
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                table.writeSelectedRows(0, -1, 36, 26, canvas);
                canvas.endMarkedContentSequence();
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), font),
                    2, 4, 0);
        }
    }

    public void uploadPdf(){
//        FileUploadHandler fileUploadHandler = new FileUploadHandler();

    }

}
