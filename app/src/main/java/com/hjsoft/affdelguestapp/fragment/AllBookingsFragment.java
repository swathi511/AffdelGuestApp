package com.hjsoft.affdelguestapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hjsoft.affdelguestapp.R;
import com.hjsoft.affdelguestapp.adapter.RecyclerAdapter;
import com.hjsoft.affdelguestapp.model.AllBookingsData;

import java.util.ArrayList;
import java.util.List;

public class AllBookingsFragment extends Fragment{

    View v;
    Spinner sp;
    String item="";
    RecyclerView rView;
    RecyclerAdapter rAdapter;
    ArrayList<AllBookingsData> dataList=new ArrayList<>();
    RecyclerAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_booking_history,container,false);
        rView=(RecyclerView)v.findViewById(R.id.fbh_rview);


        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Pickup"));
        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Pickup"));
        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Delivery"));
        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Pickup"));
        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Pickup"));
        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Delivery"));
        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Pickup"));
        dataList.add(new AllBookingsData("HYD - VIJ","22 May 2018","Pickup"));

        mAdapter = new RecyclerAdapter(getActivity(), dataList, rView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rView.setLayoutManager(mLayoutManager);
        rView.setItemAnimator(new DefaultItemAnimator());
        rView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();




        return v;
    }
}
