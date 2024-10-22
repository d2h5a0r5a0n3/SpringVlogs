package com.mypackage.SpringStarter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mypackage.SpringStarter.models.Authority;

@Repository
public interface  AuthorityRepository extends JpaRepository<Authority,Long>{}
