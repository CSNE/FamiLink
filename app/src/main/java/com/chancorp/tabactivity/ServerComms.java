package com.chancorp.tabactivity;

import android.content.Context;
import android.content.Intent;
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
import java.util.Timer;
import java.util.TimerTask;

//서버 통신 클래스
public class ServerComms {
    private static final int MAX_RETRIES = 5, RETRY_INTERVAL_MILLISEC=5000;
    static String serverBaseURL;
    static FamilyData fd;
    static RedrawableFragment[] rdf;
    DataReturnListener drl;


    public static void setup(String u, FamilyData f, RedrawableFragment[] r) {
        fd = f;
        serverBaseURL = u;
        rdf = r;

    }

    public static FamilyData getStaticFamilyData(){
        return fd;
    }

    private URL getURL() {
        try {
            if (!fd.isRegistered()) {
                return new URL(serverBaseURL + "/-1");
            } else {
                return new URL(serverBaseURL + "/" + Integer.toString(fd.getFamilyID()) + "?pw=" + fd.getCredentials().getPasswordHash());
            }
        } catch (MalformedURLException e) {
            Log.e("Familink", "MalformedURLException");
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

    public void setDataReturnListener(DataReturnListener drl) {
        this.drl = drl;
    }

    public void clearDataReturnListener() {
        this.drl = null;
    }

    public void refreshData() {
        sendGET("Parse Family Data");
    }

    public void getID(String familyName) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "get ID");
        pe.addDataSet("name", familyName);
        postReq = pe.encode();
        this.sendPOST(postReq, "get ID");
    }

    public void addFamily(UserInformation c) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add family");
        pe.addDataSet("password hash", c.getPasswordHash());
        pe.addDataSet("name", c.getID());
        postReq = pe.encode();
        this.sendPOST(postReq, "Add Family");
    }

    public void deleteFamily() {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "delete family");
        pe.addDataSet("familyID", Integer.toString(fd.getFamilyID()));
        postReq = pe.encode();
        this.sendPOST(postReq, "Delete Family");
    }

    public void addMe(String name, String number) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add person");
        pe.addDataSet("familyID", Integer.toString(fd.getFamilyID()));
        pe.addDataSet("name", name);
        pe.addDataSet("phoneNumber", number);
        postReq = pe.encode();
        this.sendPOST(postReq, "Add Myself");
    }

    public void updateStatus(RouterInformation ri, boolean extraCheck, int now, Context c) {
        Log.d("Familink", "is there server delay ???! ServerComms>updateStatus() called");

        if (fd.matchRouter(ri)) {
            Log.d("Familink", "is there server delay ???! router matched. inside.");
            this.gotInside();
        } else {
            Log.d("Familink", "router not matched. outside.");
            if(extraCheck && now == 1) {
                //TODO : when extracheck activited, check if left the home last.
                if (fd.numInside()<=1) {
                    Log.d("Familink", "Only one person in home, and going out. Starting lockscreen.");
                    Intent itt = new Intent(c, Activity_Lockscreen.class);
                    itt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(itt);
                }

            }
            this.gotOutside();
        }

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

    public void addToDo(ToDo td) {
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add task");
        pe.addDataSet("personID", Integer.toString(fd.getMyID()));
        pe.addDataSet("name", td.getTitle());
        pe.addDataSet("text", td.getDescription());
        pe.addDataSet("due", td.getStringDue(true));
        postReq = pe.encode();
        this.sendPOST(postReq, "Add ToDo");
    }
    public void deleteToDo(ToDo td){
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "delete task");
        pe.addDataSet("taskID", Integer.toString(td.getID()));
        postReq = pe.encode();
        this.sendPOST(postReq, "Report Outside");
    }

    public void addRouter(RouterInformation ri){
        fd.addRouter(ri);
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "add wifi");
        pe.addDataSet("name", ri.getName());
        pe.addDataSet("address", ri.getMacAddr());
        postReq = pe.encode();
        this.sendPOST(postReq, "Add Wi-Fi");
    }

    public void deleteRouter(RouterInformation ri){
        fd.deleteRouter(ri);
        String postReq = new String();
        POSTEncoder pe = new POSTEncoder();
        pe.addDataSet("request type", "delete wifi");
        pe.addDataSet("wifiID", Integer.toString(ri.getID()));
        postReq = pe.encode();
        this.sendPOST(postReq, "Delete Wi-Fi");
    }


    public void sendGET(String requestType) {
        Log.d("Familink", "GETting from " + getURL());
        DataRetriever dr = new DataRetriever(this, 1);
        dr.setRequestType(requestType);
        dr.execute(getURL());
    }

    public void onGETReturn(String data, String requestType, int tries) {
        if (data == null) {

            if (tries >= MAX_RETRIES) {
                Log.e("Familink", "GET failed after "+MAX_RETRIES+" tries.");
                return;
            } else Log.d("Familink", "Null returned to GET request. Retrying. (try "+tries+")");

            final String requestTypeF=requestType;
            final int triesF=tries;
            final ServerComms scF=this;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    DataRetriever dr = new DataRetriever(scF, triesF + 1);
                    dr.setRequestType(requestTypeF);
                    dr.execute(getURL());
                }
            }, RETRY_INTERVAL_MILLISEC);

        } else {
            Log.d("Familink", "GET returned. \nRequest type:" + requestType + "\nData Returned: " + data);
            if (requestType.equals("Parse Family Data")) {
                fd.parseData(data);
                for (RedrawableFragment r : this.rdf) {
                    r.redraw();
                }
            }
            if (drl != null) {
                Log.d("Familink", "Calling DataReturnListener");
                drl.onReturn(data);
            }
        }

    }

    public void sendPOST(String s, String requestType) {
        Log.d("FamiLink", "Sending POST to " + getURL() + " msg: " + s);
        DataSender ds = new DataSender(this, s, requestType, 1);
        ds.setRequestType(requestType);
        ds.execute(getURL());
    }


    public void onPOSTReturn(String data, String origParams, String requestType, int tries) {
        if (data == null) {
            if (tries >= MAX_RETRIES) {
                Log.e("Familink", "POST failed after "+MAX_RETRIES+" tries.");
                return;
            } else Log.d("Familink", "Null returned to POST request. Retrying. (try "+tries+")");

            final String requestTypeF=requestType,origParamsF=origParams;
            final int triesF=tries;
            final ServerComms scF=this;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    DataSender ds = new DataSender(scF, origParamsF, requestTypeF, triesF + 1);
                    ds.setRequestType(requestTypeF);
                    ds.execute(getURL());
                }
            }, RETRY_INTERVAL_MILLISEC);

        } else {
            Log.d("Familink", "POST returned. \nRequest type:" + requestType + "\nData Returned: " + data);
            if (requestType.equals("Add Family") || requestType.equals("get ID")) {
                try {
                    fd.setFamilyID(Integer.parseInt(data.trim()));
                } catch (NumberFormatException e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    Log.e("Familink", "NumberFormatException occurred in ServerComms>onPOSTReturn>get ID");
                }
            } else if (requestType.equals("Add Myself")) {
                try {
                    fd.setMyID(Integer.parseInt(data.trim()));
                } catch (NumberFormatException e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    Log.e("Familink", "NumberFormatException occurred in ServerComms>onPOSTReturn>AddMyself");
                }
            }

            refreshData();

            if (drl != null) {
                Log.d("Familink", "Calling DataReturnListener");
                drl.onReturn(data);
            }
        }
    }


    private class DataRetriever extends AsyncTask<URL, Void, String> {
        String requestType;
        ServerComms sc;
        int tries = 0;

        public DataRetriever(ServerComms sc, int tries) {

            super();
            this.sc = sc;
            this.tries = tries;
            Log.d("Familink", "DataRetriever initialized. try " + tries);
        }

        public void setRequestType(String s) {
            this.requestType = s;
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
                Log.w("Familink", "Error in GET(ServerComms>DataRetriever>doInBackground).\n" + errors.toString().substring(0, 100) + "...(omitted)");
                //Log.w("Familink", "Error in GET(ServerComms>DataRetriever>doInBackground).");
                return null;
            }
        }

        protected void onPostExecute(String result) {
            Log.d("Familink", "DataRetruever result:" + result);

            this.sc.onGETReturn(result, requestType, tries);
        }


    }

    private class DataSender extends AsyncTask<URL, Void, String> {


        ServerComms sc;
        String params;
        String requestType;
        String origParams;
        int tries;

        public DataSender(ServerComms sc, String params, String requestType, int tries) {
            this.params = params;
            this.sc = sc;
            this.origParams = params;
            this.tries = tries;
            Log.d("Familink", "DataSender initialized. try " + tries);
        }

        public void setRequestType(String s) {
            this.requestType = s;
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
                Log.w("Familink", "Error in POST(ServerComms>DataSender>doInBackground).\n" + errors.toString().substring(0, 100) + "...(omitted)");
                //Log.w("Familink", "Error in POST(ServerComms>DataSender>doInBackground).");
                return null;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }


        @Override
        protected void onPostExecute(String res) {

            this.sc.onPOSTReturn(res, origParams, requestType, tries);
        }
    }

}


