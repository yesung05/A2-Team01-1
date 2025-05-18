package kr.ac.dongyang.ailabproj1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private int[] restaurantIcons;
    private String [] restaurantNames;
    // Constructor
    public RestaurantAdapter(int[] restaurantIcons, String[] restaurantNames) {
        this.restaurantIcons = restaurantIcons;
        this.restaurantNames = restaurantNames;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // item_layout.xml을 인플레이트해서 ViewHolder에 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        // 각 항목에 대한 데이터 바인딩
        holder.restaurantIcon.setImageResource(restaurantIcons[position]);
        holder.restaurantName.setText(restaurantNames[position]);
    }

    @Override
    public int getItemCount() {
        return restaurantIcons.length;
    }
}
