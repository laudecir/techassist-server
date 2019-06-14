package com.hasckel.techassist.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_service")
public class OrderService extends BaseModel {

    public static enum Status {
        PENDING,
        CANCELED,
        IN_PROGRESS,
        WAITING,
        FINISHED;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotEmpty
    @Length(max = 100)
    private String type;

    @NotEmpty
    private String brand;

    @NotEmpty
    private String model;

    @NotEmpty
    private String specification;

    @NotEmpty
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime canceledAt;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User technical;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

        if (this.status == null) {
            this.status = Status.PENDING;
        }
    }

    @PreUpdate
    protected void preUpdate() {
        if (Status.IN_PROGRESS.equals(this.status)) {
            this.startedAt = LocalDateTime.now();
        } else if (Status.FINISHED.equals(this.status)) {
            this.finishedAt = LocalDateTime.now();
        } else if (Status.CANCELED.equals(this.status)) {
            this.canceledAt = LocalDateTime.now();
        }
    }

}
