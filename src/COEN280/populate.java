package COEN280;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.List;

/**
 * Created by Kuldeep on 11/12/2015.
 */
public class populate {

    private JPanel formOut;
    private JButton button1;

    @SuppressWarnings("unchecked")
    public void readUserFile(){
        BufferedReader br = null;
        Object obj;
        JSONParser parser = new JSONParser();
        String sCurrentLine;
        int count_user = 0,count_business = 0,count_reviews = 0,count_checkin = 0;

        Map<String, Object> retMap = new HashMap<String, Object>();
        try{
            br = new BufferedReader(new FileReader("E:\\Quarter 5\\COEN280 Database\\Homework\\Homework 3\\YelpDataset\\YelpDataset-CptS451\\yelp_user.json"));
            while(count_user <= 2000) {
                sCurrentLine = br.readLine();
                obj = parser.parse(sCurrentLine);
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
                retMap = toMap(jsonObject);
                connect(retMap, 1);    //populate YELP_USER
                connect(retMap, 2);     //populate USER_FRIENDS
                connect(retMap, 3);     //populate USER_COMPLIMENTS
                count_user++;
            }
            br = new BufferedReader(new FileReader("E:\\Quarter 5\\COEN280 Database\\Homework\\Homework 3\\YelpDataset\\YelpDataset-CptS451\\yelp_business.json"));
            while(count_business <= 2000) {
                sCurrentLine = br.readLine();
                obj = parser.parse(sCurrentLine);
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
                retMap = toMap(jsonObject);
                connect(retMap, 4);    //populate BUSINESS
                connect(retMap, 5);     //populate BUSINESS_NEIGHBORHOODS
                connect(retMap, 6);     //populate BUSINESS_ATTRIBUTES
                connect(retMap, 7);     //populate BUSINESS_HOURS
                connect(retMap, 8);     //populate BUSINESS_Categories
                count_business++;
            }
            br = new BufferedReader(new FileReader("E:\\Quarter 5\\COEN280 Database\\Homework\\Homework 3\\YelpDataset\\YelpDataset-CptS451\\yelp_review.json"));
            //br = new BufferedReader(new FileReader(args[1]));
            while(count_reviews <= 2000) {
                sCurrentLine = br.readLine();
                obj = parser.parse(sCurrentLine);
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
                retMap = toMap(jsonObject);
                connect(retMap, 9);    //populate reviews
                count_reviews++;
            }
            br = new BufferedReader(new FileReader("E:\\Quarter 5\\COEN280 Database\\Homework\\Homework 3\\YelpDataset\\YelpDataset-CptS451\\yelp_checkin.json"));
            while(count_checkin <= 2000) {
                sCurrentLine = br.readLine();
                obj = parser.parse(sCurrentLine);
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
                retMap = toMap(jsonObject);
                connect(retMap, 10);    //populate checkin
                count_checkin++;
            }

        }

        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException p){
            System.out.println("Exception");
        }
        catch (ParseException p){
            System.out.println("Error");
        }
        catch (JSONException j){
            System.out.println("Error1");
        }
    }
    public static Map<String, Object> toMap(org.json.simple.JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator keysItr = object.entrySet().iterator();
        while(keysItr.hasNext()) {
            Map.Entry pair = (Map.Entry)keysItr.next();
            String key = pair.getKey().toString();
            Object value = pair.getValue();
            if(value instanceof JSONArray) {
                value = toList((org.json.simple.JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((org.json.simple.JSONObject) value);
            }
            map.put(key, value);
        }
        return map;

    }
    public static List<Object> toList(org.json.simple.JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((org.json.simple.JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((org.json.simple.JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static void connect(Map<String, Object> obj, int code) {

            /* 1. Load the driver */
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            /*2. Define the connection URL*/
            String host = "localhost";
            String dbName = "orcl";
            int port = 1521;
            String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;

            /*3.Establish the connection*/
            String username = "scott";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(oracleURL, username, password);

            /*4.Create a statement*/
            Statement statement = connection.createStatement();
            String query_del = null;

//            query_del="DELETE FROM BUSINESS_ATTRIBUTES";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM BUSINESS_CATEGORIES";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM BUSINESS_HOURS";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM BUSINESS_NEIGHBORHOODS";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM REVIEWS";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM BUSINESS";
//            statement.executeQuery(query_del);
//            query_del= "DELETE FROM USER_FRIENDS";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM USER_COMPLIMENTS";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM YELP_USER ";
//            statement.executeQuery(query_del);
//            query_del="DELETE FROM CHECKIN";
//            statement.executeQuery(query_del);

            switch (code) {
                case 1:
                    populateUser(obj, statement);
                    break;
                case 2:
                    populateUserFriends(obj, statement);
                    break;
                case 3:
                    populateUserCompliments(obj, statement);
                    break;
                case 4:
                    populateBusiness(obj, statement);
                    break;
                case 5:
                    populateNeighbors(obj, statement);
                    break;
                case 6:
                    populateAttributes(obj, statement);
                    break;
                case 7:
                    populateHours(obj,statement);
                    break;
                case 8:
                    populateCategories(obj, statement);
                    break;
                case 9:
                    populateReviews(obj,statement);
                    break;
                case 10:
                     populateCheckin(obj,statement);
                    break;
            }
            /*Close the connection*/
            connection.close();

        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error loading driver: " + cnfe);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateUser(Map<String, Object> obj, Statement statement) {
        String query = "INSERT INTO YELP_USER VALUES (" + "'" + obj.get("yelping_since") + "', " + ((Map) obj.get("votes")).get("funny") + ", " + ((Map) obj.get("votes")).get("useful") + ", " + ((Map) obj.get("votes")).get("cool") + ", " + obj.get("review_count") + ", '" + obj.get("name").toString().replaceAll("'","''") + "', '" + obj.get("user_id") + "', " + obj.get("fans") + ", " + obj.get("average_stars") + ", '" + obj.get("type") + "')";
        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateUserFriends(Map<String, Object> obj, Statement statement) {
        String userID = null;
        String query = null;
        for (Map.Entry<String, Object> friend : obj.entrySet()) {
            try {
                if (friend.getKey().equals("friends")) {
                    String key = friend.getKey();
                    ArrayList<String> value = (ArrayList<String>) friend.getValue();
                    Iterator itr = value.iterator();
                    while (itr.hasNext()) {
                        query = "INSERT INTO USER_FRIENDS VALUES('" + obj.get("user_id").toString() + "', '" + itr.next() + "')";
                        statement.executeQuery(query);
                    }
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    public static void populateUserCompliments(Map<String, Object> obj, Statement statement) {
        String userID = null;
        String query = null;
        for (Map.Entry<String, Object> friend : obj.entrySet()) {
            try {
                if (friend.getKey().equals("compliments")) {
                    Map<String, Object> inner = (Map<String, Object>) friend.getValue();

                    Iterator itr = inner.entrySet().iterator();
                    while (itr.hasNext()) {
                        Map.Entry pair = (Map.Entry) itr.next();
                        query = "INSERT INTO USER_COMPLIMENTS VALUES('" + obj.get("user_id").toString() + "', '" + pair.getKey().toString() + "', '" + pair.getValue() + "')";
                        statement.executeQuery(query);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void populateBusiness(Map<String, Object> obj, Statement statement) {
        String query = "INSERT INTO BUSINESS VALUES (" + "'" + obj.get("business_id") + "', '" + obj.get("full_address").toString().replaceAll("'","''") + "', '" + obj.get("open") + "', '" + obj.get("city") + "', '" + obj.get("state") + "', " + obj.get("latitude") + ", " + obj.get("longitude") + ", " + obj.get("review_count") + ", '" + obj.get("name").toString().replaceAll("'", "''") + "', " + obj.get("stars") + ", '" + obj.get("type") + "')";
        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateNeighbors(Map<String, Object> obj, Statement statement) {
        String businessID = null;
        String query = null;
        String neighbor = null;

        for (Map.Entry<String, Object> business : obj.entrySet()) {
            try {
                if (business.getKey().equals("neighborhoods")) {
                    ArrayList<String> value = (ArrayList<String>) business.getValue();

                    Iterator itr = value.iterator();
                    while (itr.hasNext()) {
                        neighbor = itr.next().toString();
                        query = "INSERT INTO BUSINESS_NEIGHBORHOODS VALUES('" + obj.get("business_id").toString() + "', '" + neighbor + "')";
                        statement.executeQuery(query);
                    }
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    public static void populateAttributes(Map<String, Object> obj, Statement statement) {
        String businessID = null;
        String query = null;
        String concat = null;
        String business_id = obj.get("business_id").toString();
        Map<String,Object> attr = ((Map<String,Object>)obj.get("attributes"));
        Object objTemp = new Object();

        for(Map.Entry<String, Object>attrItr : attr.entrySet()){
            try {
                if (attrItr.getValue() instanceof Map) {
                    objTemp = attrItr.getValue();
                    Map<String, Object> temp = (Map<String, Object>) objTemp;
                    nestedMap(temp, business_id, statement);
                }
                else {
                    if (attrItr.getValue().toString().equals("true")) {
                        //insert in db
                        query = "INSERT INTO BUSINESS_ATTRIBUTES VALUES('" + business_id.toString().replaceAll("'", "''") + "', '" + attrItr.getKey().toString() + "')";
                        statement.executeQuery(query);

                    } else if (attrItr.getValue().toString().equals("false") || attrItr.getValue().toString().equals("no")) {
                        //do nothing
                    } else {                   // concatenate
                        concat = attrItr.getKey().toString() + "_" + attrItr.getValue().toString();
                        //insert in db
                        query = "INSERT INTO BUSINESS_ATTRIBUTES VALUES('" + business_id + "', '" + concat + "')";
                    }
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }



    public static void nestedMap(Map<String,Object> innerObj,String businessID,Statement statement) {
        String query = null;
        String concat = null;

        for (Map.Entry<String, Object> attrItr : innerObj.entrySet()) {
            try{
                if (attrItr.getValue().toString().equals("true")) {
                    //insert in db
                    query = "INSERT INTO BUSINESS_ATTRIBUTES VALUES('" + businessID + "', '" + attrItr.getKey().toString() + "')";
                    statement.executeQuery(query);
                }
                else if (attrItr.getValue().toString().equals("false") ||attrItr.getValue().toString().equals("no")) {
                    //do nothing
                }
                else {                   // concatenate
                    businessID = attrItr.getKey().toString() + "_" + attrItr.getValue().toString();
                    //insert in db
                    query = "INSERT INTO BUSINESS_ATTRIBUTES VALUES('" + businessID + "', '" + concat + "')";
                    statement.executeQuery(query);

                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    public static void populateHours(Map<String, Object> obj, Statement statement) {
        String businessID = "nothing";
        String query = null;

        //iterate entire object
        for (Map.Entry<String, Object> business : obj.entrySet()) {
            if (business.getKey().equals("hours")) {
                //get days
                Map<String, Object> business_hours = (Map<String, Object>) business.getValue();

                //iterate through days
                for (Map.Entry<String, Object> days : business_hours.entrySet()) {
                    Map<String, Object> innerHours = (Map<String, Object>) days.getValue();
                    //float open_time = Float.parseFloat(open_jsonObject.get("open").toString().substring(0,2) + "." + open_jsonObject.get("open").toString().substring(3));
                    query = "INSERT INTO BUSINESS_HOURS VALUES('" + obj.get("business_id") + "', '" + days.getKey() + "', '" + innerHours.get("close").toString().replaceAll(":",".") + "', '" + innerHours.get("open").toString().replaceAll(":", ".")+ "')";
                    try {
                        statement.executeQuery(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static void populateCategories(Map<String, Object> obj, Statement statement) {
        String userID = null;
        String query = null;
        String temp_itr = null;
        ArrayList<String> main = new ArrayList<String>();
        ArrayList<String> temp_main = new ArrayList<String>();
        ArrayList<String> temp_sub = new ArrayList<String>();

        main.add("Active Life");
        main.add("Arts & Entertainment");
        main.add("Automotive");
        main.add("Car Rental");
        main.add("Cafes");
        main.add("Beauty & Spas");
        main.add("Convenience Stores");
        main.add("Dentists");
        main.add("Doctors");
        main.add("Drugstores");
        main.add("Department Stores");
        main.add("Education");
        main.add("Event Planning & Services");
        main.add("Flowers & Gifts");
        main.add("Food");
        main.add("Health & Medical");
        main.add("Home Services");
        main.add("Home & Garden");
        main.add("Hospitals");
        main.add("Hotels & Travel");
        main.add("Hardware Stores");
        main.add("Grocery");
        main.add("Medical Centers");
        main.add("Nurseries & Gardening");
        main.add("Nightlife");
        main.add("Restaurants");
        main.add("Shopping");
        main.add("Transportation");


        for (Map.Entry<String, Object> friend : obj.entrySet()) {
            if (friend.getKey().equals("user_id")) {
                userID = (String) friend.getValue();
            }
            if (friend.getKey().equals("categories")) {
                ArrayList<String> value = (ArrayList<String>) friend.getValue();

                Iterator itr = value.iterator();
                while (itr.hasNext()) {
                    temp_itr = itr.next().toString();
                    if (main.contains(temp_itr)) {
                        temp_main.add(temp_itr);
                    } else {
                        temp_sub.add(temp_itr);
                    }
                }
                try {
                    if (temp_sub.size() == 0) {
                        for (String main_arr : temp_main) {
                            query = "INSERT INTO BUSINESS_CATEGORIES VALUES('" + obj.get("business_id") + "', '" + main_arr.replaceAll("'", "''") + "', '" + null + "')";
                            statement.executeQuery(query);
                        }

                    } else {
                        for (String main_arr : temp_main) {
                            for (String sub_arr : temp_sub) {
                                query = "INSERT INTO BUSINESS_CATEGORIES VALUES('" + obj.get("business_id") + "', '" + main_arr.replaceAll("'", "''") + "', '" + sub_arr.replaceAll("'", "''") + "')";
                                statement.executeQuery(query);
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void populateReviews(Map<String, Object> obj, Statement statement) {
        String query = "INSERT INTO REVIEWS VALUES ("+((Map) obj.get("votes")).get("funny") + ", " + ((Map) obj.get("votes")).get("useful") + ", " + ((Map) obj.get("votes")).get("cool") + ", '" + obj.get("user_id") + "', '" + obj.get("review_id") + "', " + obj.get("stars") + ", '" + obj.get("date") + "', '" + obj.get("text").toString().replaceAll("'", "''") + "', '" + obj.get("type") +"', '" + obj.get("business_id") +"')";

        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void populateCheckin(Map<String, Object> obj, Statement statement){
        String query = "INSERT INTO CHECKIN VALUES ('"+ obj.get("type") + "', '" + obj.get("business_id") + "')";

        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        populate pop = new populate();
         // pop.readUserFile();
        //  pop.readBusinessFile();
       //   pop.readReviewsFile();
          //pop.readCheckinFile(args);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    hw3 window = new hw3();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Map<String, Object> user = new HashMap<String, Object>();
    }
}
