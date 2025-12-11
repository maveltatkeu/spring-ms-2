package com.batch.batchalibou.config;

import com.batch.batchalibou.model.Customer;
import com.batch.batchalibou.model.Student;
import com.batch.batchalibou.processor.CustomerProcessor;
import com.batch.batchalibou.processor.StudentProcessor;
import com.batch.batchalibou.repository.CustomerRepository;
import com.batch.batchalibou.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Thu 04 / Dec / 2025
 * Time: 16 : 38
 */
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchConfig {

  private final StudentRepository studentRepository;
  private final CustomerRepository customerRepository;
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
  public FlatFileItemReader<Customer> customerReader() {

    FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();

    itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
    itemReader.setName("customerReader");
    itemReader.setLinesToSkip(1);
    itemReader.setLineMapper(customerLineMapper());

    return itemReader;
  }

  // ItemProcessors
  @Bean
  public ItemProcessor<Student, Student> processor() {
    return new StudentProcessor();
  }

  @Bean
  public ItemProcessor<Customer, Customer> customerProcessor() {
    return new CustomerProcessor();
  }

  // ItemWriters
  @Bean
  public RepositoryItemWriter<Student> writer() {
    RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
    writer.setRepository(studentRepository);
    writer.setMethodName("save");
    return writer;
  }

  @Bean
  public RepositoryItemWriter<Customer> customerWriter() {
    RepositoryItemWriter<Customer> custWriter = new RepositoryItemWriter<>();
    custWriter.setRepository(customerRepository);
    custWriter.setMethodName("save");

    return custWriter;
  }

  // Steps
  @Bean
  public Step importStep() {
    return new StepBuilder("importStep", jobRepository)
        .<Student, Student>chunk(1000, transactionManager)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .taskExecutor(taskExecutor())
        .build();
  }

  @Bean
  public Step customerStep() {
    return new StepBuilder("customerStep", jobRepository)
        .<Customer,Customer>chunk(100, transactionManager)
        .reader(customerReader())
        .processor(customerProcessor())
        .writer(customerWriter())
        .taskExecutor(taskExecutor())
        .faultTolerant()
        .build();
  }

  //Jobs
  @Bean
  public Job importJob() {
    return new JobBuilder("importJob", jobRepository)
        .start(importStep())
        .build();
  }

  @Bean
  public Job customerJob() {
    return new JobBuilder("customerJob",jobRepository)
        .start(customerStep())
        .build();

  }

  @Bean
  public TaskExecutor taskExecutor() {
    SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
    executor.setThreadNamePrefix("batchJob_");
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

  private LineMapper<Customer> customerLineMapper() {

    DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

    tokenizer.setDelimiter("|");
    tokenizer.setStrict(false);
    tokenizer.setNames("id", "FIRST_NAME", "lastName", "email", "phoneNumber", "country", "gender", "birthDate");

    BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(Customer.class);
    lineMapper.setLineTokenizer(tokenizer);
    lineMapper.setFieldSetMapper(fieldSetMapper);
    return lineMapper;
  }

  private LineMapper<Customer> lineCustomerMapper() {
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setDelimiter("|");
    tokenizer.setStrict(true);
    tokenizer.setNames("id","firstName","lastName","email","phoneNumber","country","gender","birthDate");

    BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>() {
      {
        setTargetType(Customer.class);
      }
    };

    return (line, lineNumber) -> {
      try {
        FieldSet fs = tokenizer.tokenize(line);
        Customer rec = fieldSetMapper.mapFieldSet(fs);
        // parse id and date properly
        try {
          rec.setId(fs.readLong("id"));
        } catch (Exception e) {
          throw new ValidationException("Invalid id (not a long): " + fs.readString("id"));
        }
        try {
          rec.setBirthDate(LocalDate.parse(fs.readString("birthDate")).toString());
        } catch (Exception e) {
          throw new ValidationException("Invalid birthDate: " + fs.readString("birthDate"));
        }
        return rec;
      } catch (Exception ex) {
        // wrap original exception, include line for logging
        throw new FlatFileParseException("Error parsing data "+ex.getMessage(), ex,
            line, lineNumber);
      }
    };
  }

}
