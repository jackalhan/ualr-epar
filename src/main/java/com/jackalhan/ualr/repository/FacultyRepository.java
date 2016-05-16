package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by txcakaloglu on 5/16/16.
 */
public interface FacultyRepository extends JpaRepository<Faculty, String> {
    Faculty findByCode(String code);

}
