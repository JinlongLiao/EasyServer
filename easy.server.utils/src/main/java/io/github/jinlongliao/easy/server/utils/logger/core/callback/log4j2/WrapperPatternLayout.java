package io.github.jinlongliao.easy.server.utils.logger.core.callback.log4j2;


import io.github.jinlongliao.easy.server.utils.logger.core.callback.LoggerCallback;
import io.github.jinlongliao.easy.server.utils.logger.core.callback.LoggerCallbackFactory;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.*;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.nio.charset.Charset;
import java.util.Map;

import static org.apache.logging.log4j.core.layout.PatternLayout.newSerializerBuilder;

/**
 * @author: liaojinlong
 * @date: 2023/5/28 20:20
 */
@Plugin(name = "WrapperPatternLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class WrapperPatternLayout extends AbstractStringLayout {
    private final PatternLayout patternLayout;

    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        StringBuilder text = toText(this.patternLayout.getEventSerializer(), event, getStringBuilder());
        for (LoggerCallback loggerCallback : LoggerCallbackFactory.LOGGER_CALLBACKS) {
            text = loggerCallback.loggerCall(text);
        }
        final Encoder<StringBuilder> encoder = getStringBuilderEncoder();
        encoder.encode(text, destination);
        trimToMaxSize(text);

    }

    /**
     * Creates a text representation of the specified log event
     * and writes it into the specified StringBuilder.
     * <p>
     * Implementations are free to return a new StringBuilder if they can
     * detect in advance that the specified StringBuilder is too small.
     */
    private StringBuilder toText(final Serializer2 serializer, final LogEvent event,
                                 final StringBuilder destination) {
        return serializer.toSerializable(event, destination);
    }


    @Override
    public String toString() {
        return patternLayout.toString();
    }

    @Deprecated
    @PluginFactory
    public static PatternLayout createLayout(String pattern, PatternSelector patternSelector, Configuration config, RegexReplacement replace, Charset charset, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, String headerPattern, String footerPattern) {
        return PatternLayout.createLayout(pattern, patternSelector, config, replace, charset, alwaysWriteExceptions, noConsoleNoAnsi, headerPattern, footerPattern);
    }

    public static PatternLayout createDefaultLayout() {
        return PatternLayout.createDefaultLayout();
    }

    /**
     * Constructs a PatternLayout using the supplied conversion pattern.
     *
     * @param config                The Configuration.
     * @param replace               The regular expression to match.
     * @param eventPattern          conversion pattern.
     * @param patternSelector       The PatternSelector.
     * @param charset               The character set.
     * @param alwaysWriteExceptions Whether or not exceptions should always be handled in this pattern (if {@code true},
     *                              exceptions will be written even if the pattern does not specify so).
     * @param disableAnsi           If {@code "true"}, do not output ANSI escape codes
     * @param noConsoleNoAnsi       If {@code "true"} (default) and {@link System#console()} is null, do not output ANSI escape codes
     * @param headerPattern         header conversion pattern.
     * @param footerPattern         footer conversion pattern.
     */
    private WrapperPatternLayout(final Configuration config, final RegexReplacement replace, final String eventPattern,
                                 final PatternSelector patternSelector, final Charset charset, final boolean alwaysWriteExceptions,
                                 final boolean disableAnsi, final boolean noConsoleNoAnsi, final String headerPattern,
                                 final String footerPattern) {
        super(config, charset,
                newSerializerBuilder()
                        .setConfiguration(config)
                        .setReplace(replace)
                        .setPatternSelector(patternSelector)
                        .setAlwaysWriteExceptions(alwaysWriteExceptions)
                        .setDisableAnsi(disableAnsi)
                        .setNoConsoleNoAnsi(noConsoleNoAnsi)
                        .setPattern(headerPattern)
                        .build(),
                newSerializerBuilder()
                        .setConfiguration(config)
                        .setReplace(replace)
                        .setPatternSelector(patternSelector)
                        .setAlwaysWriteExceptions(alwaysWriteExceptions)
                        .setDisableAnsi(disableAnsi)
                        .setNoConsoleNoAnsi(noConsoleNoAnsi)
                        .setPattern(footerPattern)
                        .build());
        this.patternLayout = PatternLayout.newBuilder()
                .withConfiguration(config)
                .withRegexReplacement(replace)
                .withPattern(eventPattern)
                .withPatternSelector(patternSelector)
                .withCharset(charset)
                .withAlwaysWriteExceptions(alwaysWriteExceptions)
                .withDisableAnsi(disableAnsi)
                .withNoConsoleNoAnsi(noConsoleNoAnsi)
                .withDisableAnsi(noConsoleNoAnsi)
                .withHeader(headerPattern)
                .withFooter(footerPattern)
                .build();
    }


    @Override
    public boolean requiresLocation() {
        return patternLayout.requiresLocation();
    }

    @Deprecated
    public static Serializer createSerializer(Configuration configuration, RegexReplacement replace, String pattern, String defaultPattern, PatternSelector patternSelector, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
        return PatternLayout.createSerializer(configuration, replace, pattern, defaultPattern, patternSelector, alwaysWriteExceptions, noConsoleNoAnsi);
    }


    @Override
    public Map<String, String> getContentFormat() {
        return patternLayout.getContentFormat();
    }

    @Override
    public String toSerializable(LogEvent event) {
        return patternLayout.toSerializable(event);
    }


    public static WrapperPatternLayout createDefaultLayout(final Configuration configuration) {
        return newBuilder().withConfiguration(configuration).build();
    }

    @PluginBuilderFactory
    public static WrapperPatternLayout.Builder newBuilder() {
        return new WrapperPatternLayout.Builder();
    }


    @Override
    public Charset getCharset() {
        return patternLayout.getCharset();
    }

    @Override
    public String getContentType() {
        return patternLayout.getContentType();
    }

    @Override
    public byte[] getFooter() {
        return patternLayout.getFooter();
    }

    @Override
    public Serializer getFooterSerializer() {
        return patternLayout.getFooterSerializer();
    }

    @Override
    public byte[] getHeader() {
        return patternLayout.getHeader();
    }

    @Override
    public Serializer getHeaderSerializer() {
        return patternLayout.getHeaderSerializer();
    }


    @Override
    public byte[] toByteArray(LogEvent event) {
        return patternLayout.toByteArray(event);
    }

    @Override
    public Configuration getConfiguration() {
        return patternLayout.getConfiguration();
    }


    /**
     * Custom PatternLayout builder. Use the {@link PatternLayout#newBuilder() builder factory method} to create this.
     */
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<WrapperPatternLayout> {

        @PluginBuilderAttribute
        private String pattern = PatternLayout.DEFAULT_CONVERSION_PATTERN;

        @PluginElement("PatternSelector")
        private PatternSelector patternSelector;

        @PluginConfiguration
        private Configuration configuration;

        @PluginElement("Replace")
        private RegexReplacement regexReplacement;

        // LOG4J2-783 use platform default by default
        @PluginBuilderAttribute
        private Charset charset = Charset.defaultCharset();

        @PluginBuilderAttribute
        private boolean alwaysWriteExceptions = true;

        @PluginBuilderAttribute
        private boolean disableAnsi = !useAnsiEscapeCodes();

        @PluginBuilderAttribute
        private boolean noConsoleNoAnsi;

        @PluginBuilderAttribute
        private String header;

        @PluginBuilderAttribute
        private String footer;

        private Builder() {
        }

        private boolean useAnsiEscapeCodes() {
            final PropertiesUtil propertiesUtil = PropertiesUtil.getProperties();
            final boolean isPlatformSupportsAnsi = !propertiesUtil.isOsWindows();
            final boolean isJansiRequested = !propertiesUtil.getBooleanProperty("log4j.skipJansi", true);
            return isPlatformSupportsAnsi || isJansiRequested;
        }

        /**
         * @param pattern The pattern. If not specified, defaults to DEFAULT_CONVERSION_PATTERN.
         */
        public WrapperPatternLayout.Builder withPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }

        /**
         * @param patternSelector Allows different patterns to be used based on some selection criteria.
         */
        public WrapperPatternLayout.Builder withPatternSelector(final PatternSelector patternSelector) {
            this.patternSelector = patternSelector;
            return this;
        }

        /**
         * @param configuration The Configuration. Some Converters require access to the Interpolator.
         */
        public WrapperPatternLayout.Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        /**
         * @param regexReplacement A Regex replacement
         */
        public WrapperPatternLayout.Builder withRegexReplacement(final RegexReplacement regexReplacement) {
            this.regexReplacement = regexReplacement;
            return this;
        }

        /**
         * @param charset The character set. The platform default is used if not specified.
         */
        public WrapperPatternLayout.Builder withCharset(final Charset charset) {
            // LOG4J2-783 if null, use platform default by default
            if (charset != null) {
                this.charset = charset;
            }
            return this;
        }

        /**
         * @param alwaysWriteExceptions If {@code "true"} (default) exceptions are always written even if the pattern contains no exception tokens.
         */
        public WrapperPatternLayout.Builder withAlwaysWriteExceptions(final boolean alwaysWriteExceptions) {
            this.alwaysWriteExceptions = alwaysWriteExceptions;
            return this;
        }

        /**
         * @param disableAnsi If {@code "true"} (default is value of system property `log4j.skipJansi`, or `true` if undefined),
         *                    do not output ANSI escape codes
         */
        public WrapperPatternLayout.Builder withDisableAnsi(final boolean disableAnsi) {
            this.disableAnsi = disableAnsi;
            return this;
        }

        /**
         * @param noConsoleNoAnsi If {@code "true"} (default is false) and {@link System#console()} is null, do not output ANSI escape codes
         */
        public WrapperPatternLayout.Builder withNoConsoleNoAnsi(final boolean noConsoleNoAnsi) {
            this.noConsoleNoAnsi = noConsoleNoAnsi;
            return this;
        }

        /**
         * @param header The footer to place at the top of the document, once.
         */
        public WrapperPatternLayout.Builder withHeader(final String header) {
            this.header = header;
            return this;
        }

        /**
         * @param footer The footer to place at the bottom of the document, once.
         */
        public WrapperPatternLayout.Builder withFooter(final String footer) {
            this.footer = footer;
            return this;
        }

        @Override
        public WrapperPatternLayout build() {
            // should work with a null configuration
            return new WrapperPatternLayout(configuration, regexReplacement, pattern, patternSelector, charset,
                    alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi, header, footer);
        }
    }
}
