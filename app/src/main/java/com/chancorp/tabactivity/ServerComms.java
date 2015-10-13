package com.chancorp.tabactivity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//서버 통신 클래스
public class ServerComms {

    static URL serverURL;
    static String serverURLString;
    static FamilyData fd;
    static RedrawableFragment[] rdf;


    public static void setup(String u, FamilyData f, RedrawableFragment[] r) {
        fd = f;

        rdf = r;

        try {
            serverURL = new URL(u + "/" + f.getID());
            serverURLString = u + "/" + f.getID();
        } catch (MalformedURLException e) {
            Log.e("Familink", "URL parsing error in ServerComms constructor.");
        }
    }

    public ServerComms() {
    }


    public void updateStatus(RouterInformation ri) {
        if (fd.matchRouter(ri)) this.gotInside();
        else this.gotOutside();
    }

    public void setQueryHash(String pass) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(pass.getBytes());
            byte[] digest = sha.digest();

            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest)
                sb.append(String.format("%02x", b & 0xff));

            try {
                serverURL = new URL(serverURLString + "?pw=" + sb.toString());
            } catch (MalformedURLException e) {
                Log.e("Familink", "MalformedURLException on ServerComms>setQueryHash");
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Familink", "NoSuchAlgorithmException on ServerComms>setQueryHash");
        }

    }

    public void resetQueryHash() {
        try {
            serverURL = new URL(serverURLString);
        } catch (MalformedURLException e) {
            Log.e("Familink", "MalformedURLException on ServerComms>setQueryHash");
        }
    }
/*
    public void logIn() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("username", "user");
        pe.addDataSet("password", "qpwoeiruty");
        postReq = pe.encode();
        this.sendPOSTToLogin(postReq);
    }

    public void logOut() {
        Log.d("Familink", "GETting from " + logoutURL);
        DataRetriever dr = new DataRetriever(this);
        dr.parseAfterwards(false);
        dr.execute(logoutURL);
    }

    public void test() {
        Log.d("Familink", "GETting from " + testURL);
        DataRetriever dr = new DataRetriever(this);
        dr.parseAfterwards(false);
        dr.execute(testURL);
    }*/

    public void addFamily(String name) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add family");
        pe.addDataSet("name", name);
        postReq = pe.encode();
        this.sendPOST(postReq);
    }

    public void deleteFamily() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "delete family");
        pe.addDataSet("familyID", Integer.toString(fd.getID()));
        postReq = pe.encode();
        this.sendPOST(postReq);
    }

    public void addMe(String name, String number) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add person");
        pe.addDataSet("familyID", Integer.toString(fd.getID()));
        pe.addDataSet("name", name);
        pe.addDataSet("phoneNumber", number);
        postReq = pe.encode();
        this.sendPOST(postReq);
    }

    public void gotInside() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "chk inside");
        pe.addDataSet("personID", Integer.toString(fd.getID()));
        pe.addDataSet("isInside", "1");
        postReq = pe.encode();
        this.sendPOST(postReq);
    }

    public void gotOutside() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "chk inside");
        pe.addDataSet("personID", Integer.toString(fd.getID()));
        pe.addDataSet("isInside", "0");
        postReq = pe.encode();
        this.sendPOST(postReq);
    }


    public void sendGET() {
        Log.d("Familink", "GETting from " + this.serverURL);
        DataRetriever dr = new DataRetriever(this);
        dr.execute(this.serverURL);
    }

    public void onGETReturn(String data) {
        fd.parseData(data);
        for (RedrawableFragment r : this.rdf) {
            r.redraw();
        }
    }

    public void sendPOST(String s) {
        Log.d("FamiLink", "Sending POST to " + this.serverURL + " msg: " + s);
        DataSender ds = new DataSender(this, s);
        ds.execute(this.serverURL);
    }


    public void onPOSTReturn(String data) {
        Log.d("Familink", "POST Data Returned: " + data);
        //TODO something here.
    }


    private class DataRetriever extends AsyncTask<URL, Void, String> {
        boolean parseAfter = true;
        ServerComms sc;

        public DataRetriever(ServerComms sc) {
            super();
            this.sc = sc;
        }

        public void parseAfterwards(boolean b) {
            parseAfter = b;
        }

        protected String doInBackground(URL... urls) {
            try {

                URL url = urls[0];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.getInputStream();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                urlConnection.disconnect();

                return sb.toString();
            } catch (Exception e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                Log.d("Familink", "Error in GET(ServerComms>DataRetriever>doInBackground).\n" + errors.toString());
                return "ERR";
            }
        }

        protected void onPostExecute(String result) {
            Log.d("Familink", "DataRetruever result:" + result);

            if (parseAfter) this.sc.onGETReturn(result);
        }


    }

    private class DataSender extends AsyncTask<URL, Void, String> {


        ServerComms sc;
        String params;

        public DataSender(ServerComms sc, String params) {
            this.params = params;
            this.sc = sc;

        }

        @Override
        protected String doInBackground(URL... urls) {
            //Code from http://www.xyzws.com/javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
            URL url = urls[0];
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Connection", "close");
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(params.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(params);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();

            } catch (Exception e) {

                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                Log.e("Familink", "Error in POST(ServerComms>DataSender>doInBackground).\n" + errors.toString());
                return null;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

/*
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", "" +
                        Integer.toString(this.params.getBytes().length));
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(params.getBytes());
                out.flush();
                out.close();
/*
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                urlConnection.disconnect();
                return sb.toString();

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }*/

        @Override
        protected void onPostExecute(String res) {
            this.sc.onPOSTReturn(res);
        }
    }

}


