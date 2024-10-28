package com.upc.gessi.automation.domain.respositories;

import com.upc.gessi.automation.domain.models.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users,Integer> {

    Users findByUsername(String name);

    Boolean existsByUsername(String username);
}
