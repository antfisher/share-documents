package com.example.ShareDocuments.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Table()
@Data
@Entity(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;
    private String name;
    private String type;

    @ManyToOne
    @JoinColumn(
            name = "owner_id", nullable = false
    )
    private User owner;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Coworker> coworkers;
}
