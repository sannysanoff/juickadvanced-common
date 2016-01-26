package com.juickadvanced.parsers;

import com.juickadvanced.lang.StringSplitter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author sannysanoff
 */
public class URLParser implements Serializable {
    private String protocol;
    private String host;
    private String port;
    private String path;
    private final Map<String, String> argsMap = new HashMap<String, String>();
	private String hash;

    public URLParser(final String url) {
        final int ix = url.indexOf("//");
        if (ix == -1)
            throw new IllegalArgumentException("Invalid full url: " + url);
        protocol = url.substring(0, ix-1);
        int ix2 = url.indexOf('/', ix + 2);
        if (ix2 == -1)
            throw new IllegalArgumentException("Invalid full url: " + url);
        String hostport = url.substring(ix + 2, ix2);
        path = url.substring(ix2 + 1);
        int portIx = hostport.indexOf(":");
        if (portIx == -1) {
            host = hostport;
        } else {
            host = hostport.substring(0, portIx);
            port = hostport.substring(portIx + 1);
        }
        int paramsIx = path.indexOf("?");
        if (paramsIx != -1) {
            String[] args = StringSplitter.split(StringSplitter.split(path.substring(paramsIx + 1), "#")[0], "&");
            path = path.substring(0, paramsIx);
            for (int i=0; i<args.length; i++) {
                String arg  = args[i];
                paramsIx = arg.indexOf("=");
                if (paramsIx == -1) {
                    argsMap.put(arg, "");
                } else {
                    argsMap.put(arg.substring(0, paramsIx), arg.substring(paramsIx + 1));
                }
            }
        } else {
            path = StringSplitter.split(path, "#")[0];
        }
	    int hashIx = url.indexOf("#");
	    if (hashIx != -1) {
		    hash = url.substring(hashIx+1);
	    }
    }

    public String getHostPart() {
        StringBuffer sb = new StringBuffer();
        sb.append(protocol);
        sb.append("://");
        sb.append(host);
        if (port != null) {
            sb.append(':');
            sb.append(port);
        }
        sb.append('/');
        return sb.toString();
    }

    public String getPathPart() {
        return path;
    }

    public String getFullURL() {
        StringBuffer sb = new StringBuffer();
        sb.append(protocol);
        sb.append("://");
        sb.append(host);
        if (port != null) {
            if (protocol.equals("http") && !"80".equals(port) || protocol.equals("https") && !"443".equals(port)) {
                sb.append(':');
                sb.append(port);
            }
        }
        sb.append('/');
        sb.append(path);
        if (argsMap.size() > 0) {
            StringBuffer q = new StringBuffer();
            final Iterator it = argsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                if (q.length() == 0)
                    q.append("?");
                else
                    q.append("&");
                q.append(entry.getKey());
                q.append('=');
                q.append(entry.getValue());
            }
            sb.append(q.toString());
        }
	    if (hash != null) {
		    sb.append("#").append(hash);
	    }
        return sb.toString();
    }


    public Map<String, String> getArgsMap() {
        return argsMap;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}