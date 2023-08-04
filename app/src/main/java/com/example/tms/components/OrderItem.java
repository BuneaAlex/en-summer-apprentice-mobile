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
import com.example.tms.model.dtos.OrderPatchRequest;
import com.example.tms.model.dtos.TicketCategoryDTO;
import com.example.tms.service.NetApiService;
import com.example.tms.utils.Observable;
import com.example.tms.utils.Observer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItem extends ConstraintLayout implements Observable {

    private TextView orderInformationTextView;
    private Button deleteButton;
    private Button updateButton;
    private Spinner ticketCategoryTypeSpinner;
    private EditText ticketNumberEditText;

    private OrderDTO order;

    private EventDTO event;

    private NetApiService netApiService;

    private int numberOfModificationForOrder = 0;

    private List<Observer> observers=new ArrayList<>();

    public OrderItem(Context context, OrderDTO order, EventDTO event) {
        super(context);
        this.order = order;
        this.event = event;
        init(context);
        setupSpinnerAdapter();
        uploadOrderInformation();
    }

    public OrderItem(Context context, AttributeSet attrs, OrderDTO order, EventDTO event) {
        super(context, attrs);
        this.order = order;
        this.event = event;
        init(context);
        setupSpinnerAdapter();
        uploadOrderInformation();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_actions_layout, this, true);

        deleteButton = findViewById(R.id.custom_button);
        ticketCategoryTypeSpinner = findViewById(R.id.custom_spinner);
        ticketNumberEditText = findViewById(R.id.custom_integer_edit_text);
        orderInformationTextView = findViewById(R.id.order_information);
        updateButton = findViewById(R.id.update_button);
        updateButton.setVisibility(GONE);
        netApiService = new NetApiService();

        // Set up the spinner options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.custom_spinner_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ticketCategoryTypeSpinner.setAdapter(adapter);
        deleteButton.setOnClickListener(v -> deleteOrder());
        updateButton.setOnClickListener(v -> updateOrder());

        ticketCategoryTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTicketCategory(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ticketNumberEditText.addTextChangedListener(new TextWatcher() {
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
                updateTicketNumber();
            }
        });
    }

    private void changeTicketCategory(int position) {
        Log.i("my app: pos",String.valueOf(position));

        if(position!=getPositionForTicketCategoryOfOrder())
        {
            numberOfModificationForOrder++;
        }
        else {
            if(numberOfModificationForOrder > 0)
                numberOfModificationForOrder--;
        }
        handleUpdateButtonVisibility();
    }

    private void updateTicketNumber() {
        String numberOfTickets = ticketNumberEditText.getText().toString();
        if(numberOfTickets.equals(""))
        {
            return;
        }

        if(Integer.parseInt(numberOfTickets) != order.getNumberOfTickets())
        {
            numberOfModificationForOrder++;
        }
        else {
            if(numberOfModificationForOrder > 0)
                numberOfModificationForOrder--;
        }
        handleUpdateButtonVisibility();
    }

    private void handleUpdateButtonVisibility()
    {
        if(numberOfModificationForOrder == 0)
            updateButton.setVisibility(GONE);
        else
        {
            updateButton.setVisibility(VISIBLE);
        }
    }


    private void uploadOrderInformation()
    {
        int position = getPositionForTicketCategoryOfOrder();
        if (position != -1) {
            ticketCategoryTypeSpinner.setSelection(position);
        }

        ticketNumberEditText.setText(String.valueOf(order.getNumberOfTickets()));

        String information = "Event:" + event.getName() + " Type: " + event.getEventType() + "\nLocation:" +
                event.getVenue().getLocation() + "\nPeriod: " + event.getStartDate() + " - " + event.getEndDate() + "\nPrice:" + order.getTotalPrice();
        orderInformationTextView.setText(information);
    }

    private int getPositionForTicketCategoryOfOrder() {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) ticketCategoryTypeSpinner.getAdapter();
        int position = adapter.getPosition(order.getTicketCategory().getDescription());
        return position;
    }


    private void deleteOrder() {
        int orderId = order.getOrderID();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete the order?")
                .setPositiveButton("OK", (dialog, which) -> {
                netApiService.deleteOrder(orderId, new Callback<OrderDTO>() {
                    @Override
                    public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                        if (response.isSuccessful()) {
                            // Handle successful response here

                            OrderDTO orderDeleted = response.body();
                            notifyObservers();
                            Log.i("content order",orderDeleted.toString());

                        } else {
                            // Handle unsuccessful response here
                            Log.i("unsuccesful","nope");
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDTO> call, Throwable t) {
                        Log.i("unsuccesful delete",t.getMessage());
                    }
                });

            dialog.dismiss(); // Close the dialog
        })
        .setNegativeButton("Cancel",(dialog, which) -> {dialog.dismiss();})// Prevent dismissing the dialog by clicking outside or pressing back
        .create()
        .show();
    }

    private void updateOrder() {
        String numberOfTickets = ticketNumberEditText.getText().toString();
        if(numberOfTickets.equals(""))
        {
            Toast.makeText(getContext(),"No number of tickets selected",Toast.LENGTH_LONG).show();
            return;
        }
        String ticketCategoryType = ticketCategoryTypeSpinner.getSelectedItem().toString();
        OrderPatchRequest orderPatchRequest = new OrderPatchRequest(Integer.parseInt(numberOfTickets),ticketCategoryType);
        Toast.makeText(getContext(),orderPatchRequest.toString(),Toast.LENGTH_LONG).show();

        int orderId = order.getOrderID();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Update")
                .setMessage("Are you sure you want to update the order?")
                .setPositiveButton("OK", (dialog, which) -> {

                    netApiService.updateOrder(orderId, orderPatchRequest, new Callback<OrderDTO>() {
                        @Override
                        public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                            if (response.isSuccessful()) {
                                // Handle successful response here

                                OrderDTO orderUpdated = response.body();
                                notifyObservers();
                                Log.i("content order",orderUpdated.toString());

                            } else {
                                // Handle unsuccessful response here
                                Log.i("unsuccesful","nope");
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderDTO> call, Throwable t) {
                            Log.i("unsuccesful update",t.getMessage());
                        }
                    });

                    dialog.dismiss(); // Close the dialog
                })
                .setNegativeButton("Cancel",(dialog, which) -> {dialog.dismiss();})// Prevent dismissing the dialog by clicking outside or pressing back
                .create()
                .show();

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
        ticketCategoryTypeSpinner.setAdapter(spinnerAdapter);
    }

    // You can provide public methods to access and interact with the views in the custom view
    public Button getDeleteButton() {
        return deleteButton;
    }

    public Spinner getTicketCategoryTypeSpinner() {
        return ticketCategoryTypeSpinner;
    }

    public EditText getTicketNumberEditText() {
        return ticketNumberEditText;
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(obs -> obs.update());
    }
}
