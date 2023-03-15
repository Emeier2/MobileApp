package com.example.dev;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class NavBar extends Fragment implements View.OnClickListener{

    public static NavBar newInstance() {
        return new NavBar();
    }

    private static final float Icon_Spacing_And_Width = 1f/9f;
    private float Screen_Width;
    private float Screen_Height;

    private ImageView Mountains, EditPencil, HomeIcon, Barbell, Weather;
    private String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private String endURL = "id=5780993&appid=71192fa59e717e727b2cfe2415a218b5";
    private String WOEIDLocation = "2487610";
    private RequestQueue queue;
    private UserData currData;
    private ProfileViewModel profVM;
    private FragmentContainerView mainFragContainer;
    private String url = "";
    private StringRequest stringRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.nav_bar_fragment, container, false);
        mainFragContainer = (FragmentContainerView) v.findViewById(R.id.menuFragment);
        profVM = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        Screen_Width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Screen_Height = Resources.getSystem().getDisplayMetrics().heightPixels;

        Mountains = (ImageView)v.findViewById(R.id.mountainsIcon);
        //Mountains.setX((Icon_Spacing_And_Width * 2) * Screen_Width);
        Mountains.setOnClickListener(this);

        EditPencil = (ImageView)v.findViewById(R.id.pencilIcon);
        EditPencil.setOnClickListener(this);

        HomeIcon = (ImageView)v.findViewById(R.id.homeIcon);
        HomeIcon.setOnClickListener(this);

        Barbell = (ImageView)v.findViewById(R.id.weightsIcon);
        Barbell.setOnClickListener(this);

        queue = Volley.newRequestQueue(getActivity());
        queue.start();
        Weather = (ImageView)v.findViewById(R.id.cloudsIcon);
        //Weather.setX((Icon_Spacing_And_Width * 6) * Screen_Width);
        Weather.setOnClickListener(this);

        String location = profVM.getData().getCity().replace(" ", "&");
        String url = baseURL+ location+ endURL;



        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    public void onClick(View v)
    {
        //launch google maps searching "hikes near me"
        //location is currently hard coded.
        if(v.getId() == R.id.mountainsIcon)
        {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode("Hikes near me")));
            startActivity(mapIntent);
        }

        //queue a request for weather data.
        if(v.getId() == R.id.cloudsIcon)
        {

            //the method to request weather data, and open a new fragment when data has been processed.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, baseURL+profVM.getData().getCity().replace(" ", "&")+endURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //process the response, and set data in the View model accordingly
                                JSONWeatherUtils.processWeatherData(response);
                                profVM.currTemp = WeatherData.Temperature.getTemp();
                                profVM.currHumidity = WeatherData.CurrentCondition.getHumidity();
                                profVM.currWind = WeatherData.Wind.getSpeed();
                                profVM.currCondition.setValue(WeatherData.CurrentCondition.getIcon());

                                //start the transaction and change the fragment.
                                FragmentTransaction fTrans = getFragmentManager().beginTransaction();
                                fTrans.replace(R.id.MenuDisplayFCV, new WeatherFragment());
                                fTrans.commit();

                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG);
                    Toast.makeText(getActivity(), "Error. \nHint: smaller cities won't be found by the weather tab.", Toast.LENGTH_LONG).show();
                }
            });

            queue.add(stringRequest);

        }

        //opens the edit profile activity
        if(v.getId() == R.id.pencilIcon)
        {
            Intent editIntent = new Intent(this.getActivity(), ProfileActivity.class);
            this.getActivity().startActivity(editIntent);
        }
        //open the weight activity
        if(v.getId() == R.id.weightsIcon)
        {
            FragmentTransaction fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.MenuDisplayFCV, new WeightFragment());
            fTrans.commit();
        }

        //open the profile list activity.
        if(v.getId() == R.id.homeIcon)
        {
            FragmentTransaction fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.MenuDisplayFCV, new MenuFragment());
            fTrans.commit();
        }

    }

}