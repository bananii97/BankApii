package com.bank.user.jpa.repository;

import com.bank.user.jpa.model.UserDataHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataHistoryRepository extends JpaRepository<UserDataHistory, Long> {
}
