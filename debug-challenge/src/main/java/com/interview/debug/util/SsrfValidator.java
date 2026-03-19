package com.interview.debug.util;

import java.net.InetAddress;
import java.net.URI;
import java.util.Set;

public class SsrfValidator {

    private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");
    

    public static boolean isValidUrl(String urlString) {
        try {
            URI uri = new URI(urlString);
            
            // 1. Check scheme
            if (!ALLOWED_SCHEMES.contains(uri.getScheme())) {
                return false;
            }

            // 2. Resolve hostname to IP
            String host = uri.getHost();
            if (host == null) return false;

            InetAddress address = InetAddress.getByName(host);
            
            // 3. Check if IP is internal/private
            if (address.isLoopbackAddress() || 
                address.isSiteLocalAddress() || 
                address.isLinkLocalAddress() || 
                address.isAnyLocalAddress()) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
