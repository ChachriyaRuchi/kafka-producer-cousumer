package com.ruchi.paytmproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruchi.paytmproducer.dto.PaymentRequest;
import com.ruchi.paytmproducer.dto.PaytmRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
public class PaytmController {
    @Value("${paytm.producer.topic.name}")
    private String topicName;
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @GetMapping("/publish/{message}")
    public void sendMessage(@PathVariable String message){
        kafkaTemplate.send(topicName,message);
    }
    @PostMapping("/paytm/payment")
    public String doPayment(@RequestBody PaytmRequest<PaymentRequest> paytmRequest) throws JsonProcessingException {
        PaymentRequest request=paytmRequest.getPayload();
        request.setTransactionId(UUID.randomUUID().toString());
        request.setTxDate(new Date());
    //    kafkaTemplate.send(topicName, new ObjectMapper().writeValueAsString(request));
        kafkaTemplate.send(topicName,request);
        return "payment instantiated successfully";

    }
}
