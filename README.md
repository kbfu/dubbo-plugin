### 如何使用
* Clone这里的代码首先[dubbo-test](https://github.com/kbfu/dubbo-test)
* 本地运行zk，可以使用docker
    ```shell
    docker run --name some-zookeeper -p 2181:2181 -it --rm zookeeper
    ```
* 然后运行dubbo-test里的org.apache.dubbo.samples.provider.Application主类，让该范例代码注册到zk上
* 使用`mvn clean package`编译本项目
* 在target中得到dubbo-plugin-*.jar这个文件，将其copy到jmeter的lib/ext目录下
* 修改一下JMeter的heap设置，我这里改成了`-Xms2g -Xmx2g -XX:MaxMetaspaceSize=512m`
* 启动JMeter，你会看到一个新的configuration和sampler组件，分别叫`Dubbo Configuration`和`Dubbo Sampler`
  * Dubbo Configuration如下所示
  ![config.png](doc%2Fconfig.png)
  大多数配置和你在创建dubbo ReferenceConfig对象时是一样的，这里要注意*Dubbo variable*这个配置，之后会在Sampler中引用使用
  * Dubbo Sampler如下所示
  ![sampler.png](doc%2Fsampler.png)
  在*Dubbo variable*这一栏引用刚才配置的Configuration。底下的value-type配置对应dubbo的泛化引用调用的方式，如果你不熟悉可以参考[这里](https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/advanced-features-and-usage/service/generic-reference/#%E9%80%9A%E8%BF%87api%E4%BD%BF%E7%94%A8%E6%B3%9B%E5%8C%96%E8%B0%83%E7%94%A8)
* 现在让我们启动一下
  ![res1.png](doc%2Fres1.png)
  ![res2.png](doc%2Fres2.png)
  ![res3.png](doc%2Fres3.png)
  结果如上
* 这个jmx可以在sample路径下找到

### 注意
目前在JMeter 5.6.2中测试基本可以使用，但是可能会存在一些问题