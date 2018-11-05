---
--- redis分布式锁解锁
--- Created by seer.
--- DateTime: 2018/11/5 10:45
---
local getValue = redis.call('get', KEYS[1])
if getValue == false then
    return 1
end
if getValue ~= KEYS[2] then
    return 0
end
return redis.call('del', KEYS[1])