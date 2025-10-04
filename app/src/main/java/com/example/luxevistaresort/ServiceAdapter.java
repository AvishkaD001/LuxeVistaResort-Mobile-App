package com.example.luxevistaresort;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;
    private final Context context;

    public ServiceAdapter(List<Service> serviceList, Context context) {
        this.serviceList = serviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);

        // Bind data to the views
        holder.serviceType.setText(service.getServiceType());
        holder.serviceDescription.setText(service.getDescription());
        holder.servicePrice.setText("$" + service.getPrice());

        // Handle image loading with Glide and add error handling
        Glide.with(context)
                .load(service.getImageUrl())
                .placeholder(R.drawable.cabana)  // Default placeholder
                .error(R.drawable.cabana)       // Fallback image if the URL is invalid
                .into(holder.serviceImage);

        // Set up the button click listener
        holder.bookServiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReservationActivity.class);
            intent.putExtra("SERVICE_TYPE", service.getServiceType());
            intent.putExtra("SERVICE_DESCRIPTION", service.getDescription());
            intent.putExtra("SERVICE_PRICE", service.getPrice());
            context.startActivity(intent);
        });
    }

    public void updateServices(List<Service> newServices) {
        this.serviceList = newServices;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceType, serviceDescription, servicePrice;
        ImageView serviceImage;
        Button bookServiceButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceType = itemView.findViewById(R.id.ServiceType);
            serviceDescription = itemView.findViewById(R.id.ServiceDescription);
            servicePrice = itemView.findViewById(R.id.ServicePrice);
            serviceImage = itemView.findViewById(R.id.ServiceImage);
            bookServiceButton = itemView.findViewById(R.id.bookServiceButton);
        }
    }
}
