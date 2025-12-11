https://www.youtube.com/watch?v=QtK0VNUkfzY&list=PLpxcSt9FGVVFqDPqI8m_F5SvDZTMbZ1YX&index=4

https://chatgpt.com/c/6933d922-d5c8-8329-a12f-85817204ce48

// src/main/java/com/example/batch/config/BatchConfig.java
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ErrorLineRepository errorLineRepository; // JPA repo for ErrorLine, or use Jdbc template
    @Autowired private UserRepository userRepository;           // JPA repo for valid users

    @Bean
    public Job importUsersJob(Step importStep) {
        return jobBuilderFactory.get("importUsersJob")
                .start(importStep)
                .build();
    }

    @Bean
    public Step importStep() {
        return stepBuilderFactory.get("importStep")
                .<UserRecord, UserRecord>chunk(50)
                .reader(itemReader(null))     // Step-scoped, injected at runtime
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .skipPolicy(fileValidationSkipPolicy())
                .listener(skipListener())
                .listener(readListener())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<UserRecord> itemReader(
            @Value("#{jobParameters['input.file']}") String inputFile) {
        return new FlatFileItemReaderBuilder<UserRecord>()
                .name("userItemReader")
                .resource(new FileSystemResource(inputFile))
                .linesToSkip(1)  // skip header
                .lineMapper(lineMapper())
                .build();
    }

    private LineMapper<UserRecord> lineMapper() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|");
        tokenizer.setStrict(true);
        tokenizer.setNames("id","firstName","lastName","email","phoneNumber","country","gender","birthDate");

        BeanWrapperFieldSetMapper<UserRecord> fieldSetMapper = new BeanWrapperFieldSetMapper<>() {
            {
                setTargetType(UserRecord.class);
            }
        };

        return (line, lineNumber) -> {
            try {
                FieldSet fs = tokenizer.tokenize(line);
                UserRecord rec = fieldSetMapper.mapFieldSet(fs);
                // parse id and date properly
                try {
                    rec.setId(fs.readLong("id"));
                } catch (Exception e) {
                    throw new ValidationException("Invalid id (not a long): " + fs.readString("id"));
                }
                try {
                    rec.setBirthDate(LocalDate.parse(fs.readString("birthDate")));
                } catch (Exception e) {
                    throw new ValidationException("Invalid birthDate: " + fs.readString("birthDate"));
                }
                return rec;
            } catch (Exception ex) {
                // wrap original exception, include line for logging
                throw new FlatFileParseException("Error parsing line at " + lineNumber + ": " + ex.getMessage(),
                                                 line, lineNumber, ex);
            }
        };
    }

    @Bean
    public ItemProcessor<UserRecord, UserRecord> processor() {
        return user -> {
            // optionally additional validation
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                throw new ValidationException("Invalid email: " + user.getEmail());
            }
            return user;
        };
    }

    @Bean
    public ItemWriter<UserRecord> writer() {
        return users -> {
            // for example, using JPA
            userRepository.saveAll(users);
        };
    }

    @Bean
    public SkipPolicy fileValidationSkipPolicy() {
        // skip parse + validation exceptions, up to a limit
        return new LimitCheckingItemSkipPolicy(
                Set.of(FlatFileParseException.class, ValidationException.class),
                100L // skip up to 100 bad records before failing
        );
    }

    @Bean
    public SkipListener<UserRecord, UserRecord> skipListener() {
        return new SkipListener<>() {
            @Override
            public void onSkipInRead(Throwable t) {
                logAndPersistError(null, t);
            }
            @Override
            public void onSkipInProcess(UserRecord item, Throwable t) {
                logAndPersistError(item, t);
            }
            @Override
            public void onSkipInWrite(UserRecord item, Throwable t) {
                logAndPersistError(item, t);
            }

            private void logAndPersistError(UserRecord item, Throwable t) {
                String raw = (t instanceof FlatFileParseException ffp)
                    ? ffp.getInput() 
                    : (item != null ? item.toString() : "n/a");
                long line = (t instanceof FlatFileParseException ffp2)
                    ? ffp2.getLineNumber() 
                    : -1;

                ErrorLine err = new ErrorLine();
                err.setFileName("input-file.csv");  // or from job parameter
                err.setLineNumber(line);
                err.setRawLine(raw);
                err.setErrorMessage(t.getMessage());
                errorLineRepository.save(err);  // persist error for later review
                // optionally log
                System.err.printf("Skipped line %d: %s — %s%n", line, raw, t.getMessage());
            }
        };
    }

    @Bean[Architecture.md](../../../_PROJECTS%20FOLDERS/Neko%20Services/Architecture.md)
    public ItemReadListener<UserRecord> readListener() {
        return new ItemReadListener<>() {
            @Override
            public void beforeRead() {}
            @Override
            public void afterRead(UserRecord item) {}
            @Override
            public void onReadError(Exception ex) {
                // This is called for read errors (if not skip/skipPolicy) — optionally log it
                System.err.println("Error reading record: " + ex.getMessage());
            }
        };
    }
}
