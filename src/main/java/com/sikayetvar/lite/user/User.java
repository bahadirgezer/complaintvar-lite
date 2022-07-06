package com.sikayetvar.lite.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sikayetvar.lite.complaint.Complaint;
import lombok.*;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Table(name="USER")
@Entity // Marks this class as an Entity
@Getter // Defines the getter methods of the member fields
@Setter // Defines the setter methods of the member fields
@ToString // Defines a meaningful toString implementation of this class
@NoArgsConstructor // Defines the default constructor
@AllArgsConstructor
public class User {
    @Id // Marks the "id" field as the identifier of this entity
    @GeneratedValue(strategy = GenerationType.AUTO) // The "id" field is to be generated automatically
    @Column(name="id", nullable = false, unique = true, insertable = false, updatable = false)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="first_name", nullable = false, unique = false)
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @OneToMany(mappedBy = "user")
    private Set<Complaint> complaints = new HashSet<Complaint>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    //TODO: pass password on different call?
}
