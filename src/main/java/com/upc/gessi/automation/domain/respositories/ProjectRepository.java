package com.upc.gessi.automation.domain.respositories;

import com.upc.gessi.automation.domain.models.Project;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ProjectRepository extends CrudRepository<Project,Integer> {

    Project findByNameAndSubject(String name, String subject);

    Project findByName(String name);

    Boolean existsByNameAndSubject(String name,String subject);

    List<Project> findAllBySubject(String subject);


}
