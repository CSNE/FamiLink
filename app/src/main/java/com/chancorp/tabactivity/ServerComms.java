package com.chancorp.tabactivity;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Chan on 2015-09-20.
 */
public class ServerComms {

    URL serverURL;
    String urlAppend;
    FamilyData fd;

    RedrawableFragment[] rdf;

    public ServerComms(String url, FamilyData fd, RedrawableFragment[] rdf) {
        this.fd = fd;
        urlAppend="/"+Integer.toString(fd.getID());

        this.rdf=rdf;

        try {
            this.serverURL = new URL(url+urlAppend);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void retrive_data(){
        DataRetriever dr=new DataRetriever(this);
        dr.execute(this.serverURL);
    }
    public void on_data_return(String data){
        System.out.println("Data Returned: "+data);
        try {
            fd.parseData(data);
        }catch (FamilyDataException e){
            e.printStackTrace();
        }

        for(RedrawableFragment r:this.rdf){
            r.redraw();
        }

    }

    public void sendData(){
        DataSender ds=new DataSender(this, "Hello=world&what=the+fuck");
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
                return "ERR";
            }
        }

        protected void onPostExecute(String result) {
            this.sc.on_data_return(result);
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


