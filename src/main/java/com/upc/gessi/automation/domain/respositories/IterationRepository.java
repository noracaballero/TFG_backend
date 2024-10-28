package com.upc.gessi.automation.domain.respositories;

import com.upc.gessi.automation.domain.models.Iteration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IterationRepository extends CrudRepository<Iteration, Integer> {

    Iteration findByName(String name);

    List<Iteration> findAllBySubject(String subject);
}
