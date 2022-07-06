package com.sikayetvar.lite.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sikayetvar.lite.complaint.Complaint;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Table(name="COMPANY")
@Entity // Marks this class as an Entity
@Getter // Defines the getter methods of the member fields
@Setter // Defines the setter methods of the member fields
@ToString // Defines a meaningful toString implementation of this class
@NoArgsConstructor // Defines the default constructor
@AllArgsConstructor
public class Company {
    @Id // Marks the "id" field as the identifier of this entity
    @GeneratedValue(strategy = GenerationType.AUTO) // The "id" field is to be generated automatically
    @Column(name="id", nullable = false, unique = true, insertable = false, updatable = false)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="name", nullable = false, unique = false)
    private String name;

    @JsonIgnore //recursive problem
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "company")
    private Set<Complaint> complaints = new HashSet<Complaint>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    //TODO: pass password on different call?
}
