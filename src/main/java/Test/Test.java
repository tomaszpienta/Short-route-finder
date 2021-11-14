package Test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Test extends Application {
    public static void main2(String[] args) {
        launch(args);
    }

    ObservableList<String> items = FXCollections.observableArrayList();
    ListView<String> listView = new ListView<>();
    Text text = new Text("");

    Path path12 = new Path();
    LineTo lineTo = new LineTo();
    MoveTo moveTo = new MoveTo();

    ToolBar toolBar = new ToolBar();
    Button addImage = new Button("File");
    Button calculate = new Button("Calculate");
    Button clear = new Button("Clear");

    Line line = new Line();

    Polyline polyline = new Polyline();

    javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();

    ArrayList list = new ArrayList();
    ArrayList list2 = new ArrayList();
    ArrayList list4 = new ArrayList();

    List list1 = new ArrayList();
    AtomicInteger click = new AtomicInteger();
    int f = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane pane1 = new GridPane();

        pane1.setAlignment(Pos.TOP_LEFT);
        toolBar.getItems().addAll(addImage, calculate, clear);

        pane1.add(toolBar, 0, 0);
        pane1.add(listView, 0, 1);

        Scene scene = new Scene(pane1, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text Canvas");
        primaryStage.show();

        FileChooser fileChooser = new FileChooser();

        listView.setEditable(true);
        listView.setPrefSize(200, 200);
        listView.setOrientation(Orientation.VERTICAL);
        listView.setCellFactory(TextFieldListCell.forListView());

        addImage.setOnAction(event1 -> {

            GridPane pane2 = new GridPane();
            Pane pane3 = new Pane();

            File file = fileChooser.showOpenDialog(primaryStage);
            String path = file.getPath();
            ImageView imageView = null;

            try {
                imageView = printImage(new FileInputStream(path));

                canvas.setOnMouseClicked(event -> {

                    //liczba kropek
                    click.getAndIncrement();

                    String s = (event.getX() + ", " + event.getY());

                    list.add(event.getX());
                    list.add(event.getY());

                    items.add(s);
                    listView.setItems(items);
                    listView.setEditable(false);

                    listView.setOnMouseClicked(event2 -> {

                        String selectedItem = listView.getSelectionModel().getSelectedItem();
                        String part[] = selectedItem.split(", ");
                        Double part1 = Double.parseDouble(part[0]);
                        Double part2 = Double.parseDouble(part[1]);

                        int itemPos = list.indexOf(part1);
                        int itemPos2 = list.indexOf(part2);

                        gc.strokeRoundRect(part1, part2, 2, 2, 5, 5);
                        gc.setStroke(Color.RED);

                        list.remove(itemPos);
                        list.add(0, part1);
                        list.remove(itemPos2);
                        list.add(1, part2);

                    });
                    text.setTranslateX(event.getX() + 1);
                    text.setTranslateY(event.getY() - 1);
                    //WYpisywanie liczby przy kropce
                    for (int i = 0; i < click.get(); i++) {
                        text.setText("" + i);

                    }

                    gc.setLineWidth(2.0);
                    gc.setStroke(Color.BLACK);
                    gc.strokeRoundRect(event.getX(), event.getY(), 2, 2, 5, 5);


                    if (list.size() < 3) {
                    } else {
                        for (int i = 0; i < list.size() - 3; i++) {
                            moveTo.setX((Double) list.get(i));
                            moveTo.setY((Double) list.get(i + 1));
                            lineTo.setX((Double) list.get(i + 2));
                            lineTo.setY((Double) list.get(i + 3));

                        }
                    }
                });

                calculate.setOnAction(event2 -> {
                    System.out.println("list: " + list);
                    int size = list.size();
                    //Wypisuje pary pozycji kropek do list.
                    for (int i = 0; i < size; i++) {
                        if (list.size() % 2 == 0)
                            list1.add(list.subList(i, i + 1));
                    }

                    //Obliczanie odleglości 1.[x, y] - 2.[x, y]
                    for (int i = 0; i < size; i++) {
                        if (i % 2 == 0) {
                            Double o = (Double) list.get(i);
                            list2.add(o);
                        }
                    }

                    //Nowa wersja wyliczania odleglosci punktow
                    double o = 0.0, o1 = 0.0, o2 = 0.0, o3 = 0.0;
                    double wynik = 0.0;

                    Map<Integer, List> nowa = new TreeMap<>();
                    List<Double> nowaLista = new ArrayList<>();
                    nowaLista.add((Double) list.get(0));
                    nowaLista.add((Double) list.get(1));
                    if (list.size() < 3) {
                        System.out.println("Za malo");
                    } else {

                        TreeMap<Integer, List> distanceByPoint = new TreeMap<>();
                        for (int i = 0; i < list.size() - 1; i++) {
                            System.out.println("Lista: " + list);
                            o = (Double) list.get(i);
                            o1 = (Double) list.get(i + 1);
                            for (int j = 2; j < list.size(); j += 2) {
                                o2 = (Double) list.get(j);
                                o3 = (Double) list.get(j + 1);

                                double wynikX = o2 - o;
                                double wynikX1 = (Math.pow(wynikX, 2));

                                double wynikY = o1 - o3;
                                double wynikY1 = (Math.pow(wynikY, 2));

                                wynik = Math.sqrt(wynikX1 + wynikY1);

                                List<Double> otherPiont = new ArrayList<>();
                                otherPiont.add((Double) list.get(j));
                                otherPiont.add((Double) list.get(j + 1));
                                distanceByPoint.put((int) wynik, otherPiont);

                            }
                            for (Map.Entry<Integer, List> entry : distanceByPoint.entrySet()) {
                                Integer key = entry.getKey();
                                List tab = entry.getValue();
                                nowa.put(key, tab);
                            }

                            if (!distanceByPoint.isEmpty()) {

                                System.out.println("kopytko: " + (distanceByPoint.firstEntry()));

                                if (list.contains(distanceByPoint.firstEntry().getValue().get(0)) && list.contains(distanceByPoint.firstEntry().getValue().get(1))) {
                                    Object o4 = (distanceByPoint.firstEntry().getValue().get(0));
                                    System.out.println("o4: " + o4);
                                    Object o5 = (distanceByPoint.firstEntry().getValue().get(1));
                                    System.out.println("o5: " + o5);
                                    list.remove(0);
                                    list.remove(0);
                                    list.remove(o4);
                                    list.remove(o5);
                                    list.add(0, (Double) (o4));
                                    list.add(1, (Double) (o5));

                                    nowaLista.add(2, (Double) (o4));
                                    nowaLista.add(3, (Double) (o5));


                                }
                                System.out.println("Lista2 : " + list);
                            }
                            if (nowaLista.size() < 3) {
                                System.out.println("Za mało");
                            } else {
                                for (int f = 0; f < nowaLista.size() - 1; f++) {
                                    if (f % 2 == 0) {
                                        o = (Double) nowaLista.get(f);
                                        o1 = (Double) nowaLista.get(f + 1);
                                        System.out.println(o + " o1: " + o1);
                                        polyline.getPoints().addAll(o, o1);
                                    }
                                }
                            }
                            line.setStroke(Color.BLACK);
                            System.out.println("Posortowana lista: " + nowaLista);
                            distanceByPoint.clear();
                        }
                    }
                });

                //Dostosowanie rozmiaru canvas do rozmiaru obrazu.
                canvas.setWidth(imageView.getFitWidth());
                canvas.setHeight(imageView.getFitHeight());

                path12.getElements().add(moveTo);
                path12.getElements().addAll(lineTo);

                pane3.getChildren().addAll(imageView, canvas, polyline, text);

                pane2.add(toolBar, 0, 0);
                pane2.add(listView, 0, 2);
                pane2.add(pane3, 0, 1);

                Scene scen = new Scene(pane2, imageView.getFitWidth(), imageView.getFitHeight());

                clearScane();

                primaryStage.setScene(scen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });

        clear.setOnAction(event ->

        {
            clearScane();
        });
    }


    private ImageView printImage(FileInputStream image) {
        Image image1 = new Image(image);
        ImageView imageView = new ImageView();
        imageView.setImage(image1);
        imageView.setFitHeight(700);
        imageView.setFitWidth(700);
        return imageView;
    }

    private void clearScane() {
        polyline.getPoints().clear();
        moveTo.setX(0);
        moveTo.setY(0);
        lineTo.setY(0);
        lineTo.setX(0);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        line.getStartX();
        line.getStartY();
        line.getEndX();
        line.getEndY();
        line.setStroke(Color.TRANSPARENT);
        list.clear();
        list1.clear();
        list2.clear();
        list4.clear();
        text.setText("");
        click.set(0);
        items.setAll("");
        listView.setItems(items);
    }

}

