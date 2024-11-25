package com.example.ShareDocuments.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table()
@Data
@Entity(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "token")
public class RefreshToken {

    @Id
    String token;

    @ManyToOne
    @JoinColumn(
            name = "owner_id",
            referencedColumnName = "id"
    )
    private User owner;
}
