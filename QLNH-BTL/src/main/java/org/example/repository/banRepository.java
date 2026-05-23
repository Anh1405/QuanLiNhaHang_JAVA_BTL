package org.example.repository;

import org.example.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface banRepository extends JpaRepository<Ban, Long> {

    Optional<Ban> findBySoBan(int soBan);
}
