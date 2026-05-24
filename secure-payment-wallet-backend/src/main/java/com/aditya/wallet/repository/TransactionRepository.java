package com.aditya.wallet.repository;

import com.aditya.wallet.entity.Transaction;
import com.aditya.wallet.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findAllByOrderByCreatedAtDesc();
}
