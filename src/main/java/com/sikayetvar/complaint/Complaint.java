package com.sikayetvar.complaint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sikayetvar.company.Company;
import com.sikayetvar.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Table(name="complaint")
@Entity // Marks this class as an Entity
@Getter // Defines the getter methods of the member fields
@Setter // Defines the setter methods of the member fields
@ToString // Defines a meaningful toString implementation of this class
@NoArgsConstructor // Defines the default constructor
public class Complaint {
    @Id // Marks the "id" field as the identifier of this entity
    @GeneratedValue(strategy = GenerationType.AUTO) // The "id" field is to be generated automatically
    @Column(name="id", nullable = false, unique = true, insertable = false, updatable = false)
    private Long id;

    @Column(name="body", nullable = false)
    private String body;

    @Column(name="title", nullable = false)
    private String title;

    @OneToOne
    private User user;

    @OneToOne
    private Company company;
}
