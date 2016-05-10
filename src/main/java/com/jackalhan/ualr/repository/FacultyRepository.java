package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by txcakaloglu on 5/10/16.
 */
public interface FacultyRepository extends JpaRepository<Faculty, String> {
}
