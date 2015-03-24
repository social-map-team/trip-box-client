package com.socialmap.yy.travelbox.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 3/5/15.
 */
public class TbsClient {
    private static TbsClient instance;
    private static Activity activity;
    public CloseableHttpClient client;
    public HttpHost server;
    public HttpClientContext context;

    private TbsClient() {
        init();
    }

    public static void init(Activity activity) {
        TbsClient.activity = activity;
        instance = new TbsClient();
    }

    /**
     * 当服务器地址发生变化时手动刷新
     */
    public static void refresh() {
        if (instance != null)
            instance.init();
    }

    /**
     * 当服务器地址，用户名和密码发生变化时手动刷新
     *
     * @param username
     * @param password
     */
    public static void refresh(String username, String password) {
        if (instance != null)
            instance.init(username, password);
    }

    public static TbsClient getInstance(Activity activity) {
        if (instance == null) {
            throw new RuntimeException("TbsClient未初始化");
        }
        TbsClient.activity = activity;
        return instance;
    }

    public static TbsClient getInstance() {
        if (instance == null) {
            throw new RuntimeException("TbsClient未初始化");
        }
        return instance;
    }

    private void init(String username, String password) {
        String host = "192.168.1.103";
        int port = 8080;
        server = new HttpHost(host, port, "http");

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                new AuthScope(server.getHostName(), server.getPort()),
                new UsernamePasswordCredentials(username, password)
        );

        client = HttpClients.custom().setDefaultCredentialsProvider(provider).build();

        AuthCache cache = new BasicAuthCache();
        DigestScheme digest = new DigestScheme();
        digest.overrideParamter("realm", "some realm");
        digest.overrideParamter("nonce", "whatever");
        cache.put(server, digest);

        context = HttpClientContext.create();
        context.setAuthCache(cache);
    }

    private void init() {
        String host = "192.168.1.103";
        int port = 8080;
        server = new HttpHost(host, port, "http");

        client = HttpClients.createDefault();

        context = HttpClientContext.create();
    }

    @Override
    protected void finalize() throws Throwable {
        client.close();
        super.finalize();
    }

    /**
     * 这个接口只能用一般的字符串模式的请求
     *
     * @param uri
     * @param method
     * @param params
     * @return
     */
    public TbsClientRequest request(String uri, String method, Object... params) {
        if (method.equals("get")) {
            return get(uri, params);
        } else if (method.equals("post")) {
            return post(uri, params);
        } else if (method.equals("put")) {
            return put(uri, params);
        } else if (method.equals("delete")) {
            return delete(uri, params);
        }
        return null;
    }

    /**
     * 这个接口的设计是考虑的传送图片的需求
     *
     * @param r
     * @return
     */
    public TbsClientRequest request(HttpRequestBase r) {
        return new TbsClientRequest(client, server, context, r);
    }

    private String encode(Object origin) {
        try {
            return URLEncoder.encode(origin.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String buildUrlParams(Object[] params) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            String key = params[i].toString();
            String value = null;
            if (params[i + 1].getClass().isArray()) {
                Object[] array = (Object[]) params[i + 1];
                value = array[0].toString();
                for (int j = 1; j < array.length; j++) {
                    value = value + "," + array[j];
                }
            } else {
                value = params[i + 1].toString();
            }

            builder.append(encode(key)).append("=").append(encode(value));
            if (i + 2 < params.length) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    private List<NameValuePair> buildEntityParams(Object[] params) {
        List<NameValuePair> r = new ArrayList<>();
        for (int i = 0; i < params.length; i += 2) {
            String key = params[i].toString();
            String value = null;
            if (params[i + 1].getClass().isArray()) {
                Object[] array = (Object[]) params[i + 1];
                value = array[0].toString();
                for (int j = 1; j < array.length; j++) {
                    value = value + "," + array[j];
                }
            } else {
                value = params[i + 1].toString();
            }

            r.add(new BasicNameValuePair(key, value));
        }
        return r;
    }

    private TbsClientRequest get(String uri, Object[] params) {
        HttpGet r = new HttpGet(uri + "?" + buildUrlParams(params));
        return new TbsClientRequest(client, server, context, r);
    }

    private TbsClientRequest post(String uri, Object[] params) {
        // TODO Post Image
        HttpPost r = new HttpPost(uri);
        try {
            r.setEntity(new UrlEncodedFormEntity(buildEntityParams(params), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        return new TbsClientRequest(client, server, context, r);
    }

    private TbsClientRequest delete(String uri, Object[] params) {
        HttpDelete r = new HttpDelete(uri + "?" + buildUrlParams(params));
        return new TbsClientRequest(client, server, context, r);
    }

    private TbsClientRequest put(String uri, Object[] params) {
        HttpPut r = new HttpPut(uri);
        try {
            r.setEntity(new UrlEncodedFormEntity(buildEntityParams(params), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        return new TbsClientRequest(client, server, context, r);
    }

    public static interface Callback {
        public void onFinished(ServerResponse response);
    }

    public static class ServerResponse {
        private int statusCode = 0;
        private String contentType = "";
        private byte[] content;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public boolean is1xx() {
            return statusCode >= 100 && statusCode < 200;
        }

        public boolean is2xx() {
            return statusCode >= 200 && statusCode < 300;
        }

        public boolean is3xx() {
            return statusCode >= 300 && statusCode < 400;
        }

        public boolean is4xx() {
            return statusCode >= 400 && statusCode < 500;
        }

        public boolean is5xx() {
            return statusCode >= 500;
        }

        public Error getError() {
            if (is2xx() && getContent().length == 0) {
                return null;
            } else {
                Error error = new Error();
                try {
                    JSONObject json = new JSONObject(getString());
                    error.setCode(json.getInt("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return error;
            }
        }

        public String getString() {
            try {
                return IOUtils.toString(getContent(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public static class Error {
        private int code;
        private int message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getMessage() {
            return message;
        }

        public void setMessage(int message) {
            this.message = message;
        }
    }

    public static class TbsClientRequest {
        private HttpRequestBase request;
        private CloseableHttpClient client;
        private HttpHost server;
        private HttpClientContext context;

        public TbsClientRequest(CloseableHttpClient client, HttpHost server, HttpClientContext context, HttpRequestBase request) {
            this.client = client;
            this.server = server;
            this.context = context;
            this.request = request;
        }

        public void execute(final Callback callback) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        CloseableHttpResponse response = client.execute(server, request, context);
                        ServerResponse resp = new ServerResponse();
                        resp.setStatusCode(response.getStatusLine().getStatusCode());
                        if (response.getEntity().getContentType() != null) {
                            resp.setContentType(response.getEntity().getContentType().getValue());
                        }
                        resp.setContent(IOUtils.toByteArray(response.getEntity().getContent()));
                        response.close();
                        final ServerResponse r = resp;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFinished(r);
                            }
                        });
                    } catch (IOException e) {
                        Log.e("tbs", "网络故障", e);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "网络故障", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }
}
