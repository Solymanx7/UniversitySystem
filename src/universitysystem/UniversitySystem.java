package universitysystem;

import java.awt.Dialog;
import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javax.swing.JOptionPane;


public class UniversitySystem extends Application {
    Statement statement;
    
    @Override
    public void start(Stage primaryStage) throws SQLException {
       Database db = new Database();
       this.statement = db.StartDatabase("university","root","root");
       
       LoginPage();
       //TestPage();
       
       
      
    }

 
    public static void main(String[] args) {
        //Database handling
        
        
        launch(args);
    }
    
    public void LoginPage()
    {
       final Stage Login = new Stage();
       //Elements
       final Button loginB = new Button("Login");
       Label unL = new Label("Username:");
       Label pwL = new Label("Password:");
       final TextField unT = new TextField();
       final PasswordField pwT = new PasswordField();
      
      
       //Content Layout
       GridPane gp = new GridPane();
       loginB.setMinSize(50, 30);
       gp.add(unL, 0, 0);
       gp.add(unT, 1, 0);
       gp.add(pwL, 0, 1);
       gp.add(pwT, 1, 1);
       gp.add(loginB, 1, 7);
       gp.setHgap(5);
       gp.setVgap(5);
       gp.setAlignment(Pos.CENTER);
       //Window Layout
       Scene scene = new Scene(gp);
       Login.setScene(scene);
       Login.setHeight(200);
       Login.setWidth(310);
       Login.setResizable(false);
       Login.setTitle("Login | University System");
       Login.show();
       
       loginB.setOnAction(new EventHandler<ActionEvent>() {
           //check username and password and if found get type of user
           @Override
           public void handle(ActionEvent t) {
               try {
                   String Type = null;
                   ResultSet resultset ;
                   
                   resultset = statement.executeQuery("select * from students where Username="+"'"+unT.getText()+"'"+"and Password="+"'"+pwT.getText()+"'");
                    if(resultset.first())
                    {
                     System.out.println(resultset.getString(1)+" "+resultset.getString(2)+" "+resultset.getString(3));
                     Type ="students";
                     StudentPage(resultset.getString(1),resultset.getString(2),resultset.getString(3),Type,Login);
                     
                    }
                    
                   resultset=statement.executeQuery("select * from instructors where Username="+"'"+unT.getText()+"'"+"and Password="+"'"+pwT.getText()+"'");
                    if(resultset.first())
                    {
                         System.out.println(resultset.getString(1)+" "+resultset.getString(2)+" "+resultset.getString(3));
                         Type ="instructors";
                         InstructorPage(resultset.getString(1),resultset.getString(2),resultset.getString(3),Type,Login);
                       
                    }
                    
                    if(Type==null){
                        JOptionPane.showMessageDialog(null, "The username or password is incorrect", "Invalid Login", JOptionPane.CANCEL_OPTION);
                       Login.close();
                       pwT.clear();
                       Login.show();
                    }
                    
                    
                   
               } catch (SQLException ex) {
                   JOptionPane.showMessageDialog(null, "SQL Exception in Login Button", "Invalid Login", JOptionPane.CANCEL_OPTION);
               }
               
           }
       });
       
       unT.setOnKeyPressed(new EventHandler<KeyEvent> () {

           @Override
           public void handle(KeyEvent t) {
               if(t.getCode() == KeyCode.ENTER)
               {
                  pwT.requestFocus();
               }
           }
       });
       pwT.setOnKeyPressed(new EventHandler<KeyEvent> () {

           @Override
           public void handle(KeyEvent t) {
               if(t.getCode() == KeyCode.ENTER)
               {
                  loginB.requestFocus();
               }
           }
       });
       
        
        
    }
    
    public void StudentPage(final String ID,final String fname,final String lname,final String Type,Stage Prev) throws SQLException
    {
        Prev.close(); // Login 
        final Stage Student = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 900, 800, Color.WHITE);
        TabPane tp = new TabPane();
        BorderPane bp = new BorderPane();
        Tab[] tab = new Tab[5];
        
        //Create 4 Tabs
        for (int i=1;i<5;i++)
        {
            tab[i] = new Tab();
            tab[i].setStyle(Styles.getTabPaneStyle());
            tab[i].setGraphic(buildImage("resources/"+Integer.toString(i)+".png"));
            tab[i].setClosable(false);
            tp.getTabs().add(tab[i]);
        }
        //==============
       
        
        DrawInfoTab1(tab[1], ID, fname, lname, Type, Student);
        DrawRegTab2(tab[2], ID, fname, lname, Type, Student);
        DrawScheduleTab3(tab[3], ID, fname, lname, Type, Student);
        DrawGraduationTab4(tab[4], ID, fname, lname, Type, Student);
       
        //==============
        
        
        
        
        
        
        
        bp.prefHeightProperty().bind(scene.heightProperty());
        bp.prefWidthProperty().bind(scene.widthProperty());
        bp.setCenter(tp);
        root.getChildren().add(bp);
        
    Student.setScene(scene);
    Student.show();
        
     
        
    }
    
    public void InstructorPage(final String ID,final String fname,final String lname,final String Type,Stage Prev) throws SQLException
    {
        Prev.close(); // Login 
        final Stage Instructor = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 900, 800, Color.WHITE);
        TabPane tp = new TabPane();
        BorderPane bp = new BorderPane();
        Tab[] tab = new Tab[5];
        
        //Create 4 Tabs
        for (int i=1;i<5;i++)
        {
            tab[i] = new Tab();
            tab[i].setStyle(Styles.getTabPaneStyle());
            tab[i].setGraphic(buildImage("resources/I"+Integer.toString(i)+".png"));
            tab[i].setClosable(false);
            tp.getTabs().add(tab[i]);
        }
        //==============
       
        
          IDrawInfoTab1(tab[1], ID, fname, lname, Type, Instructor);
          IDrawRegTab2(tab[2], ID, fname, lname, Type, Instructor);
          IDrawScheduleTab3(tab[3], ID, fname, lname, Type, Instructor);
          IDrawStudentsTab4(tab[4], ID, fname, lname, Type, Instructor);
       
        //==============
        
        
        
        
        
        
        bp.prefHeightProperty().bind(scene.heightProperty());
        bp.prefWidthProperty().bind(scene.widthProperty());
        bp.setCenter(tp);
        root.getChildren().add(bp);
        
    Instructor.setScene(scene);
    Instructor.show();
        
    }
  
    public void DrawInfoTab1(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage) throws SQLException
    {
        //Info Tab
        
        //Info Section
        final ResultSet info = statement.executeQuery("select * from students where SID="+"'"+ID+"'");
        info.next();
        //==============
        
        //Image Section
        ImageView image = buildImage("resources/default.png");
        image.setFitWidth(225);
        image.setFitHeight(200);
        //==============
        
        //Edit Section
        HBox hb = new HBox(10);
        ObservableList<String> options = 
        FXCollections.observableArrayList(
        "LocalAddress",
        "Mobile",
        "Email"
        );
        final ComboBox comboBox = new ComboBox(options);
        final TextField tf = new TextField();
        Button Edit = new Button("Edit");
        Edit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    // System.out.println(comboBox.getSelectionModel().getSelectedItem().toString());
                    
                    statement.executeUpdate("Update students Set "+comboBox.getSelectionModel().getSelectedItem().toString()+"="+"'"+tf.getText()+"'"+"Where SID="+"'"+ID+"'");
                    StudentPage(ID, fname, lname, Type, stage);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                }
                
              
            }
        });
        hb.getChildren().addAll(comboBox,tf,Edit);
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(100));
        //==============
        
        //Content
        BorderPane bp1 = new BorderPane();
        bp1.setCenter(GetinfoLabel(info));
        bp1.setRight(image);
        bp1.setBottom(hb);
        bp1.setPadding(new Insets(20, 10, 0, 0));
        tab.setContent(bp1);
        //return tab;
    }
    public void IDrawInfoTab1(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage)throws SQLException
    {
         //Info Tab
        
        //Info Section
        final ResultSet info = statement.executeQuery("select * from instructors where IID="+"'"+ID+"'");
        info.next();
        //==============
        
        //Image Section
        ImageView image = buildImage("resources/default.png");
        image.setFitWidth(225);
        image.setFitHeight(200);
        //==============
        
        //Edit Section
        HBox hb = new HBox(10);
        ObservableList<String> options = 
        FXCollections.observableArrayList(
        "LocalAddress",
        "Mobile",
        "Email"
        );
        final ComboBox comboBox = new ComboBox(options);
        final TextField tf = new TextField();
        Button Edit = new Button("Edit");
        Edit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    // System.out.println(comboBox.getSelectionModel().getSelectedItem().toString());
                    
                    statement.executeUpdate("Update instructors Set "+comboBox.getSelectionModel().getSelectedItem().toString()+"="+"'"+tf.getText()+"'"+"Where IID="+"'"+ID+"'");
                    InstructorPage(ID, fname, lname, Type, stage);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                }
                
              
            }
        });
        hb.getChildren().addAll(comboBox,tf,Edit);
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(100));
        //==============
        
        //Content
        BorderPane bp1 = new BorderPane();
        bp1.setCenter(IGetinfoLabel(info));
        bp1.setRight(image);
        bp1.setBottom(hb);
        bp1.setPadding(new Insets(20, 10, 0, 0));
        tab.setContent(bp1);
        //return tab;
    }
    
    public void DrawRegTab2(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage) throws SQLException
    {
        VBox vb = new VBox(10);
        VBox vb1 = new VBox();
        VBox vb2 = new VBox();
        BorderPane bpTop = new BorderPane();
        BorderPane bpBot = new BorderPane();
        int size;
        int i=0;
      
        
        ResultSet TakencoursesCount = statement.executeQuery("select count(*) from students_courses Where SID="+"'"+ID+"'"+"and Grade !='U'");
        TakencoursesCount.next();
        
        size = Integer.parseInt(TakencoursesCount.getString(1));
        String[] courses = new String[size];
        
        //Registerd Courses
        ResultSet coursesCode= statement.executeQuery("select Code,TextBooks from courses where CID in( select CID from students_courses where SID = "+"'"+ID+"'"+"and Grade !='U')");
        while(coursesCode.next())
        {
            courses[i] = coursesCode.getString(1)+" "+coursesCode.getString(2)+"\t\t";
            i++;
        }
        i=0;
       
        ResultSet coursesCID = statement.executeQuery("select * from students_courses Where SID="+"'"+ID+"'"+"and Grade !='U'");
        while(coursesCID.next())
        {
           
            courses[i]= courses[i] +coursesCID.getString(3); // get course code and its grade
            i++;
        }
        i=0;
        
        ListView<String> list = new ListView<>();
        ObservableList<String> items =FXCollections.observableArrayList (courses);
        list.setItems(items);
        list.setPrefHeight(250);
        list.setPrefWidth(300);
        vb1.getChildren().add(new Label("Taken Courses"));
        vb1.getChildren().add(list);
        
        //===========================================
        
           
        ResultSet UntakencoursesCount = statement.executeQuery("select count(*) from students_courses Where SID="+"'"+ID+"'"+"and Grade ='U'");
        UntakencoursesCount.next();
        
        size = Integer.parseInt(UntakencoursesCount.getString(1));
        String[] Ucourses = new String[size];
        
        //Untaken Courses and remove button
        ResultSet UcoursesCode= statement.executeQuery("select Code,TextBooks from courses where CID in( select CID from students_courses where SID = "+"'"+ID+"'"+"and Grade ='U')");
        while(UcoursesCode.next())
        {
            Ucourses[i] = UcoursesCode.getString(1)+" "+UcoursesCode.getString(2)+"\t\t"; // get course Code
            i++;
        }
        i=0;
       
        ResultSet UcoursesCID = statement.executeQuery("select * from students_courses Where SID="+"'"+ID+"'"+"and Grade ='U'");
        while(UcoursesCID.next())
        {
           
            Ucourses[i]= Ucourses[i] +UcoursesCID.getString(3); // get course grade
            i++;
        }
        
        
        final ListView<String> Ulist = new ListView<>();
        final ObservableList<String> Uitems =FXCollections.observableArrayList (Ucourses);
        Ulist.setItems(Uitems);
        Ulist.setPrefHeight(250);
        Ulist.setPrefWidth(300);
     
        Button Remove = new Button("Remove");
        Remove.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                  String[] m =Ulist.getSelectionModel().getSelectedItem().split(" ");
                  System.out.println(m[0]);
                  Uitems.remove(Ulist.getSelectionModel().getSelectedItem());
                  
                  
                try {
                    statement.execute("Delete from  students_courses where SID ="+"'"+ID+"'"+"and CID = ( select CID from courses where Code = "+"'"+m[0]+"'"+")");
                    statement.executeUpdate("update courses set Capacity = Capacity +1 where Code ="+"'"+m[0]+"'");
                    System.out.println("Data Removed !!!");
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                }
                   
            }
        });
        
        vb2.getChildren().add(new Label("Untaken Courses"));
        vb2.getChildren().add(Ulist);
        vb2.getChildren().add(Remove);
        
 
        //====================================
        
        //Add Course
        int sum =0;
        ResultSet availableCoursesCount = statement.executeQuery("select count(*) from courses where PrerequisiteCourses = 0\n" +
        "Union\n" +
        "select count(*) from courses where PrerequisiteCourses in (Select CID from students_courses where SID ="+"'"+ID+"'"+")");
        
        while(availableCoursesCount.next())
        {
            sum = sum + Integer.parseInt(availableCoursesCount.getString(1));
        }
        
        String [] Avcourses = new String[sum];
        
        ResultSet availableCourses = statement.executeQuery("select distinct CID,Code,TextBooks,Capacity,Day,Periods from courses where PrerequisiteCourses = 0\n" +
        "Union\n" +
        "select distinct CID,Code,TextBooks,Capacity,Day,Periods from courses where PrerequisiteCourses in (Select CID from students_courses where SID  ="+"'"+ID+"'"+")");
        i=0;
        while(availableCourses.next())
        {
            Avcourses[i] =  availableCourses.getString(1)+"\t"+availableCourses.getString(2)+"\t"+availableCourses.getString(3)+"\t"+availableCourses.getString(4)+"\n\t"+availableCourses.getString(5)+"\t"+availableCourses.getString(6);
                    i++;
        }
        
        final ListView<String> Addlist = new ListView<>();
        final ObservableList<String> Additems =FXCollections.observableArrayList (Avcourses);
        Addlist.setItems(Additems);
        Addlist.setPrefHeight(350);
        Addlist.setPrefWidth(800);
        HBox cA = new HBox(300);
        
        Button addB = new Button("Add");
        final Label checkL = new Label("Waiting");
        checkL.setPrefSize(400, 100);
        cA.getChildren().addAll(checkL,addB);
        cA.setAlignment(Pos.TOP_RIGHT);
  
        addB.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
               String[] Choosed = Addlist.getSelectionModel().getSelectedItem().split("\t");
               String CID = Choosed[0];
               String Code = Choosed[1];
               String Textbooks = Choosed[2];
               String Day = Choosed[4];
               String Periods = Choosed[5];
               //String Time = Day+Periods;
               //System.out.println(Time);
               System.out.println("Choosed CID number to add :"+CID);
                try {
                    if(Uitems.size()<3){
                    
                        
                        
                    statement.executeUpdate("insert into students_courses values("+"'"+ID+"'"+","+"'"+CID+"'"+","+"'U')");
                    statement.executeUpdate("update courses set Capacity = Capacity -1 where CID ="+"'"+CID+"'");
                    
                    Uitems.add(Code+" "+Textbooks+"\t\tU\n");
                    checkL.setText("The Course regsiterd successfully :)");
                    checkL.setTextFill(Color.GREEN);
                    }
                    else
                    {
                    checkL.setText("Maximum Course is 3 !!");
                    checkL.setTextFill(Color.RED);
                    }
                } catch (SQLException ex) {
                    //Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                    checkL.setText("The Course maybe taken/registred or Class is full !!");
                    checkL.setTextFill(Color.RED);
                    
                }
               
            }
        });
        addB.setPrefSize(100, 60);
        
        VBox vb3 = new VBox(10);
        vb3.setPadding(new Insets(0, 20, 0, 20));
        vb3.setAlignment(Pos.BOTTOM_RIGHT);
        vb3.getChildren().addAll(Addlist,cA);
        
        
        
        //====================================
       
        bpTop.setLeft(vb1);
        bpTop.setRight(vb2);
        //bpTop.setBottom(vb3); another way of adding register section
        bpBot.setBottom(vb3);
        bpTop.setPadding(new Insets(30,20,0,20));
        vb.getChildren().addAll(bpTop,bpBot);
        
        
        
        
        
        
        tab.setContent(vb);
    }
    public void IDrawRegTab2(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage) throws SQLException
    {
        HBox hb = new HBox();
        

        //Left Side Register Course
        VBox vb1 = new VBox(25);
        VBox vb2 = new VBox(18);
        HBox hb1 = new HBox();
        Button addCourseB = new Button("Add");
        addCourseB.setMaxSize(50, 30);
        addCourseB.setMinSize(80, 30);
        final Label Status = new Label("Status !!");
        
        Label L1 = new Label("CID: ");
        final TextField T1 = new TextField();
        Label L2 = new Label("Code: ");
        final TextField T2 = new TextField();
        Label L5 = new Label("Term: ");
        final TextField T5 = new TextField();
        Label L6 = new Label("Credits: ");
        final TextField T6 = new TextField();
        Label L7 = new Label("ClassRoom: ");
        final TextField T7 = new TextField();
        Label L8 = new Label("Day: ");
        final TextField T8 = new TextField();
        Label L9 = new Label("Periods: ");
        final TextField T9 = new TextField();
        Label L10 = new Label("PrerequisiteCourses: ");
        final TextField T10 = new TextField();
        Label L11 = new Label("TextBooks: ");
        final TextField T11 = new TextField();
        Label L12 = new Label("Note: ");
        final TextField T12 = new TextField();
        Label L13 = new Label("Capacity: ");
        final TextField T13 = new TextField();
        
        addCourseB.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    
                    String DID = getIDepartment(ID);
                    String ClassRoom = T7.getText();
                    String Day = T8.getText();
                    String Periods = T9.getText();
                    if(checkCoursesTimeConflict(Day, Periods, ID) == true || checkCoursesTimeConflict(ClassRoom, Day, Periods, ID) == true)
                    {
                    Status.setTextFill(Color.RED);
                    System.out.println("Course Hasn't Aded");   
                    }
                    else
                    {
                    statement.executeUpdate("insert into courses Values("+"'"+T1.getText()+"',"+"'"+T2.getText()+"',"+"'"+ID+"',"+"'"+DID+"',"+"'"+T5.getText()+"',"+"'"+T6.getText()+"',"+"'"+T7.getText()+"',"+"'"+T8.getText()
                            +"',"+"'"+T9.getText()+"',"+"'"+T10.getText()+"',"+"'"+T11.getText()+"',"+"'"+T12.getText()+"',"+"'"+T13.getText()+"'"+")");
                   Status.setText("Course Added");
                   Status.setTextFill(Color.GREEN);
                   System.out.println("Course added");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        vb1.getChildren().addAll(L1,L2,L5,L6,L7,L8,L9,L10,L11,L12,L13);
        vb2.getChildren().addAll(T1,T2,T5,T6,T7,T8,T9,T10,T11,T12,T13,addCourseB,Status);
        hb1.getChildren().addAll(vb1,vb2);
        hb1.setPadding(new Insets(30));
        
        
        //
        
        
        
        
        
        
        
        
        
        
        //Right Side Edit Course
        VBox vb3 = new VBox();
        HBox hb2 = new HBox(50);
        TextField editT = new TextField();
        Button Edit = new Button();
        int size =0;
        int i=0;
        
        ResultSet r = statement.executeQuery("select count(*) from courses where IID="+"'"+ID+"'");
        r.next();
        size = Integer.parseInt(r.getString(1));
        
        String [] Icourses = new String[size];
        
        r = statement.executeQuery("select Code,TextBooks from courses where IID="+"'"+ID+"'");
        while(r.next())
        {
            Icourses[i] = r.getString(1)+"\t"+r.getString(2);
            i++;
        }
        
        final ListView<String> list = new ListView<>();
        ObservableList<String> items =FXCollections.observableArrayList (Icourses);
        list.setItems(items);
        list.setMaxHeight(200);
        list.setPrefWidth(200);
        
        final ListView<String> list2 = new ListView<>();
        ObservableList<String> items2 =FXCollections.observableArrayList ("CID","Code","Term","Credits","ClassRoom","Day","Periods","PrerequisiteCourses","TextBooks","Note","Capacity");
        list2.setItems(items2);
        list2.setMaxHeight(200);
        list2.setPrefWidth(200);
        
        hb2.getChildren().addAll(list,list2);
        hb2.setPadding(new Insets(20));
        
        final TextField newData  = new TextField();
        newData.setPromptText("Enter your edit");
        Button editB = new Button("Submit Edit");
        final Label editstatusL = new Label("Status");
        editB.setMaxSize(200, 300);
        
        editB.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    String[] c = list.getSelectionModel().getSelectedItem().split("\t");
                    System.out.println(c[0]);
                    
                    statement.executeUpdate("update courses set "+list2.getSelectionModel().getSelectedItem()+"="+"'"+newData.getText()+"'"+"where Code ="+"'"+c[0]+"'"+"");
                    System.out.println("Done Edited");
                    editstatusL.setText("Done Edited");
                    editstatusL.setTextFill(Color.GREEN);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                    editstatusL.setText("Error!!");
                    editstatusL.setTextFill(Color.RED);
                    
                }
            }
        });
        
        vb3.getChildren().addAll(hb2,newData,editB,editstatusL);
        
        
        
        HBox hb3 = new HBox(40);
        hb3.getChildren().addAll(hb1,vb3);
        hb3.setPadding(new Insets(40, 0, 0, 0));
        tab.setContent(hb3);
        
        
        
    }
    
   
    
    public void DrawScheduleTab3(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage) throws SQLException
    {
        BorderPane bp = new BorderPane();
       
        VBox vb = new VBox(20);
       
      
        ResultSet r;
        
        r=statement.executeQuery("Select count(*) from courses where CID in(Select CID from students_courses where SID ="+"'"+ID+"'"+" and Grade ='U')");
        r.next();
        int size = Integer.parseInt(r.getString(1));
        
        r=statement.executeQuery("Select Code,IID,ClassRoom,Day,Periods from courses where CID in(Select CID from students_courses where SID ="+"'"+ID+"'"+" and Grade ='U')");

        
        
        String[] Details = new String[size];
        String[] IIDd = new String[size];
        int i=0;
        
        while (r.next())
        {
         IIDd[i] = r.getString(2);
         Details[i] = r.getString(1)+" "+r.getString(3)+"\t"+r.getString(4)+":"+r.getString(5);
         i++;
        }
        i=0;
        while(i<size)
        {
            Details[i] = getIName(IIDd[i]) +"\n"+ Details[i];
            i++;
        }
        i=0;
       
        if(0<size)
        {
            Label S1 = new Label(Details[0]);
            S1.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
            vb.getChildren().add(S1);
        }
        if(1<size)
        {
            Label S2 = new Label(Details[1]);
            S2.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
            vb.getChildren().add(S2);
        }
        
        if(2<size)
        {
            Label S3 = new Label(Details[2]);
            S3.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
            vb.getChildren().add(S3);
            
        }
        
        
        
       vb.setAlignment(Pos.CENTER);
       
      bp.setCenter(vb);
      bp.setStyle("-fx-border-color: red;\n" +
                   "-fx-border-insets: 200;\n" +
                   "-fx-border-width: 5;\n" +
                   "-fx-border-style: dashed;\n");
      
    
     
      tab.setContent(bp);
     
      
    }
    
     public void IDrawScheduleTab3(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage) throws SQLException
    {
        ResultSet r = statement.executeQuery("select Code,TextBooks,ClassRoom,Day,Periods from courses where IID="+"'"+ID+"'");
        VBox vb = new VBox();
        
        final ListView<String> list1 = new ListView<>();
        ObservableList<String> items1 =FXCollections.observableArrayList ();

        while(r.next())
        {
            items1.add(r.getString(1)+"\t"+r.getString(2)+"\n"+r.getString(3)+"\n"+r.getString(4)+r.getString(5));
        }
        
        list1.setItems(items1);
      
        vb.getChildren().addAll(new Label("Schdeule Timetable:"),list1);
        vb.setAlignment(Pos.CENTER);
      
        
        
        tab.setContent(vb);
        
       
       
    }
    
     
    public void DrawGraduationTab4(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage) throws SQLException
    {   
        
        VBox vb = new VBox(40);
        HBox hb = new HBox(200);
        //Top
        HBox hb1 = new HBox();
        VBox vb1 = new VBox();
        final TextField dp = new TextField();
        final TextField in = new TextField();
        Button searchB1 = new Button("Search");
        //searchB1.setPrefSize(50, 50);
        dp.setPromptText("Department ID");
        in.setPromptText("Instructor ID");
        
        hb1.getChildren().addAll(dp,in);
        final ListView<String> list = new ListView<>();
       
        //Courses ="";
        
        
        searchB1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    String Courses = new String();
                    ResultSet r = statement.executeQuery("select TextBooks,Code from courses where DID= "+"'"+dp.getText()+"'"+"and IID="+"'"+in.getText()+"'");
                    while(r.next())
                    {
                        Courses = Courses+ r.getString(2)+" "+r.getString(1)+"\n";
                    }
                    
                     ObservableList<String> items =FXCollections.observableArrayList (Courses);
                     list.setItems(items);
                     
                     
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        list.setMaxHeight(250);
        vb1.setMaxWidth(200);
        vb1.setAlignment(Pos.TOP_CENTER);
        vb1.setPadding(new Insets(5));
        vb1.setSpacing(5);
        vb1.getChildren().addAll(hb1,searchB1,list);
        tab.setContent(vb1);
   
        
        //
        
       
        //Bot
        HBox hb2 = new HBox();
        VBox vb2 = new VBox();
        final TextField day = new TextField();
        final TextField period = new TextField();
        Button searchB2 = new Button("Search");
       
        day.setPromptText("Day");
        period.setPromptText("Period");
        
        hb2.getChildren().addAll(day,period);
        final ListView<String> list2 = new ListView<>();
       
        //Courses ="";
        
        
        searchB2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    String Courses = new String();
                    ResultSet r = statement.executeQuery("select TextBooks,Code from courses where Day= "+"'"+day.getText()+"'"+"and Periods="+"'"+period.getText()+"'");
                    while(r.next())
                    {
                        Courses = Courses+ r.getString(2)+" "+r.getString(1)+"\n";
                    }
                    
                     ObservableList<String> items =FXCollections.observableArrayList (Courses);
                     list2.setItems(items);
                     
                     
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        list2.setMaxHeight(250);
        vb2.setMaxWidth(200);
        vb2.setAlignment(Pos.TOP_CENTER);
        vb2.setPadding(new Insets(5));
        vb2.setSpacing(5);
        vb2.getChildren().addAll(hb2,searchB2,list2);
        vb.getChildren().addAll(vb1,vb2);
        
        //
        
        //Right Side 
        VBox vb3 = new VBox(20);
        final Label l = new Label("[Request State]");
        
        
        Button graduateB = new Button("Request for Graduation");
        graduateB.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    ResultSet TakencoursesCount = statement.executeQuery("select count(*) from students_courses Where SID="+"'"+ID+"'"+"and Grade !='U'");
                    TakencoursesCount.next();
                    if(Integer.parseInt(TakencoursesCount.getString(1))>=10)
                    {
                        statement.executeUpdate("insert into graduation values("+"'"+ID+"')");
                        l.setText("Request Accepted and sent to yor Advisor ");
                        l.setTextFill(Color.GREEN);
                    }
                    else
                    {
                        l.setText("Request denied !!");
                        l.setTextFill(Color.RED);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
                    l.setText("Request denied !!");
                    l.setTextFill(Color.RED);
                }

            }
        });
        //graduateB.setMaxSize(200, 80);
        graduateB.setPrefSize(200, 80);
        
        vb3.getChildren().addAll(graduateB,l);
        vb3.setAlignment(Pos.CENTER);
        
        hb.getChildren().addAll(vb,vb3);
        //hb.setAlignment(Pos.CENTER_LEFT);
        
        
        
        tab.setContent(hb);
        
        
        
    }
    public void IDrawStudentsTab4(Tab tab,final String ID,final String fname,final String lname,final String Type,final Stage stage) throws SQLException
             {
                  ResultSet r = statement.executeQuery("select SID,FirstName,LastName from students where SID in(Select SID from students_courses where CID in(select CID from courses where IID="+"'"+ID+"'"+") and Grade ='U' )");
                  VBox vb = new VBox();
        
        final ListView<String> list1 = new ListView<>();
        ObservableList<String> items1 =FXCollections.observableArrayList ();

        while(r.next())
        {
            items1.add(r.getString(1)+"\t\t"+r.getString(2)+" "+r.getString(3)+"\n");
        }
        
        list1.setItems(items1);
        vb.getChildren().addAll(new Label("Students Regsiterd in your courses:"),list1);
        vb.setAlignment(Pos.CENTER);
      
        
        
        tab.setContent(vb);
             }
    
    public String getIDepartment(String IID)throws SQLException
    {
        ResultSet r = statement.executeQuery("Select DID from department where IID=("+"'"+IID+"'"+")");
        
        r.next();
        System.out.println(r);
        return r.getString(1);
    }
    public String getIName(String IID) throws SQLException
    {
        ResultSet name = statement.executeQuery("select FirstName,LastName from instructors where IID="+"'"+IID+"'");
        name.first();
        return "Dr."+name.getString(1)+" "+name.getString(2);
    }
     public boolean checkCoursesTimeConflict(String Day,String Periods,String ID)
     {
        try {
            ResultSet r = statement.executeQuery("select count(*) from courses where IID="+"'"+ID+"'"+"and Day="+"'"+Day+"'"+"and Periods="+"'"+Periods+"'");
            r.next();
            if(Integer.parseInt(r.getString(1))>=1)
            {
                return true;
            }
            else
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
     }
     
     public boolean checkCoursesTimeConflict(String ClassRoom,String Day,String Periods,String ID)
     {
         try {
            ResultSet r = statement.executeQuery("select count(*) from courses where IID="+"'"+ID+"'"+"and ClassRoom="+"'"+ClassRoom+"'"+"and Day="+"'"+Day+"'"+"and Periods="+"'"+Periods+"'");
            r.next();
            if(Integer.parseInt(r.getString(1))>=1)
            {
                return true;
            }
            else
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
     }
    public void TestPage()
    {
    Stage primaryStage = new Stage();
    primaryStage.setTitle("Tabs");
    Group root = new Group();
    Scene scene = new Scene(root, 400, 250, Color.WHITE);
    TabPane tabPane = new TabPane();
    BorderPane borderPane = new BorderPane();
    for (int i = 0; i < 5; i++) {
      Tab tab = new Tab();
      tab.setGraphic(buildImage("resources/graduation.png"));
      HBox hbox = new HBox();
      hbox.getChildren().add(new Label("Tab" + i));
      hbox.setAlignment(Pos.CENTER);
      tab.setContent(hbox);
      tab.setClosable(false);
      tabPane.getTabs().add(tab);
    }
    borderPane.prefHeightProperty().bind(scene.heightProperty());
    borderPane.prefWidthProperty().bind(scene.widthProperty());

    borderPane.setCenter(tabPane);
    root.getChildren().add(borderPane);
    primaryStage.setScene(scene);
    primaryStage.show();
   
    }
    
   
    
    
    
    public VBox GetinfoLabel(ResultSet info) throws SQLException
    { 
        VBox vb = new VBox();
        
        Label idL = new Label("ID: ");
        Label fnameL = new Label("First name: ");
        Label lnameL = new Label("Last name: ");
        Label termL = new Label("Term: ");
        Label departmentL = new Label("Department: ");
        Label genderL = new Label("Gender: ");
        Label dateofbirthL= new Label("DateofBirth: ");
        Label enrolledyearL= new Label("EnrolledYear: ");
        Label localaddressL= new Label("LocalAddress: ");
        Label mobileL= new Label("Mobile: ");
        Label EmailL= new Label("Email: ");
        Label usernameL= new Label("Username: ");
        Label noteL= new Label("Note: ");
        
        
        info.first();
   
        Label idLS = new Label(info.getString(1));
        Label fnameLS = new Label(info.getString(2));
        Label lnameLS = new Label(info.getString(3));
        Label termLS = new Label(info.getString(4));
        Label departmentLS = new Label(info.getString(5));
        Label genderLS = new Label(info.getString(6));
        Label dateofbirthLS= new Label(info.getString(7));
        Label enrolledyearLS= new Label(info.getString(8));
        Label localaddressLS= new Label(info.getString(9));
        Label mobileLS= new Label(info.getString(10));
        Label EmailLS= new Label(info.getString(11));
        Label usernameLS= new Label(info.getString(12));
        Label noteLS= new Label(info.getString(14));
        
        Label All = new Label(idL.getText()+idLS.getText()+"\n\n"+
                              fnameL.getText()+fnameLS.getText()+"\n\n"+
                              lnameL.getText()+lnameLS.getText()+"\n\n"+
                              termL.getText()+termLS.getText()+"\n\n"+
                              departmentL.getText()+departmentLS.getText()+"\n\n"+
                genderL.getText()+genderLS.getText()+"\n\n"+
                dateofbirthL.getText()+dateofbirthLS.getText()+"\n\n"+
                enrolledyearL.getText()+enrolledyearLS.getText()+"\n\n"+
                localaddressL.getText()+localaddressLS.getText()+"\n\n"+
                mobileL.getText()+mobileLS.getText()+"\n\n"+
                EmailL.getText()+EmailLS.getText()+"\n\n"+
                usernameL.getText()+usernameLS.getText()+"\n\n"+
                noteL.getText()+noteLS.getText()+"\n\n");
        All.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vb.setPadding(new Insets(40, 100, 0, 60));
        vb.getChildren().add(All);
        return vb;
    }
     public VBox IGetinfoLabel(ResultSet info) throws SQLException
    { 
        VBox vb = new VBox();
        
        Label idL = new Label("ID: ");
        Label fnameL = new Label("First name: ");
        Label lnameL = new Label("Last name: ");
        Label departmentL = new Label("Department: ");
        Label genderL = new Label("Gender: ");
        Label dateofbirthL= new Label("DateofBirth: ");
        Label localaddressL= new Label("LocalAddress: ");
        Label enrolledyearL= new Label("EnrolledYear: ");
        Label usernameL= new Label("Username: ");
        Label noteL= new Label("Note: ");
        Label EmailL= new Label("Email: ");
        Label mobileL= new Label("Mobile: ");
       
        
        
        info.first();
   
        Label idLS = new Label(info.getString(1));
        Label fnameLS = new Label(info.getString(2));
        Label lnameLS = new Label(info.getString(3));
        Label departmentLS = new Label(info.getString(4));
        Label genderLS = new Label(info.getString(5));
        Label dateofbirthLS= new Label(info.getString(6));
        Label localaddressLS= new Label(info.getString(7));
        Label enrolledyearLS= new Label(info.getString(8));
        Label usernameLS= new Label(info.getString(9));
        Label noteLS= new Label(info.getString(11));
        Label EmailLS= new Label(info.getString(12));
        Label mobileLS= new Label(info.getString(13));
        
        
       
        
        Label All = new Label(idL.getText()+idLS.getText()+"\n\n"+
                              fnameL.getText()+fnameLS.getText()+"\n\n"+
                              lnameL.getText()+lnameLS.getText()+"\n\n"+
                              departmentL.getText()+departmentLS.getText()+"\n\n"+
                genderL.getText()+genderLS.getText()+"\n\n"+
                dateofbirthL.getText()+dateofbirthLS.getText()+"\n\n"+
                enrolledyearL.getText()+enrolledyearLS.getText()+"\n\n"+
                localaddressL.getText()+localaddressLS.getText()+"\n\n"+
                mobileL.getText()+mobileLS.getText()+"\n\n"+
                EmailL.getText()+EmailLS.getText()+"\n\n"+
                usernameL.getText()+usernameLS.getText()+"\n\n"+
                noteL.getText()+noteLS.getText()+"\n\n");
        All.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vb.setPadding(new Insets(40, 100, 0, 60));
        vb.getChildren().add(All);
        return vb;
    }
    private static ImageView buildImage(String imgPath) {
            
            ImageView imageView = new ImageView(new Image(imgPath));
            //You can set width and height
            imageView.setFitHeight(35);
            imageView.setFitWidth(40);
            return imageView;
        }
    public void ShowMessageBox(String text)
    {
    Label L = new Label(text);
        
    
    Stage dialogStage = new Stage();
    dialogStage.initModality(Modality.WINDOW_MODAL);

    VBox vbox = new VBox(5);
    vbox.getChildren().add(L);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(30));

    dialogStage.setScene(new Scene(vbox));
    dialogStage.show();
        
    }
    
    
    
    
    
    
}


