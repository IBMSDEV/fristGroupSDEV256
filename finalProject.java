
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.io.IOException;

//TODO CHANGE THIS FROM * TO WHAT IS NEEDED BECAUSE IT IS BAD FOR IT TO BE *
import java.sql.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;

public class finalProject extends Application {
    static Connection connection = null;
    int userType = 0;
    TextField username = new TextField();
    TextField password = new TextField();
    // text fields for editing and adding data for the other scenes
    TextField partsUsed = new TextField();
    TextField partCost = new TextField();
    TextField totalCost = new TextField();
    TextField hoursLabor = new TextField();
    TextField ServiceDesc = new TextField();
    TextField techDealershipID = new TextField();
    TextField techName = new TextField();
    TextField dealerName = new TextField();
    TextField dealerShipPhone = new TextField();

    TextField dealerAddress = new TextField();
    TextField vin = new TextField();
    TextField serviceDate = new TextField();
    TextField owner = new TextField();
    TextField year = new TextField();
    TextField mileage = new TextField();
    TextField make = new TextField();
    TextField model = new TextField();

    int index = -1;
    // the collection of the information need
    static ObservableList<Dealership> dealerships = FXCollections.observableArrayList();
    static ObservableList<Dealership.ServiceTech> ServiceTechs = FXCollections.observableArrayList();
    static ObservableList<Car> Cars = FXCollections.observableArrayList();
    static ObservableList<ServiceOrder> serviceOrders = FXCollections.observableArrayList();
    String selectedDealerNames = "";

    public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {
        
        try {

            // calls to have the data be added into the collection from files
            fillData();
            System.out.print("SUP");
        } catch (Exception e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Cannot Connect To DataBase");

            // show the dialog
            a.show();
        }

        // the making a table
        TableView<ServiceOrder> serviceTable = new TableView<ServiceOrder>();
        // allowing the table to be able to be edited
        serviceTable.setEditable(true);

        // place holder for when no dealer is selected
        serviceTable.setPlaceholder(new Label("No dealer has been selected"));

        // Table Columns and sizes
        TableColumn<ServiceOrder, String> VINColumn = new TableColumn<>("VIN");
        VINColumn.setMinWidth(200);
        VINColumn.setCellValueFactory(new PropertyValueFactory<>("carVin"));

        TableColumn<ServiceOrder, String> ServiceDescColumn = new TableColumn<>("Service Description");
        ServiceDescColumn.setMinWidth(200);
        ServiceDescColumn.setCellValueFactory(new PropertyValueFactory<>("serviceDesc"));
        // For editing the data already in the table
        ServiceDescColumn.setCellFactory(TextFieldTableCell.<ServiceOrder>forTableColumn());
        ServiceDescColumn.setOnEditCommit((CellEditEvent<ServiceOrder, String> t) -> {
            ((ServiceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setServiceDesc(t.getNewValue());
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Service_Info SET service_description = ? where service_num = ?");
                preparedStatement.setString(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getServiceDesc());
                preparedStatement.setInt(2, (t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                preparedStatement.executeUpdate();
            } catch (SQLException e1) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("update ERROR");
                System.out.println(e1);
                // show the dialog
                a.show();
            }
        });

        TableColumn<ServiceOrder, String> partsUsedColumn = new TableColumn<>("Parts Used");
        partsUsedColumn.setMinWidth(200);
        partsUsedColumn.setCellValueFactory(new PropertyValueFactory<>("partsUsed"));
        // For editing the data already in the table
        partsUsedColumn.setCellFactory(TextFieldTableCell.<ServiceOrder>forTableColumn());
        partsUsedColumn.setOnEditCommit((CellEditEvent<ServiceOrder, String> t) -> {
            ((ServiceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setPartsUsed(t.getNewValue());
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update Service_Info SET parts_used = ? where service_num = ?");
                        preparedStatement.setString(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getPartsUsed());
                        preparedStatement.setInt(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e1) {
                        Alert a = new Alert(AlertType.ERROR);
                        a.setContentText("update ERROR");
                        // show the dialog
                        a.show();
                    }
        });

        TableColumn<ServiceOrder, String> TechIDColumn = new TableColumn<>("Tech ID");
        TechIDColumn.setMinWidth(100);
        TechIDColumn.setCellValueFactory(new PropertyValueFactory<>("techID"));
        // These aren't allowed to be changed
        TableColumn<ServiceOrder, String> DealershipIDColumn = new TableColumn<>("Dealership ID");
        DealershipIDColumn.setMinWidth(100);
        DealershipIDColumn.setCellValueFactory(new PropertyValueFactory<>("dealershipID"));

        TableColumn<ServiceOrder, String> costPartsColumn = new TableColumn<>("Cost of Parts");
        costPartsColumn.setMinWidth(100);
        costPartsColumn.setCellValueFactory(new PropertyValueFactory<>("tablePartsCost"));
        // For editing the data already in the table
        costPartsColumn.setCellFactory(TextFieldTableCell.<ServiceOrder>forTableColumn());
        costPartsColumn.setOnEditCommit((CellEditEvent<ServiceOrder, String> t) -> {
            ((ServiceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setPartsCost(Double.parseDouble(t.getNewValue()));
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update Service_Info SET cost_of_parts = ? where service_num = ?");
                        preparedStatement.setDouble(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getPartsCost());
                        preparedStatement.setInt(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e1) {
                        Alert a = new Alert(AlertType.ERROR);
                        a.setContentText("update ERROR");
                        // show the dialog
                        a.show();
                    }
        });
        TableColumn<ServiceOrder, String> LaborHoursColumn = new TableColumn<>("Hours of Labor");
        LaborHoursColumn.setMinWidth(100);
        LaborHoursColumn.setCellValueFactory(new PropertyValueFactory<>("tableLaborHours"));
        // For editing the data already in the table
        LaborHoursColumn.setCellFactory(TextFieldTableCell.<ServiceOrder>forTableColumn());
        LaborHoursColumn.setOnEditCommit((CellEditEvent<ServiceOrder, String> t) -> {
            ((ServiceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setLaborHours(Double.parseDouble(t.getNewValue()));
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update Service_Info SET labor_hours = ? where service_num = ?");
                        preparedStatement.setDouble(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getLaborHours());
                        preparedStatement.setInt(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e1) {
                        Alert a = new Alert(AlertType.ERROR);
                        a.setContentText("update ERROR");
                        // show the dialog
                        a.show();
                    }
        });
        TableColumn<ServiceOrder, String> totalCostColumn = new TableColumn<>("Total Cost");
        totalCostColumn.setMinWidth(100);
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("tableTotalCost"));

        // For editing the data already in the table
        totalCostColumn.setCellFactory(TextFieldTableCell.<ServiceOrder>forTableColumn());
        totalCostColumn.setOnEditCommit((CellEditEvent<ServiceOrder, String> t) -> {
            ((ServiceOrder) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setTotalCost(Double.parseDouble(t.getNewValue()));
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("update Service_Info SET cost_of_service = ? where service_num = ?");
                        preparedStatement.setDouble(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getTotalCost());
                        preparedStatement.setInt(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e1) {
                        Alert a = new Alert(AlertType.ERROR);
                        a.setContentText("update ERROR");
                        // show the dialog
                        a.show();
                    }
        });
        // end of Table Columns

        // adding table columns

        serviceTable.getColumns().add(VINColumn);
        serviceTable.getColumns().add(ServiceDescColumn);
        serviceTable.getColumns().add(partsUsedColumn);
        serviceTable.getColumns().add(TechIDColumn);
        serviceTable.getColumns().add(DealershipIDColumn);
        serviceTable.getColumns().add(costPartsColumn);
        serviceTable.getColumns().add(LaborHoursColumn);
        serviceTable.getColumns().add(totalCostColumn);

        BorderPane pane = new BorderPane();
        GridPane textFeildsPane = new GridPane();
        // adding padding
        textFeildsPane.setPadding(new Insets(15, 15, 15, 15));
        textFeildsPane.setHgap(5);
        textFeildsPane.setVgap(5);
        // the dealer combobox for selecting the dealer to look at the service orders of
        textFeildsPane.add(new Label("Dealers' service looking for service records: "), 0, 1);
        final ComboBox<String> dealerBox = new ComboBox<String>();

        for (int i = 0; i < dealerships.size(); i++)
            dealerBox.getItems().add(dealerships.get(i).dealerName);
        textFeildsPane.add(dealerBox, 1, 1);
        // buttons to add or edit the information
        Button addServiceOrder = new Button("Add Service Order");
        Button addDealerTech = new Button("Add new or edit Dealer and Tech");
        Button addCar = new Button("Add new or edit Car");
        Alert a = new Alert(AlertType.ERROR);
        // adding textFeilds to add the Service orders
        TextField CarVin = new TextField();
        TextField SOtechID = new TextField();
        TextField SOtechDealershipID = new TextField();
        TextField Date = new TextField();
        textFeildsPane.add(
                new Label("Feilds with * are optional. For the * feilds to be save all * need to be filled: "), 1, 3);
        textFeildsPane.add(new Label("Cars VIN That is getting service: "), 0, 4);
        textFeildsPane.add(CarVin, 1, 4);
        textFeildsPane.add(new Label("What is the cars service*: "), 2, 4);
        textFeildsPane.add(ServiceDesc, 3, 4);
        textFeildsPane.add(new Label("Parts were used for the cars service*: "), 0, 5);
        textFeildsPane.add(partsUsed, 1, 5);
        textFeildsPane.add(new Label("What was the price of the parts used*: "), 2, 5);
        textFeildsPane.add(partCost, 3, 5);
        textFeildsPane.add(new Label("What was hours of labor for the service*: "), 0, 6);
        textFeildsPane.add(hoursLabor, 1, 6);
        textFeildsPane.add(new Label("What was the total cost of the service*: "), 2, 6);
        textFeildsPane.add(totalCost, 3, 6);
        textFeildsPane.add(new Label("What is the ID of the tech that was working on the car: "), 0, 7);
        textFeildsPane.add(SOtechID, 1, 7);
        textFeildsPane.add(new Label("What is the ID of the Dealership that the car got its service: "), 2, 7);
        textFeildsPane.add(SOtechDealershipID, 3, 7);
        // adding a serivce order to the the table and list
        addServiceOrder.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (CarVin.getText().length() != 0 && SOtechDealershipID.getText().length() != 0
                        && SOtechID.getText().length() != 0) {
                    try {
                        if (totalCost.getText().length() != 0 && partCost.getText().length() != 0
                                && hoursLabor.getText().length() != 0 && ServiceDesc.getText().length() != 0
                                && partsUsed.getText().length() != 0) {

                            serviceOrders.add(new ServiceOrder(CarVin.getText(), ServiceDesc.getText(), Date.getText(),
                                    partsUsed.getText(), Integer.parseInt(SOtechID.getText()),
                                    Integer.parseInt(SOtechDealershipID.getText()),
                                    Double.parseDouble(partCost.getText()), Double.parseDouble(totalCost.getText()),
                                    Double.parseDouble(hoursLabor.getText())));

                                    PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO Service_Info (car_VIN, dealer_ID, tech_ID, service_date, service_description, parts_used, cost_of_parts, cost_of_service, labor_hours)" 
                                    +"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                                    preparedStatement.setString(1, CarVin.getText());
                                    preparedStatement.setInt(2, Integer.parseInt(SOtechDealershipID.getText()));
                                    preparedStatement.setInt(3, Integer.parseInt(SOtechID.getText()));
                                    preparedStatement.setString(4, Date.getText());
                                    preparedStatement.setString(5, ServiceDesc.getText());
                                    preparedStatement.setString(6, partsUsed.getText());
                                    preparedStatement.setDouble(7, Double.parseDouble(partCost.getText()));
                                    preparedStatement.setDouble(8, Double.parseDouble(totalCost.getText()));
                                    preparedStatement.setDouble(9, Double.parseDouble(hoursLabor.getText()));

                        } else
                            serviceOrders.add(new ServiceOrder(CarVin.getText(), Integer.parseInt(SOtechID.getText()),
                                    Integer.parseInt(SOtechDealershipID.getText()), Date.getText()));
                                PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO Service_Info (car_VIN, dealer_ID, tech_ID, service_date)" 
                            +"VALUES (?, ?, ?, ?)");
                            preparedStatement.setString(1, CarVin.getText());
                            preparedStatement.setInt(2, Integer.parseInt(SOtechDealershipID.getText()));
                            preparedStatement.setInt(3, Integer.parseInt(SOtechID.getText()));
                            preparedStatement.setString(4, Date.getText());

                    } catch (java.lang.NumberFormatException e2) {
                        a.setContentText("There is an letter or word in the id spot");
                        // show the dialog
                        a.show();
                    } catch (SQLException e1) {
						a.setContentText("ADDING ERROR");
                        // show the dialog
                        a.show();
					}
                    try {
                        serviceTable.setPlaceholder(new Label("No dealer has been selected"));
                        ObservableList<ServiceOrder> recorders = FXCollections.observableArrayList();
                        if (selectedDealerNames != "") {
                            for (int i = 0; i < dealerships.size(); i++)
                                if (dealerships.get(i).dealerName.contains(selectedDealerNames)) {
                                    index = i;
                                    break;
                                }
                        }
                        ArrayList<Integer> indexOfOrders = new ArrayList<Integer>();
                        if (index != -1) {
                            // removing the data from the data list that isn't for the dealership
                            for (int i = 0; i < serviceOrders.size(); i++)
                                if (serviceOrders.get(i).dealershipID == index + 1) {
                                    indexOfOrders.add(i);
                                    recorders.add(serviceOrders.get(i));
                                } else
                                    serviceTable.setPlaceholder(new Label("Dealer has no service orders on file"));

                            // show those orders
                            serviceTable.setItems(recorders);
                        }
                    } catch (Exception e1) {

                    }
                    // clearing all the old text after adding it to the list
                    CarVin.clear();
                    SOtechDealershipID.clear();
                    SOtechID.clear();
                    ServiceDesc.clear();
                    partsUsed.clear();
                    totalCost.clear();
                    partCost.clear();
                    hoursLabor.clear();

                }
            }

        });

        textFeildsPane.add(addServiceOrder, 0, 3);
        textFeildsPane.add(addDealerTech, 1, 0);
        textFeildsPane.add(addCar, 2, 0);
        // setting location
        pane.setTop(textFeildsPane);

        // if the dealer combo box is changed change the info in the table to show their
        // service orders

        dealerBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                serviceTable.setPlaceholder(new Label("No dealer has been selected"));
                ObservableList<ServiceOrder> recorders = FXCollections.observableArrayList();
                selectedDealerNames = t1;
                if (selectedDealerNames != "") {
                    for (int i = 0; i < dealerships.size(); i++)
                        if (dealerships.get(i).dealerName.contains(selectedDealerNames)) {
                            index = i;
                            break;
                        }
                }
                ArrayList<Integer> indexOfOrders = new ArrayList<Integer>();
                if (index != -1) {
                    // removing the data from the data list that isn't for the dealership
                    for (int i = 0; i < serviceOrders.size(); i++)
                        if (serviceOrders.get(i).dealershipID == index + 1) {
                            indexOfOrders.add(i);
                            recorders.add(serviceOrders.get(i));
                        } else
                            serviceTable.setPlaceholder(new Label("Dealer has no service orders on file"));
                    // show those orders
                    serviceTable.setItems(recorders);
                }
            }
        });

        pane.setCenter(serviceTable);
        Scene mainScene = new Scene(pane, 1200, 500);

        // load the add car scene and the needed items for that
        addCar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                GridPane pane2 = new GridPane();
                BorderPane scenePane = new BorderPane();

                TableView<Car> carTable = new TableView<Car>();
                carTable.setEditable(true);
                // Table Columes and sizes
                TableColumn<Car, String> VINColumn = new TableColumn<>("VIN");
                VINColumn.setMinWidth(200);
                VINColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));

                TableColumn<Car, String> makeColumn = new TableColumn<>("Make");
                makeColumn.setMinWidth(200);
                makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));

                TableColumn<Car, String> modelColumn = new TableColumn<>("Model");
                modelColumn.setMinWidth(200);
                modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

                TableColumn<Car, String> yearColumn = new TableColumn<>("Year");
                yearColumn.setMinWidth(200);
                yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

                TableColumn<Car, String> mileageColumn = new TableColumn<>("Mileage");
                mileageColumn.setMinWidth(200);
                mileageColumn.setCellValueFactory(new PropertyValueFactory<>("tableMileage"));
                mileageColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                mileageColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setMileage(Integer.parseInt(t.getNewValue()));
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Car_Info SET car_mileage = ? where car_VIN = ?");
                                preparedStatement.setInt(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getMileage());
                                preparedStatement.setString(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getVin()));
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });

                TableColumn<Car, String> ownerColumn = new TableColumn<>("Owner");
                ownerColumn.setMinWidth(400);
                ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
                ownerColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                ownerColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow())).setOwner(t.getNewValue());
                    //  try {
                    //             PreparedStatement preparedStatement = connection.prepareStatement("update Car_Info car_mileage = ? where car_VIN = ?");
                    //             preparedStatement.setInt(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getMileage());
                    //             preparedStatement.setString(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getVin()));
                    //             preparedStatement.executeUpdate();
                    //         } catch (SQLException e1) {
                    //             Alert a = new Alert(AlertType.ERROR);
                    //             a.setContentText("update ERROR");
                    //             // show the dialog
                    //             a.show();
                    //         }
                });
                TableColumn<Car, String> serviceDateColumn = new TableColumn<>("Service Date");
                serviceDateColumn.setMinWidth(400);
                serviceDateColumn.setCellValueFactory(new PropertyValueFactory<>("serviceDate"));
                serviceDateColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                serviceDateColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setServiceDate(t.getNewValue());
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Car_Info SET date_of_last_service = ? where car_VIN = ?");
                                preparedStatement.setString(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getServiceDate());
                                preparedStatement.setString(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getVin()));
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });

                // end of Table Columns
                carTable.getColumns().add(VINColumn);
                carTable.getColumns().add(makeColumn);
                carTable.getColumns().add(modelColumn);
                carTable.getColumns().add(yearColumn);
                carTable.getColumns().add(mileageColumn);
                carTable.getColumns().add(ownerColumn);
                // carTable.getColumns().add(serviceDateColumn);

                pane2.setPadding(new Insets(15, 15, 15, 15));
                pane2.setHgap(5);
                pane2.setVgap(5);
                pane2.add(new Label("Vin: "), 0, 0);
                pane2.add(vin, 1, 0);
                pane2.add(new Label("Year: "), 2, 0);
                pane2.add(year, 3, 0);
                pane2.add(new Label("Owner: "), 2, 1);
                pane2.add(owner, 3, 1);
                pane2.add(new Label("Service Date: "), 2, 2);
                pane2.add(serviceDate, 3, 2);
                pane2.add(new Label("Mileage: "), 2, 3);
                pane2.add(mileage, 3, 3);
                pane2.add(new Label("Make: "), 0, 1);
                pane2.add(make, 1, 1);
                pane2.add(new Label("Model: "), 0, 2);
                pane2.add(model, 1, 2);
                Button backButton = new Button("Back");
                Button addButton = new Button("Add");
                pane2.add(backButton, 4, 3);
                pane2.add(addButton, 0, 3);

                carTable.setItems(Cars);
                System.out.println(Cars.toString());

                scenePane.setTop(pane2);
                scenePane.setBottom(carTable);
                // goes back to the other scene
                backButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        primaryStage.setScene(mainScene);
                        primaryStage.setTitle("Service Order Shower");
                    }
                });
                // adds the information to the Car collection and saves the data
                addButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        if (vin.getText().length() != 0 && make.getText().length() != 0 && model.getText().length() != 0
                                && year.getText().length() != 0 && owner.getText().length() != 0
                                && mileage.getText().length() != 0) {
                            if (Cars.get(0).CheckVin(vin.getText())) {
                                Cars.add(new Car(vin.getText(), make.getText(), model.getText(),
                                        Integer.parseInt(year.getText()), Integer.parseInt(mileage.getText()),
                                        serviceDate.getText(), owner.getText()));
                                try {
                                    PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO Car_Info (car_VIN, car_make, car_model, car_year, car_mileage, date_of_last_service, owner_ID)" 
                                    +"VALUES (?, ?, ?, ?, ?, ?, ?)");
                                    preparedStatement.setString(1, vin.getText());
                                    preparedStatement.setString(2, make.getText() );
                                    preparedStatement.setString(3, model.getText());
                                    preparedStatement.setInt(4, Integer.parseInt(year.getText()));
                                    preparedStatement.setInt(5, Integer.parseInt(mileage.getText()));
                                    preparedStatement.setString(6, serviceDate.getText());
                                    //preparedStatement.setInt(7, owner.getText());
                                    preparedStatement.executeUpdate();
                                } catch (Exception e1) {
                                }
                                // clearing all the old text after adding it to the list
                                vin.clear();
                                make.clear();
                                model.clear();
                                mileage.clear();
                                owner.clear();
                                year.clear();
                            } else {
                                a.setContentText("The Vin is not vaild");

                                // show the dialog
                                a.show();
                            }
                        } else {

                            a.setContentText("The is an empty field");

                            // show the dialog
                            a.show();
                        }
                    }
                });

                Scene carScene = new Scene(scenePane, 1500, 350);

                primaryStage.setTitle("Car Adder");
                primaryStage.setScene(carScene);
            }
        });

        // load the add Dealer and Tech scene and the needed items for that
        addDealerTech.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent f) {
                TableView<Dealership> dealerTable = new TableView<Dealership>();
                TableView<Dealership.ServiceTech> techTable = new TableView<Dealership.ServiceTech>();

                dealerTable.setEditable(true);
                techTable.setEditable(true);

                // Table Columns and sizes for the dealer Table
                TableColumn<Dealership, String> dealerIDColumn = new TableColumn<>("Dealer ID");
                dealerIDColumn.setMinWidth(10);
                dealerIDColumn.setCellValueFactory(new PropertyValueFactory<>("dealershipID"));

                TableColumn<Dealership, String> dealerNameColumn = new TableColumn<>("Dealer Name");
                dealerNameColumn.setMinWidth(200);
                dealerNameColumn.setCellValueFactory(new PropertyValueFactory<>("dealerName"));
                dealerNameColumn.setCellFactory(TextFieldTableCell.<Dealership>forTableColumn());
                dealerNameColumn.setOnEditCommit((CellEditEvent<Dealership, String> t) -> {
                    ((Dealership) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setDealerName(t.getNewValue());
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Dealership SET dealer_name = ? where dealer_ID = ?");
                                preparedStatement.setString(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealerName());
                                preparedStatement.setInt(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });

                TableColumn<Dealership, String> dealerAddressColumn = new TableColumn<>("Dealer Address");
                dealerAddressColumn.setMinWidth(200);
                dealerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("dealerAddress"));
                dealerAddressColumn.setCellFactory(TextFieldTableCell.<Dealership>forTableColumn());
                dealerAddressColumn.setOnEditCommit((CellEditEvent<Dealership, String> t) -> {
                    ((Dealership) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setDealerAddress(t.getNewValue());
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Dealership SET dealer_address = ? where dealer_ID = ?");
                                preparedStatement.setString(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealerAddress());
                                preparedStatement.setInt(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });
                TableColumn<Dealership, String> dealerPhoneColumn = new TableColumn<>("Dealer Phone Number");
                dealerPhoneColumn.setMinWidth(200);
                dealerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("dealerPhoneNumber"));
                dealerPhoneColumn.setCellFactory(TextFieldTableCell.<Dealership>forTableColumn());
                dealerPhoneColumn.setOnEditCommit((CellEditEvent<Dealership, String> t) -> {
                    ((Dealership) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setDealerPhoneNumber(t.getNewValue());
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Dealership SET dealer_phoneNum = ? where dealer_ID = ?");
                                preparedStatement.setString(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealerPhoneNumber());
                                preparedStatement.setInt(2,(t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID()));
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });

                // Table Columns and sizes for the dealer Table
                TableColumn<Dealership.ServiceTech, String> dealershipIDColumn = new TableColumn<>("Dealer ID");
                dealershipIDColumn.setMinWidth(100);
                dealershipIDColumn.setCellValueFactory(new PropertyValueFactory<>("tableDealershipID"));
                dealershipIDColumn.setCellFactory(TextFieldTableCell.<Dealership.ServiceTech>forTableColumn());
                dealershipIDColumn.setOnEditCommit((CellEditEvent<Dealership.ServiceTech, String> t) -> {
                    ((Dealership.ServiceTech) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setDealershipID(Integer.parseInt(t.getNewValue()));
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Service_Techs SET dealer_ID = ? where tech_Num = ?");
                                preparedStatement.setInt(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getDealershipID());
                                preparedStatement.setInt(2, t.getTableView().getItems().get(t.getTablePosition().getRow()).getDBtechNum());
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });

                TableColumn<Dealership.ServiceTech, String> techIDColumn = new TableColumn<>("Tech ID");
                techIDColumn.setMinWidth(100);
                techIDColumn.setCellValueFactory(new PropertyValueFactory<>("tableTechId"));
                techIDColumn.setCellFactory(TextFieldTableCell.<Dealership.ServiceTech>forTableColumn());
                techIDColumn.setOnEditCommit((CellEditEvent<Dealership.ServiceTech, String> t) -> {
                    ((Dealership.ServiceTech) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setTechId(Integer.parseInt(t.getNewValue()));
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Service_Techs SET tech_ID = ? where tech_Num = ?");
                                preparedStatement.setInt(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getTechId());
                                preparedStatement.setInt(2, t.getTableView().getItems().get(t.getTablePosition().getRow()).getDBtechNum());
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });

                TableColumn<Dealership.ServiceTech, String> techNameColumn = new TableColumn<>("Tech Name");
                techNameColumn.setMinWidth(200);
                techNameColumn.setCellValueFactory(new PropertyValueFactory<>("techName"));
                techNameColumn.setCellFactory(TextFieldTableCell.<Dealership.ServiceTech>forTableColumn());
                techNameColumn.setOnEditCommit((CellEditEvent<Dealership.ServiceTech, String> t) -> {
                    ((Dealership.ServiceTech) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setTechName(t.getNewValue());
                            try {
                                PreparedStatement preparedStatement = connection.prepareStatement("update Service_Techs SET tech_Name = ? where tech_Num = ?");
                                preparedStatement.setString(1, t.getTableView().getItems().get(t.getTablePosition().getRow()).getTechName());
                                preparedStatement.setInt(2, t.getTableView().getItems().get(t.getTablePosition().getRow()).getDBtechNum());
                                preparedStatement.executeUpdate();
                            } catch (SQLException e1) {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("update ERROR");
                                // show the dialog
                                a.show();
                            }
                });

                // end of Table Columns
                // adding columns and data to the dealer table
                dealerTable.getColumns().add(dealerIDColumn);
                dealerTable.getColumns().add(dealerNameColumn);
                dealerTable.getColumns().add(dealerAddressColumn);
                dealerTable.getColumns().add(dealerPhoneColumn);
                dealerTable.setItems(dealerships);
                // adding columns and data to the tech table
                techTable.getColumns().add(dealershipIDColumn);
                techTable.getColumns().add(techIDColumn);
                techTable.getColumns().add(techNameColumn);
                techTable.setItems(ServiceTechs);

                GridPane pane3 = new GridPane();
                GridPane pane4 = new GridPane();
                BorderPane scenePane = new BorderPane();
                pane3.setPadding(new Insets(15, 15, 15, 15));
                pane3.setHgap(5);
                pane3.setVgap(5);
                pane3.add(new Label("Dealer Name: "), 0, 0);
                pane3.add(dealerName, 1, 0);
                pane3.add(new Label("Dealer Address: "), 0, 1);
                pane3.add(dealerAddress, 1, 1);
                pane3.add(new Label("Dealer Phone Number: "), 0, 2);
                pane3.add(dealerShipPhone, 1, 2);

                pane4.add(dealerTable, 0, 4);

                pane3.add(new Label("Dealer Id: "), 3, 1);
                pane3.add(techDealershipID, 4, 1);
                pane3.add(new Label("Tech Name: "), 3, 0);
                pane3.add(techName, 4, 0);

                pane4.add(techTable, 3, 4);

                Button backButton = new Button("Back");
                Button addDealerButton = new Button("Add New Dealer");
                Button addTechButton = new Button("Add New Tech");
                pane3.add(backButton, 6, 3);
                pane3.add(addTechButton, 4, 3);
                pane3.add(addDealerButton, 0, 3);

                // adds the information to the Tech collection and saves the data
                addTechButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {
                            for (int i = 0; i < dealerships.size(); i++)
                                if (techDealershipID.getText().length() != 0 && techName.getText().length() != 0
                                        && dealerships.get(i)
                                                .getDealershipID() == (Integer.parseInt(techDealershipID.getText()))) {

                                    try {
                                        ServiceTechs.add(dealerships
                                                .get(Integer.parseInt(techDealershipID.getText()) - 1).new ServiceTech(
                                                        Integer.parseInt(techDealershipID.getText()),
                                                        techName.getText()));
                                        PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO Service_Techs (tech_ID, dealer_ID, tech_Name)" 
                                                        +"VALUES (?, ?, ?)");
                                        preparedStatement.setInt(1, ServiceTechs.get(ServiceTechs.size()-1).getTechId());
                                        preparedStatement.setInt(2, Integer.parseInt(techDealershipID.getText()));
                                        preparedStatement.setString(3, techName.getText());
                                        preparedStatement.executeUpdate();
                            
                                        techDealershipID.clear();
                                        techName.clear();
                                        break;

                                    } catch (Exception f) {

                                        a.setContentText("There is an letter or word in the id spot");
                                        System.out.println(f);
                                        // show the dialog
                                        a.show();
                                    }

                                } else if (i == dealerships.size() - 1) {
                                    a.setContentText("There is an empty field");

                                    // show the dialog
                                    a.show();

                                }
                        } catch (Exception f) {

                            a.setContentText("There is an letter or word in the id spot");
                            System.out.println(f);
                            // show the dialog
                            a.show();
                        }
                    }
                });
                // adds the information to the dealer collection and saves the data
                addDealerButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {
                            if (dealerName.getText() != " " && dealerAddress.getText() != " ") {

                                try {
                                    dealerships.add(new Dealership(dealerName.getText(), dealerAddress.getText(),
                                            dealerShipPhone.getText()));
                                    
                                    PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO DealerShip (dealer_name, dealer_address, dealer_phoneNum)" 
                                    +"VALUES (?, ?, ?)");
                                    
                                    preparedStatement.setString(1, dealerName.getText());
                                    preparedStatement.setString(2, dealerAddress.getText());
                                    preparedStatement.setString(3, dealerShipPhone.getText());
                                    preparedStatement.executeUpdate();
                                    dealerName.clear();
                                    dealerAddress.clear();
                                    dealerShipPhone.clear();
                                } catch (Exception f) {
                                    a.setContentText("There is an letter or word in the id spot");
                                    System.out.println(f);
                                    // show the dialog
                                    a.show();
                                }
                            } else {
                                Alert a = new Alert(AlertType.ERROR);
                                a.setContentText("The is an empty field");

                                // show the dialog
                                a.show();
                            }
                        } catch (Exception f) {

                            a.setContentText("There is an letter or word in the id spot");
                            // show the dialog
                            a.show();
                        }
                    }

                });
                // goes back to the other scene
                backButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        dealerBox.getItems().clear();
                        for (int i = 0; i < dealerships.size(); i++)
                            dealerBox.getItems().add(dealerships.get(i).dealerName);
                        primaryStage.setScene(mainScene);
                        primaryStage.setTitle("Service Order Shower");
                    }
                });

                scenePane.setTop(pane3);
                scenePane.setBottom(pane4);
                Scene dealersScene = new Scene(scenePane, 800, 500);

                primaryStage.setTitle("Dealer and tech Adder");
                primaryStage.setScene(dealersScene);
            }
        });

        // naming the scene
        primaryStage.setTitle("Service Order Shower");
        // setting the scene
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // used to remove used data from a string for the txt file
    static String removeOldDataFromString(String oldString) {
        oldString = oldString.substring(oldString.indexOf('^') + 1);
        return oldString;
    }

    // takes the data from the txt file adds it to its collection
    static void fillData() throws IOException {

        try {
            // Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:CarServiceDatabase.db");
            // connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            Statement stmt = null;

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from Car_Info;");

            // adding Cars to the Software store
            while (rs.next()) {

                String model = rs.getString("car_model");
                String make = rs.getString("car_make");
                String VIN = rs.getString("car_VIN");
                int year = rs.getInt("car_year");
                int mileage = rs.getInt("car_mileage");
                String serviceDate = null;

                String owner = connection.createStatement()
                        .executeQuery("select owner_firstName from Car_Owner where owner_ID = " + rs.getInt("owner_ID"))
                        .getString("owner_firstName");
                Cars.add(new Car(VIN, make, model, year, mileage, serviceDate, owner));

            }

            rs = stmt.executeQuery("SELECT * FROM Dealership;");

            // adding Dealer to the Software store
            while (rs.next()) {
                String dealer_Name = rs.getString("dealer_name");
                String dealer_address = rs.getString("dealer_address");
                String dealer_phoneNum = rs.getString("dealer_phoneNum");
                dealerships.add(new Dealership(dealer_Name, dealer_address, dealer_phoneNum));
            }

            rs = stmt.executeQuery("SELECT * FROM Service_Techs;");

            // adding TECHS to the Software store
            while (rs.next()) {
                int dealer_ID = rs.getInt("dealer_ID");
                String techName = rs.getString("tech_Name");

                ServiceTechs.add(dealerships.get(dealer_ID - 1).new ServiceTech(dealer_ID, techName));
            }

            // adding Service_Info to the Software store
            rs = stmt.executeQuery("SELECT * FROM Service_Info;");

            while (rs.next()) {
                String serviceDate = null;
                int techID = rs.getInt("tech_ID");
                int dealershipID = rs.getInt("dealer_ID");
                String carVIN = rs.getString("car_VIN");

                String partsUsed = rs.getString("parts_used");
                String serviceDesc = rs.getString("service_description");
                double partsCost = rs.getDouble("cost_of_parts");
                double totalCost = rs.getDouble("cost_of_service");
                double laborHours = rs.getDouble("labor_hours");

                serviceOrders.add(new ServiceOrder(carVIN, serviceDesc, serviceDate, partsUsed, techID, dealershipID,
                        partsCost, totalCost, laborHours));
                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            System.out.println("PLEASE HELP ME: " + e);
        }

    }



    private static void testingPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String  originalPassword = "password";
        String generatedSecuredPasswordHash = hashPassword(originalPassword);
        System.out.println(generatedSecuredPasswordHash);
        String[] parts = generatedSecuredPasswordHash.split(":");
        boolean matched = checkingPassword(parts[1], parts[0], "password");
        System.out.println(matched);
         
        matched = checkingPassword(parts[1], parts[0], "password1");
        System.out.println(matched);

    }

//checks if the password entered eqauls the old password
    private static Boolean checkingPassword(String passwordHash, String Salt, String tryingPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //stores the old salt an hash
        byte[] salt = fromHex(Salt);
        byte[] hash = fromHex(passwordHash);
        //makes a new has for the trying password with the old salt
        PBEKeySpec spec = new PBEKeySpec(tryingPassword.toCharArray(), salt, 1000, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
        //checking for differences
        int differece = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            differece |= hash[i] ^ testHash[i];
        }
        //returns if they are different
        return differece == 0;
    }

    //hashes the password and returns the hash and the salt that was used to hash it with
    private static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        char[] passwordChar = password.toCharArray();
        byte[] salt = getSalt();
        PBEKeySpec spec = new PBEKeySpec(passwordChar, salt, 1000, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return toHex(salt) + ":" + toHex(hash);
    }

    //gets a random salt for hashing a password
    private static byte[] getSalt() throws NoSuchAlgorithmException {

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    // converts from a bytes to a hex
    private static String toHex(byte[] array) throws NoSuchAlgorithmException {

        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    // converts from a hex to a byte
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static void main(String[] args) {
        // starting the GUI
        Application.launch(args);

    }
}
