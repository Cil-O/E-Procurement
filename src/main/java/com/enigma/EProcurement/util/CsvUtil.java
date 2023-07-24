package com.enigma.EProcurement.util;



import com.enigma.EProcurement.model.response.OrderDetailCSV;
import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvUtil {

    public static byte[] exportToCsv(List<OrderDetailCSV> orderDetails) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(baos))) {

            String[] headers = {"Product ID", "Order Date", "Vendor Name", "Product Name", "Category", "Product Price", "Quantity", "Total Amount"};
            csvWriter.writeNext(headers);

            for (OrderDetailCSV orderDetail : orderDetails) {
                String[] data = {
                        orderDetail.getProductId(),
                        orderDetail.getOrderDate(),
                        orderDetail.getVendorName(),
                        orderDetail.getProductName(),
                        orderDetail.getCategory(),
                        String.valueOf(orderDetail.getProductPrice()),
                        String.valueOf(orderDetail.getQuantity()),
                        String.valueOf(orderDetail.getTotalAmount())
                };
                csvWriter.writeNext(data);
            }

            csvWriter.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}