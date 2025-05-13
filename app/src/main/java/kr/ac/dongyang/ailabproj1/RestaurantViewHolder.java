package kr.ac.dongyang.ailabproj1;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    ImageView restaurantIcon;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        restaurantIcon = itemView.findViewById(R.id.restaurantIcon);
    }
}
