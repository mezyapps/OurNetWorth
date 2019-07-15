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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Config;
import com.mezyapps.ournetworth.Fragments.Ask;
import com.mezyapps.ournetworth.Fragments.ConnectList;
import com.mezyapps.ournetworth.Fragments.Home;
import com.mezyapps.ournetworth.MVP.AllAskResponse;
import com.mezyapps.ournetworth.MVP.ICanConnectResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AllAskAdapter extends RecyclerView.Adapter<AllAskAdapter.MemberAskViewHolder> {

    Context context;
    List<AllAskResponse> allAskResponses;

    public AllAskAdapter(Context context, List<AllAskResponse> memberList) {
        this.allAskResponses = memberList;
        this.context = context;
    }

    @Override
    public AllAskAdapter.MemberAskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.member_wise_ask_item_all, null);
        AllAskAdapter.MemberAskViewHolder memberViewHolder = new AllAskAdapter.MemberAskViewHolder(view);
        return memberViewHolder;
    }

    @Override
    public void onBindViewHolder(final AllAskAdapter.MemberAskViewHolder holder, final int position) {
        // set the data
        holder.contact_name.setText(allAskResponses.get(position).getContact_name());
        holder.category.setText(allAskResponses.get(position).getCategory());
        holder.comanyaneme.setText(allAskResponses.get(position).getCompany_name());
        holder.contactby.setText("By: "+allAskResponses.get(position).getFull_name());
        holder.tiame_date_ask.setText(allAskResponses.get(position).getDay().substring(0,3)+" "+allAskResponses.get(position).getDate_time());
        if (allAskResponses.get(position).getConnected_by_user_id() == 0){
            holder.i_can_connect_ask_all.setText("I Can Connect");
            holder.i_can_connect_ask_name_all.setVisibility(View.GONE);
        }else {
           holder.i_can_connect_ask_all.setVisibility(View.GONE);
           holder.i_can_connect_ask_name_all.setVisibility(View.VISIBLE);
            holder.i_can_connect_ask_name_all.setText("Cnt By: "+allAskResponses.get(position).getCb_name());
            holder.relative_Layout_ask.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }

        holder.i_can_connect_ask_name_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Home.swipeRefreshLayout.isRefreshing()) {

                    ConnectList.allAskResponses.clear();
                    ConnectList.allAskResponses.addAll(allAskResponses);

                    ConnectList likeListView = new ConnectList();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    likeListView.setArguments(bundle);
                    ((MainActivity) context).loadFragment(likeListView, true);
                }
            }
        });

        holder.i_can_connect_ask_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with user name
                if (!Home.swipeRefreshLayout.isRefreshing()) {

                    final SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                    alertDialog.setTitleText("Are you sure you want to Connect?");
                    alertDialog.setCancelText("Cancel");
                    alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismissWithAnimation();
                        }
                    });
                    alertDialog.show();
                    Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn.setBackground(context.getResources().getDrawable(R.drawable.custom_dialog_button));
                    btn.setText("OK");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            updateConnect(holder,  position);
                            alertDialog.dismissWithAnimation();
                        }
                    });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allAskResponses.size(); // size of the list items
    }

    class MemberAskViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        private TextView contact_name, category,comanyaneme,contactby,tiame_date_ask,i_can_connect_ask_all,i_can_connect_ask_name_all;
        LinearLayout relative_Layout_ask;

        public MemberAskViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            contact_name =itemView.findViewById(R.id.contact_name_member_wise_ask_all);
            comanyaneme =itemView.findViewById(R.id.comany_name_member_wise_ask_all);
            category =itemView.findViewById(R.id.category_member_wise_ask_all);
            contactby =itemView.findViewById(R.id.contact_by_member_wise_ask_all);
            tiame_date_ask=itemView.findViewById(R.id.tiame_date_ask_all);
            i_can_connect_ask_all=itemView.findViewById(R.id.i_can_connect_ask_all);
            relative_Layout_ask=itemView.findViewById(R.id.relative_Layout_ask);
            i_can_connect_ask_name_all=itemView.findViewById(R.id.i_can_connect_ask_name_all);
        }
    }

    public void updateConnect(final AllAskAdapter.MemberAskViewHolder holder, final int position) {


        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().iCanConnect(String.valueOf(allAskResponses.get(position).getG_a_id()),
                String.valueOf(allAskResponses.get(position).getUser_id()),
                MainActivity.userId,
                new Callback<ICanConnectResponse>() {
                    @Override
                    public void success(ICanConnectResponse iCanConnectRespons, Response response) {


                        if (iCanConnectRespons.getSuccess().equalsIgnoreCase("true")) {
                                    Config.showCustomAlertDialog(context,
                                    "You Connect "+allAskResponses.get(position).getContact_name(),
                                            "Connected",
                                            SweetAlertDialog.SUCCESS_TYPE);
                            holder.i_can_connect_ask_all.setVisibility(View.GONE);
                            holder.i_can_connect_ask_name_all.setVisibility(View.VISIBLE);
                            holder.i_can_connect_ask_name_all.setText("Cnt By: "+iCanConnectRespons.getMessage());
                            holder.relative_Layout_ask.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                            //((MainActivity) context).loadFragment(new Ask(), false);
                        } else if (iCanConnectRespons.getSuccess().equalsIgnoreCase("false")){
                            Config.showCustomAlertDialog(context,
                                    iCanConnectRespons.getMessage(),
                                    "No Allowed",
                                    SweetAlertDialog.SUCCESS_TYPE);
                        }else {
                            Config.showCustomAlertDialog(context,
                                    iCanConnectRespons.getMessage(),
                                    "Your Ask",
                                    SweetAlertDialog.SUCCESS_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
        progressDialog.dismiss();
    }
}
