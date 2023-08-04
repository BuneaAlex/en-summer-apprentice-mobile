package com.example.tms.service;

import androidx.annotation.NonNull;

import com.example.tms.model.dtos.EventDTO;
import com.example.tms.model.dtos.OrderDTO;
import com.example.tms.model.dtos.OrderRequest;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "http://10.0.2.2:8080/management/";
    private final IApiService apiServiceInterface;

    public ApiService() {
        Interceptor interceptor = new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        };

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient) // Add the custom OkHttpClient with logging interceptor
                .build();

        apiServiceInterface = retrofit.create(IApiService.class);
    }

    public void getEventById(int id,Callback<EventDTO> callback)
    {
        Call<EventDTO> call = apiServiceInterface.getEventById(id);
        call.enqueue(callback);
    }

    public void getEvents(String venueType, String eventType, Callback<List<EventDTO>> callback) {
        Call<List<EventDTO>> call = apiServiceInterface.getEvents(venueType, eventType);
        call.enqueue(callback);
    }

    public void getOrders(int customerId,Callback<List<OrderDTO>> callback)
    {
        Call<List<OrderDTO>> call = apiServiceInterface.getOrders(customerId);
        call.enqueue(callback);
    }

    public void saveOrder(OrderRequest orderRequest,Callback<OrderDTO> callback)
    {
        Call<OrderDTO> call = apiServiceInterface.saveOrder(orderRequest);
        call.enqueue(callback);
    }
}
