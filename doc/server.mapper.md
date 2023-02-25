[easy.server.mapper](https://github.com/JinlongLiao/EasyServer/tree/main/easy.server.mapper) 介绍
---

## 后端开发中，消息转换常见问题


- Map 中的数据 转换成实体Bean
- 数组 中的数据 转换成实体Bean
- Servet 中的 param 转换成实体Bean

以上的三个问题是最常见的消息转换困扰。
</br>
#### 以Map 举例

常见做法是
1. 手动转换
```java
Map<String,Object> dataMap;
Person person;

person.setXX(dataMap.get("XXX"))
        ......
        ......
        ......
```
弊端是 操作繁琐

2. 采用反射技术实现

```java
   Map<String,Object> dataMap;

   final Class<Person> personClass = Person.class;
    Object person = personClass.newInstance();
    final Field[] fields = personClass.getDeclaredFields();
    for (Field field : fields) {
        // 特殊类型
        if (Modifier.isFinal(field.getModifiers())) {
            continue;
        }
      
        field.setAccessible(Boolean.TRUE);
        field.set(person, dataMap.get(field.getName()));
    }
```
操作简单，弊端是存在性能消耗

 

easy.server.mapper
解决实现
```java

  final Class<Person> personClass = Person.class;
  Person person = BeanCopier2Utils.getFullData2Object(Person.class).toMapConverter(dataMap);
```
操作简单，性能几乎同 手动转换

### 性能与反射对比

```java
public class ReflectTest {
    public static boolean warmup = false;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> dataMap = new TreeMap<String, Object>() {{
        put("grep", 1234);
        put("name", "liaojl");
        put("age", 26);
        put("birthday", new Date());
        put("arr", Arrays.asList("2312", "12423"));
        put("arr2", data);
        put("array", new int[]{1, 2, 3});
    }};
    private Object[] dataArray = new Object[]{13, "liaojl", 26, new Date(), Arrays.asList("2312", "12423"), data, new int[]{1, 2, 3}};
    private final ICoreData2Object2<Person> data2Object2 = BeanCopier2Utils.getFullData2Object(Person.class);

    @org.junit.Ignore
    @Test
    public void test() throws Exception {
        testMapCustomize2();
        testArrayCustomize2();
        testReflect();
        warmup = true;
        testReflect();
        testMapCustomize2();
        testArrayCustomize2();
    }

    public static final int SIZE = 1000000;

    private void testMapCustomize2() {
        final long start = System.currentTimeMillis();
        Person person;
        for (int i = 0; i < SIZE; i++) {
            final Class<Person> personClass = Person.class;
            person = data2Object2.toMapConverter(dataMap);
        }
        final long end = System.currentTimeMillis();
        if (warmup)
            System.out.println("testMapCustomize2:" + (end - start));
    }

    private void testArrayCustomize2() {
        final long start = System.currentTimeMillis();
        Person person = null;
        for (int i = 0; i < SIZE; i++) {
            person = data2Object2.toArrayConverter(dataArray);
        }
        final long end = System.currentTimeMillis();
        if (warmup)
            System.out.println("testArrayCustomize2:" + (end - start));
    }

    private void testReflect() throws Exception {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            final Class<Person> personClass = Person.class;
            Object person = personClass.newInstance();
            final Field[] fields = personClass.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                Ignore annotation = field.getAnnotation(Ignore.class);
                if (annotation != null) {
                    continue;
                }
                field.setAccessible(Boolean.TRUE);
                field.set(person, dataMap.get(field.getName()));
            }
        }
        final long end = System.currentTimeMillis();
        if (warmup)
            System.out.println("testReflect:" + (end - start));
    }
}
```

![mapper.test.png](img%2Fmapper.test.png)

easy.mapper性能消耗远远小于反射


## easy.mapper 的使用

### easy.mapper 介绍


#### 注意项
- 需要转换的Bean 必须包含一个无参构造方法
- 需要转换的属性必须包含其Getter 方法
1. 普通转换
```java
@Data
public class Mapper {
    private byte b;
    private Byte b1;
    private boolean bool;
    private Boolean bool1;
    private char c;
    private Character c1;
    private short s;
    private Short s1;
    private int i;
    private Integer i1;
    private long l;
    private Long l1;
    private float f;
    private Float f1;
    private double d;
    private Double d1;

    private Date date;
}
```
```java
// 转换MAP 到Person 并会对Person父类的属性进行扫描赋值
Person person1 = BeanCopier2Utils.getFullData2Object(Person.class).toMapConverter(dataMap);
        // 转换MAP 到Person 不会对Person父类的属性进行扫描赋值
Person person2 = BeanCopier2Utils.getData2Object(Person.class).toMapConverter(dataMap);
        // 转换Servlet 到Person 不会对Person父类的属性进行扫描赋值
Person person3 = BeanCopier2Utils.getFullData2WebObject(Person.class).toHttpServletRequestConverte(servelt);
        // 转换Servlet 到Person 并会对Person父类的属性进行扫描赋值
Person person4 = BeanCopier2Utils.getData2WebObject(Person.class).toHttpServletRequestConverte(servelt);

```

2. 字段忽略

```java
io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Ignore2
```
提供 ___@Ignore2___ 使用标记的字段在转换时并不会进行赋值操作

demo

```java
public class Person extends Grep implements IAnimal {
    private static final Logger log = LoggerFactory.getLogger(Person.class);
    @Ignore
    @Ignore2
    private int ignore;
}
```

3. 特殊字段转换

- 字段名称与bean 中属性名不对应
- 数据类型，双方不匹配
- 其他特殊类型


easy.mapper 默认对支持所有基础类型及其包装类，String 。详情查看 ___io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.IDataConverter___ 实现。针对特殊类型转。提供两种转换方案
- 重写 IDataConverter 实现

___io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.InnerConverter___ 接口 ，重写``` <T> T getT(Class<T> tClass, Object extra, Object data);```
针对不支持类型时，会调用此方法，自己仅需要 依据 参数tClass 的类型进行判断 进行返回相应值，参数中的data 为消息中的值

- 使用  ``` io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Mapping2 ```


```java
public @interface Mapping2 {
    /**
     * @return 映射源名称
     */
    String sourceName() default "";

    /**
     * set Method Name
     * @return
     */
    String putMethod() default "";


    /**
     * 针对非基本类型（String,byte,short,int,float,double,long,char）<br/>
     * 除外需要指定自定义 静态转换函数
     * eg:
     * <pre>
     *     public static Person person(Object obj){
     *         return (Person)obj;
     *     }
     * </pre>
     *
     * @return 数据强转函数名称
     */
    String converterMethod() default "";

    /**
     * @return Class Name
     * @see {@link Mapping2#converterMethod()}
     */
    Class converterClass() default InnerConverter.class;
}

```

    converterClass 手动指定转换自己的转换类，
    converterMethod 设置转换函数的名称  ___ 此函数必须为 public+static ___ 。
    putMethod  假设 bean 中的 属性a setter函数 不为 setA 通过 putMethod 可以指定新的setter函数
    sourceName 假设 mesaage 中的名字不是属性a 的名字  sourceName 可以指定获取源的名字


### 提前编译 支持
1. 手动指定

通过 ```io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig``` 可以指定 自动生成消息转换实现的 class 及其源文件

```java
      MapperStructConfig.setDev(true, "./target/", "./target/");
```

2. 通过 maven 插件实现 [maven-generator-plugin](https://github.com/JinlongLiao/EasyServer/tree/main/plugins/maven-generator-plugin)  

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <annotationProcessorPaths>
                        <annotationProcessorPath>
                            <groupId>io.github.jinlongliao</groupId>
                            <artifactId>easy.server.mapper</artifactId>
                            <version>${project.version}</version>
                        </annotationProcessorPath>
                        <annotationProcessorPath>
                            <groupId>io.github.jinlongliao</groupId>
                            <artifactId>easy.server.extend</artifactId>
                            <version>${project.version}</version>
                        </annotationProcessorPath>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>io.github.jinlongliao</groupId>
                <artifactId>maven-generator-plugin</artifactId>
                <version>${project.version}</version>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>java</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <arguments>
                        <argument>${project.basedir}/target/classes/</argument>
                        <argument>${project.basedir}/target/generated-sources/annotations/</argument>
                    </arguments>
                </configuration>
            </plugin>
        </plugins>
    </build>

```

![mapper-gen.png](img%2Fmapper-gen.png)

### spring 支持

将 ```io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactoryBean``` 设置spring 托管。既可在 spring 中 使用 IBeanMapper类型 进行操作，IBeanMapper封装了常见的类型操作推荐使用

```java
public interface IBeanMapper {
    /**
     * 基于Map 的转换
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return T
     */
    <T> T mapBeanMapper(Class<T> tClass, Map<String, Object> data);

    /**
     * 基于数组 的转换
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return T
     */
    <T> T arrayBeanMapper(Class<T> tClass, Object[] data);

    /**
     * 基于javax.servlet 的转换
     *
     * @param tClass
     * @param req
     * @param <T>
     * @return T
     */
    <T> T servletBeanMapper(Class<T> tClass, javax.servlet.http.HttpServletRequest req);

}
```










