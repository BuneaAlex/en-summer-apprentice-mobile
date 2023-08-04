package com.example.tms.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.tms.R;
import com.example.tms.model.dtos.EventDTO;
import com.example.tms.utils.Constants;

public class EventItem extends ConstraintLayout {

    private ImageView eventImageView;
    private TextView eventNameTextView;
    private TextView venueValueTextView;
    private TextView descriptionValueTextView;
    private TextView startDateValueTextView;
    private TextView endDateValueTextView;

    private EventDTO event;


    public EventItem(@NonNull Context context, @Nullable AttributeSet attrs,EventDTO event) {
        super(context, attrs);
        this.event = event;
        init();
    }

    public EventItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr,EventDTO event) {
        super(context, attrs, defStyleAttr);
        this.event = event;
        init();
    }


    public EventItem(@NonNull Context context,EventDTO event) {
        super(context);
        this.event = event;
        init();
        setEventNameValue(event.getName());
        setVenueValue(event.getVenue().getLocation());
        setDescriptionValue(event.getDescription());
        setStartDateValue(event.getStartDate());
        setEndDateValue(event.getEndDate());
    }

    private void init() {
        inflate(getContext(), R.layout.event_item, this);
        eventNameTextView = findViewById(R.id.event_name);
        venueValueTextView = findViewById(R.id.venue_value);
        descriptionValueTextView = findViewById(R.id.description_value);
        startDateValueTextView = findViewById(R.id.start_date_value);
        endDateValueTextView = findViewById(R.id.end_date_value);
        eventImageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load(event.getImage())
                .centerCrop()
                .into(eventImageView);
    }

    public void setEventNameValue(String eventNameValue) {
        eventNameTextView.setText(eventNameValue);
    }

    public void setVenueValue(String venueValue) {
        venueValueTextView.setText(venueValue);
    }

    public void setDescriptionValue(String descriptionValue) {
        descriptionValueTextView.setText(descriptionValue);
    }

    public void setStartDateValue(String startDateValue) {
        startDateValueTextView.setText(startDateValue);
    }

    public void setEndDateValue(String endDateValue) {
        endDateValueTextView.setText(endDateValue);
    }

    public TextView getEventNameTextView() {
        return eventNameTextView;
    }

    public TextView getVenueValueTextView() {
        return venueValueTextView;
    }

    public TextView getDescriptionValueTextView() {
        return descriptionValueTextView;
    }

    public TextView getStartDateValueTextView() {
        return startDateValueTextView;
    }

    public TextView getEndDateValueTextView() {
        return endDateValueTextView;
    }
}
