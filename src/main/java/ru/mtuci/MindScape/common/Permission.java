package ru.mtuci.MindScape.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
    READ("read"),
    WRITE("permission:write"),
    MODIFICATION("modification");

    private final String permission;
}