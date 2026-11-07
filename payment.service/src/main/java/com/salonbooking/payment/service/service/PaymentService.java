package com.salonbooking.payment.service.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.salonbooking.payment.service.domain.PaymentMethod;
import com.salonbooking.payment.service.dto.BookingDTO;
import com.salonbooking.payment.service.dto.UserDTO;
import com.salonbooking.payment.service.modal.PaymentOrder;
import com.salonbooking.payment.service.payload.response.PaymentLinkResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.apache.catalina.User;

public interface PaymentService {

    PaymentLinkResponse createPaymentOrder(UserDTO userDTO,
                                           BookingDTO bookingDTO,
                                           PaymentMethod paymentMethod) throws RazorpayException, StripeException;
    PaymentOrder getPaymentOrderById(Long Id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentOrderId(String paymentOrderId);

    PaymentLink createRazorpayPaymentLink(UserDTO userDTO,
                                          Long amount,
                                          Long orderId) throws RazorpayException;

    Session createStripePaymentLink(UserDTO userDTO,
                                    Long amount,
                                    Long orderId) throws StripeException;


    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentLinkId) throws RazorpayException, StripeException;

}
