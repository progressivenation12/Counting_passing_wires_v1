package com.karasev.excelreader.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Wire {
    private Integer id;
    private String from;
    private String to;
    private String color;
    private Double crossSection;
    private Integer length;

    @Override
    public String toString() {
        return String.format("№: %5d; %4s; %4s; %4s; %5.2g; %5d", id, from, to, color, crossSection, length);
    }
}
