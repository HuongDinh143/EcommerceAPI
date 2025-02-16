package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", length = 100, unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name= "updated_at")
    private LocalDate updatedAt;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ShoppingCart> shoppingCarts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<WishList> wishLists = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.status = true;
        this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
