local lockKey = tostring(KEYS[1])
local lockValue = tostring(ARGV[1])

redis.log(redis.LOG_NOTICE, "解锁:lockKey: " .. lockKey .. " lockValue: " .. lockValue)

local redisLockValue = tostring(redis.call("get", lockKey))

redis.log(redis.LOG_NOTICE, "解锁:lockKey: " .. lockKey .. " 在redis中的值为: " .. redisLockValue)

if redisLockValue ~= lockValue then
    redis.log(redis.LOG_NOTICE, "解锁:lockKey: 对应的锁不属于你")
    return { false }
end
redis.call("del", lockKey)
redis.log(redis.LOG_NOTICE, "解锁:lockKey: " .. lockKey .. " 解锁成功")
return { true }