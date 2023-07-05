package io.github.jinlongliao.easy.server.mapper.utils;


import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;
import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.main.decompiler.PrintStreamLogger;
import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences;
import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.util.InterpreterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @date 2022-12-23 17:02
 * @author: liaojinlong
 * @description: 创建文件
 **/

public class MapperStructConfig {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String DEBUG = "io.github.jinlongliao.easy.server.mapper.debug";
    public static final String SAVE_CLASS_PATH = "io.github.jinlongliao.easy.server.mapper.debug.classpath";
    public static final String SAVE_JAVA_PATH = "io.github.jinlongliao.easy.server.mapper.debug.source_path";
    private static boolean saveClassFile = Boolean.parseBoolean(System.getProperty(DEBUG, "false"));
    private static String saveClassFilePath = System.getProperty(SAVE_CLASS_PATH, "./");
    private static String saveJavaFilePath = System.getProperty(SAVE_JAVA_PATH, "./");

    static Map<String, Object> options = new HashMap<>();

    static {
        options.put(IFernflowerPreferences.LOG_LEVEL, "warn");
        options.put(IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES, "1");
        options.put(IFernflowerPreferences.REMOVE_SYNTHETIC, "1");
        options.put(IFernflowerPreferences.REMOVE_BRIDGE, "1");
        options.put(IFernflowerPreferences.LITERALS_AS_IS, "1");
        options.put(IFernflowerPreferences.UNIT_TEST_MODE, "1");
        //        options.put(IFernflowerPreferences.BYTECODE_SOURCE_MAPPING, "1");
        options.put(IFernflowerPreferences.DUMP_ORIGINAL_LINES, "1");
        options.put(IFernflowerPreferences.IGNORE_INVALID_BYTECODE, "1");
        options.put(IFernflowerPreferences.VERIFY_ANONYMOUS_CLASSES, "1");
    }

    public static void saveClassFile(byte[] classes, String className) {
        if (!saveClassFile) {
            return;
        }
        try {
            String classFullName = className.replace('.', '/');
            File classFile = new File(saveClassFilePath + classFullName + ".class");
            File sourceFile = new File(saveJavaFilePath + classFullName + ".java");
            createFile(classFile, false);
            createFile(sourceFile, false);
            Files.write(classFile.toPath(), classes);
            getJavaSource(classFile, sourceFile, className);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private static void getJavaSource(File classFile, File sourceFile, String className) throws Exception {
        Decompiler decompiler = new Decompiler(sourceFile.getParentFile(), options);
        decompiler.addSource(classFile);
        decompiler.decompileContext();

        //        return javaSource[0]
        //                .replace("public class", "@javax.annotation.Generated(value=\"easy.server.mapper Generated\"," +
        //                        "date = \"" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "\"," +
        //                        " comments = \"common-mapper 动态生成\")\npublic class")
        //                .getBytes(StandardCharsets.UTF_8);
    }

    public static void createFile(File file, boolean dir) throws IOException {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            createFile(parentFile, true);
        }
        if (!file.exists()) {
            if (dir) {
                file.mkdir();
            } else {
                file.createNewFile();
            }
        }
    }

    public static void setDev(boolean saveClassFile, String saveClassFilePath, String saveJavaFilePath) {
        MapperStructConfig.saveClassFile = saveClassFile;
        MapperStructConfig.saveClassFilePath = saveClassFilePath;
        MapperStructConfig.saveJavaFilePath = saveJavaFilePath;
    }

    public static class Decompiler extends ConsoleDecompiler {
        private final HashMap<String, ZipFile> zipFiles = new HashMap<>();

        public Decompiler(File destination, Map<String, Object> options) {
            super(destination, options, new PrintStreamLogger(System.out));
        }

        @Override
        public byte[] getBytecode(String externalPath, String internalPath) throws IOException {
            File file = new File(externalPath);
            if (internalPath == null) {
                return InterpreterUtil.getBytes(file);
            } else {
                ZipFile archive = zipFiles.get(file.getName());
                if (archive == null) {
                    archive = new ZipFile(file);
                    zipFiles.put(file.getName(), archive);
                }
                ZipEntry entry = archive.getEntry(internalPath);
                if (entry == null)
                    throw new IOException("Entry not found: " + internalPath);
                return InterpreterUtil.getBytes(archive, entry);
            }
        }

        @Override
        public void saveClassFile(String path, String qualifiedName, String entryName, String content, int[] mapping) {
            content = content.replace("public class", "@jakarta.annotation.Generated(value=\"easy.server.mapper Generated\"," +
                    "date = \"" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "\"," +
                    " comments = \"common-mapper 动态生成\")\npublic class");
            super.saveClassFile(path, qualifiedName, entryName, content, mapping);
        }
    }
}
