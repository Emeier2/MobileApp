package com.example.dev;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    private ImageView conditionIm;
    private TextView humidTV;
    private TextView tempTV;
    private TextView windTV;
    private ImageView thumbNail;
    private ProfileViewModel profileViewModel;
    private UserData userData;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProfileViewModel profVM;

    public WeatherFragment() {
        // Required empty public constructor

    }


    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        // create/Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        userData = profileViewModel.getData();

        thumbNail = view.findViewById(R.id.thumbnail);
        byte[] array = userData.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        thumbNail.setImageBitmap(bitmap);

        //Set the values according to the View Model
        profVM = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        TextView humidTV = view.findViewById(R.id.currentHumidity);

        int currTemp = (int) (profVM.currTemp -273)*9/5+32;
        TextView tempTV = view.findViewById(R.id.currentTemperature);
        TextView windTV = view.findViewById(R.id.currentWindSpeed);
        humidTV.setText(String.valueOf(profVM.currHumidity));
        tempTV.setText(String.valueOf( currTemp+ "F"));
        windTV.setText(String.valueOf( profVM.currWind));
        ImageView conditionIm = view.findViewById(R.id.currentWeatherClear);

        //Switch case for setting the weather Image
        switch (String.valueOf(profVM.currCondition))
        {
            //clear code
            case "O1d":
                conditionIm.setImageResource(R.drawable.ic_clear);
                break;
            case "O1n":
                conditionIm.setImageResource(R.drawable.ic_clear);
                break;

            //few clouds code
            case "O2d":
                conditionIm.setImageResource(R.drawable.ic_lightcloud);
                break;
            case "O2n":
                conditionIm.setImageResource(R.drawable.ic_lightcloud);
                break;
            case "O4d":
                conditionIm.setImageResource(R.drawable.ic_lightcloud);
                break;
            case"O4n":
                conditionIm.setImageResource(R.drawable.ic_lightcloud);
                break;
            case "50d":
                conditionIm.setImageResource(R.drawable.ic_lightcloud);
                break;
            case "50n":
                conditionIm.setImageResource(R.drawable.ic_lightcloud);
                break;

            //heavy clouds code
            case "03d":
                conditionIm.setImageResource(R.drawable.ic_heavycloud);
                break;
            case "03n":
                conditionIm.setImageResource(R.drawable.ic_heavycloud);
                break;


            //light rain
            case "09d":
                conditionIm.setImageResource(R.drawable.ic_lightrain);
                break;
            case "09n":
                conditionIm.setImageResource(R.drawable.ic_lightrain);
                break;

            //heavy rain
            case "10d":
                conditionIm.setImageResource(R.drawable.ic_heavyrain);
                break;
            case "10n":
                conditionIm.setImageResource(R.drawable.ic_heavyrain);
                break;

            //thunder
            case "11d":
                conditionIm.setImageResource(R.drawable.ic_thunder);
                break;
            case "11n":
                conditionIm.setImageResource(R.drawable.ic_thunder);
                break;
            case "13d":
                conditionIm.setImageResource(R.drawable.ic_snow);
                break;
            case "13n":
                conditionIm.setImageResource(R.drawable.ic_snow);
                break;

        }
        return view;
    }
}