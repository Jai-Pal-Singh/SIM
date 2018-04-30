package com.android_developer.jaipal.sim;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hp on 2018-04-01.
 */


public class GearsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Set numbers of List in RecyclerView.
    private static final int LENGTH = 10;
//    private final Button submitBtn;
    private final String[] name;
    private final Drawable[] picture;
    private Context mContext;
    private LayoutInflater mInflater;
    Context context;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public GearsAdapter(Context context, String[] gear, Drawable[] gear_picture) {
        name = gear;
        picture = gear_picture;
        mContext = context;
        this.mInflater = LayoutInflater.from( context );
    }

    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position > LENGTH;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.activity_gears, parent, false );
            return new ItemViewHolder( view );
        }
        else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_gears_footer, parent, false);
            return new FooterViewHolder(view);
        }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

}

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.picture.setImageDrawable( picture[position % picture.length] );
            itemViewHolder.name.setText( name[position % name.length] );
        }
        else if (holder instanceof FooterViewHolder) {
//            FooterViewHolder footerHolder = (FooterViewHolder) holder;
//            footerHolder.submitBtn(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "You clicked at Submit button   ", Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }

    @Override
    public int getItemCount() {
        return LENGTH;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView picture;
        public TextView name;
        public View View;
        public ItemViewHolder(View v) {
            super(v);
            View = v;
            // Add your UI Components here
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

        @Override
        public void onClick(View view) {

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View View;
        public FooterViewHolder(View v) {
            super(v);
            View = v;
            // Add your UI Components here
            Button submitBtn = (Button) View.findViewById( R.id.mainGearsSubmitButton );
            submitBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e( "msg", "gdffhfdh" );
                    Toast.makeText(context, "You clicked at Submit button   ", Toast.LENGTH_SHORT).show();
                }
            } );
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "You clicked at Submit button   ", Toast.LENGTH_SHORT).show();
        }
    }
}
