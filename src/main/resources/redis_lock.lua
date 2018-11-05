---
--- redis分布式锁加锁
--- Created by seer.
--- DateTime: 2018/11/5 10:45
---
local result = redis.call('set', KEYS[1], KEYS[2], 'ex', KEYS[3], 'nx')
if result == false then
    return 0
elseif type(result) == 'table' and result['ok'] == 'OK' then
    return 1
end
return 0
