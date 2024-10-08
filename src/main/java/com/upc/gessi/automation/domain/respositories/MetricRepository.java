package com.upc.gessi.automation.domain.respositories;

import com.upc.gessi.automation.domain.models.Metric;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricRepository extends CrudRepository<Metric,Integer> {

    Metric findByExternalid(Integer externalid);

    Metric findByName(String name);
    Metric findByNameAndProject(String name,String project);

    List<Metric> findAllByProject(String project);

    Boolean existsByExternalid(Integer externalId);
}
