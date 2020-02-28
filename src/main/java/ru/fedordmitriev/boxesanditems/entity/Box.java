package ru.fedordmitriev.boxesanditems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@JacksonXmlRootElement
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Box implements Serializable {

    @Id
    @JacksonXmlProperty(isAttribute = true)
    private Long id;

    @ManyToOne(targetEntity = Box.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_box_id")
    @JsonIgnore
    private Box parentBox;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentBox", cascade = CascadeType.ALL)
    @JacksonXmlElementWrapper(useWrapping = false, localName = "itemsList")
    @JacksonXmlProperty(localName = "Item")
    private List<Item> itemsList;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentBox", cascade = CascadeType.ALL)
    @JacksonXmlElementWrapper(useWrapping = false, localName = "boxesList")
    @JacksonXmlProperty(localName = "Box")
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

    public void setItemsList(List<Item> value) {
        if (itemsList == null) {
            itemsList = new ArrayList<>(value.size());
        }
        itemsList.addAll(value);

//        this.itemsList = itemsList;
    }

    public List<Box> getBoxesList() {
        return boxesList;
    }

    public void setBoxesList(List<Box> boxesList) {
        this.boxesList = boxesList;
    }
}
