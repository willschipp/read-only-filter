package com.example.cloud.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="/model")
public interface SimpleModelRepository extends JpaRepository<SimpleModel,Long> {
}
