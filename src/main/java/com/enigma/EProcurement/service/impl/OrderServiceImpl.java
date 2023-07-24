package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Order;
import com.enigma.EProcurement.entity.OrderDetail;
import com.enigma.EProcurement.entity.ProductPrice;
import com.enigma.EProcurement.entity.Vendor;
import com.enigma.EProcurement.model.request.OrderRequest;
import com.enigma.EProcurement.model.response.OrderDetailResponse;
import com.enigma.EProcurement.model.response.OrderResponse;
import com.enigma.EProcurement.model.response.ProductResponse;
import com.enigma.EProcurement.model.response.VendorResponse;
import com.enigma.EProcurement.repository.OrderRepository;
import com.enigma.EProcurement.service.OrderService;
import com.enigma.EProcurement.service.ProductPriceService;
import com.enigma.EProcurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductPriceService productPriceService;
    private final VendorService vendorService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse createNewTransaction(OrderRequest request) {
        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(orderDetailRequest -> {
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());


            return OrderDetail.builder()
                    .productPrice(productPrice)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());


        Order order = Order.builder()
                .orderDetails(orderDetails)
                .build();

        orderRepository.saveAndFlush(order);

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDetails(getOrderDetailResponses(order.getOrderDetails()))
                .build();
    }

    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDetails(getOrderDetailResponses(order.getOrderDetails()))
                .build();
    }

    @Override
    public List<OrderResponse> getAllTransaction(String vendorName) {
        List<Order> orders;
        if (vendorName != null) {
            // Jika vendorName tidak null, ambil data order dengan nama vendor tertentu
            VendorResponse vendor = vendorService.getVendorByName(vendorName); // Ganti dengan implementasi yang sesuai
            orders = orderRepository.findByOrderDetails_ProductPrice_Vendor(vendor);
        } else {
            // Jika vendorName null, ambil semua data order
            orders = orderRepository.findAll();
        }

        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .orderDetails(getOrderDetailResponses(order.getOrderDetails()))
                        .build())
                .collect(Collectors.toList());
    }


    private List<OrderDetailResponse> getOrderDetailResponses(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(orderDetail -> {
                    ProductPrice currentProductPrice = orderDetail.getProductPrice();

                    return OrderDetailResponse.builder()
                            .quantity(orderDetail.getQuantity())
                            .product(ProductResponse.builder()
                                    .id(currentProductPrice.getProduct().getId())
                                    .productName(currentProductPrice.getProduct().getName())
                                    .description(currentProductPrice.getProduct().getDescription())
                                    .price(currentProductPrice.getPrice())
                                    .vendor(VendorResponse.builder()
                                            .id(currentProductPrice.getVendor().getId())
                                            .name(currentProductPrice.getVendor().getName())
                                            .mobilePhone(currentProductPrice.getVendor().getMobilePhone())
                                            .build())
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
