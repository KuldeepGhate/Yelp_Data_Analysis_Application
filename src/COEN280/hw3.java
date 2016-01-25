package COEN280;

import net.proteanit.sql.DbUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class hw3 {

    public JFrame frame;
    public JTable table;
    JTable business_table = new JTable();
    JScrollPane scrollPane = new JScrollPane(business_table);
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel modelReviews = new DefaultTableModel();
    String header[] = {"ReviewDate","Stars","Review Text","UserId","Useful Votes"};


    /**
     * Create the application.
     */
    public hw3() {
        initialize();
       // populate();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {


        String[] labels = {"Active Life", "Arts & Entertainment", "Automotive", "Car Rental", "Cafes", "Beauty & Spas", "Convenience Stores", "Dentists", "Doctors", "Drugstores", "Department Stores", "Education", "Event Planning & Services", "Flowers & Gifts", "Food", "Health & Medical", "Home Services", "Home & Garden", "Hospitals", "Hotels & Travel", "Hardware Stores", "Grocery", "Medical Centers", "Nurseries & Gardening", "Nightlife", "Restaurants", "Shopping", "Transportation"};
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] to = {"00:00","01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        String[] from = {"1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00","8:00","9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00","24:00"};
        String[] columns = {"Business", "State", "City","Stars"};
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 950);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 750);
        frame.getContentPane().setBackground(Color.BLUE);
        frame.setResizable(false);


        table = new JTable();
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        JButton btnSearch = new JButton("SEARCH");
        JButton btnClose = new JButton("CLOSE");

        JComboBox daysComboBox = new JComboBox(days);

        JLabel lblDay = new JLabel("Day");
        lblDay.setForeground(Color.WHITE);

        JLabel lblFrom = new JLabel("From");
        lblFrom.setForeground(Color.WHITE);
        JComboBox fromBox = new JComboBox(to);

        JLabel lblTo = new JLabel("To");
        lblTo.setForeground(Color.WHITE);
        JComboBox toBox = new JComboBox(to);

        JLabel lblSearch = new JLabel("Search");
        lblSearch.setForeground(Color.WHITE);
        JComboBox searchBox = new JComboBox();

        JPanel subCatPanel = new JPanel(new GridLayout(0, 1));
        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        JPanel categoriesPanel = new JPanel(new GridLayout(0, 1));

        List<JCheckBox> categoriesCheckBoxList = new ArrayList<>();
        List<JCheckBox> subCheckboxesList = new ArrayList<>();
        List<JCheckBox> attrCheckboxesList = new ArrayList<>();

        JScrollPane categoriesScroll = new JScrollPane(categoriesPanel);
        JScrollPane subCategoriesScroll = new JScrollPane(subCatPanel);
        JScrollPane attributesScroll = new JScrollPane(attrPanel);
        JScrollPane tableScroll = new JScrollPane(table);
        List<String> businessID = new ArrayList<String>();


        /**Steps for adding displaying checkboxes in the panel
         * 1. Query the results
         * 2. Store the results in checkbox
         * 3. Put the checkboxes in an arraylist
         * 4. Populate the JPanel with the arraylist of checkboxes*/

        /**Populate categories checkbox list*/
        for (String elements : labels) {
            JCheckBox box = new JCheckBox(elements);
            categoriesCheckBoxList.add(box);
        }

        /**ActionListener for attributes list*/
        ActionListener actionListener_attr = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };


        ItemListener itemlistener_sub = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                List<String> attr_results = new ArrayList<>();

                AbstractButton absBtn = (AbstractButton) e.getSource();
                int st = e.getStateChange();
                if(st == ItemEvent.SELECTED){
                    for(int i = 0; i< businessID.size();i++) {
                        String query_attributes = "SELECT DISTINCT ATTRIBUTE FROM BUSINESS_ATTRIBUTES WHERE BUSINESSID = '" + businessID.get(i) + "'";
                        attr_results = execQuery(query_attributes);


                        //populates attributes checkboxes list
                        for (int j = 0; j < attr_results.size(); j++) {
                         //   System.out.println(attr_results.get(j));
                            JCheckBox attrBox = new JCheckBox(attr_results.get(j));
                            attrCheckboxesList.add(attrBox);
                        }
                    }
                }
                if(st == ItemEvent.DESELECTED){
                    attrCheckboxesList.clear();
                    attrPanel.removeAll();
                    attrPanel.revalidate();
                    attrPanel.repaint();
                    attr_results.clear();
                    String query_attributes=null;
                    for(int i=0 ; i < subCheckboxesList.size() ; i++){
                        if(subCheckboxesList.get(i).isSelected()){
                            query_attributes = "SELECT DISTINCT ATTRIBUTE FROM BUSINESS_ATTRIBUTES WHERE BUSINESSID = '" + businessID.get(i) + "'";
                            attr_results = execQuery(query_attributes);
                            for(int j=0;j< attr_results.size();j++){
                                JCheckBox box = new JCheckBox(attr_results.get(j));
                                attrCheckboxesList.add(box);
                            }
                        }
                    }
                }
                for(int k = 0; k<attrCheckboxesList.size(); k++){
                    if(!attrCheckboxesList.get(k).getText().equals("null")) {
                        if(attrPanel.getComponentCount() == 0){
                            attrPanel.add(attrCheckboxesList.get(k));
                        }
                        else {
                            for (Component c : attrPanel.getComponents()) {
                                //System.out.println("here");
                                if (c instanceof JCheckBox) {
                                    //         System.out.println(subCheckboxes.get(i).getText());
                                        attrPanel.add(attrCheckboxesList.get(k));
                                }
                            }
                        }
                    }
                    attrPanel.revalidate();
                    attrPanel.repaint();
                }
            }
        };

        /**ItemListener for Categories list*/
        ItemListener itemListener_cat = new ItemListener() {
            List<String> results_cat = new ArrayList<String>();
            List<String> results_bid = new ArrayList<String>();
            @Override
            public void itemStateChanged(ItemEvent e) {
                AbstractButton absBtn = (AbstractButton) e.getSource();
                int st = e.getStateChange();
                if(st == ItemEvent.SELECTED){
                    String query_categories = "SELECT DISTINCT SUB_CATEGORY FROM BUSINESS_CATEGORIES WHERE MAIN_CATEGORY = '" + absBtn.getText()+ "'";
                    String query_business= "SELECT DISTINCT BUSINESSID FROM BUSINESS_CATEGORIES WHERE MAIN_CATEGORY = '" + absBtn.getText()+ "'";
                    results_cat = execQuery(query_categories);
                    for(int i=0;i< results_cat.size();i++){
                        JCheckBox box = new JCheckBox(results_cat.get(i));
                        box.addItemListener(itemlistener_sub);
                        subCheckboxesList.add(box);
                    }
                    results_cat = execQuery(query_business);
                    for(int i=0;i< results_cat.size();i++){
                        //System.out.println(results_cat.get(i));
                        businessID.add(results_cat.get(i));
                    }
                }
                if(st == ItemEvent.DESELECTED){
                    subCheckboxesList.clear();
                    subCatPanel.removeAll();
                    subCatPanel.revalidate();
                    subCatPanel.repaint();
                    results_cat.clear();
                    String query_business=null;
                    for(int i = 0; i< categoriesCheckBoxList.size() ; i++){
                        if(categoriesCheckBoxList.get(i).isSelected()){
                            String query_categories = "SELECT DISTINCT SUB_CATEGORY FROM BUSINESS_CATEGORIES WHERE MAIN_CATEGORY = '" + categoriesCheckBoxList.get(i).getText()+ "'";
                            query_business= "SELECT DISTINCT BUSINESSID FROM BUSINESS_CATEGORIES WHERE MAIN_CATEGORY = '" + categoriesCheckBoxList.get(i).getText()+ "'";

                            results_cat = execQuery(query_categories);
                            results_bid = execQuery(query_business);
                            for(int j=0;j< results_cat.size();j++){
                                JCheckBox box = new JCheckBox(results_cat.get(j));
                                box.addItemListener(itemlistener_sub);
                                subCheckboxesList.add(box);
                            }
                            if(results_bid.size() != 0) {
                                results_bid = execQuery(query_business);
                                for (int k = 0; k < results_bid.size(); k++) {
                       //             System.out.println(results_bid.get(i));
                                    businessID.add(results_bid.get(k));
                                }
                            }
                        }
                    }
                }

                for(int i = 0; i<subCheckboxesList.size(); i++){
                    if(!subCheckboxesList.get(i).getText().equals("null")) {
                        if(subCatPanel.getComponentCount() == 0){
                            subCatPanel.add(subCheckboxesList.get(i));
                        }
                        else {
                            for (Component c : subCatPanel.getComponents()) {
                                //System.out.println("here");
                                if (c instanceof JCheckBox) {
                                    if (!((JCheckBox) c).getText().equals(subCheckboxesList.get(i).getText())) {
                                        subCatPanel.add(subCheckboxesList.get(i));
                                    }
                                }
                            }
                        }

                    }
                    subCatPanel.revalidate();
                    subCatPanel.repaint();
                }
            }
        };

        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                AbstractButton absB = (AbstractButton) e.getSource();
                int st = e.getStateChange();
                List<String> results = new ArrayList<String>();
                Map<String,String> temp = new HashMap<String,String>();

                if (st == ItemEvent.SELECTED) {
                    String query_categories = "SELECT DISTINCT SUB_CATEGORY FROM BUSINESS_CATEGORIES WHERE MAIN_CATEGORY = '" + absB.getText()+ "'";
                    String query_business= "SELECT DISTINCT BUSINESSID FROM BUSINESS_CATEGORIES WHERE MAIN_CATEGORY = '" + absB.getText()+ "'";
                    results = execQuery(query_categories);
                    for(int i=0;i<results.size();i++){
                        temp.put(absB.getText(),results.get(i));
                        JCheckBox box = new JCheckBox(results.get(i));
                        box.addItemListener(itemlistener_sub);
                        subCheckboxesList.add(box);
                        for(int j = 0; j<subCheckboxesList.size(); j++){
                            if(!subCheckboxesList.get(j).getText().equals("null")) {
                                    subCatPanel.add(subCheckboxesList.get(j));
                            }
                            subCatPanel.revalidate();
                            subCatPanel.repaint();
                        }
                    }
                    results = execQuery(query_business);
                    for(int i=0;i<results.size();i++){
                              //  System.out.println(results.get(i));
                        businessID.add(results.get(i));
                    }
                }

                if(st == ItemEvent.DESELECTED){
                    //clear the jpanel
                    //reo=m
                    //populate it with the hashmap values
                    for(int i = 0 ; i< temp.size(); i++){
                        if(temp.get(i) == absB.getText()){
                            temp.remove(temp.get(absB.getText()));
                        }
                        else{

                        }
                    }

                }
            }
        };
        ActionListener btnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> subCheckboxesChecked = new ArrayList<>();
                List<String> categoryCheckboxesChecked = new ArrayList<>();
                List<String> attrCheckboxesChecked = new ArrayList<>();
                List<String> results_catBid = new ArrayList<String>();
                List<String> results_attrBid = new ArrayList<String>();
                List<String> hours_Bid = new ArrayList<String>();
                List<String> results_Bid = new ArrayList<String>();
                List<String> temp = new ArrayList<String>();

                String cat_bid= null;
                for(int i = 0; i< categoriesCheckBoxList.size(); i++){
                    if(categoriesCheckBoxList.get(i).isSelected())
                        categoryCheckboxesChecked.add(categoriesCheckBoxList.get(i).getText());
                }

                for(int i = 0; i< subCheckboxesList.size(); i++){
                    if(subCheckboxesList.get(i).isSelected())
                        subCheckboxesChecked.add(subCheckboxesList.get(i).getText());
                }

                for(int i = 0; i< attrCheckboxesList.size(); i++){
                    if(attrCheckboxesList.get(i).isSelected())
                        attrCheckboxesChecked.add(attrCheckboxesList.get(i).getText());
                }
                //get bid from BUSINESS_CATEGORY
                //get bid from BUSINESS_ATTRIBUTES
                //get bid from BUSINESS_HOURS
                //get the final answer from BUSINESS
                String attr_bid=null;
                for(int i = 0; i< categoryCheckboxesChecked.size(); i++){
                    for(int j = 0; j< subCheckboxesChecked.size(); j++){
                        cat_bid = "SELECT Distinct BUSINESSID FROM BUSINESS_CATEGORIES WHERE MAIN_CATEGORY = '"+categoryCheckboxesChecked.get(i) + "' AND SUB_CATEGORY = '" + subCheckboxesChecked.get(j) + "'";
                        temp = execQuery(cat_bid);
                     //   System.out.println(temp);
                    }
                    for(int k = 0; k< temp.size();k++)
                        results_catBid.add(temp.get(k));
                }
                temp.clear();

               // System.out.println(results_catBid);
                for(int i = 0; i< results_catBid.size(); i++){
                    for(int j = 0 ; j<attrCheckboxesChecked.size();j++){
                        attr_bid = "SELECT Distinct BUSINESSID FROM BUSINESS_ATTRIBUTES WHERE BUSINESSID = '" + results_catBid.get(i) +"' AND ATTRIBUTE = '"+ attrCheckboxesChecked.get(j)+"'";
                        temp = execQuery(attr_bid);
                    }
                    for(int k = 0; k< temp.size();k++)
                        results_attrBid.add(temp.get(k));
                }
                //   System.out.println(results_attrBid);

                String to =null;
                temp.clear();
                for(int i = 0; i< results_attrBid.size(); i++){
                    to = "SELECT distinct BUSINESSID FROM BUSINESS_HOURS WHERE BUSINESSID = '"+results_attrBid.get(i)+"' AND OPEN<= "+fromBox.getSelectedIndex()+ "AND CLOSE >= "+toBox.getSelectedIndex();
                    temp=execQuery(to);
                }

                for(int k = 0; k< temp.size();k++)
                    hours_Bid.add(temp.get(k));


                temp.clear();
                String query_bid= null;
                for(int i = 0; i< hours_Bid.size(); i++){
                    //for(int j = 0 ; j<)
                    query_bid = "SELECT NAME,STATE,CITY,STARS FROM BUSINESS WHERE BUSINESS_ID = '"+hours_Bid.get(i) + "'";
                    execQuery(query_bid);
                }
//                for(int i = 0;i < temp.size();i++){
//                    results_Bid.add(temp.get(i));
//                }
//
//                System.out.println(results_Bid);
            }

        };

        btnSearch.addActionListener(btnListener);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        for(int i = 0; i<categoriesCheckBoxList.size(); i++){

            categoriesCheckBoxList.get(i).addItemListener(itemListener_cat);
            //    categoriesCheckBoxList.get(i).addActionListener(actionListener_cat);
            categoriesPanel.add(categoriesCheckBoxList.get(i));
        }

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {


                List<String> results_Bid = new ArrayList<String>();
                List<String> temp = new ArrayList<String>();
                String businessID=null;
                for(int i=0;i<business_table.getColumnCount();i++)
                {
                    TableColumn column1 = business_table.getTableHeader().getColumnModel().getColumn(i);
                    column1.setHeaderValue(header[i]);
                }
                String bName = table.getValueAt(table.getSelectedRow(),0).toString();

                JFrame reviewsFrame = new JFrame("Reviews");
                reviewsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                reviewsFrame.setVisible(true);
                reviewsFrame.setSize(500, 500);
                reviewsFrame.add(scrollPane,BorderLayout.CENTER);

                String query_bid = "SELECT BUSINESS_ID FROM BUSINESS WHERE NAME = '"+bName+"'";
                temp = execQuery(query_bid);
                for (int i = 0 ; i< temp.size(); i++){
                    businessID = temp.get(i);
                    //System.out.println(businessID);
                    String query_reviews = "SELECT REVIEW_DATE, STARS, TEXT, USERID, VOTES_USEFUL FROM REVIEWS WHERE BUSINESSID= '" +businessID+"'";
                    execQuery(query_reviews);
                }

                //System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
            }
        });
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(36)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(lblDay)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(daysComboBox, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addGap(7)
                                                .addComponent(lblFrom)
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addComponent(fromBox, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                                .addGap(38)
                                                .addComponent(lblTo)
                                                .addGap(18)
                                                .addComponent(toBox, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                                .addGap(38)
                                                .addComponent(lblSearch)
                                                .addGap(18)
                                                .addComponent(searchBox, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(categoriesScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                .addComponent(subCategoriesScroll, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(attributesScroll, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(btnSearch)
                                                .addGap(36)
                                                .addComponent(btnClose, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(tableScroll, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE))
                                .addGap(6))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
                                .addGap(85)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(categoriesScroll, GroupLayout.PREFERRED_SIZE, 519, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                                .addComponent(subCategoriesScroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(tableScroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                                                .addComponent(attributesScroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(fromBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblFrom)
                                        .addComponent(daysComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblDay)
                                        .addComponent(toBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTo)
                                        .addComponent(lblSearch)
                                        .addComponent(searchBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnClose, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(36))
        );
        frame.getContentPane().setLayout(groupLayout);

    }
    public List<String> execQuery(String query) {
        List<String> results = new ArrayList<String>(){};
        try {
             /*1.Load the class drivers*/
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

            /*5.Execute the statement*/
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int columnsNumber = rsmd.getColumnCount();
            if(columnsNumber == 4){
                while(resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),new Integer(resultSet.getInt(4))});
                }

            }
            if(columnsNumber == 5){

                business_table.setModel(DbUtils.resultSetToTableModel(resultSet));

            }
            else {
            /*6.Process the result*/
                while (resultSet.next()) {
                    results.add(resultSet.getString(1));
                }
            }
            connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException r){
            r.printStackTrace();
        }

        return results;
    }
}
