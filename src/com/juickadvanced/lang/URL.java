package com.juickadvanced.lang;

/**
 * Created by san on 8/9/14.
 */


import com.juickadvanced.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;

/**
 * Class <code>URL</code> represents a Uniform Resource
 * Locator, a pointer to a "resource" on the World
 * Wide Web. A resource can be something as simple as a file or a
 * directory, or it can be a reference to a more complicated object,
 * such as a query to a database or to a search engine. More
 * information on the types of URLs and their formats can be found at:
 * <blockquote>
 *     <a href="http://www.socs.uts.edu.au/MosaicDocs-old/url-primer.html">
 *    <i>http://www.socs.uts.edu.au/MosaicDocs-old/url-primer.html</i></a>
 * </blockquote>
 * <p>
 * In general, a URL can be broken into several parts. The previous
 * example of a URL indicates that the protocol to use is
 * <code>http</code> (HyperText Transfer Protocol) and that the
 * information resides on a host machine named
 * <code>www.socs.uts.edu.au</code>. The information on that host
 * machine is named <code>/MosaicDocs-old/url-primer.html</code>. The exact
 * meaning of this name on the host machine is both protocol
 * dependent and host dependent. The information normally resides in
 * a file, but it could be generated on the fly. This component of
 * the URL is called the <i>path</i> component.
 * <p>
 * A URL can optionally specify a "port", which is the
 * port number to which the TCP connection is made on the remote host
 * machine. If the port is not specified, the default port for
 * the protocol is used instead. For example, the default port for
 * <code>http</code> is <code>80</code>. An alternative port could be
 * specified as:
 * <blockquote><pre>
 *     http://www.socs.uts.edu.au:80/MosaicDocs-old/url-primer.html
 * </pre></blockquote>
 * <p>
 * The syntax of <code>URL</code> is defined by  <a
 * href="http://www.ietf.org/rfc/rfc2396.txt"><i>RFC&nbsp;2396: Uniform
 * Resource Identifiers (URI): Generic Syntax</i></a>, amended by <a
 * href="http://www.ietf.org/rfc/rfc2732.txt"><i>RFC&nbsp;2732: Format for
 * Literal IPv6 Addresses in URLs</i></a>. The Literal IPv6 address format
 * also supports scope_ids. The syntax and usage of scope_ids is described
 * <a href="Inet6Address.html#scoped">here</a>.
 * <p>
 * A URL may have appended to it a "fragment", also known
 * as a "ref" or a "reference". The fragment is indicated by the sharp
 * sign character "#" followed by more characters. For example,
 * <blockquote><pre>
 *     http://java.sun.com/index.html#chapter1
 * </pre></blockquote>
 * <p>
 * This fragment is not technically part of the URL. Rather, it
 * indicates that after the specified resource is retrieved, the
 * application is specifically interested in that part of the
 * document that has the tag <code>chapter1</code> attached to it. The
 * meaning of a tag is resource specific.
 * <p>
 * An application can also specify a "relative URL",
 * which contains only enough information to reach the resource
 * relative to another URL. Relative URLs are frequently used within
 * HTML pages. For example, if the contents of the URL:
 * <blockquote><pre>
 *     http://java.sun.com/index.html
 * </pre></blockquote>
 * contained within it the relative URL:
 * <blockquote><pre>
 *     FAQ.html
 * </pre></blockquote>
 * it would be a shorthand for:
 * <blockquote><pre>
 *     http://java.sun.com/FAQ.html
 * </pre></blockquote>
 * <p>
 * The relative URL need not specify all the components of a URL. If
 * the protocol, host name, or port number is missing, the value is
 * inherited from the fully specified URL. The file component must be
 * specified. The optional fragment is not inherited.
 * <p>
 * The URL class does not itself encode or decode any URL components
 * according to the escaping mechanism defined in RFC2396. It is the
 * responsibility of the caller to encode any fields, which need to be
 * escaped prior to calling URL, and also to decode any escaped fields,
 * that are returned from URL. Furthermore, because URL has no knowledge
 * of URL escaping, it does not recognise equivalence between the encoded
 * or decoded form of the same URL. For example, the two URLs:<br>
 * <pre>    http://foo.com/hello world/ and http://foo.com/hello%20world</pre>
 * would be considered not equal to each other.
 * <p>
 * Note, the {@link java.net.URI} class does perform escaping of its
 * component fields in certain circumstances. The recommended way
 * to manage the encoding and decoding of URLs is to use {@link java.net.URI},
 * and to convert between these two classes using {@link #toURI()} and
 * {@link java.net.URI#toURL()}.
 * <p>
 * The {@link java.net.URLEncoder} and {@link java.net.URLDecoder} classes can also be
 * used, but only for HTML form encoding, which is not the same
 * as the encoding scheme defined in RFC2396.
 *
 * @author  James Gosling
 * @since JDK1.0
 */
public final class URL implements java.io.Serializable {

    static final long serialVersionUID = -7627629688361524110L;

    URLStreamHandler handler = new URLStreamHandler();

    /**
     * The property which specifies the package prefix list to be scanned
     * for protocol handlers.  The value of this property (if any) should
     * be a vertical bar delimited list of package names to search through
     * for a protocol handler to load.  The policy of this class is that
     * all protocol handlers will be in a class called <protocolname>.Handler,
     * and each package in the list is examined in turn for a matching
     * handler.  If none are found (or the property is not specified), the
     * default package prefix, sun.net.www.protocol, is used.  The search
     * proceeds from the first package in the list to the last and stops
     * when a match is found.
     */
    private static final String protocolPathProp = "java.protocol.handler.pkgs";

    /**
     * The protocol to use (ftp, http, nntp, ... etc.) .
     * @serial
     */
    private String protocol;

    /**
     * The host name to connect to.
     * @serial
     */
    private String host;

    /**
     * The protocol port to connect to.
     * @serial
     */
    private int port = -1;

    /**
     * The specified file name on that host. <code>file</code> is
     * defined as <code>path[?query]</code>
     * @serial
     */
    private String file;

    /**
     * The query part of this URL.
     */
    private transient String query;

    /**
     * The authority part of this URL.
     * @serial
     */
    private String authority;

    /**
     * The path part of this URL.
     */
    private transient String path;

    /**
     * The userinfo part of this URL.
     */
    private transient String userInfo;

    /**
     * # reference.
     * @serial
     */
    private String ref;

    /* Our hash code.
     * @serial
     */
    private int hashCode = -1;


    /**
     * Creates a <code>URL</code> object from the <code>String</code>
     * representation.
     * <p>
     * This constructor is equivalent to a call to the two-argument
     * constructor with a <code>null</code> first argument.
     *
     * @param      spec   the <code>String</code> to parse as a URL.
     * @exception  MalformedURLException  if no protocol is specified, or an
     *               unknown protocol is found, or <tt>spec</tt> is <tt>null</tt>.
     * @see        java.net.URL#URL(java.net.URL, java.lang.String)
     */
    public URL(String spec) throws MalformedURLException {
        this(null, spec);
    }



    /**
     * Creates a URL by parsing the given spec with the specified handler
     * within a specified context. If the handler is null, the parsing
     * occurs as with the two argument constructor.
     *
     * @param      context   the context in which to parse the specification.
     * @param      spec      the <code>String</code> to parse as a URL.
     * @exception  MalformedURLException  if no protocol is specified, or an
     *               unknown protocol is found, or <tt>spec</tt> is <tt>null</tt>.
     * @exception  SecurityException
     *        if a security manager exists and its
     *        <code>checkPermission</code> method doesn't allow
     *        specifying a stream handler.
     * @see        java.net.URL#URL(java.lang.String, java.lang.String,
     *                  int, java.lang.String)
     * @see        java.net.URLStreamHandler
     * @see        java.net.URLStreamHandler#parseURL(java.net.URL,
     *                  java.lang.String, int, int)
     */
    public URL(URL context, String spec)
            throws MalformedURLException
    {
        String original = spec;
        int i, limit, c;
        int start = 0;
        String newProtocol = null;
        boolean aRef=false;
        boolean isRelative = false;

        // Check for permission to specify a handler
        try {
            limit = spec.length();
            while ((limit > 0) && (spec.charAt(limit - 1) <= ' ')) {
                limit--;        //eliminate trailing whitespace
            }
            while ((start < limit) && (spec.charAt(start) <= ' ')) {
                start++;        // eliminate leading whitespace
            }

            if (spec.regionMatches(true, start, "url:", 0, 4)) {
                start += 4;
            }
            if (start < spec.length() && spec.charAt(start) == '#') {
                /* we're assuming this is a ref relative to the context URL.
                 * This means protocols cannot start w/ '#', but we must parse
                 * ref URL's like: "hello:there" w/ a ':' in them.
                 */
                aRef=true;
            }
            for (i = start ; !aRef && (i < limit) &&
                    ((c = spec.charAt(i)) != '/') ; i++) {
                if (c == ':') {

                    String s = spec.substring(start, i).toLowerCase();
                    if (isValidProtocol(s)) {
                        newProtocol = s;
                        start = i + 1;
                    }
                    break;
                }
            }

            // Only use our context if the protocols match.
            protocol = newProtocol;
            if ((context != null) && ((newProtocol == null) ||
                    newProtocol.equalsIgnoreCase(context.protocol))) {
                // inherit the protocol handler from the context
                // if not specified to the constructor
                // If the context is a hierarchical URL scheme and the spec
                // contains a matching scheme then maintain backwards
                // compatibility and treat it as if the spec didn't contain
                // the scheme; see 5.2.3 of RFC2396
                if (context.path != null && context.path.startsWith("/"))
                    newProtocol = null;

                if (newProtocol == null) {
                    protocol = context.protocol;
                    authority = context.authority;
                    userInfo = context.userInfo;
                    host = context.host;
                    port = context.port;
                    file = context.file;
                    path = context.path;
                    isRelative = true;
                }
            }

            if (protocol == null) {
                throw new MalformedURLException("no protocol: "+original);
            }

            i = spec.indexOf('#', start);
            if (i >= 0) {
                ref = spec.substring(i + 1, limit);
                limit = i;
            }

            /*
             * Handle special case inheritance of query and fragment
             * implied by RFC2396 section 5.2.2.
             */
            if (isRelative && start == limit) {
                query = context.query;
                if (ref == null) {
                    ref = context.ref;
                }
            }

            handler.parseURL(this, spec, start, limit);

        } catch(MalformedURLException e) {
            throw e;
        } catch(Exception e) {
            MalformedURLException exception = new MalformedURLException(e.getMessage());
            exception.initCause(e);
            throw exception;
        }
    }

    /*
     * Returns true if specified string is a valid protocol name.
     */
    private boolean isValidProtocol(String protocol) {
        int len = protocol.length();
        if (len < 1)
            return false;
        char c = protocol.charAt(0);
        if (!Utils.isLetter(c))
            return false;
        for (int i = 1; i < len; i++) {
            c = protocol.charAt(i);
            if (!Utils.isLetterOrDigit(c) && c != '.' && c != '+' &&
                    c != '-') {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the fields of the URL. This is not a public method so that
     * only URLStreamHandlers can modify URL fields. URLs are
     * otherwise constant.
     *
     * @param protocol the name of the protocol to use
     * @param host the name of the host
     @param port the port number on the host
      * @param file the file on the host
     * @param ref the internal reference in the URL
     */
    protected void set(String protocol, String host,
                       int port, String file, String ref) {
        synchronized (this) {
            this.protocol = protocol;
            this.host = host;
            authority = port == -1 ? host : host + ":" + port;
            this.port = port;
            this.file = file;
            this.ref = ref;
            /* This is very important. We must recompute this after the
             * URL has been changed. */
            hashCode = -1;
            int q = file.lastIndexOf('?');
            if (q != -1) {
                query = file.substring(q+1);
                path = file.substring(0, q);
            } else
                path = file;
        }
    }

    /**
     * Sets the specified 8 fields of the URL. This is not a public method so
     * that only URLStreamHandlers can modify URL fields. URLs are otherwise
     * constant.
     *
     * @param protocol the name of the protocol to use
     * @param host the name of the host
     * @param port the port number on the host
     * @param authority the authority part for the url
     * @param userInfo the username and password
     * @param path the file on the host
     * @param ref the internal reference in the URL
     * @param query the query part of this URL
     * @since 1.3
     */
    protected void set(String protocol, String host, int port,
                       String authority, String userInfo, String path,
                       String query, String ref) {
        synchronized (this) {
            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.file = query == null ? path : path + "?" + query;
            this.userInfo = userInfo;
            this.path = path;
            this.ref = ref;
            /* This is very important. We must recompute this after the
             * URL has been changed. */
            hashCode = -1;
            this.query = query;
            this.authority = authority;
        }
    }

    /**
     * Gets the query part of this <code>URL</code>.
     *
     * @return  the query part of this <code>URL</code>,
     * or <CODE>null</CODE> if one does not exist
     * @since 1.3
     */
    public String getQuery() {
        return query;
    }

    /**
     * Gets the path part of this <code>URL</code>.
     *
     * @return  the path part of this <code>URL</code>, or an
     * empty string if one does not exist
     * @since 1.3
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the userInfo part of this <code>URL</code>.
     *
     * @return  the userInfo part of this <code>URL</code>, or
     * <CODE>null</CODE> if one does not exist
     * @since 1.3
     */
    public String getUserInfo() {
        return userInfo;
    }

    /**
     * Gets the authority part of this <code>URL</code>.
     *
     * @return  the authority part of this <code>URL</code>
     * @since 1.3
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * Gets the port number of this <code>URL</code>.
     *
     * @return  the port number, or -1 if the port is not set
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the default port number of the protocol associated
     * with this <code>URL</code>. If the URL scheme or the URLStreamHandler
     * for the URL do not define a default port number,
     * then -1 is returned.
     *
     * @return  the port number
     * @since 1.4
     */
    public int getDefaultPort() {
        return handler.getDefaultPort();
    }

    /**
     * Gets the protocol name of this <code>URL</code>.
     *
     * @return  the protocol of this <code>URL</code>.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Gets the host name of this <code>URL</code>, if applicable.
     * The format of the host conforms to RFC 2732, i.e. for a
     * literal IPv6 address, this method will return the IPv6 address
     * enclosed in square brackets (<tt>'['</tt> and <tt>']'</tt>).
     *
     * @return  the host name of this <code>URL</code>.
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the file name of this <code>URL</code>.
     * The returned file portion will be
     * the same as <CODE>getPath()</CODE>, plus the concatenation of
     * the value of <CODE>getQuery()</CODE>, if any. If there is
     * no query portion, this method and <CODE>getPath()</CODE> will
     * return identical results.
     *
     * @return  the file name of this <code>URL</code>,
     * or an empty string if one does not exist
     */
    public String getFile() {
        return file;
    }

    /**
     * Gets the anchor (also known as the "reference") of this
     * <code>URL</code>.
     *
     * @return  the anchor (also known as the "reference") of this
     *          <code>URL</code>, or <CODE>null</CODE> if one does not exist
     */
    public String getRef() {
        return ref;
    }

    /**
     * Constructs a string representation of this <code>URL</code>. The
     * string is created by calling the <code>toExternalForm</code>
     * method of the stream protocol handler for this object.
     *
     * @return  a string representation of this object.
     * @see     java.net.URL#URL(java.lang.String, java.lang.String, int,
     *                  java.lang.String)
     * @see     java.net.URLStreamHandler#toExternalForm(java.net.URL)
     */
    public String toString() {
        return toExternalForm();
    }

    /**
     * Constructs a string representation of this <code>URL</code>. The
     * string is created by calling the <code>toExternalForm</code>
     * method of the stream protocol handler for this object.
     *
     * @return  a string representation of this object.
     * @see     java.net.URL#URL(java.lang.String, java.lang.String,
     *                  int, java.lang.String)
     * @see     java.net.URLStreamHandler#toExternalForm(java.net.URL)
     */
    public String toExternalForm() {
        return handler.toExternalForm(this);
    }

    /**
     * Returns a {@link java.net.URI} equivalent to this URL.
     * This method functions in the same way as <code>new URI (this.toString())</code>.
     * <p>Note, any URL instance that complies with RFC 2396 can be converted
     * to a URI. However, some URLs that are not strictly in compliance
     * can not be converted to a URI.
     *
     * @exception java.net.URISyntaxException if this URL is not formatted strictly according to
     *            to RFC2396 and cannot be converted to a URI.
     *
     * @return    a URI instance equivalent to this URL.
     * @since 1.5
     */
    public URI toURI() throws URISyntaxException {
        return new URI(toString());
    }


    /**
     * A table of protocol handlers.
     */
    static Hashtable handlers = new Hashtable();
    private static Object streamHandlerLock = new Object();

    // special case the gopher protocol, disabled by default
    private static final String GOPHER = "gopher";
    private static final String ENABLE_GOPHER_PROP = "jdk.net.registerGopherProtocol";

    // package name of the JDK implementation protocol handlers
    private static final String JDK_PACKAGE_PREFIX =  "sun.net.www.protocol";

}

