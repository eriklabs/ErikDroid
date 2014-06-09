package com.erik.erikdroid.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.erik.erikdroid.R;
import com.erik.erikdroid.data.ErikDataModel;
import com.erik.erikdroid.data.ErikListResponse;
import com.erik.erikdroid.ui.ErikListActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class Connection {

    public ConnectionListener mListener;
    public static final String serverBaseURL = "http://eftallinkpesinde.serhatkose.com/service1.asmx";

    public Connection(ConnectionListener listener) {
        this.mListener = listener;
    }

    public void getEriks(final int pageNumber, final int siteID) {
        
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                HttpResponse response = null;
                HttpClient httpClient = getNewHttpClient();

                HttpPost post = new HttpPost(
                        serverBaseURL + "/GetErikList");
                post.setHeader("Content-Type", "application/json");
                StringEntity se;
                try {
                    se = new StringEntity("{\"pageNumber\" : \"" + pageNumber
                            + "\", \"siteID\" : \"" + siteID + "\"}");
                    post.setEntity(se);
                    response = httpClient.execute(post);

                } catch (UnsupportedEncodingException e) {
                    // TODO Connection Error
                    e.printStackTrace();
                    mListener.onResponseFailed(R.string.connectionError, null);
                    return;
                } catch (ClientProtocolException e) {
                    // TODO Connection Error
                    e.printStackTrace();
                    mListener.onResponseFailed(R.string.connectionError, null);
                    return;
                } catch (IOException e) {
                    // TODO Connection Error
                    e.printStackTrace();
                    mListener.onResponseFailed(R.string.connectionError, null);
                    return;
                }

                InputStream inputStream = null;
                try {
                    inputStream = response.getEntity().getContent();
                } catch (IllegalStateException e1) {
                    // TODO Connection Error
                    e1.printStackTrace();
                    mListener.onResponseFailed(R.string.connectionError, null);
                    return;
                } catch (IOException e1) {
                    // TODO Connection Error
                    e1.printStackTrace();
                    mListener.onResponseFailed(R.string.connectionError, null);
                    return;
                } catch (NullPointerException e) {
                    // TODO Connection Error
                    e.printStackTrace();
                    mListener.onResponseFailed(R.string.connectionError, null);
                    return;
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        inputStream), 8 * 1024);
                String line = "";
                StringBuilder sb = new StringBuilder();
                try {
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String responseStr = sb.toString();

                sb = null;

                handleResponse(responseStr);

            }
        });

        Log.i("ERIK LIST", "getEriks()");

        t.start();

    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            HttpParams params = new BasicHttpParams();

            // Set the timeout in milliseconds until a connection is
            // established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 20000;
            HttpConnectionParams
                    .setConnectionTimeout(params, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 20000;
            HttpConnectionParams.setSoTimeout(params, timeoutSocket);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "ISO-8859-1");
            HttpConnectionParams.setSocketBufferSize(params, 8192);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private void handleResponse(String responseStr) {
        Log.i("HANDLE RESPONSE", responseStr);

        GsonBuilder gBuilder = new GsonBuilder();
        String result = "";
        String[] resultArr = null;
        try {
            result = (new JSONObject(responseStr).getJSONObject("d").toString());
        } catch (JSONException e) {
            try {

                JSONArray arr = new JSONObject(responseStr).getJSONArray("d");
                resultArr = new String[arr.length()];

                for (int i = 0; i < arr.length(); i++) {
                    resultArr[i] = arr.getJSONObject(i).toString();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

        gBuilder.registerTypeAdapter(ErikDataModel.class,
                new ErikDataModelDeserializer());

        ErikListResponse responseObject = gBuilder.create().fromJson(result,
                ErikListResponse.class);

        ArrayList<ErikDataModel> responseArrList = null;

        if (responseObject == null && resultArr != null) {
            responseArrList = new ArrayList<ErikDataModel>();

            for (int i = 0; i < resultArr.length; i++) {
                responseArrList.add(gBuilder.create().fromJson(resultArr[i],
                        ErikDataModel.class));
            }

        }

        if (responseObject != null) {

            ErikListActivity.totalPageCount = responseObject.TotalPageCount;

            for (int i = 0; i < responseObject.ImageList.length; i++) {
                ErikListActivity.erikListData.add(responseObject.ImageList[i]);
            }
        }

        mListener.onResponseReceived();

    }

    private static class ErikDataModelDeserializer implements
            JsonDeserializer<ErikDataModel> {

        @Override
        public ErikDataModel deserialize(JsonElement json, Type typeOfT,
                JsonDeserializationContext ctx) throws JsonParseException {
            Gson gson = new Gson();
            ErikDataModel erik = gson.fromJson(json,
                    ErikDataModel.class);

            return erik;
        }
    };

}
