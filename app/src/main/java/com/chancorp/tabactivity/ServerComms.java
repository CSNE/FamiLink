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

    static String serverBaseURL,queryString;
    static FamilyData fd;
    static RedrawableFragment[] rdf;
    DataReturnListener drl;


    public static void setup(String u, FamilyData f, RedrawableFragment[] r) {
        fd = f;
        serverBaseURL=u;
        rdf = r;

    }

    private URL getURL(){
        try {
            if (!fd.isRegistered()) {
                return new URL(serverBaseURL+"/-1");
            }else{
                return new URL(serverBaseURL + "/" + Integer.toString(fd.getFamilyID()) + "?pw=" + fd.getCredentials().getPasswordHash());
            }
        }catch(MalformedURLException e){
            Log.e("Familink","MalformedURLException");
            return null;
        }
    }

    public ServerComms() {
    }



/*
    public void setQueryHash(String pass) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(pass.getBytes());
            byte[] digest = sha.digest();

            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest)
                sb.append(String.format("%02x", b & 0xff));


            queryString = "?pw=" + sb.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e("Familink", "NoSuchAlgorithmException on ServerComms>setQueryHash");
        }

    }

    public void resetQueryHash() {
        queryString="";
    }*/

    public void setDataReturnListener(DataReturnListener drl){
        this.drl=drl;
    }
    public void clearDataReturnListener(){
        this.drl=null;
    }

    public void refreshData(){
        sendGET("Parse Family Data");
    }

    public void getID(String familyName){
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "get ID");
        pe.addDataSet("name", familyName);
        postReq = pe.encode();
        this.sendPOST(postReq, "get ID");
    }

    public void addFamily(Credentials c) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add family");
        pe.addDataSet("password hash", c.getPasswordHash());
        pe.addDataSet("name", c.getID());
        postReq = pe.encode();
        this.sendPOST(postReq,"Add Family");
    }

    public void deleteFamily() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "delete family");
        pe.addDataSet("familyID", Integer.toString(fd.getFamilyID()));
        postReq = pe.encode();
        this.sendPOST(postReq,"Delete Family");
    }

    public void addMe(String name, String number) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add person");
        pe.addDataSet("familyID", Integer.toString(fd.getFamilyID()));
        pe.addDataSet("name", name);
        pe.addDataSet("phoneNumber", number);
        postReq = pe.encode();
        this.sendPOST(postReq,"Add Myself");
    }

    public void updateStatus(RouterInformation ri) {
        if (fd.matchRouter(ri)) this.gotInside();
        else this.gotOutside();
    }

    public void gotInside() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "chk inside");
        pe.addDataSet("personID", Integer.toString(fd.getMyID()));
        pe.addDataSet("isInside", "1");
        postReq = pe.encode();
        this.sendPOST(postReq, "Report Inside");
    }

    public void gotOutside() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "chk inside");
        pe.addDataSet("personID", Integer.toString(fd.getMyID()));
        pe.addDataSet("isInside", "0");
        postReq = pe.encode();
        this.sendPOST(postReq, "Report Outside");
    }

    public void addToDo(ToDo td){
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add task");
        pe.addDataSet("personID", Integer.toString(fd.getMyID()));
        pe.addDataSet("name", td.getTitle());
        pe.addDataSet("text",td.getDescription());
        pe.addDataSet("due", td.getStringDue());
        postReq = pe.encode();
        this.sendPOST(postReq, "Add ToDo");
    }


    public void sendGET(String requestType) {
        Log.d("Familink", "GETting from " + getURL());
        DataRetriever dr = new DataRetriever(this);
        dr.setRequestType(requestType);
        dr.execute(getURL());
    }

    public void onGETReturn(String data,String requestType) {
        if (data==null) data=new String();
        Log.d("Familink", "GET returned. \nRequest type:"+requestType+"\nData Returned: " + data);
        if (requestType.equals("Parse Family Data")) {
            fd.parseData(data);
            for (RedrawableFragment r : this.rdf) {
                r.redraw();
            }
        }
        if (drl!=null){
            Log.d("Familink","Calling DataReturnListener");
            drl.onReturn(data);
        }

    }

    public void sendPOST(String s, String requestType) {
        Log.d("FamiLink", "Sending POST to " + getURL() + " msg: " + s);
        DataSender ds = new DataSender(this, s, requestType);
        ds.setRequestType(requestType);
        ds.execute(getURL());
    }


    public void onPOSTReturn(String data, String requestType) {
        if (data==null) data=new String();
        Log.d("Familink", "POST returned. \nRequest type:"+requestType+"\nData Returned: " + data);
        if (requestType.equals("Add Family")||requestType.equals("get ID")) {
            try {
                fd.setFamilyID(Integer.parseInt(data.trim()));
            }catch(NumberFormatException e){
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                Log.e("Familink", "NumberFormatException occurred in ServerComms>onPOSTReturn>get ID");
            }
        }else if (requestType.equals("Add Myself")) {
            try {
                fd.setMyID(Integer.parseInt(data.trim()));
            }catch(NumberFormatException e){
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                Log.e("Familink", "NumberFormatException occurred in ServerComms>onPOSTReturn>AddMyself");
            }
        }
        if (drl!=null){
            Log.d("Familink","Calling DataReturnListener");
            drl.onReturn(data);
        }
    }


    private class DataRetriever extends AsyncTask<URL, Void, String> {
        String requestType;
        ServerComms sc;

        public DataRetriever(ServerComms sc) {
            super();
            this.sc = sc;
        }

        public void setRequestType(String s){
            this.requestType=s;
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

            this.sc.onGETReturn(result, requestType);
        }


    }

    private class DataSender extends AsyncTask<URL, Void, String> {


        ServerComms sc;
        String params;
        String requestType;

        public DataSender(ServerComms sc, String params, String requestType) {
            this.params = params;
            this.sc = sc;

        }

        public void setRequestType(String s){
            this.requestType=s;
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


        @Override
        protected void onPostExecute(String res) {
            this.sc.onPOSTReturn(res,requestType);
        }
    }

}


