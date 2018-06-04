package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LCInspectionNotes {

    Document document;
    File file;
    String fileName;
    String path;
    FileOutputStream fOut;
    Context mContext;
    String dateTime;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static Font boldSubFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static Font normalSubFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    public  LCInspectionNotes(Context mcontext){
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
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date date = format.parse(sharedpreferences.getString("inspectDate", ""));
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_") ;
            Date time = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss") ;
            dateTime = dateFormat.format(date)+timeFormat.format( time );

            String authDesig = sharedpreferences.getString("authDesignation", "");
            String newAuthDesig = authDesig.replaceAll( "/","_" );
            newAuthDesig  = newAuthDesig.replaceAll( "\\s", "" );

            fileName = "LC_"+sharedpreferences.getString("stationCode", "")+"_"+newAuthDesig+"_"+dateTime+".pdf";
            file = new File(dir, fileName);
            fOut = new FileOutputStream(file);
            PdfWriter writer =PdfWriter.getInstance(document, fOut);
            LCInspectionNotes.Header event = new LCInspectionNotes.Header();
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
        addLevelCrossingData(document);
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
        divisionCell.setCellEvent(new LCInspectionNotes.SolidBorder(PdfPCell.RIGHT));
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
        dateEntryCell.setCellEvent(new LCInspectionNotes.SolidBorder(PdfPCell.RIGHT));
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
        desigCell.setCellEvent(new LCInspectionNotes.SolidBorder(PdfPCell.LEFT));
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

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
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

    public void previewPdf() {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
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
                table.getDefaultCell().setBorder( Rectangle.BOTTOM);
                table.addCell(new Phrase("", font));
                table.getDefaultCell().setHorizontalAlignment( Element.ALIGN_RIGHT);
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

    class SolidBorder extends CustomBorder {
        public SolidBorder(int border) { super(border); }
        public void setLineDash(PdfContentByte canvas) {}
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

    public String returnFileName(){
        return fileName;
    }

    public String returnDateTime(){
        return dateTime;
    }

    public void uploadPdf(){
        FileUploadHandler fileUploadHandler = new FileUploadHandler(mContext, file);
    }
}
