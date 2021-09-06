package com.mobdeve.s15.group.nine.bookbay.email;

import javax.mail.MessagingException;
import java.io.IOException;

public interface GmailService {
    void setGmailCredentials(GmailCredentials gmailCredentials);

    boolean sendMessage(String recipientAddress, String subject, String body) throws MessagingException, IOException;
}