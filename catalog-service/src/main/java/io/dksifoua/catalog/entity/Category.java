package io.dksifoua.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Dimitri Sifoua
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;
}
