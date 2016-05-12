package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jackalhan on 5/11/16.
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
