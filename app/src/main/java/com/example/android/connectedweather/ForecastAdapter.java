package com.example.android.connectedweather;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder> {

    private ArrayList<String> mForecastData;
    private ArrayList<String> mForecastIcon;
    private OnForecastItemClickListener mOnForecastItemClickListener;

    public ForecastAdapter(OnForecastItemClickListener onForecastItemClickListener) {
        mOnForecastItemClickListener = onForecastItemClickListener;
    }

    public void updateForecastData(ArrayList<String> forecastData,ArrayList<String>forecastIcon) {
        mForecastData = forecastData;
        mForecastIcon = forecastIcon;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mForecastData != null) {
            return mForecastData.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastItemViewHolder holder, int position) {
        String routeIcon = "https://openweathermap.org/img/w/"+ mForecastIcon.get(position)+ ".png";
        holder.bind(mForecastData.get(position),routeIcon);
    }

    public interface OnForecastItemClickListener {
        void onForecastItemClick(String detailedForecast, String detailedIcon);
    }

    class ForecastItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mForecastTextView;
        private ImageView mForecastImageView;
        private Context context;
        
        public ForecastItemViewHolder(View itemView) {
            super(itemView);
            mForecastTextView = (TextView)itemView.findViewById(R.id.tv_forecast_text);
            mForecastImageView = (ImageView)itemView.findViewById(R.id.tv_forecast_icon);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String detailedForecast = mForecastData.get(getAdapterPosition());
                    String detailedIcon = mForecastIcon.get(getAdapterPosition());
                    mOnForecastItemClickListener.onForecastItemClick(detailedForecast,detailedIcon);
                }
            });
        }

        public void bind(String forecast,String routeIcon) {
            mForecastTextView.setText(forecast);
            Glide.with(context).load(routeIcon).into(mForecastImageView);
        }
    }
}
