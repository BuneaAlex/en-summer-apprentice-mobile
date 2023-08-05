package com.example.tms.service;

import androidx.annotation.Nullable;

import com.example.tms.model.dtos.EventDTO;
import com.example.tms.model.dtos.OrderDTO;
import com.example.tms.model.dtos.OrderRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiService {

    @GET("events/{id}")
    Call<EventDTO> getEventById(@Path("id") int id);

    @GET("events")
    Call<List<EventDTO>> getEvents(@Nullable @Query("venueType") String venueType,@Nullable @Query("eventType") String eventType);

    @GET("orders")
    Call<List<OrderDTO>> getOrders(@Query("customerId") int customerId);

    @POST("orders")
    Call<OrderDTO> saveOrder(@Body OrderRequest orderRequest);
}
