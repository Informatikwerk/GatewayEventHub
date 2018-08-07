package de.informatikwerk.gatewayeventhub.domain;


/**
 * A Action.
 */
public class Action {

    private String url;

    private String method;

    private String data;

    private String timeout;

    private String realmKey;

    private String contentType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRealmKey() {
        return realmKey;
    }

    public void setRealmKey(String realmKey) {
        this.realmKey = realmKey;
    }

    @Override
    public String toString() {
        return "Action{" +
            "url='" + url + '\'' +
            ", method='" + method + '\'' +
            ", data='" + data + '\'' +
            ", timeout='" + timeout + '\'' +
            ", realmKey='" + realmKey + '\'' +
            ", contentType='" + contentType + '\'' +
            '}';
    }
}
