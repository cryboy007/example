package com.github.crybooy007.mq.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.cryboy007.message.Result;
import com.github.cryboy007.utils.CollectionUtil;
import com.github.cryboy007.utils.StringUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.rabbitmq.http.client.domain.BindingInfo;
import com.rabbitmq.http.client.domain.ExchangeInfo;
import com.rabbitmq.http.client.domain.QueueInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "RabbitMQ监控器")
@RestController
@RequestMapping(value = "/rabbitmq", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class RabbitMQMonitorController {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQMonitorController.class);

	@Value("${spring.rabbitmq.addresses}")
	private String addresses;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	@Value("${spring.rabbitmq.virtual-host}")
	private String vhost;

	@Autowired(required = false)
	private RestTemplate rt;

	// api接口地址前缀
	private String urlPrefix;

	// 初始化接口地址
	@PostConstruct
	public void init() {
		String[] addressArr = addresses.split(":");
		StringBuilder sb = new StringBuilder();
		if (CollectionUtil.isNotEmpty(addressArr)) {
			sb.append("http://").append(addressArr[0]).append(":15672/api/");
		}
		urlPrefix = sb.toString();
	}

	/**
	 * 分页获取MQ队列 
	 * /api/queues
	 * @return
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNum", value = "当前页码", required = true, dataType = "int", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示的条数", required = true, dataType = "int", defaultValue = "15"),
			@ApiImplicitParam(name = "queueName", value = "队列名", required = false, dataType = "String", defaultValue = "") })
	@ApiOperation(value = "分页获取MQ队列")
	@RequestMapping("/listQueues")
	public Result<PageInfo<QueueInfo>> listQueuesPage(@RequestParam(defaultValue = "1", required = true) int pageNum,
                                                      @RequestParam(defaultValue = "15", required = true) int pageSize,
                                                      @RequestParam(defaultValue = "", required = false) String queueName) {
		PageInfo<QueueInfo> info = new PageInfo<>();
		try {
			// 当前页
			info.setPageNum(pageNum);
			// 每页的数量
			info.setPageSize(pageSize);
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("queues?");
				sb.append("page=").append(pageNum).append("&");
				sb.append("page_size=").append(pageSize).append("&");
				sb.append("name=").append(UriUtils.encodePathSegment(queueName, "UTF-8")).append("&");
				sb.append("use_regex=true").append("&").append("pagination=true");
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				info.setTotal(0);
				return Result.success(info);
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			JSONObject result = rt.getForObject(uri, JSONObject.class);
			// 总记录数
			info.setTotal(result.getLongValue("filtered_count"));
			// 总页数
			info.setPages(result.getIntValue("page_count"));
			// 当前页的数量
			info.setSize(result.getIntValue("item_count"));
			List<QueueInfo> list = JSON.parseArray(result.getJSONArray("items").toJSONString(), QueueInfo.class);
			// 结果集
			info.setList(list);
		} catch (Exception e) {
			logger.error("分页获取MQ队列异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success(info);
	}

	/**
	 * 根据队列名获取某个队列 
	 * /api/queues/vhost/name
	 * @param queueName 队列名
	 * @return
	 */
	@ApiOperation(value = "根据队列名获取单个队列")
	@RequestMapping("/getQueue")
	public Result<QueueInfo> getQueueByName(@RequestParam(defaultValue = "", required = true) String queueName) {
		QueueInfo queueInfo = null;
		try {
			if (StringUtil.isEmptyOrNull(queueName)) {
				return Result.success(queueInfo);
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("queues").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(queueName, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.success(queueInfo);
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			queueInfo = rt.getForObject(uri, QueueInfo.class);
		} catch (Exception e) {
			logger.error("根据队列名获取单个队列异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success(queueInfo);
	}

	/**
	 * 根据队列名获取绑定列表 
	 * /api/queues/vhost/name/bindings
	 * @param queueName 队列名
	 */
	@ApiOperation(value = "根据队列名获取所有绑定列表")
	@RequestMapping("/getQueueBindings")
	public Result<List<BindingInfo>> getQueueBindingsByQueueName(
			@RequestParam(defaultValue = "", required = true) String queueName) {
		List<BindingInfo> bindings = Lists.newArrayList();
		try {
			if (StringUtil.isEmptyOrNull(queueName)) {
				return Result.success(bindings);
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("queues").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(queueName, "UTF-8")).append("/");
				sb.append("bindings");
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.success(bindings);
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			bindings = Lists.newArrayList(rt.getForObject(uri, BindingInfo[].class));
		} catch (Exception e) {
			logger.error("根据队列名获取所有绑定列表异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success(bindings);
	}

	/**
	 * 添加队列 
	 * /api/queues/vhost/name
	 * @param queueName 队列名
	 * @return
	 */
	@ApiOperation(value = "添加队列")
	@RequestMapping("/addQueue")
	public Result<?> addQueueByName(@RequestParam(defaultValue = "", required = true) String queueName) {
		try {
			if (StringUtil.isEmptyOrNull(queueName)) {
				return Result.error("队列名[queueName]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("queues").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(queueName, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("vhost", vhost);
			reqMap.put("name", queueName);
			reqMap.put("durable", true);
			reqMap.put("auto_delete", false);
			reqMap.put("arguments", new HashMap<>());
			rt.put(uri, reqMap);
		} catch (Exception e) {
			logger.error("添加队列异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

	/**
	 * 删除队列 
	 * /api/queues/vhost/name
	 * @param queueName 队列名
	 * @return
	 */
	@ApiOperation(value = "删除队列")
	@RequestMapping("/deleteQueue")
	public Result<?> deleteQueueByName(@RequestParam(defaultValue = "", required = true) String queueName) {
		try {
			if (StringUtil.isEmptyOrNull(queueName)) {
				return Result.error("队列名[queueName]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("queues").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(queueName, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			rt.delete(uri);
		} catch (Exception e) {
			logger.error("删除队列异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

	/**
	 * 绑定交换器【exchange】到队列【queue】 
	 * /api/bindings/vhost/e/exchange/q/queue
	 * @param exchangeName 交换器
	 * @param queueName    队列
	 * @param routingKey   路由关键字
	 * @return
	 */
	@ApiOperation(value = "绑定exchange到queue")
	@RequestMapping("/bindQueue")
	public Result<?> bindExchangeToQueue(@RequestParam(defaultValue = "", required = true) String exchangeName,
                                         @RequestParam(defaultValue = "", required = true) String queueName,
                                         @RequestParam(defaultValue = "", required = false) String routingKey) {
		try {
			if (StringUtil.isEmptyOrNull(queueName)) {
				return Result.error("队列名[queueName]不能为空！");
			}
			if (StringUtil.isEmptyOrNull(exchangeName)) {
				return Result.error("交换器[exchangeName]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("bindings").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append("e/");
				sb.append(UriUtils.encodePathSegment(exchangeName, "UTF-8")).append("/");
				sb.append("q/");
				sb.append(UriUtils.encodePathSegment(queueName, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("routing_key", routingKey);
			rt.postForLocation(uri, reqMap);
		} catch (Exception e) {
			logger.error("绑定exchange到queue异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

	/**
	 * 解绑交换器【exchange】到队列【queue】 
	 * /api/bindings/vhost/e/exchange/q/queue/props
	 * @param exchangeName 交换器
	 * @param queueName    队列名
	 * @param routingKey   路由关键字
	 * @return
	 */
	@ApiOperation(value = "解绑exchange到queue")
	@RequestMapping("/unbindQueue")
	public Result<?> unbindExchangeToQueue(@RequestParam(defaultValue = "", required = true) String exchangeName,
                                           @RequestParam(defaultValue = "", required = true) String queueName,
                                           @RequestParam(defaultValue = "", required = true) String routingKey) {
		try {
			if (StringUtil.isEmptyOrNull(queueName)) {
				return Result.error("队列名[queueName]不能为空！");
			}
			if (StringUtil.isEmptyOrNull(exchangeName)) {
				return Result.error("交换器[exchangeName]不能为空！");
			}
			if (StringUtil.isEmptyOrNull(routingKey)) {
				return Result.error("路由关键字[routingKey]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("bindings").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append("e/");
				sb.append(UriUtils.encodePathSegment(exchangeName, "UTF-8")).append("/");
				sb.append("q/");
				sb.append(UriUtils.encodePathSegment(queueName, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(routingKey, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			rt.delete(uri);
		} catch (Exception e) {
			logger.error("解绑exchange到queue异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

	/**
	 * 分页获取Exchanges 
	 * /api/exchanges
	 * @param pageNum 当前页码
	 * @param pageSize 每页条数
	 * @param exchangeName 交换器名
	 * @return
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNum", value = "当前页码", required = true, dataType = "int", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示的条数", required = true, dataType = "int", defaultValue = "15"),
			@ApiImplicitParam(name = "exchangeName", value = "交换器名", required = false, dataType = "String", defaultValue = "") })
	@ApiOperation(value = "分页获取Exchanges")
	@RequestMapping("/listExchanges")
	public Result<PageInfo<ExchangeInfo>> listExchangesPage(
			@RequestParam(defaultValue = "1", required = true) int pageNum,
			@RequestParam(defaultValue = "15", required = true) int pageSize,
			@RequestParam(defaultValue = "", required = false) String exchangeName) {
		PageInfo<ExchangeInfo> info = new PageInfo<>();
		try {
			// 当前页
			info.setPageNum(pageNum);
			// 每页的数量
			info.setPageSize(pageSize);
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("exchanges?");
				sb.append("page=").append(pageNum).append("&");
				sb.append("page_size=").append(pageSize).append("&");
				sb.append("name=").append(UriUtils.encodePathSegment(exchangeName, "UTF-8")).append("&");
				sb.append("use_regex=true").append("&").append("pagination=true");
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				info.setTotal(0);
				return Result.success(info);
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			JSONObject result = rt.getForObject(uri, JSONObject.class);
			// 总记录数
			info.setTotal(result.getLongValue("filtered_count"));
			// 总页数
			info.setPages(result.getIntValue("page_count"));
			// 当前页的数量
			info.setSize(result.getIntValue("item_count"));
			List<ExchangeInfo> list = JSON.parseArray(result.getJSONArray("items").toJSONString(), ExchangeInfo.class);
			// 结果集
			info.setList(list);
		} catch (Exception e) {
			logger.error("分页获取Exchanges异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success(info);
	}

	/**
	 * 根据交换器名获取单个交换器 
	 * /api/exchanges/vhost/name
	 * @param exchangeName 交换器名
	 * @return
	 */
	@ApiOperation(value = "根据交换器名获取单个交换器")
	@RequestMapping("/getExchange")
	public Result<ExchangeInfo> getExchangeByName(
			@RequestParam(defaultValue = "", required = true) String exchangeName) {
		ExchangeInfo exchangeInfo = null;
		try {
			if (StringUtil.isEmptyOrNull(exchangeName)) {
				return Result.success(exchangeInfo);
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("exchanges").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(exchangeName, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.success(exchangeInfo);
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			exchangeInfo = rt.getForObject(uri, ExchangeInfo.class);
		} catch (Exception e) {
			logger.error("根据交换器名获取单个交换器异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success(exchangeInfo);
	}

	/**
	 * 创建exchange 
	 * 	/api/exchanges/vhost/name
	 * @param exchangeName 交换器
	 * @return
	 */
	@ApiOperation(value = "创建exchange")
	@RequestMapping("/addExchange")
	public Result<?> addExchange(@RequestParam(defaultValue = "", required = true) String exchangeName) {
		try {
			if (StringUtil.isEmptyOrNull(exchangeName)) {
				return Result.error("交换器[exchangeName]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("exchanges").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(exchangeName, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("vhost", vhost);
			reqMap.put("name", exchangeName);
			reqMap.put("type", "direct");
			reqMap.put("durable", true);
			reqMap.put("auto_delete", false);
			reqMap.put("internal", false);
			reqMap.put("arguments", new HashMap<>());
			rt.put(uri, reqMap);
		} catch (Exception e) {
			logger.error("创建exchange异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

	/**
	 * 删除交换器【exchange】 
	 * /api/exchanges/vhost/name
	 * @param exchangeName 队列名
	 * @return
	 */
	@ApiOperation(value = "删除exchange")
	@RequestMapping("/deleteExchange")
	public Result<?> deleteExchange(@RequestParam(defaultValue = "", required = true) String exchangeName) {
		try {
			if (StringUtil.isEmptyOrNull(exchangeName)) {
				return Result.error("交换器[exchangeName]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("exchanges").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(exchangeName, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			rt.delete(uri);
		} catch (Exception e) {
			logger.error("删除exchange异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

	/**
	 * 绑定exchange 
	 * /api/bindings/vhost/e/source/e/destination
	 * @param destination 目标exchange
	 * @param source      源exchange
	 * @param routingKey  路由关键字
	 * @return
	 */
	@ApiOperation(value = "绑定exchange")
	@RequestMapping("/bindExchange")
	public Result<?> bindExchange(@RequestParam(defaultValue = "", required = true) String destination,
                                  @RequestParam(defaultValue = "", required = true) String source,
                                  @RequestParam(defaultValue = "", required = false) String routingKey) {
		try {
			if (StringUtil.isEmptyOrNull(destination)) {
				return Result.error("目标[destination]不能为空！");
			}
			if (StringUtil.isEmptyOrNull(source)) {
				return Result.error("源[source]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("bindings").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append("e/");
				sb.append(UriUtils.encodePathSegment(source, "UTF-8")).append("/");
				sb.append("e/");
				sb.append(UriUtils.encodePathSegment(destination, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("routing_key", routingKey);
			rt.postForLocation(uri, reqMap);
		} catch (Exception e) {
			logger.error("绑定exchange异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

	/**
	 * 解绑exchange 
	 * /api/bindings/vhost/e/source/e/destination/props
	 * @param destination 目标exchange
	 * @param source      源exchange
	 * @param routingKey  路由关键字
	 * @return
	 */
	@ApiOperation(value = "解绑exchange")
	@RequestMapping("/unbindExchange")
	public Result<?> unbindExchange(@RequestParam(defaultValue = "", required = true) String destination,
                                    @RequestParam(defaultValue = "", required = true) String source,
                                    @RequestParam(defaultValue = "", required = true) String routingKey) {
		try {
			if (StringUtil.isEmptyOrNull(destination)) {
				return Result.error("目标[destination]不能为空！");
			}
			if (StringUtil.isEmptyOrNull(source)) {
				return Result.error("源[source]不能为空！");
			}
			if (StringUtil.isEmptyOrNull(routingKey)) {
				return Result.error("路由关键字[routingKey]不能为空！");
			}
			String[] addressArr = addresses.split(":");
			StringBuilder sb = new StringBuilder(urlPrefix);
			if (CollectionUtil.isNotEmpty(addressArr)) {
				sb.append("bindings").append("/");
				sb.append(UriUtils.encodePathSegment(vhost, "UTF-8")).append("/");
				sb.append("e/");
				sb.append(UriUtils.encodePathSegment(source, "UTF-8")).append("/");
				sb.append("e/");
				sb.append(UriUtils.encodePathSegment(destination, "UTF-8")).append("/");
				sb.append(UriUtils.encodePathSegment(routingKey, "UTF-8"));
			}
			String url = sb.toString();
			if (StringUtil.isEmptyOrNull(url)) {
				return Result.error("请求url不能为空！");
			}
			// 授权
			rt.getInterceptors().add(new BasicAuthenticationInterceptor(username, password, Charset.defaultCharset()));
			URI uri = new URI(url);
			rt.delete(uri);
		} catch (Exception e) {
			logger.error("解绑exchange异常！", e);
			return Result.error(e.getMessage());
		}
		return Result.success();
	}

}
