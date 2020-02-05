package com.adaming.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.adaming.models.User;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Bean
	public Job job(
			JobBuilderFactory jobBuilder, 
			StepBuilderFactory stepBuilder, 
			ItemReader<User> itemReader,
			ItemProcessor<User, User> itemProcessor, 
			ItemWriter<User> itemWriter) {
		
		Step step = stepBuilder.get("ETL-File-Load")
						.<User, User>chunk(2)
						.reader(itemReader)
						.processor(itemProcessor)
						.writer(itemWriter)
						.build();
		Job job = jobBuilder.get("ETL-Load")
							.incrementer(new RunIdIncrementer())
							.start(step)
							.build();
		
		return job;
	}
	@Bean
	public FlatFileItemReader<User> itemReader(
						@Value("${input}")Resource resource){
		FlatFileItemReader<User> fileReader = new FlatFileItemReader<User>();
		fileReader.setResource(resource);
		fileReader.setName("CSV-Reader");
		fileReader.setLinesToSkip(1);
		fileReader.setLineMapper(lineMapper());
		
		return fileReader;
	}
	@Bean
	public LineMapper<User> lineMapper() {
		DelimitedLineTokenizer delimiter = new DelimitedLineTokenizer();
		delimiter.setDelimiter(",");
		delimiter.setStrict(false);		
		String[] columnNames = {"id", "name", "dept", "salary"};
		delimiter.setNames(columnNames);
		
		BeanWrapperFieldSetMapper<User> lineMapper = new BeanWrapperFieldSetMapper<User>();
		lineMapper.setTargetType(User.class);
		
		DefaultLineMapper<User> mapper = new DefaultLineMapper<User>();
		mapper.setLineTokenizer(delimiter);
		mapper.setFieldSetMapper(lineMapper);
		
		return mapper;
	}
}






