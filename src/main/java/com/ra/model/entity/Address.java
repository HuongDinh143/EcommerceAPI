package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;
    @Column(name = "full_address")
    private String fullAddress;
    @Column(name = "phone")
    private String phone;
    @Column(name = "receive_name")
    private String receiveName;
}
