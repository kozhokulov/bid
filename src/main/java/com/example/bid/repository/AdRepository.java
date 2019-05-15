package com.example.bid.repository;

import com.example.bid.domain.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
    Ad findByAdvertisement(String advertisement);
    Ad getById(Long id);
}
