package com.adminzoggy.adminzoggy.services;


import com.adminzoggy.adminzoggy.model.AdminFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminFoodRepository extends JpaRepository<AdminFood,Integer> {
}
