package com.batch.batchalibou.repository;

import com.batch.batchalibou.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 06 / Dec / 2025
 * Time: 18 : 10
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
