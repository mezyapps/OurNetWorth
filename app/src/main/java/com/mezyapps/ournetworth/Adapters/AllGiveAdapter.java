package com.mezyapps.ournetworth.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Config;
import com.mezyapps.ournetworth.Fragments.Home;
import com.mezyapps.ournetworth.Fragments.LikeListView;
import com.mezyapps.ournetworth.MVP.AllGiveResponse;
import com.mezyapps.ournetworth.MVP.LikeResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AllGiveAdapter extends RecyclerView.Adapter<AllGiveAdapter.MemberAskViewHolder> {
    Context context;
    List<AllGiveResponse> allGiveResponses;

    public AllGiveAdapter(Context context, List<AllGiveResponse> memberList) {
        this.allGiveResponses = memberList;
        this.context = context;
    }

    @Override
    public AllGiveAdapter.MemberAskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_give_item, null);
        AllGiveAdapter.MemberAskViewHolder memberViewHolder = new AllGiveAdapter.MemberAskViewHolder(view);
        return memberViewHolder;
    }

    @Override
    public void onBindViewHolder(final AllGiveAdapter.MemberAskViewHolder holder, final int position) {
        // set the data
        holder.contact_name.setText(allGiveResponses.get(position).getContact_name());
        holder.category.setText(allGiveResponses.get(position).getCategory());
        holder.comanyaneme.setText(allGiveResponses.get(position).getCompany_name());
        holder.contactby.setText("By: "+allGiveResponses.get(position).getFull_name());
        holder.tiame_date_ask.setText(allGiveResponses.get(position).getDay().substring(0,3)+" "+allGiveResponses.get(position).getDate_time());
        holder.likecount_give.setText(String.valueOf(allGiveResponses.get(position).getLike_count()));
        holder.like_button_give_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with user name

                    updateLike(holder,position);

            }
        });
        holder.likecount_give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    LikeListView.allGiveResponses.clear();
                    LikeListView.allGiveResponses.addAll(allGiveResponses);

                    LikeListView likeListView = new LikeListView();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putString("tag", "G");
                    likeListView.setArguments(bundle);
                    ((MainActivity) context).loadFragment(likeListView, true);

            }
        });

    }



    @Override
    public int getItemCount() {
        return allGiveResponses.size(); // size of the list items
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    class MemberAskViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        public TextView contact_name, category,comanyaneme,contactby,tiame_date_ask,likecount_give;
        ImageView like_button_give_all;

        public MemberAskViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            contact_name =itemView.findViewById(R.id.contact_name_member_wise_give_all);
            comanyaneme =itemView.findViewById(R.id.comany_name_member_wise_give_all);
            category =itemView.findViewById(R.id.category_member_wise_give_all);
            contactby =itemView.findViewById(R.id.contact_by_member_wise_give_all);
            tiame_date_ask=itemView.findViewById(R.id.tiame_date_give_all);
            like_button_give_all=itemView.findViewById(R.id.like_button_give_all);
            likecount_give=itemView.findViewById(R.id.likecount_give);
        }
    }

    public void updateLike(final AllGiveAdapter.MemberAskViewHolder holder, final int position) {


        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().likeGiveProcess(String.valueOf(allGiveResponses.get(position).getG_a_id()),
                String.valueOf(allGiveResponses.get(position).getUser_id()),
                MainActivity.userId,
                new Callback<LikeResponse>() {
                    @Override
                    public void success(LikeResponse addToWishlistResponse, Response response) {
                        progressDialog.dismiss();
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                         //   holder.likecount_give.setText(String.valueOf(addToWishlistResponse.getLike_count()));
                            Config.showCustomAlertDialog(context, "You Liked "+allGiveResponses.get(position).getContact_name(), " ", SweetAlertDialog.SUCCESS_TYPE);
                           allGiveResponses.get(position).setLike_count(Integer.valueOf(addToWishlistResponse.getLike_count()));
                            holder.likecount_give.setText(String.valueOf(allGiveResponses.get(position).getLike_count()));
                        }else if (addToWishlistResponse.getSuccess().equalsIgnoreCase("false")){
                            allGiveResponses.get(position).setLike_count(Integer.valueOf(addToWishlistResponse.getLike_count()));
                            holder.likecount_give.setText(String.valueOf(allGiveResponses.get(position).getLike_count()));
                            //    ((MainActivity) context).loadFragment(new Give(), false);
                            Config.showCustomAlertDialog(context, "You Unliked "+allGiveResponses.get(position).getContact_name(), " ", SweetAlertDialog.NORMAL_TYPE);
                        } else {
                            Config.showCustomAlertDialog(context,
                                    "Your Give",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }

}