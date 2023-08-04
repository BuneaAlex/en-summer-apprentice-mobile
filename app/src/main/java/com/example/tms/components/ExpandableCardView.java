package com.example.tms.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tms.R;

public class ExpandableCardView extends CardView {
    private ConstraintLayout expandableContent;
    private ImageButton expandButton;
    private boolean isExpanded = false;

    public ExpandableCardView(Context context) {
        super(context);
        init(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandableCardView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.expandable_cardview_item, this, true);

        expandableContent = findViewById(R.id.expandable_content);

        expandButton = findViewById(R.id.expand_button);
        expandButton.setOnClickListener(v -> toggleExpand());

    }

    public void toggleExpand() {
        if (isExpanded) {
            expandableContent.setVisibility(View.GONE);
            expandButton.setImageResource(R.drawable.double_arrow_down);
        } else {
            expandableContent.setVisibility(View.VISIBLE);
            expandButton.setImageResource(R.drawable.double_arrow_up);
        }
        isExpanded = !isExpanded;
    }

    public ConstraintLayout getExpandableContentLayout()
    {
        return findViewById(R.id.expandable_content);
    }

    public ConstraintLayout getCardContentLayout()
    {
        return findViewById(R.id.card_content);
    }

    public ConstraintLayout getInformationLayout()
    {
        return findViewById(R.id.custom_event_item);
    }

    public ImageButton getExpandButton(){
        return findViewById(R.id.expand_button);
    }

}
