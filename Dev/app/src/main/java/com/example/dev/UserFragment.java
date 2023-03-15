package com.example.dev;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class UserFragment extends RecyclerView.Adapter<UserFragment.ViewHolder> {

    private List<String> userList;
    private Context userContext;
    private DataPasser dataPasser;

    public UserFragment(List<String> userList)
    {
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected View userLayout;
        protected TextView userData;
        protected ImageView userImage;

        public ViewHolder(View view)
        {
            super(view);
            userLayout = view;
            userData = (TextView) view.findViewById(R.id.userInfo);
        }
    }

    @NonNull
    @Override
    public UserFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userContext = parent.getContext();
        try {
            dataPasser = (DataPasser) userContext;
        }catch(ClassCastException e)
        {
            throw new ClassCastException(userContext.toString() + " must implement DataPasser");
        }

        LayoutInflater layoutInflater = LayoutInflater.from(userContext);
        View myView = layoutInflater.inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFragment.ViewHolder holder, int position) {
        holder.userData.setText("test1");
        holder.userLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                dataPasser.passData(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static interface DataPasser{
        public void passData(int position);
    }
}