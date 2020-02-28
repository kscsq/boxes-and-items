package ru.fedordmitriev.boxesanditems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fedordmitriev.boxesanditems.entity.Box;

public interface BoxRepository extends JpaRepository<Box, Long> {
}
