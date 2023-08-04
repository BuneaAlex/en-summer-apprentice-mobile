package com.example.tms.service;

import androidx.annotation.NonNull;

import com.example.tms.model.dtos.OrderDTO;
import com.example.tms.model.dtos.OrderPatchRequest;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetApiService {
    private static final String BASE_URL = "http://10.0.2.2:7162/management/";
    private final INetApiService apiServiceInterface;

    public NetApiService() {
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

        apiServiceInterface = retrofit.create(INetApiService.class);
    }

    public void updateOrder(int orderId, OrderPatchRequest orderPatchRequest, Callback<OrderDTO> callback)
    {
        Call<OrderDTO> call = apiServiceInterface.updateOrder(orderId,orderPatchRequest);
        call.enqueue(callback);
    }

    public void deleteOrder(int orderId,Callback<OrderDTO> callback)
    {
        Call<OrderDTO> call = apiServiceInterface.deleteOrder(orderId);
        call.enqueue(callback);
    }
}



