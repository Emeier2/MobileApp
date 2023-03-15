package com.example.dev;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;

import android.gesture.Prediction;
import android.gesture.Gesture;
import android.widget.Toast;

import java.util.ArrayList;


public class MenuFragment extends Fragment
        implements View.OnClickListener, SensorEventListener, OnGesturePerformedListener {

    private UserData userData;
    private Button editButton;
    private LinearLayout topBar;

    // step counter
    private SensorManager sensorManager;
    private TextView steps;
    private Sensor stepCounter;
    private int stepCount = 0;

    private ProfileViewModel profileViewModel;
    private GestureLibrary gestureLib;
    private MediaPlayer turnOffSound;
    private MediaPlayer turnOnSound;
    private TextView GestureText;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        float Screen_Width = Resources.getSystem().getDisplayMetrics().widthPixels;
        float Screen_Height = Resources.getSystem().getDisplayMetrics().heightPixels;

        GestureText = view.findViewById(R.id.GestureText);

        editButton = (Button) view.findViewById(R.id.edit);
        //editButton.setY(Screen_Height * 2f);
        editButton.setOnClickListener(this);

        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        userData = profileViewModel.getData();

        TextView welcome = view.findViewById(R.id.welcome);
        ImageView thumbnail = view.findViewById(R.id.thumbnail);
        byte[] array = userData.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        thumbnail.setImageBitmap(bitmap);
        topBar = (LinearLayout) view.findViewById(R.id.TopBar);
        welcome.setText("Welcome " + userData.getFirstName() + " " + userData.getLastName());

        // step counter
        steps = (TextView) view.findViewById((R.id.steps));
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //gesture lib setup
        gestureLib = GestureLibraries.fromRawResource(this.getActivity(),R.raw.gestures);
        if(!gestureLib.load())
        {
            Toast.makeText(this.getActivity(), "unable to load custom gestures", Toast.LENGTH_SHORT).show();
        }
        GestureOverlayView gestureOverlay = (GestureOverlayView) view.findViewById(R.id.gestureWidget);
        gestureOverlay.addOnGesturePerformedListener(this);
        turnOffSound = MediaPlayer.create(this.getActivity(), R.raw.offsound);
        turnOnSound = MediaPlayer.create(this.getActivity(), R.raw.onsound);

        // Inflate the layout for this fragment
        return view;
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            stepCount =(int) sensorEvent.values[0];
            steps.setText(String.valueOf(stepCount));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit: {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(stepCounter != null)
        {
            sensorManager.registerListener(this, stepCounter, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(stepCounter != null)
        {
            sensorManager.unregisterListener(listener);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        stepCount =(int) sensorEvent.values[0];
        steps.setText(String.valueOf(stepCount));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        ArrayList<Prediction> objPrediction = gestureLib.recognize(gesture);

        if(objPrediction.size() > 0 )
        {
            String gestureName = objPrediction.get(0).name;
            if(gestureName.equals("circle"))
            {
                GestureText.setText("Draw a checkmark to disable step counter");
                steps.setText(String.valueOf(stepCount));
                turnOnSound.start();
            }
            else if(gestureName.equals("X"))
            {
                GestureText.setText("Draw a circle to enable step counter");
                steps.setText("");
                turnOffSound.start();
            }
        }
    }
}