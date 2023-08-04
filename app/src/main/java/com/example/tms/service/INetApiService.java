package com.example.tms.service;

import com.example.tms.model.dtos.OrderDTO;
import com.example.tms.model.dtos.OrderPatchRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface INetApiService {

    @PATCH("orders/{id}")
    Call<OrderDTO> updateOrder(@Path("id") int orderId, @Body OrderPatchRequest orderPatchRequest);

    @DELETE("orders/{id}")
    Call<OrderDTO> deleteOrder(@Path("id") int orderId);
}
