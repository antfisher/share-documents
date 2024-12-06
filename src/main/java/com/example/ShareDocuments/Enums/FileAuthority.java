package com.example.ShareDocuments.Enums;

import lombok.Getter;

@Getter
public enum FileAuthority {

    READ("read");

    private final String authority;

    FileAuthority(String authority) {
        this.authority = authority;
    }
}
