ASimpleCache
============


----
 ASimpleCache 是一个为android制定的 轻量级的 开源缓存框架。轻量到只有一个java文件（由十几个类精简而来）。


---
## 1、它可以缓存什么东西？
普通的字符串、json、序列化的java对象，和 byte数据。


## 2、它有什么特色？
* 特色主要是：
 *  1：轻，轻到只有一个JAVA文件。
 *  2：可配置，可以配置缓存路径，缓存大小，缓存数量等。
 *  3：可以设置缓存超时时间，缓存超时自动失效，并被删除。
 *  4：支持多进程。

##3、它在android中可以用在哪些场景？
* 1、替换SharePreference当做配置文件
* 2、可以缓存网络请求数据，比如oschina的android客户端可以缓存http请求的新闻内容，缓存时间假设为1个小时，超时后自动失效，让客户端重新请求新的数据，减少客户端流量，同时减少服务器并发量。
* 3、您来说...


##4、如何使用 ASimpleCache？
以下有个小的demo，希望您能喜欢：

```java
ACache mCache = ACache.get(this);
mCache.put("test_key1", "test value");
mCache.put("test_key2", "test value", 10);//保存10秒，如果超过10秒去获取这个key，将为null
mCache.put("test_key3", "test value", 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null
```
获取数据
```java
ACache mCache = ACache.get(this);
String value = mCache.getAsString("test_key1");
```

#关于作者无为
* 个人博客：[http://www.yangfuhai.com](http://www.yangfuhai.com)
* 交流QQ群 ： 192341294（已满） 246710918（未满）


