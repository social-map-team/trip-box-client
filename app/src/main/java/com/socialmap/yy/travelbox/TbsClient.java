package com.socialmap.yy.travelbox;


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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;





public class TbsClient {
    public static String serverAddress = "192.168.0.101:8080";

    public static class ServerResponse {
        private int statusCode = 0;
        private String contentType = "";
        private byte[] content = null;

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

        public Error getError() {
            if (getStatusCode() >= 200 && getStatusCode() < 300) {
                if (getContent() == null) {
                    return null;
                } else {
                    if (getContentType().equals("application/json")) {
                        String content = new String(getContent(), StandardCharsets.UTF_8);
                        try {
                            JSONObject json = new JSONObject(content);
                            if (json.get("errorCode") == null) {
                                return null;
                            } else {
                                Error error = new Error(json.getInt("errorCode"));
                                return error;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }
    }

    public static class Error {
        private int errorCode;
        private int errorMessage;

        public Error() {

        }

        public Error(int errorCode) {
            this.errorCode = errorCode;
            // fetch Error Message
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public int getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(int errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    public static class TbsClientRequest {
        private CloseableHttpClient client;
        private HttpHost server;
        private HttpRequestBase request;
        private HttpClientContext context;


        public TbsClientRequest(CloseableHttpClient client, HttpHost server, HttpClientContext context, HttpRequestBase request) {
            this.client = client;
            this.server = server;
            this.context = context;
            this.request = request;
        }

        private ServerResponse resp;

        public void execute(Callback callback) {
            try {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CloseableHttpResponse response = client.execute(server, request, context);
                            resp = new ServerResponse();
                            resp.setStatusCode(response.getStatusLine().getStatusCode());
                            if (response.getEntity().getContentType() != null) {
                                resp.setContentType(response.getEntity().getContentType().getValue());
                            }
                            resp.setContent(IOUtils.toByteArray(response.getEntity().getContent()));
                            response.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                t.join();//这是假死的bug
                callback.onFinished(resp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static interface Callback {
        public void onFinished(ServerResponse response);
    }

    private static TbsClient instance = null;
    private String host;
    private int port;
    private String protocal;// http, https
    private CloseableHttpClient client;
    private HttpHost server;
    private HttpClientContext context;

    // hide constructor, this class is singleton
    private TbsClient() {
    }

    public static TbsClient getInstance() {
        if (instance == null) {
            // create client here
            instance = new TbsClient();

            String[] parts = serverAddress.split(":");
            instance.host = parts[0];
            instance.port = Integer.parseInt(parts[1]);
            instance.protocal = "http";
            instance.server = new HttpHost(instance.host, instance.port, instance.protocal);
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(
                    new AuthScope(instance.server.getHostName(), instance.server.getPort()),
                    new UsernamePasswordCredentials("test", "123")
            );
            instance.client = HttpClients.custom()
                    .setDefaultCredentialsProvider(provider).build();
            AuthCache cache = new BasicAuthCache();
            DigestScheme digest = new DigestScheme();
            digest.overrideParamter("realm", "some realm");
            digest.overrideParamter("nonce", "whatever");
            cache.put(instance.server, digest);

            instance.context = HttpClientContext.create();
            instance.context.setAuthCache(cache);
        }
        return instance;
    }

    public TbsClientRequest requestAnonymous(String url, String method, Object... params) {
        return null;
    }

    private String encode(Object origin) {
        try {
            return URLEncoder.encode(origin.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public TbsClientRequest request(String uri, String method, Object... params) {
        if (method.equalsIgnoreCase("get")) {
            StringBuilder builder = new StringBuilder(uri);
            if (params.length > 0) {
                builder.append("?");
            }
            for (int i = 0; i < params.length; i += 2) {
                builder.append(encode(params[i]))
                        .append("=")
                        .append(encode(params[i + 1]));
                if (i + 2 < params.length) {
                    builder.append("&");
                }
            }
            HttpGet get = new HttpGet(builder.toString());
            return new TbsClientRequest(client, server, context, get);

        }
        else if (method.equalsIgnoreCase("post")) {
            List<NameValuePair> postParams = new ArrayList<>();
            for (int i = 0; i < params.length; i += 2) {
                postParams.add(new BasicNameValuePair(params[i].toString(), params[i + 1].toString()));
            }
            HttpPost post = new HttpPost(uri);
            try {
                post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return new TbsClientRequest(client, server, context, post);

        } else if (method.equalsIgnoreCase("put")) {
            List<NameValuePair> postParams = new ArrayList<>();
            for (int i = 0; i < params.length; i += 2) {
                postParams.add(new BasicNameValuePair(params[i].toString(), params[i + 1].toString()));
            }
            HttpPost post = new HttpPost(uri);
            HttpPut put = new HttpPut(uri);
            try {
                post.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return new TbsClientRequest(client, server, context, put);

        } else if (method.equalsIgnoreCase("delete")) {
            StringBuilder builder = new StringBuilder(uri);
            if (params.length > 0) {
                builder.append("?");
            }
            for (int i = 0; i < params.length; i += 2) {
                builder.append(encode(params[i]))
                        .append("=")
                        .append(encode(params[i + 1]));
                if (i + 2 < params.length) {
                    builder.append("&");
                }
            }
            HttpDelete delete = new HttpDelete(builder.toString());
            return new TbsClientRequest(client, server, context, delete);
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        client.close();
        super.finalize();
    }
}
