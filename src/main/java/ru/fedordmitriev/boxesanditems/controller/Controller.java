package ru.fedordmitriev.boxesanditems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fedordmitriev.boxesanditems.service.Service;

import java.util.Set;

@RestController
public class Controller {

    @Autowired
    private Service service;

    @PostMapping("/test")
    public Set<Long> findItems(@RequestParam(name = "box") Long id, @RequestParam String color) {
        return service.findAllItemsInBoxWithColor(id, color);
    }

}
