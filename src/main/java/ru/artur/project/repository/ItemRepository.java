package ru.artur.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artur.project.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
