package com.batch.batchalibou.processor;

import com.batch.batchalibou.model.Result;
import com.batch.batchalibou.model.Student;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Thu 04 / Dec / 2025
 * Time: 17 : 04
 */
public class StudentProcessor implements ItemProcessor<Student, Student> {
  @Override
  public Student process(@NonNull Student student) {
    return Student.builder()
        .firstName(student.getFirstName())
        .lastName(student.getLastName())
        .email(student.getEmail())
        .salary(student.getSalary())
        .result(getStudentResult(student))
        .build();
  }

  private String getStudentResult(Student student) {
    if (student.getSalary() < 200000) {
      return Result.NOURISH.name();
    } else if (student.getSalary() < 300000) {
      return Result.BRONZE.name();
    } else if (student.getSalary() < 500000) {
      return Result.SILVER.name();
    } else return Result.GOLD.name();
  }
}
