package com.example.ShareDocuments.Entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
            name = "owner_id",
            referencedColumnName = "id"
    )
    private User owner;
}
