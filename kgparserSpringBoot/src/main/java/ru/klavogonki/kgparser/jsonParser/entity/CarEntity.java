package ru.klavogonki.kgparser.jsonParser.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class CarEntity {

    private Integer id;

    private String color;
}
