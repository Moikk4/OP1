package com.example.temperature;

public class UnitOption {
    private final int id;
    private final String code;
    private final String name;

    public UnitOption(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}

