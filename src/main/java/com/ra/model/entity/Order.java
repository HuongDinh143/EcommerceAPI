package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "serial_number",length = 100)
    private String serialNumber = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @Column(name = "total_price")
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private Status status;
    public enum Status {
        WAITING, CONFIRM, DELIVERY, SUCCESS, CANCEL
    }
    @Column(name = "note",length = 100)
    private String note;
    @Column(name = "receive_name",length = 100)
    private String receiveName;
    @Column(name = "receive_address",length = 250)
    private String receiveAddress;
    @Column(name = "receive_phone",length = 15)
    private String receivePhone;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "received_at")
    private LocalDate receivedAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.receivedAt = createdAt.plusDays(4);
    }


}
