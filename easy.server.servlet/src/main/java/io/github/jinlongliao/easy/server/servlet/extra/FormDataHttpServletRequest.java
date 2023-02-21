package io.github.jinlongliao.easy.server.servlet.extra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 用于解析 multipart/form-data
 *
 * @author: liaojinlong
 * @date: 2022/12/6 22:47
 */
public class FormDataHttpServletRequest extends HttpServletRequestWrapper {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final boolean needParse;
    private Enumeration<String> extraParamNames;
    private Map<String, String[]> extraParams;

    private final ByteArrayOutputStream cachedContent;

    @Nullable
    private final Integer contentCacheLimit;

    @Nullable
    private ContentCachingInputStream inputStream;

    public ByteArrayOutputStream getCachedContent() {
        return cachedContent;
    }

    @Nullable
    public Integer getContentCacheLimit() {
        return contentCacheLimit;
    }

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public FormDataHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.needParse = request.getContentType().toLowerCase().contains("multipart/form-data");
        int contentLength = request.getContentLength();
        this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        this.contentCacheLimit = null;
    }

    private void parseRequest() {
        if (Objects.nonNull(this.extraParamNames)) {
            return;
        }
        if (!needParse) {
            extraParamNames = super.getParameterNames();
            extraParams = super.getParameterMap();
            return;
        }
        try {
            this.inputStream = new ContentCachingInputStream(super.getInputStream());
            byte[] bytes = new byte[1024];
            while (this.inputStream.read(bytes) > 1) {

            }
            this.realParser();
            extraParamNames = super.getParameterNames();
            extraParams = super.getParameterMap();


        } catch (IOException e) {
            extraParamNames = super.getParameterNames();
            extraParams = super.getParameterMap();
            log.error(e.getLocalizedMessage(), e);
        }

    }

    private void realParser() {
        String postData =new String( cachedContent.toByteArray(),StandardCharsets.UTF_8);


    }

    @Override
    @Nullable
    public ServletInputStream getInputStream() throws IOException {
        this.parseRequest();
        return Objects.isNull(this.inputStream) ? super.getInputStream() : this.inputStream;
    }

    /**
     * The default behavior of this method is to return
     * getParameter(String name) on the wrapped request object.
     */
    public String getParameter(String name) {
        this.parseRequest();
        String[] values = getParameterValues(name);
        return Objects.isNull(values) ? "" : values[0];
    }


    /**
     * The default behavior of this method is to return getParameterMap()
     * on the wrapped request object.
     */
    public Map<String, String[]> getParameterMap() {
        this.parseRequest();
        return extraParams;
    }


    /**
     * The default behavior of this method is to return getParameterNames()
     * on the wrapped request object.
     */
    public Enumeration<String> getParameterNames() {
        this.parseRequest();
        return extraParamNames;
    }


    /**
     * The default behavior of this method is to return
     * getParameterValues(String name) on the wrapped request object.
     */
    public String[] getParameterValues(String name) {
        this.parseRequest();
        return this.extraParams.get(name);
    }


    private class ContentCachingInputStream extends ServletInputStream {
        private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        private final ServletInputStream is;

        private boolean overflow = false;

        public ContentCachingInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            int ch = this.is.read();
            if (ch != -1 && !this.overflow) {
                if (contentCacheLimit != null && cachedContent.size() == contentCacheLimit) {
                    this.overflow = true;
                    handleContentOverflow(contentCacheLimit);
                } else {
                    cachedContent.write(ch);
                }
            }
            return ch;
        }

        protected void handleContentOverflow(int contentCacheLimit) {
            log.warn("read overflow :{} ", contentCacheLimit);
        }


        @Override
        public int read(byte[] b) throws IOException {
            int count = this.is.read(b);
            writeToCache(b, 0, count);
            return count;
        }

        private void writeToCache(final byte[] b, final int off, int count) {
            if (!this.overflow && count > 0) {
                if (contentCacheLimit != null &&
                        count + cachedContent.size() > contentCacheLimit) {
                    this.overflow = true;
                    cachedContent.write(b, off, contentCacheLimit - cachedContent.size());
                    handleContentOverflow(contentCacheLimit);
                    return;
                }
                cachedContent.write(b, off, count);
            }
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.read(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        @Override
        public int readLine(final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.readLine(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        @Override
        public boolean isFinished() {
            return this.is.isFinished();
        }

        @Override
        public boolean isReady() {
            return this.is.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            this.is.setReadListener(readListener);
        }
    }

}
