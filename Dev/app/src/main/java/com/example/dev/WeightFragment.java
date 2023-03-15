package com.example.dev;

import android.app.Activity;
import android.app.AlertDialog;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightFragment extends Fragment {
    private String gender, weightgoal;
    private int weight,age,height,goal,count1,count2;
    private double weightRate, bmi;
    private Spinner spinner,spinner2;
    private Button button;
    //private AlertDialog alertView;
    private TextView needed,calories,alertView,bmiText;
    private LinearLayout linLayout;
    private ProfileViewModel profileViewModel;
    private UserData userData;
    private ImageView thumbNail;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private float Screen_Width;
    private float Screen_Height;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public WeightFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeightFragment newInstance(String param1, String param2) {
        WeightFragment fragment = new WeightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        userData = profileViewModel.getData();

        weight = Integer.parseInt(userData.getWeight());
        age = Integer.parseInt(userData.getAge());
        height = Integer.parseInt(userData.getHeight());
        gender = userData.getSex();
        weightgoal = "Maintain";
        weightRate = 0.25;
        goal = 0;
        count1 = 0;
        count2 = 0;
        bmi = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] goals = new String[3];
        goals[0] = "Lose";
        goals[1] = "Gain";
        goals[2] = "Maintain";

        String[] rate = new String[4];
        rate[0] = "0.25";
        rate[1] = "0.50";
        rate[2] = "1.00";
        rate[3] = "2.00";

        Screen_Width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Screen_Height = Resources.getSystem().getDisplayMetrics().heightPixels;



        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weight, container, false);

        thumbNail = v.findViewById(R.id.thumbnail);
        byte[] array = userData.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        thumbNail.setImageBitmap(bitmap);

        linLayout = (LinearLayout) v.findViewById(R.id.linearWeightLayout);
        //linLayout.setY((int) ((Screen_Height) * 0.15) + 43);
        linLayout.setMinimumWidth((int) (Screen_Width * 0.9));

        spinner = (Spinner) v.findViewById(R.id.goal_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, goals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new GoalSpinnerClass());

        spinner2 = (Spinner) v.findViewById(R.id.weight_spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, rate);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new WeightPerWeek());

        button = v.findViewById(R.id.button);
        button.setOnClickListener(button -> {
            replaceBMRText();
        });
        needed = v.findViewById(R.id.needed);
        calories = v.findViewById(R.id.calories);
        alertView = v.findViewById(R.id.warning);
        bmiText = v.findViewById(R.id.bmiText);

        return v;
    }
    public void replaceBMRText(){
        int gen = 5;
        if(gender == "Female"){
            gen = 161;
        }
        double BMR = (10 * weight)/2.2 + (6.25 * height)*2.54 - gen -5 * age;
        needed.setText("" + ((int) BMR));
        replaceCaloricNeed(BMR);
    }
    public void replaceCaloricNeed(double BMR){
        if(weightgoal == "Gain"){
            goal = 1;
        }
        else if(weightgoal == "Lose"){
            goal = -1;
        }
        else if (weightgoal == "Maintain"){
            goal = 0;
        }
        int caloricNeed = 0;
        if(goal < 0)
            caloricNeed = (int) (BMR - ((3600/7) * weightRate));
        else
            caloricNeed = (int) (BMR + goal * ((3600/7) * weightRate));
        calories.setText("" + caloricNeed);
        if(caloricNeed < 1200){
            alertView.setText("too few!");
        }else
            alertView.setText("");

        double bmi = (703 * weight) / (height*height);
        bmiText.setText("" + bmi);
    }

    public class GoalSpinnerClass extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            if(count1 >= 1) {
                weightgoal = parent.getItemAtPosition(pos).toString();
            }
            count1++;
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    public class WeightPerWeek extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            if(count1 >= 1) {
                weightRate = Double.parseDouble(parent.getItemAtPosition(pos).toString());
            }
            count1++;
        }
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}