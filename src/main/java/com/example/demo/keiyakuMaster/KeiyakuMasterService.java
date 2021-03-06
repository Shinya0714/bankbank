package com.example.demo.keiyakuMaster;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KeiyakuMasterService {

	@Autowired
	KeiyakuMasterRepository keiyakuMasterRepositry;

	@Autowired
	KeiyakuMasterSpecifications keiyakuMasterSpecifications;

	@PersistenceContext
	EntityManager entityManager;

	public KeiyakuMaster save(KeiyakuMaster entity) {

		return this.keiyakuMasterRepositry.save(entity);
	}

	public List<KeiyakuMaster> findAll() {
		return keiyakuMasterRepositry.findAll(Sort.by("id"));
	}

	public List<KeiyakuMaster> findByKeiyakuRanking() {

		return entityManager
				.createQuery("select koinmaster.koinname, keiyakumaster.koinid, count(keiyakumaster.id) as result from KeiyakuMaster keiyakumaster left outer join KoinMaster koinmaster ON keiyakumaster.koinid = koinmaster.id where keiyakumaster.shoninflg = 1 group by keiyakumaster.koinid, koinmaster.koinname order by result desc")
				.getResultList();
	}

    public Page<KeiyakuMaster> findUsers(KeiyakuMasterListForm keiyakuMasterListForm, Pageable pageable) {

    	return keiyakuMasterRepositry.findAll(Specification
    										.where(keiyakuMasterSpecifications.shoninFlgContains(keiyakuMasterListForm.getShoninflg()))
    										,pageable);
    }

    public long getSumPrice(int shitenid) {
    	
    	var sumPrice = entityManager
        .createQuery("select sum(price) from KeiyakuMaster where shitenid = :shitenid")
        .setParameter("shitenid", shitenid)
        .getSingleResult();
    	
    	
    	long sumPriceLong = 0;

    	if(sumPrice != null) {
    		
    		sumPriceLong = (long)sumPrice;
    	}
    	
    	return sumPriceLong;
    }
    
    public long getCountKeiyaku(int shitenid) {
    	
    	var countKeiyaku = entityManager
    			.createQuery("select count(*) from KeiyakuMaster where shitenid = :shitenid")
    			.setParameter("shitenid", shitenid)
    			.getSingleResult();
    	
    	
    	long countKeiyakuLong = 0;
    	
    	if(countKeiyaku != null) {
    		
    		countKeiyakuLong = (long)countKeiyaku;
    	}
    	
    	return countKeiyakuLong;
    }
}
