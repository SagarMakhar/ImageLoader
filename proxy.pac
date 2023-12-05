function FindProxyForURL(url, host) {

// If the hostname matches, send to the proxy.
    if (shExpMatch(url, "*oogle.com*"))
        return "PROXY 10.79.30.35:3128";


// DEFAULT RULE: All other traffic, send direct.
    //return "DIRECT";
    return "PROXY 10.79.30.35:3128";
}
