package com.example.demo.configuration;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.demo.domain.Person;
import com.example.demo.domain.PersonFieldSetMapper;
import com.example.demo.domain.PersonRowMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


@org.springframework.context.annotation.Configuration
@EnableBatchProcessing
public class Configuration {
 @Autowired
    private JobBuilderFactory jobBuilderFactory;
 @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    public DataSource dataSource;
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://ec2-107-22-83-3.compute-1.amazonaws.com:5432/ddj9sraarreotq");
        dataSource.setUsername("kgbhfadzzeosfl");
        dataSource.setPassword("539ead5c7f047f5b342358b803afb63e704b11ec491c730bfc8f3b2fe86937ba");
        return dataSource;
    }





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
    public JdbcBatchItemWriter<Person> personItemWriter() throws SQLException {
     JdbcBatchItemWriter<Person> itemWriter=new JdbcBatchItemWriter<>();
     itemWriter.setDataSource(this.dataSource);
     itemWriter.setSql("INSERT INTO PEOPLE(id,name,email,dateOfBirth,phoneNumber,gender) VALUES (:id,:name,:email,:dateOfBirth,:phoneNumber,:gender)");
     itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
     itemWriter.afterPropertiesSet();
//     itemWriter.setSql("update batch_job_execution set status= 'FAILED', version=version+1 where job_instance_id=jobId;");
        dataSource.getConnection().close();
     return itemWriter;
 }

    @Bean
    public JdbcCursorItemReader<Person> cursorItemReader() throws SQLException {
        JdbcCursorItemReader<Person> reader=new JdbcCursorItemReader<>();
        reader.setSql("select id,name,email,dateOfBirth, phoneNumber, gender from people");
        reader.setDataSource(this.dataSource);
        reader.setRowMapper(new PersonRowMapper());
        dataSource.getConnection().close();
        return  reader;
    }

    @Bean
    public ItemWriter<Person> step2ItemWriter() {
        return items -> {
            for(Person item : items) {
                System.out.println(item.toString());
            }
        };
    }

    @Bean
    public FlatFileItemWriter<Person> step2PersonFlatFileItemWriter() throws Exception {
        FlatFileItemWriter<Person>  itemWriter=new FlatFileItemWriter<>();
        itemWriter.setLineAggregator(new PassThroughLineAggregator<>());
        String path = File.createTempFile("Outputa",".csv").getAbsolutePath();
        System.out.println("output:"+path);
        itemWriter.setResource(new FileSystemResource(path));
        itemWriter.afterPropertiesSet();
        dataSource.getConnection().close();
        return itemWriter;
    }

    @Bean
    public Step Step1() throws SQLException {
        return stepBuilderFactory.get("step1")
                .<Person,Person>chunk(5)
                .reader(personItemReader())
                .writer(personItemWriter())
                .build();
    }
    @Bean
    public Step Step2() throws Exception {
        return stepBuilderFactory.get("step2")
                .<Person,Person>chunk(5)
                .reader(cursorItemReader())
                .writer(step2PersonFlatFileItemWriter())
                .build();
    }

    @Bean
    public Job job() throws Exception {
     return  jobBuilderFactory.get("job")
             .start(Step1()).next(Step2()).build();
    }







}
