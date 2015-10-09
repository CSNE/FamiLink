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

//서버 통신 클래스
public class ServerComms {

    static URL serverURL;
    static FamilyData fd;
    static RedrawableFragment[] rdf;

    static String urlAppend;

    public static void setup(String u, FamilyData f, RedrawableFragment[] r) {
        fd = f;
        urlAppend="/"+f.getID();

        rdf=r;

        try {
            serverURL = new URL(u+urlAppend);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public ServerComms(){
    }

    public void retriveData(){
        Log.d("Familink","GETting from "+this.serverURL);
        DataRetriever dr=new DataRetriever(this);
        dr.execute(this.serverURL);
    }

    public void onDataReturn(String data){


        fd.parseData(data);


        for(RedrawableFragment r:this.rdf){
            r.redraw();
        }
    }

    public void gotInside(){
        String postReq=new String();
        POSTEncoder pe=new POSTEncoder();
        pe.addDataSet("request_type", "chk-isInside");
        pe.addDataSet("personID", Integer.toString(fd.getID()));
        pe.addDataSet("isInside", "1");
        postReq=pe.encode();
        this.sendPOST(postReq);
    }
    public void gotOutside(){
        String postReq=new String();
        POSTEncoder pe=new POSTEncoder();
        pe.addDataSet("request type", "chk inside");
        pe.addDataSet("personID", Integer.toString(fd.getID()));
        pe.addDataSet("isInside", "0");
        postReq=pe.encode();
        this.sendPOST(postReq);
    }

    public void updateStatus(RouterInformation currentlyConnected){
        String postReq=new String();
        postReq=postReq;
        Log.d("FamiLink", "Sending POST to " + this.serverURL + " msg: " + postReq);
        this.sendPOST(postReq);
    }

    public void test(){
        String postReq=new String();
        POSTEncoder pe=new POSTEncoder();
        pe.addDataSet("percent", "100%!");
        pe.addDataSet("wow", "qwerty");
        postReq=pe.encode();
        this.sendPOST(postReq);
    }

    public void sendPOST(String s){
        Log.d("FamiLink", "Sending POST to " + this.serverURL + " msg: " + s);
        DataSender ds=new DataSender(this, s);
        ds.execute(this.serverURL);
    }

    public void onSendReturn(String data){
        System.out.println("POST Data Returned: "+data);
    }


    private class DataRetriever extends AsyncTask<URL,Void,String> {

        ServerComms sc;

        public DataRetriever(ServerComms sc){
            super();
            this.sc=sc;
        }

        protected String doInBackground(URL... urls) {
            try {

                URL url=urls[0];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("Familink", "Checkpoint 5");
                urlConnection.setRequestProperty("Connection", "close");
                Log.d("Familink","Checkpoint 1");
                urlConnection.getInputStream();
                Log.d("Familink", "Checkpoint 4");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("Familink","Checkpoint 2");
                StringBuilder sb = new StringBuilder();
                Log.d("Familink","Checkpoint 3");
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                urlConnection.disconnect();

                return sb.toString();
            }catch(Exception e){
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                Log.d("Familink",errors.toString());
                return "ERR";
            }
        }

        protected void onPostExecute(String result) {
            this.sc.onDataReturn(result);
        }


    }

    private class DataSender extends AsyncTask<URL,Void,String>{


        ServerComms sc;
        String params;

        public DataSender(ServerComms sc, String params){
            this.params=params;
            this.sc=sc;

        }

        @Override
        protected String doInBackground(URL... urls) {
            //Code from http://www.xyzws.com/javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
            URL url=urls[0];
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(params.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches (false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream ());
                wr.writeBytes (params);
                wr.flush ();
                wr.close ();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();

            } catch (Exception e) {

                e.printStackTrace();
                return null;

            } finally {

                if(connection != null) {
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
        protected void onPostExecute(String res){
            this.sc.onSendReturn(res);
        }
    }

}


