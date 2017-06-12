package com.zk.api.helper;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.*;

@Component
public class MessageSource extends AbstractMessageSource implements BeanClassLoaderAware {


    //data引用的business
    public MessageSource() {
        setBaseNames("dubbo","web");
    }

    private static MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(new MessageSource(),Locale.CHINA);

//    public static MessageSourceAccessor getAccessor() {
//        //只读取   中文  语言包
//        return new MessageSourceAccessor(messageSource,, Locale.CHINA);
//    }

    public static String message(String s) {
        return messageSourceAccessor.getMessage(s);
    }

    private String[] baseNames;

    private String defaultEncoding;

    private boolean fallbackToSystemLocale = true;

    private long cacheMillis = -1;

    private ClassLoader bundleClassLoader;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /**
     * Cache to hold loaded ResourceBundles.
     * This Map is keyed with the bundle basename, which holds a Map that is
     * keyed with the Locale and in turn holds the ResourceBundle instances.
     * This allows for very efficient hash lookups, significantly faster
     * than the ResourceBundle class's own cache.
     */
    private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles =
            new HashMap<String, Map<Locale, ResourceBundle>>();

    /**
     * Cache to hold already generated MessageFormats.
     * This Map is keyed with the ResourceBundle, which holds a Map that is
     * keyed with the message code, which in turn holds a Map that is keyed
     * with the Locale and holds the MessageFormat values. This allows for
     * very efficient hash lookups without concatenated keys.
     *
     * @see #getMessageFormat
     */
    private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats =
            new HashMap<ResourceBundle, Map<String, Map<Locale, MessageFormat>>>();

    /**
     * Set an array of basenames, each following {@link ResourceBundle}
     * conventions: essentially, a fully-qualified classpath location. If it
     * doesn't contain a package qualifier (such as {@code org.mypackage}),
     * it will be resolved from the classpath root.
     * <p>The associated resource bundles will be checked sequentially
     * when resolving a message code. Note that message definitions in a
     * <i>previous</i> resource bundle will override ones in a later bundle,
     * due to the sequential lookup.
     * <p>Note that ResourceBundle names are effectively classpath locations: As a
     * consequence, the JDK's standard ResourceBundle treats dots as package separators.
     * This means that "test.theme" is effectively equivalent to "test/theme",
     * just like it is for programmatic {@code java.util.ResourceBundle} usage.
     *
     * @see #setBaseNames
     * @see ResourceBundle#getBundle(String)
     */
    public void setBaseNames(String... baseNames) {
        if (baseNames != null) {
            this.baseNames = new String[baseNames.length];
            for (int i = 0; i < baseNames.length; i++) {
                String basename = baseNames[i];
                Assert.hasText(basename, "Basename must not be empty");
                this.baseNames[i] = basename.trim();
            }
        } else {
            this.baseNames = new String[0];
        }
    }

    /**
     * Set the default charset to use for parsing resource bundle files.
     * <p>Default is none, using the {@code java.util.ResourceBundle}
     * default encoding: ISO-8859-1.
     * and more flexibility in setting of an encoding per file.
     */
    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    /**
     * Set whether to fall back to the system Locale if no files for a specific
     * Locale have been found. Default is "true"; if this is turned off, the only
     * fallback will be the default file (e.g. "messages.properties" for
     * basename "messages").
     * <p>Falling back to the system Locale is the default behavior of
     * {@code java.util.ResourceBundle}. However, this is often not desirable
     * in an application server environment, where the system Locale is not relevant
     * to the application at all: Set this flag to "false" in such a scenario.
     */
    public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
        this.fallbackToSystemLocale = fallbackToSystemLocale;
    }

    /**
     * Set the number of seconds to cache loaded resource bundle files.
     * <ul>
     * <li>Default is "-1", indicating to cache forever.
     * <li>A positive number will expire resource bundles after the given
     * number of seconds. This is essentially the interval between refresh checks.
     * Note that a refresh attempt will first check the last-modified timestamp
     * of the file before actually reloading it; so if files don't change, this
     * interval can be set rather low, as refresh attempts will not actually reload.
     * <li>A value of "0" will check the last-modified timestamp of the file on
     * every message access. <b>Do not use this in a production environment!</b>
     * <li><b>Note that depending on your ClassLoader, expiration might not work reliably
     * since the ClassLoader may hold on to a cached version of the bundle file.</b>
     * Consider {@link org.springframework.context.support.ReloadableResourceBundleMessageSource} in combination
     * with resource bundle files in a non-classpath location.
     * </ul>
     */
    public void setCacheSeconds(int cacheSeconds) {
        this.cacheMillis = (cacheSeconds * 1000);
    }

    /**
     * Set the ClassLoader to load resource bundles with.
     * <p>Default is the containing BeanFactory's
     * {@link org.springframework.beans.factory.BeanClassLoaderAware bean ClassLoader},
     * or the default ClassLoader determined by
     * {@link org.springframework.util.ClassUtils#getDefaultClassLoader()}
     * if not running within a BeanFactory.
     */
    public void setBundleClassLoader(ClassLoader classLoader) {
        this.bundleClassLoader = classLoader;
    }

    /**
     * Return the ClassLoader to load resource bundles with.
     * <p>Default is the containing BeanFactory's bean ClassLoader.
     *
     * @see #setBundleClassLoader
     */
    protected ClassLoader getBundleClassLoader() {
        return (this.bundleClassLoader != null ? this.bundleClassLoader : this.beanClassLoader);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }


    /**
     * Resolves the given message code as key in the registered resource bundles,
     * returning the value found in the bundle as-is (without MessageFormat parsing).
     */
    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String result = null;
        for (int i = 0; result == null && i < this.baseNames.length; i++) {
            ResourceBundle bundle = getResourceBundle(this.baseNames[i], locale);
            if (bundle != null) {
                result = getStringOrNull(bundle, code);
            }
        }
        return result;
    }

    /**
     * Resolves the given message code as key in the registered resource bundles,
     * using a cached MessageFormat instance per message code.
     */
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        MessageFormat messageFormat = null;
        for (int i = 0; messageFormat == null && i < this.baseNames.length; i++) {
            ResourceBundle bundle = getResourceBundle(this.baseNames[i], locale);
            if (bundle != null) {
                messageFormat = getMessageFormat(bundle, code, locale);
            }
        }
        return messageFormat;
    }


    /**
     * Return a ResourceBundle for the given basename and code,
     * fetching already generated MessageFormats from the cache.
     *
     * @param basename the basename of the ResourceBundle
     * @param locale   the Locale to find the ResourceBundle for
     * @return the resulting ResourceBundle, or {@code null} if none
     * found for the given basename and Locale
     */
    protected ResourceBundle getResourceBundle(String basename, Locale locale) {
        if (this.cacheMillis >= 0) {
            // Fresh ResourceBundle.getBundle call in order to let ResourceBundle
            // do its native caching, at the expense of more extensive lookup steps.
            return doGetBundle(basename, locale);
        } else {
            // Cache forever: prefer locale cache over repeated getBundle calls.
            synchronized (this.cachedResourceBundles) {
                Map<Locale, ResourceBundle> localeMap = this.cachedResourceBundles.get(basename);
                if (localeMap != null) {
                    ResourceBundle bundle = localeMap.get(locale);
                    if (bundle != null) {
                        return bundle;
                    }
                }
                try {
                    ResourceBundle bundle = doGetBundle(basename, locale);
                    if (localeMap == null) {
                        localeMap = new HashMap<Locale, ResourceBundle>();
                        this.cachedResourceBundles.put(basename, localeMap);
                    }
                    localeMap.put(locale, bundle);
                    return bundle;
                } catch (MissingResourceException ex) {
                    // Assume bundle not found
                    // -> do NOT throw the exception to allow for checking parent message source.
                    return null;
                }
            }
        }
    }

    /**
     * Obtain the resource bundle for the given basename and Locale.
     *
     * @param basename the basename to look for
     * @param locale   the Locale to look for
     * @return the corresponding ResourceBundle
     * @throws MissingResourceException if no matching bundle could be found
     * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
     * @see #getBundleClassLoader()
     */
    protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
        if ((this.defaultEncoding != null && !"ISO-8859-1".equals(this.defaultEncoding)) ||
                !this.fallbackToSystemLocale || this.cacheMillis >= 0) {
            return ResourceBundle.getBundle(basename, locale, getBundleClassLoader(), new MessageSourceControl());
        } else {
            // Good old standard call...
            return ResourceBundle.getBundle(basename, locale, getBundleClassLoader());
        }
    }

    /**
     * Return a MessageFormat for the given bundle and code,
     * fetching already generated MessageFormats from the cache.
     *
     * @param bundle the ResourceBundle to work on
     * @param code   the message code to retrieve
     * @param locale the Locale to use to build the MessageFormat
     * @return the resulting MessageFormat, or {@code null} if no message
     * defined for the given code
     * @throws MissingResourceException if thrown by the ResourceBundle
     */
    protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale)
            throws MissingResourceException {

        synchronized (this.cachedBundleMessageFormats) {
            Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);
            Map<Locale, MessageFormat> localeMap = null;
            if (codeMap != null) {
                localeMap = codeMap.get(code);
                if (localeMap != null) {
                    MessageFormat result = localeMap.get(locale);
                    if (result != null) {
                        return result;
                    }
                }
            }

            String msg = getStringOrNull(bundle, code);
            if (msg != null) {
                if (codeMap == null) {
                    codeMap = new HashMap<String, Map<Locale, MessageFormat>>();
                    this.cachedBundleMessageFormats.put(bundle, codeMap);
                }
                if (localeMap == null) {
                    localeMap = new HashMap<Locale, MessageFormat>();
                    codeMap.put(code, localeMap);
                }
                MessageFormat result = createMessageFormat(msg, locale);
                localeMap.put(locale, result);
                return result;
            }

            return null;
        }
    }

    private String getStringOrNull(ResourceBundle bundle, String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException ex) {
            // Assume key not found
            // -> do NOT throw the exception to allow for checking parent message source.
            return null;
        }
    }

    /**
     * Show the configuration of this MessageSource.
     */
    @Override
    public String toString() {
        return getClass().getName() + ": basenames=[" +
                org.springframework.util.StringUtils.arrayToCommaDelimitedString(this.baseNames) + "]";
    }


    /**
     * Custom implementation of Java 6's {@code ResourceBundle.Control},
     * adding support for custom file encodings, deactivating the fallback to the
     * system locale and activating ResourceBundle's native cache, if desired.
     */
    private class MessageSourceControl extends ResourceBundle.Control {

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            if (format.equals("java.properties")) {
                String bundleName = toBundleName(baseName, locale);
                final String resourceName = toResourceName(bundleName, "properties");
                final ClassLoader classLoader = loader;
                final boolean reloadFlag = reload;
                InputStream stream;
                try {
                    stream = AccessController.doPrivileged(
                            new PrivilegedExceptionAction<InputStream>() {
                                @Override
                                public InputStream run() throws IOException {
                                    InputStream is = null;
                                    if (reloadFlag) {
                                        URL url = classLoader.getResource(resourceName);
                                        if (url != null) {
                                            URLConnection connection = url.openConnection();
                                            if (connection != null) {
                                                connection.setUseCaches(false);
                                                is = connection.getInputStream();
                                            }
                                        }
                                    } else {
                                        is = classLoader.getResourceAsStream(resourceName);
                                    }
                                    return is;
                                }
                            });
                } catch (PrivilegedActionException ex) {
                    throw (IOException) ex.getException();
                }
                if (stream != null) {
                    try {
                        return (defaultEncoding != null ?
                                new PropertyResourceBundle(new InputStreamReader(stream, defaultEncoding)) :
                                new PropertyResourceBundle(stream));
                    } finally {
                        stream.close();
                    }
                } else {
                    return null;
                }
            } else {
                return super.newBundle(baseName, locale, format, loader, reload);
            }
        }

        @Override
        public Locale getFallbackLocale(String baseName, Locale locale) {
            return (fallbackToSystemLocale ? super.getFallbackLocale(baseName, locale) : null);
        }

        @Override
        public long getTimeToLive(String baseName, Locale locale) {
            return (cacheMillis >= 0 ? cacheMillis : super.getTimeToLive(baseName, locale));
        }

        @Override
        public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
            if (super.needsReload(baseName, locale, format, loader, bundle, loadTime)) {
                cachedBundleMessageFormats.remove(bundle);
                return true;
            } else {
                return false;
            }
        }
    }

}
