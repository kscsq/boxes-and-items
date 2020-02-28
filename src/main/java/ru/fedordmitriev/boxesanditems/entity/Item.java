package ru.fedordmitriev.boxesanditems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Item implements Serializable {

    @Id
    @JacksonXmlProperty(isAttribute = true)
    private Long id;

    @Column
    @JacksonXmlProperty(isAttribute = true)
    private String color;

    @ManyToOne(targetEntity = Box.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_box_id")
    @JsonIgnore
    private Box parentBox;

    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Box getParentBox() {
        return parentBox;
    }

    public void setParentBox(Box parentBox) {
        this.parentBox = parentBox;
    }
}
