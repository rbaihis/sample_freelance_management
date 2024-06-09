package com.example.user.services.freelance.utils.sms;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class TwilioSmsService  implements ISmsService{

    private final String accountSid;
    private final String authToken;
    private final String fromNumber;

    @Autowired
    public TwilioSmsService(
            @Value("${twilio.account.sid}") String accountSid,
            @Value("${twilio.auth.token}") String authToken,
            @Value("+13309131521") String fromNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromNumber = fromNumber;
    }

    @Override
    public void sendSingleSms(String toNumber, String message) {
        Twilio.init(accountSid, authToken);

        Message messageSent = Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(fromNumber),
                message).create();

        System.out.println(messageSent.getSid());
    }



}
