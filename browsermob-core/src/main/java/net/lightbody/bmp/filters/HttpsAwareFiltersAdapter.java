package net.lightbody.bmp.filters;

import com.google.common.net.HostAndPort;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.util.HttpUtil;
import net.lightbody.bmp.util.BrowserMobHttpUtil;
import org.littleshoot.proxy.HttpFiltersAdapter;

/**
 * The HttpsAwareFiltersAdapter exposes the original host and the "real" host (after filter modifications) to filters for HTTPS
 * requets. HTTPS requests do not normally contain the host in the URI, and the Host header may be missing or spoofed.
 * <p/>
 * <b>Note:</b> The {@link #getHttpsRequestHostAndPort()} and {@link #getHttpsOriginalRequestHostAndPort()} methods can only be
 * called when the request is an HTTPS request. Otherwise they will throw an IllegalStateException.
 */
public class HttpsAwareFiltersAdapter extends HttpFiltersAdapter {

    public HttpsAwareFiltersAdapter(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
    }

    /**
     * Returns true if this is an HTTPS request.
     *
     * @return true if https, false if http
     */
    public boolean isHttps() {
        return BrowserMobHttpUtil.isHttps(ctx);
    }

    /**
     * Returns the full, absolute URL of the specified request for both HTTP and HTTPS URLs. The request may reflect
     * modifications from this or other filters. This filter instance must be currently handling the specified request;
     * otherwise the results are undefined.
     *
     * @param modifiedRequest a possibly-modified version of the request currently being processed
     * @return the full URL of the request, including scheme, host, port, path, and query parameters
     */
    public String getFullUrl(HttpRequest modifiedRequest) {
        return BrowserMobHttpUtil.getFullUrl(modifiedRequest, ctx);
    }

    /**
     * Returns the full, absolute URL of the original request from the client for both HTTP and HTTPS URLs. The URL
     * will not reflect modifications from this or other filters.
     *
     * @return the full URL of the original request, including scheme, host, port, path, and query parameters
     */
    public String getOriginalUrl() {
        return getFullUrl(originalRequest);
    }

    /**
     * Returns the hostname (but not the port) the specified request for both HTTP and HTTPS requests.  The request may reflect
     * modifications from this or other filters. This filter instance must be currently handling the specified request;
     * otherwise the results are undefined.
     *
     * @param modifiedRequest a possibly-modified version of the request currently being processed
     * @return hostname of the specified request, without the port
     */
    public String getHost(HttpRequest modifiedRequest) {
        String serverHost;
        if (isHttps()) {
            HostAndPort hostAndPort = HostAndPort.fromString(BrowserMobHttpUtil.getHttpsRequestHostAndPort(ctx));
            serverHost = hostAndPort.getHost();
        } else {
            serverHost = HttpUtil.getHostFromRequest(modifiedRequest);
        }
        return serverHost;
    }

    /**
     * Returns the host and port of the specified request for both HTTP and HTTPS requests.  The request may reflect
     * modifications from this or other filters. This filter instance must be currently handling the specified request;
     * otherwise the results are undefined.
     *
     * @param modifiedRequest a possibly-modified version of the request currently being processed
     * @return host and port of the specified request
     */
    public String getHostAndPort(HttpRequest modifiedRequest) {
        return BrowserMobHttpUtil.getHostAndPort(modifiedRequest, ctx);
    }

}
