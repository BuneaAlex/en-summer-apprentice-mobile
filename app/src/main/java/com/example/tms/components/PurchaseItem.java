package com.example.tms.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tms.R;
import com.example.tms.model.dtos.EventDTO;
import com.example.tms.model.dtos.OrderDTO;
import com.example.tms.model.dtos.OrderRequest;
import com.example.tms.model.dtos.TicketCategoryDTO;
import com.example.tms.service.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseItem extends ConstraintLayout {
    private Spinner customSpinner;
    private TextView priceLabel;
    private EditText editTextNumber;
    private Button purchaseButton;

    private EventDTO event;

    private ApiService apiService;

    public PurchaseItem(Context context,EventDTO event) {
        super(context);
        this.event = event;
        init(context);
        setupSpinnerAdapter();
    }

    public PurchaseItem(Context context, AttributeSet attrs,EventDTO event) {
        super(context, attrs);
        this.event = event;
        init(context);
        setupSpinnerAdapter();
    }

    public PurchaseItem(Context context, AttributeSet attrs, int defStyleAttr,EventDTO event) {
        super(context, attrs, defStyleAttr);
        this.event = event;
        init(context);
        setupSpinnerAdapter();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.buy_ticket_item, this);

        customSpinner = findViewById(R.id.custom_spinner);
        editTextNumber = findViewById(R.id.editTextNumber);
        purchaseButton = findViewById(R.id.purchase_button);
        priceLabel = findViewById(R.id.price_label);
        purchaseButton.setOnClickListener(v -> confirmPurchaseButtonAction());
        apiService = new ApiService();
        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearDataInserted();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editTextNumber.addTextChangedListener(new TextWatcher() {
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
                updatePrice();
            }
        });

    }

    private void setupSpinnerAdapter() {
        List<String> ticketCategoryDescriptions = new ArrayList<>();
        for (TicketCategoryDTO t : event.getTicketCategories()) {
            ticketCategoryDescriptions.add(t.getDescription());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                ticketCategoryDescriptions
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(spinnerAdapter);
    }

    private void clearDataInserted() {
        priceLabel.setText("Price:");
        editTextNumber.getText().clear();
    }

    private void updatePrice()
    {
        float currentPrice = 0;
        String ticketType = customSpinner.getSelectedItem().toString();
        Optional<TicketCategoryDTO> optionalTicketCategoryDTO = getTicketCategory(ticketType);
        String rez = editTextNumber.getText().toString();
        if(!rez.equals(""))
        {
            int numberOfTickets = Integer.parseInt(rez);
            if(optionalTicketCategoryDTO.isPresent())
            {
                currentPrice = numberOfTickets * optionalTicketCategoryDTO.get().getPrice();
                priceLabel.setText("Price:" + String.valueOf(currentPrice));
            }
        }

    }

    private void confirmPurchaseButtonAction() {
        String ticketType = customSpinner.getSelectedItem().toString();
        String numberOfTicketsStr = editTextNumber.getText().toString();
        if(numberOfTicketsStr.equals(""))
        {
            Toast.makeText(getContext(), "Enter number of tickets", Toast.LENGTH_LONG).show();
            return;
        }
        int numberOfTickets = Integer.parseInt(numberOfTicketsStr);
        Optional<TicketCategoryDTO> optionalTicketCategoryDTO = getTicketCategory(ticketType);
        int ticketCategoryId = -1;
        if(optionalTicketCategoryDTO.isPresent())
            ticketCategoryId = optionalTicketCategoryDTO.get().getId();
        OrderRequest orderRequest = new OrderRequest(3, event.getEventID(),ticketCategoryId,numberOfTickets);
        Log.i("order request",orderRequest.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm purchase")
                .setMessage("Are you sure you want to confirm purchase?" + orderRequest.toString())
                .setPositiveButton("OK", (dialog, which) -> {
                    apiService.saveOrder(orderRequest, new Callback<OrderDTO>() {
                        @Override
                        public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {

                            if (response.isSuccessful()) {
                                // Handle successful response here
                                OrderDTO orderAdded = response.body();
                                Toast.makeText(getContext(), orderAdded.toString(), Toast.LENGTH_LONG).show();
                                Log.i("order add",orderAdded.toString());

                            } else {
                                // Handle unsuccessful response here
                                Log.i("unsuccesful",response.toString());
                                //Toast.makeText(getContext(), orderRequest.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderDTO> call, Throwable t) {
                            Log.i("failure","nope");
                        }
                    });

                    dialog.dismiss(); // Close the dialog
                })
                .setNegativeButton("Cancel",(dialog, which) -> {dialog.dismiss();}) // Prevent dismissing the dialog by clicking outside or pressing back
                .create()
                .show();




    }

    private Optional<TicketCategoryDTO> getTicketCategory(String ticketType) {
        for(TicketCategoryDTO t : event.getTicketCategories())
        {
            if(ticketType.equals(t.getDescription()))
                return Optional.of(t);
        }
        return Optional.empty();
    }


}

