package com.batch.batchalibou.config;

import com.batch.batchalibou.model.Student;
import com.batch.batchalibou.processor.StudentProcessor;
import com.batch.batchalibou.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Thu 04 / Dec / 2025
 * Time: 16 : 38
 */
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final StudentRepository studentRepository;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  //ItemReader to read items from file
  @Bean
  public FlatFileItemReader<Student> reader() {
    FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
    reader.setResource(new FileSystemResource("src/main/resources/students.csv"));
    reader.setName("studentsReader");
    reader.setLinesToSkip(1);
    reader.setLineMapper(lineMapper());

    return reader;
  }

  @Bean
  public ItemProcessor<Student, Student> processor() {
    return new StudentProcessor();
  }

  @Bean
  public RepositoryItemWriter<Student> writer() {
    RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
    writer.setRepository(studentRepository);
    writer.setMethodName("save");
    return writer;
  }

  @Bean
  public Step importStep() {
    return new StepBuilder("importStep", jobRepository)
        .<Student,Student>chunk(1000, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .taskExecutor(taskExecutor())
        .build();
  }

  @Bean
  public Job importJob() {
    return new JobBuilder("importJob", jobRepository)
        .start(importStep())
        .build();
  }

  @Bean
  public TaskExecutor taskExecutor() {
//    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
    executor.setThreadNamePrefix("importJob");
    executor.setConcurrencyLimit(10);
    return executor;
  }

  private LineMapper<Student> lineMapper() {
    DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setDelimiter("|");
    tokenizer.setStrict(false);
    tokenizer.setNames("id", "firstName", "lastName", "email", "result", "salary");

    BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(Student.class);

    lineMapper.setLineTokenizer(tokenizer);
    lineMapper.setFieldSetMapper(fieldSetMapper);
    return lineMapper;
  }

}
