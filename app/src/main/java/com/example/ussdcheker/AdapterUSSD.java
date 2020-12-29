package com.example.ussdcheker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterUSSD extends RecyclerView.Adapter<AdapterUSSD.ViewHolder> {

    private final List<USSDItem> ussdItemList;
    private final USSDItemListener mListener;
    private Context context;


    public AdapterUSSD(Context context, List<USSDItem> policeStationList, USSDItemListener listener) {
        this.ussdItemList = policeStationList;
        mListener = listener;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ussd_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = ussdItemList.get(position);



        String description = holder.mItem.getDescription();
        String ussd = holder.mItem.getUssd();


        holder.tvDescription.setText(description);
        holder.tvUSSD.setText(ussd);


        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: " );
                Toast.makeText(holder.mView.getContext(), "Will edit", Toast.LENGTH_SHORT).show();
                mListener.onUSSDItemEditClicked(holder.mItem, USSDItem.USSD_ITEM_DIALOG_OPERATION.EDIT);
            }
        });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.onUSSDItemListener(holder.mItem);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return ussdItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView, ivEdit;
        public final TextView tvDescription, tvUSSD;

        public USSDItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivEdit = view.findViewById(R.id.ivEdit);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvUSSD = view.findViewById(R.id.tvUSSD);


        }


    }

    public interface USSDItemListener {

        void onUSSDItemListener(USSDItem ussdItem);

        void onUSSDItemEditClicked(USSDItem mItem, USSDItem.USSD_ITEM_DIALOG_OPERATION operation);
    }
}
