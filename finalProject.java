
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
import java.util.Scanner;

import java.io.File;
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
    // text fields for editing and adding data for the other scenes
    TextField partsUsed = new TextField();
    TextField partCost = new TextField();
    TextField totalCost = new TextField();
    TextField hoursLabor = new TextField();
    TextField ServiceDesc = new TextField();
    TextField techDealershipID = new TextField();
    TextField techName = new TextField();
    TextField dealerName = new TextField();
    TextField dealerAddress = new TextField();
    TextField vin = new TextField();
    TextField options = new TextField();
    TextField engineSize = new TextField();
    TextField transmissionType = new TextField();
    TextField make = new TextField();
    TextField model = new TextField();

    int index = -1;
    // the collection of the information need
    static ObservableList<Dealership> dealerships = FXCollections.observableArrayList();
    static ObservableList<Dealership.ServiceTech> ServiceTechs = FXCollections.observableArrayList();
    static ObservableList<Car> Cars = FXCollections.observableArrayList();
    static ObservableList<ServiceOrder> serviceOrders = FXCollections.observableArrayList();
    String selectedDealerNames = "";

    public void start(Stage primaryStage) throws IOException {
        // calls to have the data be added into the collection from files
        fillData();
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
                saveData();
            } catch (IOException e1) {
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
                saveData();
            } catch (IOException e1) {
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
                saveData();
            } catch (IOException e1) {
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
                saveData();
            } catch (IOException e1) {
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
                saveData();
            } catch (IOException e1) {
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

                            serviceOrders.add(new ServiceOrder(CarVin.getText(), ServiceDesc.getText(),
                                    partsUsed.getText(), Integer.parseInt(SOtechID.getText()),
                                    Integer.parseInt(SOtechDealershipID.getText()),
                                    Double.parseDouble(partCost.getText()), Double.parseDouble(totalCost.getText()),
                                    Double.parseDouble(hoursLabor.getText())));
                        } else
                            serviceOrders.add(new ServiceOrder(CarVin.getText(), Integer.parseInt(SOtechID.getText()),
                                    Integer.parseInt(SOtechDealershipID.getText())));

                    } catch (java.lang.NumberFormatException e2) {
                        a.setContentText("There is an letter or word in the id spot");
                        // show the dialog
                        a.show();
                    }
                    try {

                        saveData();
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
                    } catch (IOException e1) {

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
                makeColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                makeColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMake(t.getNewValue());
                    try {
                        saveData();
                    } catch (IOException e1) {
                    }
                });

                TableColumn<Car, String> modelColumn = new TableColumn<>("Model");
                modelColumn.setMinWidth(200);
                modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
                modelColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                modelColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow())).setModel(t.getNewValue());
                    try {
                        saveData();
                    } catch (IOException e1) {
                    }
                });

                TableColumn<Car, String> engineSizeColumn = new TableColumn<>("Engine Size");
                engineSizeColumn.setMinWidth(200);
                engineSizeColumn.setCellValueFactory(new PropertyValueFactory<>("engineSize"));
                engineSizeColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                engineSizeColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setEngineSize(t.getNewValue());
                    try {
                        saveData();
                    } catch (IOException e1) {
                    }
                });

                TableColumn<Car, String> transmissionTypeColumn = new TableColumn<>("Transmission Type");
                transmissionTypeColumn.setMinWidth(200);
                transmissionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transmissionType"));
                transmissionTypeColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                transmissionTypeColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                            .setTransmissionType(t.getNewValue());
                    try {
                        saveData();
                    } catch (IOException e1) {
                    }
                });

                TableColumn<Car, String> optionsColumn = new TableColumn<>("Options");
                optionsColumn.setMinWidth(400);
                optionsColumn.setCellValueFactory(new PropertyValueFactory<>("options"));
                optionsColumn.setCellFactory(TextFieldTableCell.<Car>forTableColumn());
                optionsColumn.setOnEditCommit((CellEditEvent<Car, String> t) -> {
                    ((Car) t.getTableView().getItems().get(t.getTablePosition().getRow())).setOptions(t.getNewValue());
                    try {
                        saveData();
                    } catch (IOException e1) {
                    }
                });

                // end of Table Columns
                carTable.getColumns().add(VINColumn);
                carTable.getColumns().add(makeColumn);
                carTable.getColumns().add(modelColumn);
                carTable.getColumns().add(engineSizeColumn);
                carTable.getColumns().add(transmissionTypeColumn);
                carTable.getColumns().add(optionsColumn);

                pane2.setPadding(new Insets(15, 15, 15, 15));
                pane2.setHgap(5);
                pane2.setVgap(5);
                pane2.add(new Label("Vin: "), 0, 0);
                pane2.add(vin, 1, 0);
                pane2.add(new Label("Options: "), 2, 0);
                pane2.add(options, 3, 0);
                pane2.add(new Label("Engine Size: "), 2, 1);
                pane2.add(engineSize, 3, 1);
                pane2.add(new Label("Transmission Type: "), 2, 2);
                pane2.add(transmissionType, 3, 2);
                pane2.add(new Label("Make: "), 0, 1);
                pane2.add(make, 1, 1);
                pane2.add(new Label("Model: "), 0, 2);
                pane2.add(model, 1, 2);
                Button backButton = new Button("Back");
                Button addButton = new Button("Add");
                pane2.add(backButton, 4, 3);
                pane2.add(addButton, 0, 3);

                carTable.setItems(Cars);

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
                                && engineSize.getText().length() != 0 && transmissionType.getText().length() != 0
                                && options.getText().length() != 0) {
                            if (Cars.get(0).CheckVin(vin.getText())) {
                                Cars.add(new Car(vin.getText(), make.getText(), model.getText(), engineSize.getText(),
                                        transmissionType.getText(), options.getText()));
                                try {
                                    saveData();
                                } catch (IOException e1) {
                                }
                                // clearing all the old text after adding it to the list
                                vin.clear();
                                make.clear();
                                model.clear();
                                engineSize.clear();
                                transmissionType.clear();
                                options.clear();
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
                        saveData();
                    } catch (IOException e1) {
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
                        saveData();
                    } catch (IOException e1) {
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
                        saveData();
                    } catch (IOException e1) {
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
                        saveData();
                    } catch (IOException e1) {
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
                        saveData();
                    } catch (IOException e1) {
                    }
                });

                // end of Table Columns
                // adding columns and data to the dealer table
                dealerTable.getColumns().add(dealerIDColumn);
                dealerTable.getColumns().add(dealerNameColumn);
                dealerTable.getColumns().add(dealerAddressColumn);
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
                                        saveData();
                                        // clearing the textFeild
                                        techDealershipID.clear();
                                        techName.clear();
                                        break;

                                    } catch (Exception f) {

                                        a.setContentText("There is an letter or word in the id spot");
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
                                    dealerships.add(new Dealership(dealerName.getText(), dealerAddress.getText()));
                                    dealerName.clear();
                                    dealerAddress.clear();
                                    saveData();
                                } catch (Exception f) {
                                    a.setContentText("There is an letter or word in the id spot");
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
        File dealerFile = new File("dealerData.txt");
        File techFile = new File("ServiceTechData.txt");
        File carFile = new File("carData.txt");
        File ServiceOrder = new File("serviceOrderData.txt");
        if (dealerFile.exists()) {
            try (Scanner reader = new Scanner(dealerFile);) {
                while (reader.hasNext()) {
                    String dealerString = reader.nextLine();
                    // int id = Integer.parseInt(dealerString.substring(0,
                    // dealerString.indexOf('^')));
                    dealerString = removeOldDataFromString(dealerString);
                    String dealerName = (dealerString.substring(0, dealerString.indexOf('^')));
                    dealerString = removeOldDataFromString(dealerString);
                    String dealerAddress = (dealerString.substring(0, dealerString.indexOf('^')));
                    dealerships.add(new Dealership(dealerName, dealerAddress));

                }
                reader.close();
            }
            if (techFile.exists()) {
                try (Scanner reader = new Scanner(techFile);) {
                    while (reader.hasNext()) {
                        String techString = reader.nextLine();
                        // int techId = Integer.parseInt(techString.substring(0,
                        // techString.indexOf('^')));
                        techString = removeOldDataFromString(techString);
                        String techName = (techString.substring(0, techString.indexOf('^')));
                        techString = removeOldDataFromString(techString);
                        int dealerId = Integer.parseInt(techString.substring(0, techString.indexOf('^')));

                        ServiceTechs.add(dealerships.get(dealerId - 1).new ServiceTech(dealerId, techName));
                    }
                    reader.close();
                }
            }
        }
        if (carFile.exists()) {
            try (Scanner reader = new Scanner(carFile);) {
                while (reader.hasNext()) {
                    String carString = reader.nextLine();
                    String vin = (carString.substring(0, carString.indexOf('^')));
                    carString = removeOldDataFromString(carString);
                    String make = (carString.substring(0, carString.indexOf('^')));
                    carString = removeOldDataFromString(carString);
                    String model = (carString.substring(0, carString.indexOf('^')));
                    carString = removeOldDataFromString(carString);
                    String engineSize = (carString.substring(0, carString.indexOf('^')));
                    carString = removeOldDataFromString(carString);
                    String transmissionType = (carString.substring(0, carString.indexOf('^')));
                    carString = removeOldDataFromString(carString);
                    String options = (carString.substring(0, carString.indexOf('^')));

                    Cars.add(new Car(vin, make, model, engineSize, transmissionType, options));
                }
                reader.close();
            }
            if (ServiceOrder.exists()) {
                try (Scanner reader = new Scanner(ServiceOrder);) {
                    while (reader.hasNext()) {
                        String pastServiceOrderString = reader.nextLine();
                        String carVIN = (pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));
                        pastServiceOrderString = removeOldDataFromString(pastServiceOrderString);
                        String serviceDesc = (pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));
                        pastServiceOrderString = removeOldDataFromString(pastServiceOrderString);
                        String partsUsed = (pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));
                        pastServiceOrderString = removeOldDataFromString(pastServiceOrderString);
                        int techID = Integer
                                .parseInt(pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));
                        pastServiceOrderString = removeOldDataFromString(pastServiceOrderString);
                        int dealershipID = Integer
                                .parseInt(pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));
                        pastServiceOrderString = removeOldDataFromString(pastServiceOrderString);
                        double partsCost = Double
                                .parseDouble(pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));
                        pastServiceOrderString = removeOldDataFromString(pastServiceOrderString);
                        double totalCost = Double
                                .parseDouble(pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));
                        pastServiceOrderString = removeOldDataFromString(pastServiceOrderString);
                        double laborHours = Double
                                .parseDouble(pastServiceOrderString.substring(0, pastServiceOrderString.indexOf('^')));

                        serviceOrders.add(new ServiceOrder(carVIN, serviceDesc, partsUsed, techID, dealershipID,
                                partsCost, totalCost, laborHours));
                    }
                    reader.close();
                }
            }
        }
    }

    // saves the data that it has changed to the file that it needs
    static void saveData() throws IOException {
        String serviceOrdersString = "";
        String dealershipsString = "";
        String carsString = "";
        String ServiceTechsString = "";

        for (int i = 0; i < ServiceTechs.size(); i++)
            ServiceTechsString = ServiceTechsString + ServiceTechs.get(i).formatForFile();
        ServiceTechs.get(0).toFile(ServiceTechsString);

        for (int i = 0; i < dealerships.size(); i++)
            dealershipsString = dealershipsString + dealerships.get(i).formatForFile();
        dealerships.get(0).toFile(dealershipsString);

        for (int i = 0; i < serviceOrders.size(); i++)
            serviceOrdersString = serviceOrdersString + serviceOrders.get(i).formatForFile();
        serviceOrders.get(0).toFile(serviceOrdersString);

        for (int i = 0; i < Cars.size(); i++)
            carsString = carsString + Cars.get(i).formatForFile();
        Cars.get(0).toFile(carsString);

    }

    public static void main(String[] args) {
        // starting the GUI
        Application.launch(args);
    }
}
