package com.example.demo.clientMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientMasterRepository extends JpaRepository<ClientMaster, Integer>, JpaSpecificationExecutor<ClientMaster> {

	ClientMaster findById(int id);
}
