package com.sikayetvar.company;

import com.sikayetvar.complaint.Complaint;
import com.sikayetvar.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;


@Table(name="company")
@Entity // Marks this class as an Entity
@Getter // Defines the getter methods of the member fields
@Setter // Defines the setter methods of the member fields
@ToString // Defines a meaningful toString implementation of this class
@NoArgsConstructor // Defines the default constructor
public class Company {
    @Id // Marks the "id" field as the identifier of this entity
    @GeneratedValue(strategy = GenerationType.AUTO) // The "id" field is to be generated automatically
    @Column(name="id", nullable = false, unique = true, insertable = false, updatable = false)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="name", nullable = false, unique = false)
    private String name;

    @OneToMany
    private Set<Complaint> complaints;

}
