package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cat_name",length = 100)
    private String catName;
    @Column(name = "cat_desc")
    private String catDesc;
    @Column(name = "status")
    private Boolean status;
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}
