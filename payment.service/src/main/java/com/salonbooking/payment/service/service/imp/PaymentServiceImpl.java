package com.salonbooking.payment.service.service.imp;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.salonbooking.payment.service.domain.PaymentMethod;
import com.salonbooking.payment.service.domain.PaymentOrderStatus;
import com.salonbooking.payment.service.dto.BookingDTO;
import com.salonbooking.payment.service.dto.UserDTO;
import com.salonbooking.payment.service.messaging.BookingEventProducer;
import com.salonbooking.payment.service.messaging.NotificationEventProducer;
import com.salonbooking.payment.service.modal.PaymentOrder;
import com.salonbooking.payment.service.payload.response.PaymentLinkResponse;
import com.salonbooking.payment.service.repository.PaymentOrderRepository;
import com.salonbooking.payment.service.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final BookingEventProducer  bookingEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.api.secret}")
    private String stripeApiSecret;

    @Value("${razorpay.api.key}")
    private String razorpayApiKey;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    @Override
    public PaymentLinkResponse createPaymentOrder(UserDTO userDTO, BookingDTO bookingDTO, PaymentMethod paymentMethod) throws RazorpayException, StripeException {

        Long amount = (long) bookingDTO.getTotalPrice();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setBookingId(bookingDTO.getId());
        paymentOrder.setSalonId(bookingDTO.getSalonId());
        paymentOrder.setUserId(userDTO.getId());

        PaymentOrder savedPaymentOrder = paymentOrderRepository.save(paymentOrder);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink paymentLink = createRazorpayPaymentLink(userDTO,
                    savedPaymentOrder.getAmount(),
                    savedPaymentOrder.getId());

            String paymentUrl = paymentLink.get("short_url");
            String paymentUrlId = paymentLink.get("id");

            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
            paymentLinkResponse.setPaymentLinkId(paymentUrlId);

            savedPaymentOrder.setPaymentLinkId(paymentUrlId);

            paymentOrderRepository.save(savedPaymentOrder);
        } else if(paymentMethod.equals(PaymentMethod.STRIPE)){
            Session session = createStripePaymentLink(userDTO,
                    savedPaymentOrder.getAmount(),
                    savedPaymentOrder.getId());
            String paymentUrl = session.getUrl();
            String paymentSessionId = session.getId();

            System.out.println("Stripe session id: " + paymentSessionId);

            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
            paymentLinkResponse.setPaymentLinkId(paymentSessionId);

            System.out.println("Stripe session id: " + paymentLinkResponse.getPaymentLinkId());

            savedPaymentOrder.setPaymentLinkId(paymentSessionId);
            paymentOrderRepository.save(savedPaymentOrder);
        }

        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long Id) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findById(Id).orElse(null);

        if(paymentOrder == null){
            throw new Exception("Payment order not found");
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentOrderId(String paymentOrderId) {

        return paymentOrderRepository.findByPaymentLinkId(paymentOrderId);
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO userDTO,
                                                 Long amount,
                                                 Long orderId) throws RazorpayException {
        Long paiseAmount = amount * 100;

        RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey,razorpayApiSecret);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");

        JSONObject customerRequest = new JSONObject();
        customerRequest.put("name",userDTO.getFullName());
        customerRequest.put("email",userDTO.getEmail());

        paymentLinkRequest.put("customer",customerRequest);

        JSONObject notify = new JSONObject();
        notify.put("email",true);

        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);

        paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success" + orderId);
        
        paymentLinkRequest.put("callback_method","get");

        return razorpayClient.paymentLink.create(paymentLinkRequest);
    }

    @Override
    public Session createStripePaymentLink(UserDTO userDTO,
                                          Long amount,
                                          Long orderId) throws StripeException {
        Stripe.apiKey = stripeApiSecret;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment-success/" + orderId + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:5173/payment-cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder().setName("Salon appointment booking").build()

                                ).build()
                        ).build()
                ).build();

        return Session.create(params);
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder,
                                       String paymentLinkId) throws RazorpayException, StripeException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecret);
                Payment payment = razorpayClient.payments.fetch(paymentLinkId);
                Integer amount =  payment.get("amount");
                String status = payment.get("status");
                if(status.equals("captured")){
//                  produce RabbitMQ event to send message to notify user
                    return markPaymentSuccess(paymentOrder);
                }
                return false;
            }
//            Is that right for check status for stripe ?
            else if(paymentOrder.getPaymentMethod().equals(PaymentMethod.STRIPE)){
                Stripe.apiKey = stripeApiSecret;
                String stripeSessionId = (paymentOrder.getPaymentLinkId() != null)
                        ? paymentOrder.getPaymentLinkId()
                        : paymentLinkId;

                if (stripeSessionId == null) {
                    return false;
                }
                Session session = Session.retrieve(stripeSessionId);

                if ("paid".equalsIgnoreCase(session.getPaymentStatus())) {
                    return markPaymentSuccess(paymentOrder);
                }
            }
        }
        return false;
    }

    private Boolean markPaymentSuccess(PaymentOrder paymentOrder){
        bookingEventProducer.sentBookingUpdateEvent(paymentOrder);
        notificationEventProducer.sentNotification(
                paymentOrder.getBookingId(),
                paymentOrder.getUserId(),
                paymentOrder.getSalonId()
        );
        paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
        paymentOrderRepository.save(paymentOrder);
        return true;
    }
}
