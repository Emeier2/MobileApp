package com.example.dev;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {
    private UserData userData;
    private ProfileRepository profileRepository;
    public double currTemp;
    public double currHumidity;
    public double currWind;
    public MutableLiveData<String> currCondition;


    public ProfileViewModel(Application application) {
        super(application);
        profileRepository = ProfileRepository.getInstance(application);
        userData = profileRepository.getData(0);
        currCondition = new MutableLiveData<String>("");
        this.currHumidity = -999.0;
        this.currWind = -999.0;
        this.currTemp = -999.0;
    }

    public UserData getData()
    {
        return profileRepository.getData(0);
    }

    public void setData (UserData data)
    {
        profileRepository.setData(data);
    }

//    public Step getSteps(){ return profileRepository.getSteps();}
//
//    public void setSteps(Step steps){ profileRepository.setSteps(steps);}
}
