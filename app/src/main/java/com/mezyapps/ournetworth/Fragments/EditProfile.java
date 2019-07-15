package com.mezyapps.ournetworth.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Config;
import com.mezyapps.ournetworth.MVP.SignUpResponse;
import com.mezyapps.ournetworth.MVP.UserProfileResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public class EditProfile extends Fragment {

EditText editText_full_name_pr,editText_category_pr,editText_company_pr,editText_mpbin_pr,editText_email_pr,editText_adress_pr,business_keyword_ed_pr;
Button update_profile_btn;
ImageView profileimage_edit_pr,updateimage_btn;
String url_image;

    public EditProfile() {
        // Required empty public constructor
    }
    UserProfileResponse userProfileResponseData;


    private static final String IMAGE_DIRECTORY = "/OurNetWorth";
    private int GALLERY = 1, CAMERA = 2;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText("My Profile");
        getUserProfileData();
        MainActivity.fab.setVisibility(View.GONE);
//        xml object strat
        profileimage_edit_pr=view.findViewById(R.id.profileimage_edit_pr);
        editText_full_name_pr=view.findViewById(R.id.full_name_edit_pr);
        editText_category_pr=view.findViewById(R.id.category_edit_pr);
        editText_company_pr=view.findViewById(R.id.company_name_edit_pr);
        editText_mpbin_pr=view.findViewById(R.id.mobile_edit_pr);
        editText_email_pr=view.findViewById(R.id.email_edit_pr);
        editText_adress_pr=view.findViewById(R.id.address_edit_pr);
        update_profile_btn=view.findViewById(R.id.edit_profile_submit_button);
        updateimage_btn=view.findViewById(R.id.updateimage_btn);
        business_keyword_ed_pr=view.findViewById(R.id.business_keyword_ed_pr);
//        xml object end


        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        updateimage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    showPictureDialog();
                } else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        showPictureDialog();
                    }
                }

            }
        });



        profileimage_edit_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileImageFull profileImageFull = new ProfileImageFull();
                Bundle bundle = new Bundle();
                bundle.putString("url_image", url_image);
                profileImageFull.setArguments(bundle);
                ((MainActivity) getActivity()).loadFragment(profileImageFull, true);
            }
        });

        return view;
    }

    ///new picker start


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    profileimage_edit_pr.setImageBitmap(bitmap);

                    //                    update pick start
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] imgByte = byteArrayOutputStream.toByteArray();
                  String  encodedimage= Base64.encodeToString(imgByte,Base64.DEFAULT);
                    uodateimage(encodedimage);

//                    update pick end



                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profileimage_edit_pr.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            //                    update pick start
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            String  encodedimage= Base64.encodeToString(imgByte,Base64.DEFAULT);
            uodateimage(encodedimage);

//                    update pick end


        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }



    /// end picker end








    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    public void getUserProfileData() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getUserProfile(
                MainActivity.userId, new Callback<UserProfileResponse>() {
                    @Override
                    public void success(UserProfileResponse userProfileResponse, Response response) {
                        userProfileResponseData = userProfileResponse;
                        pDialog.dismiss();
                        if (userProfileResponse.getSuccess().equalsIgnoreCase("false")) {
//                            profileLayout.setVisibility(View.INVISIBLE);
//                            verifyEmailLayout.setVisibility(View.VISIBLE);
                        } else
                            setUserProfileData();

//
                  }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                    }
                });
    }

    private void setUserProfileData() {
//        logout.setText("Logout ( "+userProfileResponseData.get);
        url_image=userProfileResponseData.getImage();
        editText_full_name_pr.setText(userProfileResponseData.getFull_name());
        editText_category_pr.setText(userProfileResponseData.getCategory());
        editText_company_pr.setText(userProfileResponseData.getCompany_name());
        editText_mpbin_pr.setText(userProfileResponseData.getMobile());
        editText_email_pr.setText(userProfileResponseData.getEmail());
        editText_adress_pr.setText(userProfileResponseData.getAddress());
        business_keyword_ed_pr.setText(userProfileResponseData.getBusiness_keyword());
        try {
            Picasso.with(getContext())
                    .load(userProfileResponseData.getImage())
                    // .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(profileimage_edit_pr);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }

    }

    public void updateProfile() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().updateProfile(
                MainActivity.userId,
                editText_full_name_pr.getText().toString().trim(),
                editText_category_pr.getText().toString().trim(),
                editText_company_pr.getText().toString().trim(),
                editText_mpbin_pr.getText().toString().trim(),
                editText_email_pr.getText().toString().trim(),
                business_keyword_ed_pr.getText().toString().trim(),
                editText_adress_pr.getText().toString().trim(),
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.showCustomAlertDialog(getActivity(),
                                    "Profile Status",
                                    "Profile updated",
                                    SweetAlertDialog.SUCCESS_TYPE);
                            getUserProfileData();
                        } else {
                            Toast.makeText(getActivity(), signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                    }
                });
    }




    private void uodateimage(String encodedimage) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().UploadImage(
                MainActivity.userId,
                encodedimage,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.showCustomAlertDialog(getActivity(),
                                    "Profile Pick",
                                    signUpResponse.getMessage(),
                                    SweetAlertDialog.SUCCESS_TYPE);
                            getUserProfileData();
                        } else {
                            Toast.makeText(getActivity(), signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                    }
                });

    }

}