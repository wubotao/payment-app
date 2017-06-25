package io.taotao.dao;

import io.taotao.bean.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Record findByFirstNameAndLastNameAndPaymentId(String firstName, String lastName, String paymentId);
}

