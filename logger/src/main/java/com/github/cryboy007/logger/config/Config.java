package com.github.cryboy007.logger.config;

import com.github.cryboy007.logger.factory.ParseFunctionFactory;
import com.github.cryboy007.logger.resolver.LogRecordValueParser;
import com.github.cryboy007.logger.service.DefaultFunctionServiceImpl;
import com.github.cryboy007.logger.service.DefaultParseFunction;
import com.github.cryboy007.logger.service.IFunctionService;
import com.github.cryboy007.logger.service.IParseFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *@ClassName Config
 *@Author tao.he
 *@Since 2022/5/1 12:35
 */
@Configuration
public class Config {
	@Bean
	@ConditionalOnMissingBean(IFunctionService.class)
	public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
		return new DefaultFunctionServiceImpl(parseFunctionFactory);
	}

	@Bean
	public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
		return new ParseFunctionFactory(parseFunctions);
	}

	@Bean
	@ConditionalOnMissingBean(IParseFunction.class)
	public DefaultParseFunction parseFunction() {
		return new DefaultParseFunction();
	}

	@Bean
	public LogRecordValueParser.LogRecordExpressionEvaluator evaluator() {
		return new LogRecordValueParser.LogRecordExpressionEvaluator();
	}

}
