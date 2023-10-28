package com.service.profile.utility;

import com.service.profile.profiles.Guardian;
import com.service.profile.profiles.User;
import org.springframework.mail.SimpleMailMessage;

public class MailComposer {

    public static SimpleMailMessage composeReqIdMailForForgotPasswordForGuardian(Guardian guardian){
        String text = "Dear "+guardian.getName()+", \n"
                +"\n"
                +"Your request to reset your password is under process. Following is your request Id. \n"
                +"Req ID: "+guardian.getPasswordResetReqId()+"\n"
                +"Please enter your email, request Id and new password on reset password API.\n"
                +"\n"
                +"Please take appropriate action if the request was not made by you.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(text);
        message.setSubject("Password reset request!!");
        message.setFrom("hungryllama.service@gmail.com");
        message.setTo(guardian.getEmail());

        return message;
    }
    public static SimpleMailMessage composeReqIdMailForForgotPasswordForUser(User user){
        String text = "Dear "+user.getName()+", \n"
                +"\n"
                +"Your request to reset your password is under process. Following is your request Id. \n"
                +"Req ID: "+user.getPasswordResetReqId()+"\n"
                +"Please enter your email, request Id and new password on reset password API.\n"
                +"\n"
                +"Please take appropriate action if the request was not made by you.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(text);
        message.setSubject("Password reset request!!");
        message.setFrom("hungryllama.service@gmail.com");
        message.setTo(user.getEmail());

        return message;
    }
}
