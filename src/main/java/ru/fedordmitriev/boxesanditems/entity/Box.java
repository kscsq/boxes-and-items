package ru.fedordmitriev.boxesanditems.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
@JacksonXmlRootElement(localName = "Box")
public class Box implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JacksonXmlProperty(isAttribute = true)
    private Long id;

    @ManyToOne(targetEntity = Box.class, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "parent_box_id")
    private Box parentBox;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "parentBox", cascade = CascadeType.ALL)
    private List<Item> itemsList;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "parentBox", cascade = CascadeType.ALL)
    private List<Box> boxesList;

    public Box() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Box getParentBox() {
        return parentBox;
    }

    public void setParentBox(Box containedInBoxes) {
        this.parentBox = containedInBoxes;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public List<Box> getBoxesList() {
        return boxesList;
    }

    public void setBoxesList(List<Box> boxesList) {
        this.boxesList = boxesList;
    }
}
