package com.batch.batchalibou.controller;

import com.batch.batchalibou.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Thu 04 / Dec / 2025
 * Time: 17 : 39
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/batch")
public class StudentController {

  private final JobLauncher jobLauncher;
  private final Job job;

  @PostMapping
  public void importStudentToDb( ) {
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("startAt", System.currentTimeMillis())
        .toJobParameters();

    try {
      jobLauncher.run(job,jobParameters);
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
             JobParametersInvalidException e) {
      log.error("Failed to load data: {}",e.getMessage());
    }

  }
}
