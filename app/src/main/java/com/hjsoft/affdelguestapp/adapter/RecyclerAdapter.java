package com.hjsoft.affdelguestapp.adapter;

import android.content.Context;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjsoft.affdelguestapp.R;
import com.hjsoft.affdelguestapp.model.AllBookingsData;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by hjsoft on 1/12/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    Context context;
    LayoutInflater inflater;
    Geocoder geocoder;
    RecyclerView rview;
    ArrayList<AllBookingsData> mResultList;
    AllBookingsData data;
    private AdapterCallback mAdapterCallback;
    int pos;


    public RecyclerAdapter(Context context, ArrayList<AllBookingsData> mResultList, RecyclerView rview)
    {

        this.context=context;
        this.mResultList=mResultList;
        this.rview=rview;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        geocoder=new Geocoder(context, Locale.getDefault());
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        data=mResultList.get(position);

        holder.tvRoute.setText(data.getRoute());
        holder.tvDate.setText(data.getDate());
        holder.tvStatus.setText(data.getStatus());
        holder.lLayout.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRoute,tvDate,tvStatus;
        LinearLayout lLayout;


        public MyViewHolder(final View itemView) {
            super(itemView);
           tvRoute=(TextView)itemView.findViewById(R.id.rw_tv_route);
            tvDate=(TextView)itemView.findViewById(R.id.rw_tv_date);
            tvStatus=(TextView)itemView.findViewById(R.id.rw_tv_status);
            lLayout=(LinearLayout)itemView.findViewById(R.id.lLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        pos= (int) view.getTag();
                        mAdapterCallback.onMethodCallback(pos,mResultList);

                    }
                    catch (ClassCastException e)
                    {
                        e.printStackTrace();
                    }
                    //Toast.makeText(context,"clicked .."+pos,Toast.LENGTH_LONG).show();
                }
            });




        }


    }

    public static interface AdapterCallback {
        void onMethodCallback(int position, ArrayList<AllBookingsData> data);
    }



}
