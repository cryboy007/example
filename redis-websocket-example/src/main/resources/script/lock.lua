--[[
    1、角标是从1开始，比如 KEYS[1]
    2、redis.call('一个不存在的key') 返回的类型是 boolean 类型
    3、redis.call('一个不存在的key') or nil 返回的是 nil
    4、判断是否是nil 需要使用 type(值) ~= "nil"(此处判断的是不等于nil,`nil`需要加上双引号)
    5、需要注意redis.call命令的返回值类型
]]

redis.log(redis.LOG_NOTICE, "开始获取redis锁")

for i, v in pairs(KEYS) do
    redis.log(redis.LOG_NOTICE, "key" .. i .. " = " .. v)
end

for i, v in pairs(ARGV) do
    redis.log(redis.LOG_NOTICE, "argv" .. i .. " = " .. v)
end

local lockKey = tostring(KEYS[1])
local localValue = tostring(ARGV[1])
-- 过期时间，单位毫秒
local expire = tonumber(ARGV[2])

-- 如果 get 获取值不存在，此处返回的是 false ，如果后面跟个 nil 则返回的是 nil ,如果lockKey对应的值就是false,则返回的还是false
local oldLockValue = redis.call('get', lockKey) or nil;
redis.log(redis.LOG_NOTICE, "lockKey: " .. lockKey .. " 对应旧的锁的值为: " .. tostring(oldLockValue))

-- 说明锁别别人获取了
if type(oldLockValue) ~= "nil" then
    redis.log(redis.LOG_NOTICE, "锁lockKey: " .. lockKey .. " 被别人持有.")
    return { false }
    --说明没有人获取锁，自己获取锁
else
    -- 打一个断点
    redis.breakpoint()

    -- 获取锁
    local lockResult = redis.call('set', lockKey, localValue, 'PX', expire, 'NX') or nil

    if (type(lockResult) ~= "nil") then
        for i, v in pairs(lockResult) do
            redis.log(redis.LOG_NOTICE, "i: " .. i .. " v: " .. v)
        end
    end

    redis.log(redis.LOG_NOTICE, "lockKey: " .. lockKey .. " 获取锁结果 " .. " " .. tostring(lockResult))

    if (type(lockResult) == "nil") then
        redis.log(redis.LOG_NOTICE, "获取锁失败")
        return { false }
    end

    redis.log(redis.LOG_NOTICE, "获取锁成功")

    return { true }
end