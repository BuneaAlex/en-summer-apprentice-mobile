package com.example.tms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.tms.components.OrderItem;
import com.example.tms.R;
import com.example.tms.model.dtos.EventDTO;
import com.example.tms.model.dtos.OrderDTO;
import com.example.tms.service.ApiService;
import com.example.tms.utils.Observer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity implements Observer {

    private List<OrderDTO> orders;
    private ApiService apiService;

    private ConstraintLayout constraintLayout;

    private ConstraintLayout ordersContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        apiService = new ApiService();
        orders = new ArrayList<>();

        ImageButton myButton = findViewById(R.id.back_button);

        myButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
            startActivity(intent);
        });

        constraintLayout = findViewById(R.id.orders_constraint_layout);
        ordersContainer = findViewById(R.id.orders_container);

        getAllOrdersForCustomer();

    }

    private void displayOrders()  {
        ordersContainer.removeAllViews();
        for (int i = 0; i < orders.size(); i++) {
            OrderDTO order = orders.get(i);
            if (i == 0) {
                apiService.getEventById(order.getEventID(), new Callback<EventDTO>() {
                    @Override
                    public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                        if (response.isSuccessful()) {
                            // Handle successful response here
                            EventDTO eventDTO = response.body();
                            addCardViewToLayout(ordersContainer, order, eventDTO ,100); // Lower the first CardView
                        } else {

                            Log.i("unsuccesful",response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<EventDTO> call, Throwable t) {
                        Log.i("failure",t.getMessage());
                    }
                });

            } else {
                apiService.getEventById(order.getEventID(), new Callback<EventDTO>() {
                    @Override
                    public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                        if (response.isSuccessful()) {
                            // Handle successful response here
                            EventDTO eventDTO = response.body();

                            addCardViewToLayout(ordersContainer, order, eventDTO ,8); // Lower the first CardView
                        } else {
                            Log.i("unsuccesful","nope");
                        }
                    }

                    @Override
                    public void onFailure(Call<EventDTO> call, Throwable t) {

                    }
                });

            }
        }
    }

    private void addCardViewToLayout(ConstraintLayout layout, OrderDTO order,EventDTO event,int topMargin) {
        CardView cardView = new CardView(this);
        cardView.setId(View.generateViewId()); // Set a unique ID for the CardView

        // Set CardView attributes programmatically
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, topMargin, 0, 8);
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(8);
        cardView.setElevation(4); // in pixels, adjust as needed
        cardView.setContentPadding(8, 8, 8, 8); // in pixels, adjust as needed
        //cardView.setCardBackgroundColor(getResources().getColor(R.color.cardBackground)); // Use your desired color

        OrderItem orderItem = new OrderItem(this,order,event);
        orderItem.addObserver(this);
        cardView.addView(orderItem);

        // Add the CardView to the ConstraintLayout
        layout.addView(cardView);

        // Update layout rules for the new CardView
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cardView.getLayoutParams();
        if (layout.getChildCount() == 1) {
            // First CardView, constraint it to the top of the parent
            params.topToTop = layout.getId();
        } else {
            // For subsequent CardViews, constrain them below the previous one
            params.topToBottom = layout.getChildAt(layout.getChildCount() - 2).getId();
        }
        // Constrain the start and end of the new CardView to the start and end of the parent ConstraintLayout
        params.startToStart = layout.getId();
        params.endToEnd = layout.getId();

        cardView.setLayoutParams(params);
    }



    private void getAllOrdersForCustomer() {

        apiService.getOrders(3, new Callback<List<OrderDTO>>() {

            @Override
            public void onResponse(Call<List<OrderDTO>> call, Response<List<OrderDTO>> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here

                    orders = response.body();
                    displayOrders();
                    Log.i("content order",orders.get(0).toString());

                } else {
                    // Handle unsuccessful response here
                    Log.i("unsuccesful","nope");
                }
            }

            @Override
            public void onFailure(Call<List<OrderDTO>> call, Throwable t) {
                // Handle network errors or failures here
                Log.i("Failure",t.getMessage());
            }
        });

    }


    @Override
    public void update() {
        getAllOrdersForCustomer();
    }
}