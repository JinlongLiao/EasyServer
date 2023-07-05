package io.github.jinlongliao.easy.server.mapper.annotation;

import io.github.jinlongliao.easy.server.mapper.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Element;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URL;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @date 2022-12-29 12:22
 * @author: liaojinlong
 * @description: 配置文件 写入与读取工具
 **/

public class GeneratorHelper {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final ClassLoader CLASS_LOADER = MethodHandles.lookup().lookupClass().getClassLoader();

    public static List<String> loadAllConfigClass(String path) {
        List<String> strs = null;
        try {
            final Enumeration<URL> resources = CLASS_LOADER.getResources(path);

            if (Objects.nonNull(resources)) {

                strs = loadCacheFiles(resources);
            }
        } catch (final IOException ioe) {
            log.warn("Unable to preload plugins", ioe);
        }
        return strs;
    }

    private static List<String> loadCacheFiles(Enumeration<URL> resources) throws IOException {
        List<String> className = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String readLine;
                while (!StringUtil.isEmpty((readLine = bufferedReader.readLine()))) {
                    className.add(readLine);
                }
            }
        }
        return className;
    }

    public static void writeClassToFile(final OutputStream os, Set<? extends Element> elementsAnnotatedWith) throws IOException {
        writeToFile(os, elementsAnnotatedWith.stream().map(Element::toString).collect(Collectors.toList()));
    }

    public static void writeToFile(final OutputStream os, List<String> strs) throws IOException {
        if (Objects.isNull(strs) || strs.size() == 0) {
            return;
        }
        try (final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
            boolean notFirstLine = false;
            for (String str : strs) {
                if (notFirstLine) {
                    writer.newLine();
                }
                notFirstLine = true;
                writer.write(str);
            }
        }
    }

    public static void generatorResource(Object... args) {
        Iterator<LoaderGenerator> generatorIterator = ServiceLoader.load(LoaderGenerator.class, CLASS_LOADER).iterator();
        while (generatorIterator.hasNext()) {
            LoaderGenerator loaderGenerator = generatorIterator.next();
            try {
                loaderGenerator.loader(args);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
