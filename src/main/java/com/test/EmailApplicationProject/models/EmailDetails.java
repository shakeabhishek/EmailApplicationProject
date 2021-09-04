package com.test.EmailApplicationProject.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class EmailDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "FROM_USER_ID", referencedColumnName = "ID")
    private User fromUserId;
    private String subject;
    private String body;

    public EmailDetails() {
    }

    public EmailDetails(User fromUserId, String subject, String body) {
        this.fromUserId = fromUserId;
        this.subject = subject;
        this.body = body;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(User fromUser) {
        this.fromUserId = fromUser;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
