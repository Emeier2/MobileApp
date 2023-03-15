package com.example.dev;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dev.databinding.FragmentProfileListBinding;

import java.util.ArrayList;
import java.util.List;


public class ProfileListFragment extends Fragment {
    public ProfileListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);

        // Create new Button function
        Button createNew = (Button) view.findViewById(R.id.createNew);
        createNew.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentTransaction fTrans = getFragmentManager().beginTransaction();


                fTrans.commit();
            }
        });


        return view;
    }
}