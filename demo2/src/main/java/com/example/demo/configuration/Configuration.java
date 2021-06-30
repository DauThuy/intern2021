package com.example.demo.configuration;

import com.example.demo.domain.Person;
import com.example.demo.domain.PersonFieldSetMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
@EnableBatchProcessing
public class Configuration {
 @Autowired
    private JobBuilderFactory jobBuilderFactory;
 @Autowired
    private StepBuilderFactory stepBuilderFactory;

// @Autowired
//    public DataSource dataSource;
//    @Bean
//    public Step Step2() {
//        return stepBuilderFactory.get("step2")
//                .tasklet(new Tasklet() {
//                    @Override
//                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//                        System.out.println("Hello world@ 222");
//                        return RepeatStatus.FINISHED;
//                    }
//                }).build();
//    }
// @Bean
//    public Job helloW() {
//     return jobBuilderFactory.get("helloasfdgf").start(Step1()).next(Step2()).build();
// }

 @Bean
    public FlatFileItemReader<Person> personItemReader(){
     FlatFileItemReader<Person> personFlatFileItemReader=new FlatFileItemReader<>();

     personFlatFileItemReader.setLinesToSkip(1);
     personFlatFileItemReader.setResource(new ClassPathResource("data/infomation.csv"));

     DefaultLineMapper<Person> personDefaultLineMapper=new DefaultLineMapper<>();

     DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();
     tokenizer.setNames(new String[] {"id","name","email","dateOfBirth","phoneNumber","gender"});

     personDefaultLineMapper.setLineTokenizer(tokenizer);
     personDefaultLineMapper.setFieldSetMapper( new PersonFieldSetMapper());
     personDefaultLineMapper.afterPropertiesSet();

     personFlatFileItemReader.setLineMapper(personDefaultLineMapper);
     return personFlatFileItemReader;
  }

    @Bean
    public ItemWriter<Person> personItemWriter() {
        return items -> {
            for(Person item :  items) {
                System.out.println(item.toString());
            }
        };
    }

    @Bean
    public Step Step1() {
        return stepBuilderFactory.get("step1")
                .<Person,Person>chunk(10)
                .reader(personItemReader())
                .writer(personItemWriter())
                .build();
    }

    @Bean
    public Job job() {
     return  jobBuilderFactory.get("job")
             .start(Step1()).build();
    }



}
