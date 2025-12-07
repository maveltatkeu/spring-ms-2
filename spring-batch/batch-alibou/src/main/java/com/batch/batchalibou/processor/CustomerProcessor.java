package com.batch.batchalibou.processor;

import com.batch.batchalibou.model.Customer;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 06 / Dec / 2025
 * Time: 18 : 06
 */
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
  @Override
  public Customer process(Customer item) throws Exception {
    return Customer.builder()
        .firstName(item.getFirstName())
        .lastName(item.getLastName())
        .email(item.getEmail())
        .phoneNumber(item.getPhoneNumber())
        .country(item.getCountry())
        .gender(item.getGender())
        .birthDate(item.getBirthDate())
        .build();
  }
}
