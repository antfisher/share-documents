package com.example.ShareDocuments.Entities;


import com.example.ShareDocuments.DTO.CoworkerDto;
import com.example.ShareDocuments.Enums.FileAuthority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table()
@Data
@Entity(name = "coworkers")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Coworker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private Long userID;
    @Enumerated(EnumType.STRING)
    private FileAuthority authority;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    public CoworkerDto toDto() {
        return new CoworkerDto(
                getId(),
                getEmail(),
                getUserID(),
                getAuthority().toString(),
                getFile().getId()
        );
    }
}
