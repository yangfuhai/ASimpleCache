ASimpleCache
============


----
 ASimpleCache 是一个为android制定的 轻量级的 开源缓存框架。轻量到只有一个java文件（由十几个类精简而来）。它的下载地址是：https://github.com/yangfuhai/ASimpleCache


---
## 1、它可以缓存什么东西？
普通的字符串、json、序列化的java对象，和 字节数字。


## 2、它有什么特色？
* 特色主要是：
 *  1：轻，轻到只有一个JAVA文件。
 *   2：可配置，可以配置缓存路径，缓存大小，缓存数量等。
 *  3：可以设置缓存超时时间，缓存超时自动失效，并被删除。

##3、它在android中可以用在哪些场景？
* 1、替换SharePreference当做配置文件
* 2、可以缓存网络请求数据，比如oschina的android客户端可以缓存http请求的新闻内容，缓存时间假设为1个小时，超时后自动失效，让客户端重新请求新的数据，减少客户端流量，同时减少服务器并发量。
* 3、您来说...

##4、如何使用 ASimpleCache？
以下有个小的demo，希望您能喜欢：

```java
ACache mCache = ACache.get(this);
mCache.put("test_key", "test value");
```


#关于作者无为
* 个人博客：[http://www.yangfuhai.com](http://www.yangfuhai.com)
* 交流QQ群 ： 192341294（已满） 246710918（未满）


