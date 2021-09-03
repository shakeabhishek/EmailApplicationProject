package com.test.EmailApplicationProject.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class UserEmailMapping {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id", updatable = false, nullable = false)
    private UUID id;

    @OneToMany
    @JoinColumn(name = "EMAIL_DETAILS_ID", referencedColumnName = "ID")
    private UUID emailDetailsId; // Id from the EmailDetails entity
    private UUID toUser;
    private UUID fromUser;
    private int status; // 0 -> Active (not deleted), 1 -> Deleted

    public UserEmailMapping() {
    }

    public UserEmailMapping(UUID emailId, UUID to, UUID from, int status) {
        this.emailDetailsId = emailId;
        this.toUser = to;
        this.fromUser = from;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEmailDetailsId() {
        return emailDetailsId;
    }

    public void setEmailDetailsId(UUID emailDetailsId) {
        this.emailDetailsId = emailDetailsId;
    }

    public UUID getToUser() {
        return toUser;
    }

    public void setToUser(UUID toUser) {
        this.toUser = toUser;
    }

    public UUID getFromUser() {
        return fromUser;
    }

    public void setFromUser(UUID fromUser) {
        this.fromUser = fromUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
