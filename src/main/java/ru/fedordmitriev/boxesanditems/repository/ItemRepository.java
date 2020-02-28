package ru.fedordmitriev.boxesanditems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fedordmitriev.boxesanditems.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
