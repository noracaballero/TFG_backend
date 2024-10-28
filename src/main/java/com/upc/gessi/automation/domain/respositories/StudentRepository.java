package com.upc.gessi.automation.domain.respositories;

import com.upc.gessi.automation.domain.models.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student,Integer> {

    Optional<Student> findById(Integer id);

    List<Student> findAllByProject(Integer project);
}

