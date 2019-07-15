package com.mezyapps.ournetworth.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mezyapps.ournetworth.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileImageFull extends Fragment {


    public ProfileImageFull() {
        // Required empty public constructor
    }
String url_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_image_full, container, false);
        Bundle bundle = getArguments();
        url_image = bundle.getString("url_image");
        ImageView full_screen_image=view.findViewById(R.id.full_screen_image);


        try {
            Picasso.with(getContext())
                    .load(url_image)
                    // .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(full_screen_image);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
        return view;
    }

}
