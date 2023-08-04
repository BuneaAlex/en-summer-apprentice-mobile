package com.example.tms.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.tms.components.EventItem;
import com.example.tms.components.ExpandableCardView;
import com.example.tms.components.PurchaseItem;
import com.example.tms.R;
import com.example.tms.model.dtos.EventDTO;
import com.example.tms.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<EventDTO> events;

    private EditText eventNameEditText;

    private ConstraintLayout eventsConstraintLayout;

    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        eventNameEditText = findViewById(R.id.event_name_search);
        apiService = new ApiService();
        events = new ArrayList<>();
        eventNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed. You can perform actions here if needed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed. You can perform actions here if needed.
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterEvents();
            }
        });

        setSupportActionBar(toolbar);


        String title = "Ticket Management";
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
        getSupportActionBar().setLogo(R.drawable.local_activity);

        //displayedAllEvents = createSample();
        eventsConstraintLayout = findViewById(R.id.events_constraint_layout);

        getAllEvents();
    }

    private void displayEvents(List<EventDTO> displayedEvents)
    {
        eventsConstraintLayout.removeAllViews();
        for (int i = 0; i < displayedEvents.size(); i++) {
            if (i == 0) {
                addCardViewToLayout(eventsConstraintLayout, displayedEvents.get(i), 100); // Lower the first CardView
            } else {
                addCardViewToLayout(eventsConstraintLayout, displayedEvents.get(i), 8); // Use smaller margin for subsequent CardViews
            }
        }
    }

    private void filterEvents() {

        List<EventDTO> filteredEvents = new ArrayList<>();
        String text = eventNameEditText.getText().toString();
        if(text.equals(""))
        {
            displayEvents(events);
            return;
        }

        for(EventDTO e : events)
        {
            if(e.getName().toLowerCase().contains(text.toLowerCase()))
                filteredEvents.add(e);
        }
        displayEvents(filteredEvents);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.orders_item) {

            Intent intent = new Intent(this, OrdersActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addCardViewToLayout(ConstraintLayout layout, EventDTO event,int topMargin) {
        CardView cardView = new CardView(this);
        cardView.setId(View.generateViewId()); // Set a unique ID for the CardView

        // Set CardView attributes programmatically
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, topMargin, 0, 8);
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(20);
        cardView.setElevation(4); // in pixels, adjust as needed
        cardView.setContentPadding(8, 8, 8, 8); // in pixels, adjust as needed

        ExpandableCardView expandableCardView = new ExpandableCardView(this);
        cardView.addView(expandableCardView);


        EventItem eventItem = new EventItem(this,event);
        expandableCardView.getInformationLayout().addView(eventItem);

        PurchaseItem purchaseItem = new PurchaseItem(this,event);
        expandableCardView.getExpandableContentLayout().addView(purchaseItem);

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

    private void getAllEvents() {

        apiService.getEvents(null,null, new Callback<List<EventDTO>>() {

            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here

                    events = response.body();
                    displayEvents(events);
                    Log.i("content order",events.get(0).toString());

                } else {
                    // Handle unsuccessful response here
                    Log.i("unsuccesful","nope");
                }
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                // Handle network errors or failures here
                Log.i("Failure",t.getMessage());
            }
        });

    }


}