package com.hjsoft.affdelguestapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hjsoft.affdelguestapp.R;
import com.hjsoft.affdelguestapp.activity.PlacesAutoCompleteActivity;

import java.util.ArrayList;
import java.util.List;

public class ParcelBookingFragment extends Fragment {

    View view;
    Spinner sp;
    String item="",stClicked;
    EditText etPickup,etDrop;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_parcel_booking,container,false);
        sp=(Spinner)view.findViewById(R.id.fpb_spinner);
        etPickup=(EditText) view.findViewById(R.id.fpb_et_pickup);
        etDrop=(EditText) view.findViewById(R.id.fpb_et_drop);

        final List<String> categories = new ArrayList<String>();
        categories.add("inch");
        categories.add("feet");
        categories.add("centimeter");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp.setAdapter(dataAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position != 0) {

                    item = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*etPickup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                etPickup.setInputType(InputType.TYPE_NULL);

                Intent i=new Intent(getContext(),PlacesAutoCompleteActivity.class);
                i.putExtra("value","pickup");
                startActivityForResult(i,2);
                stClicked="pickup";
            }
        });*/


        etPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etPickup.setInputType(InputType.TYPE_NULL);

                Intent i=new Intent(getContext(),PlacesAutoCompleteActivity.class);
                i.putExtra("value","pickup");
                startActivityForResult(i,2);
                stClicked="pickup";
            }
        });

        etDrop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {

                    etDrop.setInputType(InputType.TYPE_NULL);

                    Intent i = new Intent(getContext(), PlacesAutoCompleteActivity.class);
                    i.putExtra("value", "drop");
                    startActivityForResult(i, 2);
                    stClicked = "drop";
                }

            }
        });

        etDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etDrop.setInputType(InputType.TYPE_NULL);

                Intent i=new Intent(getContext(),PlacesAutoCompleteActivity.class);
                i.putExtra("value","drop");
                startActivityForResult(i,2);
                stClicked="drop";
            }
        });



        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 2:
                if(data!=null)
                {
                    String locationName=data.getStringExtra("loc");

                    if(stClicked.equals("pickup"))
                    {
                        etPickup.setText(locationName);
                    }
                    else
                    {
                        etDrop.setText(locationName);
                    }

                }
                else
                {

                }
                break;
        }
    }
}
