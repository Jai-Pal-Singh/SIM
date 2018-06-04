package com.android_developer.jaipal.sim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class InspectionNotesAdapter extends RecyclerView.Adapter <InspectionNotesAdapter.MyViewHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    List<InspectionNoteDataModel> dataModelArrayList;
    AdapterView.OnItemClickListener mItemClickListener;

    public InspectionNotesAdapter(Context mContext, List<InspectionNoteDataModel> dataModelArrayList) {
        this.mContext = mContext;
        this.dataModelArrayList = dataModelArrayList;

        this.mInflater = LayoutInflater.from( mContext );
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView filenameTxtView, inspectedByTxtView, timeTxtView, stationCodeTxtView, dateTxtView;

        public MyViewHolder(View view) {
            super(view);
            filenameTxtView = (TextView) view.findViewById(R.id.filename);
            inspectedByTxtView = (TextView) view.findViewById(R.id.inspectedBy);
            timeTxtView = (TextView) view.findViewById(R.id.time);
            stationCodeTxtView = (TextView) view.findViewById(R.id.stationCode);
            dateTxtView = (TextView) view.findViewById(R.id.date);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inspection_note_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InspectionNotesAdapter.MyViewHolder holder, int position) {
        String inspectTime="", inspectDate="";
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.US);
        InspectionNoteDataModel dataModel=dataModelArrayList.get(position);
        try {
            Date date = format.parse(dataModel.getTime());
            inspectDate = (String) DateFormat.format("MMM dd, yyyy",  date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.filenameTxtView.setText( dataModel.getFilename());
        holder.inspectedByTxtView.setText( dataModel.getInspectedBy());
        holder.dateTxtView.setText(inspectDate);
        holder.stationCodeTxtView.setText( dataModel.getStationCode() );
        holder.timeTxtView.setText(dataModel.getDivision() );

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
}
