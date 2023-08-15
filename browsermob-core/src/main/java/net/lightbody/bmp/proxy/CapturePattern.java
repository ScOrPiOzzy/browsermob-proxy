package net.lightbody.bmp.proxy;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;

public class CapturePattern {
    /**
     * A disabled Whitelist.
     */
    public static final CapturePattern CAPTURE_PATTERN = new CapturePattern();

    private final List<Pattern> patterns;

    public CapturePattern() {
        this.patterns = Collections.emptyList();
    }

    public CapturePattern(List<String> _patterns) {
        if (_patterns == null || _patterns.isEmpty()) {
            this.patterns = Collections.emptyList();
        } else {
            ImmutableList.Builder<Pattern> builder = ImmutableList.builder();
            for (String pattern : _patterns) {
                builder.add(Pattern.compile(pattern));
            }

            this.patterns = builder.build();
        }
    }

    public Collection<Pattern> getPatterns() {
        return this.patterns;
    }

    public boolean matches(String url) {
        if (this.patterns.isEmpty()) {
            return true;
        }
        for (Pattern pattern : getPatterns()) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }

        return false;
    }
}
