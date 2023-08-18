package net.lightbody.bmp.proxy;

import java.util.regex.Pattern;

public class CapturePattern {

    private final Pattern pattern;
    private final Pattern httpMethodPattern;


    public CapturePattern(String pattern, String httpMethodPattern) {
        this.pattern = Pattern.compile(pattern);
        if (httpMethodPattern == null || httpMethodPattern.isEmpty()) {
            this.httpMethodPattern = null;
        } else {
            this.httpMethodPattern = Pattern.compile(httpMethodPattern);
        }
    }

    public Pattern getHttpMethodPattern() {
        return httpMethodPattern;
    }


    public boolean matches(String url, String httpMethod) {
        if (httpMethodPattern != null) {
            return pattern.matcher(url).matches() && httpMethodPattern.matcher(httpMethod).matches();
        }
        return pattern.matcher(url).matches();
    }

}
