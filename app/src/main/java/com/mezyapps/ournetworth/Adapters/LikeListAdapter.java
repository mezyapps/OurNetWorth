package com.mezyapps.ournetworth.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezyapps.ournetworth.MVP.LikeListResponse;
import com.mezyapps.ournetworth.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.MemberViewHolder> {

    Context context;
    List<LikeListResponse> memberList;

    public LikeListAdapter(Context context, List<LikeListResponse> memberList) {
        this.memberList = memberList;
        this.context = context;
    }

    @Override
    public LikeListAdapter.MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.member_list_items, null);
        LikeListAdapter.MemberViewHolder memberViewHolder = new LikeListAdapter.MemberViewHolder(view);
        return memberViewHolder;
    }

    @Override
    public void onBindViewHolder(LikeListAdapter.MemberViewHolder holder, final int position) {
        // set the data
        holder.name.setText(memberList.get(position).getFull_name());
        holder.category.setText(memberList.get(position).getCategory());
        holder.mobileno_re.setText(memberList.get(position).getMobile());
        holder.company_re.setText(memberList.get(position).getCompany_name());
        try {
            Picasso.with(context)
                    .load(memberList.get(position).getImage())
                    // .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.imageView_re);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }

//
//        holder.mobileno_re.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, memberList.get(position).getMobile(), Toast.LENGTH_SHORT).show();
//            }
//        });
        // implement setONCLickListtener on itemView
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // display a toast with user name
//                if (!Home.swipeRefreshLayout.isRefreshing()) {
//                    MemberDetail.memberList.clear();
//                    MemberDetail.memberList.addAll(memberList);
//
//                    MemberDetail memberDetail = new MemberDetail();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("position", position);
//                    memberDetail.setArguments(bundle);
//                    ((MainActivity) context).loadFragment(memberDetail, true);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return memberList.size(); // size of the list items
    }



    class MemberViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name, category,mobileno_re,company_re;
        ImageView imageView_re;

        public MemberViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name =itemView.findViewById(R.id.name_re);
            category =itemView.findViewById(R.id.category_re);
            mobileno_re =itemView.findViewById(R.id.mobile_no_re);
            imageView_re =itemView.findViewById(R.id.profileimage_re);
            company_re=itemView.findViewById(R.id.company_re);
        }
    }
}
