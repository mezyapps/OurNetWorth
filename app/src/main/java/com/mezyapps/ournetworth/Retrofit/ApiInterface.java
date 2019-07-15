package com.mezyapps.ournetworth.Retrofit;



import com.mezyapps.ournetworth.MVP.AllAskResponse;
import com.mezyapps.ournetworth.MVP.AllGiveResponse;
import com.mezyapps.ournetworth.MVP.ICanConnectResponse;
import com.mezyapps.ournetworth.MVP.LikeListResponse;
import com.mezyapps.ournetworth.MVP.LikeResponse;
import com.mezyapps.ournetworth.MVP.Member;
import com.mezyapps.ournetworth.MVP.MemberWiseAskReponse;
import com.mezyapps.ournetworth.MVP.MemberWiseGiveReponse;
import com.mezyapps.ournetworth.MVP.SignUpResponse;
import com.mezyapps.ournetworth.MVP.UserImageResponse;
import com.mezyapps.ournetworth.MVP.UserProfileResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {

    // API's endpoints

    @GET("/ournetworth/json/getmember.php")
    public void getAllMember(
            Callback<List<Member>> callback);


    @FormUrlEncoded
    @POST("/ournetworth/json/getmembergive.php")
    public void getUserWiseGive(@Field("user_id") String user_id,
                                Callback<List<MemberWiseGiveReponse>> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/likelistview.php")
    public void getLikeListView(@Field("g_a_id") Integer g_a_id,
                                Callback<List<LikeListResponse>> callback);

    @GET("/ournetworth/json/getAllgive.php")
    public void getAllGive(
            Callback<List<AllGiveResponse>> callback);

    @GET("/ournetworth/json/getAllAsk.php")
    public void getAllAsk(
            Callback<List<AllAskResponse>> callback);


    @FormUrlEncoded
    @POST("/ournetworth/json/getimageuserwise.php")
    public void getUserImage(@Field("user_id") String user_id, Callback<UserImageResponse> callback);


    @FormUrlEncoded
    @POST("/ournetworth/json/getmemberask.php")
    public void getUserAskGive(@Field("user_id") String user_id,
                               Callback<List<MemberWiseAskReponse>> callback);
    @FormUrlEncoded
    @POST("/ournetworth/json/login.php")
    public void login(@Field("mobile") String email, @Field("password") String password, Callback<SignUpResponse> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/givesubmit.php")
    public void givesubmit(@Field("user_id") String user_id,
                           @Field("date_time") String date_time,
                           @Field("contact_name") String contact_name,
                           @Field("category") String category,
                           @Field("company") String company,
                           @Field("location") String location,
                           @Field("strengths") String strengths,
                           @Field("business_type") String business_type,
                           @Field("tag") String tag, Callback<SignUpResponse> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/asksubmit.php")
    public void asksubmit(@Field("user_id") String user_id, @Field("date_time") String date_time, @Field("contact_name") String contact_name, @Field("category") String category, @Field("company") String company, @Field("remarks") String remarks, @Field("tag") String tag, Callback<SignUpResponse> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/userprofile.php")
    public void getUserProfile(@Field("user_id") String user_id, Callback<UserProfileResponse> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/likesubmit.php")
    public void likeGiveProcess(@Field("g_a_id") String g_a_id, @Field("user_id") String user_id, @Field("session_id") String session_id, Callback<LikeResponse> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/icanconnect.php")
    public void iCanConnect(@Field("g_a_id") String g_a_id, @Field("user_id") String user_id, @Field("session_id") String session_id, Callback<ICanConnectResponse> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/updateprofile.php")
    public void updateProfile(@Field("user_id") String user_id,
                              @Field("full_name") String full_name,
                              @Field("category") String category,
                              @Field("company_name") String company_name,
                              @Field("mobile") String mobile,
                              @Field("email") String email,
                              @Field("business_keyword") String business_keyword,
                              @Field("address") String address,
                              Callback<SignUpResponse> callback);

    @FormUrlEncoded
    @POST("/ournetworth/json/updatePimage.php")
    public void UploadImage(@Field("user_id") String user_id, @Field("image") String image, Callback<SignUpResponse> callback);

}
