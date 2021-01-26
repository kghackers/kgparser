package ru.klavogonki.statistics.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CarEntity implements Serializable {

    private Integer id;

    private String color;
}
