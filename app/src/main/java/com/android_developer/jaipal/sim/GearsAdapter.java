package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hp on 2018-04-01.
 */


public class GearsAdapter extends RecyclerView.Adapter<GearsAdapter.ViewHolder> {

    // Set numbers of List in RecyclerView.
    private static final int LENGTH = 10;
    private final String[] name;
    private final Drawable[] picture;
    private Context mContext;
    private LayoutInflater mInflater;
    Context context;

    public GearsAdapter(Context context, String[] gear, Drawable[] gear_picture) {
        name = gear;
        picture = gear_picture;
        mContext = context;
        this.mInflater = LayoutInflater.from( context );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from( parent.getContext() ), parent );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.picture.setImageDrawable( picture[position % picture.length] );
        holder.name.setText( name[position % name.length] );
    }

    @Override
    public int getItemCount() {
        return LENGTH;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView picture;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super( inflater.inflate( R.layout.activity_gears, parent, false ) );
            picture = (ImageView) itemView.findViewById( R.id.tile_picture );
            name = (TextView) itemView.findViewById( R.id.tile_title );
//            itemView.setOnClickListener(this);
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()==0) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, DetailActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==1) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, PointsCrossingActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==2) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, TrackCircuitsActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==3) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, SignalsActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==4) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, CCIPActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==5) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, BlockInstrumentsActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==6) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, PowerSupplyActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==7) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, LevelCrossingActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==8) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, RecordsActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                    else if(getAdapterPosition()==9) {
                        Context context = v.getContext();
                        Intent intent = new Intent( context, NonSNTActivity.class );
                        intent.putExtra( "position", getAdapterPosition() );
                        context.startActivity( intent );
                    }
                }
            } );


        }

        // convenience method for getting data at click position
//        String getItem(int id) {
//            return name[id];
//        }

        // allows clicks events to be caught


        @Override
        public void onClick(View view) {

        }

        // parent activity will implement this method to respond to click events

    }
}
