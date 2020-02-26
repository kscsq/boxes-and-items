package ru.fedordmitriev.boxesanditems.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@JacksonXmlRootElement(localName = "Item")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JacksonXmlProperty(isAttribute = true)
    private Long id;

    @Column
    @JacksonXmlProperty(isAttribute = true)
    private String color;

    @JsonProperty("box")
    @ManyToOne(targetEntity = Box.class, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "parent_box_id")
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
