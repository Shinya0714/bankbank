package com.example.demo.shohinMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShohinMasterRepository extends JpaRepository<ShohinMaster, Long> {


}
