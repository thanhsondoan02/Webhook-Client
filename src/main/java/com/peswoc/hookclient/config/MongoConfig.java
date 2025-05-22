package com.peswoc.hookclient.config;

import com.peswoc.hookclient.model.base.StateReadConverter;
import com.peswoc.hookclient.model.base.StateWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

  @Bean
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(List.of(
      new StateReadConverter(),
      new StateWriteConverter()
    ));
  }
}
