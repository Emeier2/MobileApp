package com.example.dev;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ProfileRepository {
    private static ProfileRepository instance;
    private Application app;
    private ProfileDao profileDao;
    //private StepDao stepDao;

    private ProfileRepository(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        app = application;
        profileDao = db.profileDao();
        //stepDao = db.stepDao();
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(application.getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    public static synchronized ProfileRepository getInstance(Application application)
    {
        if(instance == null)
        {
            instance = new ProfileRepository(application);
        }
        return instance;
    }
    private void uploadFile(UserData userData,int ID) {
        File exampleFile = new File(app.getApplicationContext().getFilesDir(), "ExampleKey");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append(userData.getAge());
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }

        Amplify.Storage.uploadFile(
                "ExampleKey",
                exampleFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }
    public UserData getData(int ID)
    {
        List<UserData> list = profileDao.getData(ID);//grab from s3? or only internal?
        if(list.size() > 0)
        {
            return list.get(0);
        }
        else
        {
            return null;
        }
    }

    public void setData(UserData userData)
    {
        profileDao.insert(userData); //pull from s3 and append?
    }

//    public Step getSteps()
//    {
//        List<Step> list = stepDao.getStep();
//        return list.get(0);
//    }
//
//    public void setSteps(Step steps)
//    {
//        stepDao.deleteALL();
//        stepDao.insert(steps);
//    }
}
