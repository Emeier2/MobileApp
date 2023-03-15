package com.example.dev;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.dev.databinding.ActivityProfileBinding;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

        private String firstName, lastName, age, sex, city, country, height, weight;

        EditText firstNameText, lastNameText, cityText, countryText, heightText, weightText;

        private Button save;
        private ImageButton imageButton;
        private Spinner ageSpinner;
        private Spinner sexSpinner;
        private byte[] image;

        private ProfileViewModel profileViewModel;


        // get picture from camera
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bundle extras = data.getExtras();
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            image = stream.toByteArray();
                            Drawable drawable = new BitmapDrawable(bitmap);
                            imageButton.setBackgroundDrawable(drawable);
                        }
                    }
                });

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            // Get Edit Text
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_profile);

            firstNameText = (EditText) findViewById(R.id.firstName);
            lastNameText = (EditText) findViewById(R.id.lastName);
            cityText = (EditText) findViewById(R.id.city);
            countryText = (EditText) findViewById(R.id.country);
            weightText = (EditText) findViewById(R.id.weight);
            heightText = (EditText) findViewById(R.id.height);

            save = (Button) findViewById(R.id.save_button);
            imageButton = (ImageButton) findViewById(R.id.image_button);

            save.setOnClickListener(this);
            imageButton.setOnClickListener(this);

            // age spinner
            ageSpinner = findViewById(R.id.age);
            ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.age));
            ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ageSpinner.setAdapter(ageAdapter);

            // sex spinner
            sexSpinner = findViewById(R.id.sex);
            ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sex));
            sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sexSpinner.setAdapter(sexAdapter);

            UserData userData;
            try {
                Amplify.addPlugin(new AWSCognitoAuthPlugin());
                Amplify.addPlugin(new AWSS3StoragePlugin());
                Amplify.configure(getApplicationContext());//fails
                Log.i("ADM-amp", "Initialized Amplify");

                Amplify.Auth.signIn(
                        "amuser",
                        "admpw123",
                        result -> Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );

//                Amplify.Auth.signInWithWebUI(
//                        this,
//                        result -> Log.i("AuthQuickStart", result.toString()),
//                        error -> Log.e("AuthQuickStart", error.toString())
//                );
                UserData ud = downloadFile();
                if(ud == null){
                    throw new Exception("No file to download");
                }
                profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
                userData = ud;
                profileViewModel.setData(ud);
            } catch (Exception error) {
                Log.e("ADM-amp", "Could not initialize Amplify", error);
                profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
                userData = profileViewModel.getData();
            }



            if (userData != null) {
                setData(userData);
            }


        }
    private void uploadFile(UserData userData) {
        File userFile = new File(getApplicationContext().getFilesDir(), "IrrelevantKey");
        Gson gson = new Gson();
        //logic specific to userdata
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            writer.append(gson.toJson(userData));
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }

        Amplify.Storage.uploadFile(
                "IrrelevantKey",
                userFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

    private UserData downloadFile(){
        UserData userData = null;
        String fileName = getApplicationContext().getFilesDir() + "/download.json";
        Log.i("Downloading to:", fileName);

        Amplify.Storage.downloadFile(
                "IrrelevantKey",
                new File(fileName), //our file is now stored in this location
                StorageDownloadFileOptions.defaultInstance(),
                progress -> Log.i("MyAmplifyApp", "Fraction completed: " + progress.getFractionCompleted()),
                result -> Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName()),
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        try {
            Gson gson = new Gson();
            FileReader fr = new FileReader(fileName);
            userData = (UserData) gson.fromJson(fr, UserData.class);
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return userData;
    }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.save_button:{

                    // Get data
                    firstName = firstNameText.getText().toString();
                    lastName = lastNameText.getText().toString();
                    age = ageSpinner.getSelectedItem().toString();
                    sex = sexSpinner.getSelectedItem().toString();
                    city = cityText.getText().toString();
                    country = countryText.getText().toString();
                    weight = weightText.getText().toString();
                    height = heightText.getText().toString();


                    // check enter
                    if(firstName.matches(""))
                    {
                        Toast.makeText(this, "Enter your first name.", Toast.LENGTH_SHORT).show();
                    }
                    else if(lastName.matches(""))
                    {
                        Toast.makeText(this, "Enter your last name.", Toast.LENGTH_SHORT).show();
                    }
                    else if(age.matches(""))
                    {
                        Toast.makeText(this, "Enter your age.", Toast.LENGTH_SHORT).show();
                    }
                    else if(sex.matches(""))
                    {
                        Toast.makeText(this, "Enter your sex.", Toast.LENGTH_SHORT).show();
                    }
                    else if(city.matches(""))
                    {
                        Toast.makeText(this, "Enter your city.", Toast.LENGTH_SHORT).show();
                    }
                    else if(country.matches(""))
                    {
                        Toast.makeText(this, "Enter your country.", Toast.LENGTH_SHORT).show();
                    }
                    else if(weight.matches(""))
                    {
                        Toast.makeText(this, "Enter your weight.", Toast.LENGTH_SHORT).show();
                    }
                    else if(height.matches(""))
                    {
                        Toast.makeText(this, "Enter your height.", Toast.LENGTH_SHORT).show();
                    }
                    else if (image == null)
                    {
                        Toast.makeText(this, "Take an image photo", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            Integer.parseInt(height);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(this, "Your height needs to be a number.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        try {
                            Integer.parseInt(weight);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(this, "Your weight needs to be a number.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        UserData userData = new UserData(0,firstName,lastName,age,sex,city,country, height,weight,image);

                        profileViewModel.setData(userData);

                        Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                        startActivity(intent);
                        uploadFile(userData);
                    }
                    break;
                }
                case R.id.image_button:{
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(cameraIntent);
                }
            }
        }

        private void setData(UserData userData)
        {

            // set data
            firstNameText.setText(userData.getFirstName());
            lastNameText.setText(userData.getLastName());
            cityText.setText(userData.getCity());
            countryText.setText(userData.getCountry());
            weightText.setText(userData.getWeight());
            heightText.setText(userData.getHeight());

            //set spinner
            ArrayAdapter<CharSequence> ageAdapter2 = ArrayAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_item);
            ageAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ageSpinner.setAdapter(ageAdapter2);
            int agePosition = ageAdapter2.getPosition(userData.getAge());
            ageSpinner.setSelection(agePosition);

            ArrayAdapter<CharSequence> sexAdapter2 = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
            sexAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sexSpinner.setAdapter(sexAdapter2);
            int sexPosition = sexAdapter2.getPosition(userData.getSex());
            sexSpinner.setSelection(sexPosition);

            image = userData.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Drawable drawable = new BitmapDrawable(bitmap);
            imageButton.setBackgroundDrawable(drawable);
        }
    }
