-- 实现一个简单的限流功能

-- 实现的功能，在单位时间内，某个key只能有limit次访问

-- 输出用户传递进来的参数
for i, v in pairs(KEYS) do
    redis.log(redis.LOG_NOTICE, "limit: key" .. i .. " = " .. v)
end
for i, v in pairs(ARGV) do
    redis.log(redis.LOG_NOTICE, "limit: argv" .. i .. " = " .. v)
end

-- 限流的key
local limitKey = tostring(KEYS[1])
-- 限流的次数
local limit = tonumber(ARGV[1])
-- 多长时间过期
local expireMs = tonumber(ARGV[2])

-- 当前已经执行的次数
local current = tonumber(redis.call('get', limitKey) or '0')

-- 设置一个断点
redis.breakpoint()

redis.log(redis.LOG_NOTICE, "limit key: " .. tostring(limitKey) .. " 在[" .. tostring(expireMs) .. "]ms内已经访问了 " .. tostring(current) .. " 次,最多可以访问: " .. limit .. " 次")

-- 限流了
if (current + 1 > limit) then
    return { true }
end

-- 未达到访问限制
-- 访问次数+1
redis.call("incrby", limitKey, "1")
if (current == 0) then
    -- 设置过期时间
    redis.call("pexpire", limitKey, expireMs)
end

return { false }
