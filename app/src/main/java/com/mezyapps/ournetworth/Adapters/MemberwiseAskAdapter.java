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
import com.mezyapps.ournetworth.Fragments.MemberDetail;
import com.mezyapps.ournetworth.MVP.ICanConnectResponse;
import com.mezyapps.ournetworth.MVP.MemberWiseAskReponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MemberwiseAskAdapter  extends RecyclerView.Adapter<MemberwiseAskAdapter.MemberAskViewHolder> {

    Context context;
    List<MemberWiseAskReponse> memberWiseAskReponses;

    public MemberwiseAskAdapter(Context context, List<MemberWiseAskReponse> memberList) {
        this.memberWiseAskReponses = memberList;
        this.context = context;
    }

    @Override
    public MemberwiseAskAdapter.MemberAskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.member_wise_ask_item, null);
        MemberwiseAskAdapter.MemberAskViewHolder memberViewHolder = new MemberwiseAskAdapter.MemberAskViewHolder(view);
        return memberViewHolder;
    }

    @Override
    public void onBindViewHolder(final MemberwiseAskAdapter.MemberAskViewHolder holder, final int position) {
        // set the data
        holder.contact_name.setText(memberWiseAskReponses.get(position).getContact_name());
        holder.category.setText(memberWiseAskReponses.get(position).getCategory());
        holder.comanyaneme.setText(memberWiseAskReponses.get(position).getCompany_name());
        holder.contactby.setText("By: "+memberWiseAskReponses.get(position).getFull_name());
        holder.tiame_date_ask.setText(memberWiseAskReponses.get(position).getDay().substring(0,3)+" "+memberWiseAskReponses.get(position).getDate_time());


        if (memberWiseAskReponses.get(position).getConnected_by_user_id() == 0){
            holder.i_can_connect_ask.setText("I Can Connect");
            holder.i_can_connect_ask_name.setVisibility(View.GONE);
        }else {
            holder.i_can_connect_ask.setVisibility(View.GONE);
            holder.i_can_connect_ask_name.setVisibility(View.VISIBLE);
            holder.i_can_connect_ask_name.setText("Cnt By: "+memberWiseAskReponses.get(position).getCb_name());
            holder.relative_Layout_ask_w.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }

        holder.i_can_connect_ask_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Home.swipeRefreshLayout.isRefreshing()) {

                    ConnectList.allAskResponses1.clear();
                    ConnectList.allAskResponses1.addAll(memberWiseAskReponses);

                    ConnectList likeListView = new ConnectList();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putString("tag", "W");
                    likeListView.setArguments(bundle);
                    ((MainActivity) context).loadFragment(likeListView, true);
                }
            }
        });

        holder.i_can_connect_ask.setOnClickListener(new View.OnClickListener() {
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
        return memberWiseAskReponses.size(); // size of the list items
    }

    class MemberAskViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        private TextView contact_name, category,comanyaneme,contactby,tiame_date_ask,i_can_connect_ask,i_can_connect_ask_name;
        LinearLayout relative_Layout_ask_w;
        public MemberAskViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            contact_name =itemView.findViewById(R.id.contact_name_member_wise_ask);
            comanyaneme =itemView.findViewById(R.id.comany_name_member_wise_ask);
            category =itemView.findViewById(R.id.category_member_wise_ask);
            contactby =itemView.findViewById(R.id.contact_by_member_wise_ask);
            tiame_date_ask=itemView.findViewById(R.id.tiame_date_ask);
            i_can_connect_ask=itemView.findViewById(R.id.i_can_connect_ask);
            i_can_connect_ask_name=itemView.findViewById(R.id.i_can_connect_ask_name);
            relative_Layout_ask_w=itemView.findViewById(R.id.relative_Layout_ask_w);
        }
    }

    public void updateConnect(final MemberwiseAskAdapter.MemberAskViewHolder holder, final int position) {


        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().iCanConnect(String.valueOf(memberWiseAskReponses.get(position).getG_a_id()),
                String.valueOf(memberWiseAskReponses.get(position).getUser_id()),
                MainActivity.userId,
                new Callback<ICanConnectResponse>() {
                    @Override
                    public void success(ICanConnectResponse iCanConnectRespons, Response response) {


                        if (iCanConnectRespons.getSuccess().equalsIgnoreCase("true")) {
                            Config.showCustomAlertDialog(context,
                                    "You Connect "+memberWiseAskReponses.get(position).getContact_name(),
                                    "Connected",
                                    SweetAlertDialog.SUCCESS_TYPE);
                            holder.i_can_connect_ask.setVisibility(View.GONE);
                            holder.i_can_connect_ask_name.setVisibility(View.VISIBLE);
                            holder.i_can_connect_ask_name.setText("Cnt By: "+iCanConnectRespons.getMessage());
                            holder.relative_Layout_ask_w.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                           // ((MainActivity) context).loadFragment(new MemberDetail(), false);
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
