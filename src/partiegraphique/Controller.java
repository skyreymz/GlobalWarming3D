package partiegraphique;

/* TODO
 * trier les import
 */

import climatechange.*;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Circle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.rules.Timeout;

import javafx.scene.control.Toggle;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.SubScene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;

public class Controller implements Initializable {
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    
    private float maxAnomalie; // Anomalie maximale de température, pour éviter de faire plein de fois l'appel à la méthode terre.getMaxAnomalie()
    private float minAnomalie; // Anomalie minimale de température, pour éviter de faire plein de fois l'appel à la méthode terre.getMinAnomalie()
	
    //Couleurs quadrilatères
	private static final Color COLOR_QUAD_5 = new Color(1, 0, 0, 0.75);
	private static final Color COLOR_QUAD_4 = new Color(1, 0.15, 0, 0.75);
	private static final Color COLOR_QUAD_3 = new Color(1, 0.3, 0, 0.75);
	private static final Color COLOR_QUAD_2 = new Color(1, 0.45, 0, 0.75);
	private static final Color COLOR_QUAD_1 = new Color(1, 0.6, 0, 0.75);
	private static final Color COLOR_QUAD_0 = new Color(0.75, 0.75, 0.75, 0.75);
	private static final Color COLOR_QUAD_n1 = new Color(0, 0.29, 1, 0.75);
	private static final Color COLOR_QUAD_n2 = new Color(0, 0.22, 1, 0.75);
	private static final Color COLOR_QUAD_n3 = new Color(0, 0.15, 1, 0.75);
	private static final Color COLOR_QUAD_n4 = new Color(0, 0.07, 1, 0.75);
	private static final Color COLOR_QUAD_n5 = new Color(0, 0, 1, 0.75);
	
	//Couleurs histogrammes
    
    
	@FXML
	private Pane pane3D;
	
	@FXML
	private Pane pane5;
	
	@FXML
	private Pane pane4;
	
	@FXML
	private Pane pane3;
	
	@FXML
	private Pane pane2;
	
	@FXML
	private Pane pane1;
	
	@FXML
	private Pane pane0;
	
	@FXML
	private Pane pane_1;
	
	@FXML
	private Pane pane_2;
	
	@FXML
	private Pane pane_3;
	
	@FXML
	private Pane pane_4;
	
	@FXML
	private Pane pane_5;
	
	@FXML
	private Label labelAnnee;
	
	@FXML
	private Slider sliderAnnee;
	
	@FXML
	private RadioButton quadrilatere;
	
	@FXML
	private RadioButton histogramme;
	
	@FXML
	private Button start_pause;
	
	@FXML
	private Slider speed;
	
	@FXML
	private LineChart<String, Float> chart;
	
	//@FXML
	//private NumberAxis xAxis;
	
	@FXML
	private NumberAxis yAxis;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Data
		Terre terre = Read_file.getDataFromCSVFile("src/climatechange/tempanomaly_4x4grid.csv");
		maxAnomalie = terre.getMaxAnomalie();
    	minAnomalie = terre.getMinAnomalie();
    	
		//Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
        	URL modelUrl = this.getClass().getResource("Earth/earth.obj");
        	objImporter.read(modelUrl);
        } catch (ImportException e) {
        	// handle exception
        	System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
    	
        //Mise en place de la légende
        pane5.setStyle("-fx-background-color: rgb(255, 0, 0)");
    	pane4.setStyle("-fx-background-color: rgb(255, 38, 0)");
    	pane4.setStyle("-fx-background-color: rgb(255, 77, 0)");
    	pane3.setStyle("-fx-background-color: rgb(255, 115, 0)");
    	pane2.setStyle("-fx-background-color: rgb(255, 153, 0)");
    	pane1.setStyle("-fx-background-color: rgb(255, 191, 0)");
    	pane0.setStyle("-fx-background-color: rgb(255, 255, 255)");
    	pane_1.setStyle("-fx-background-color: rgb(0, 125, 255)");
    	pane_2.setStyle("-fx-background-color: rgb(0, 100, 255)");
    	pane_3.setStyle("-fx-background-color: rgb(0, 75, 255)");
    	pane_4.setStyle("-fx-background-color: rgb(0, 50, 255)");
    	pane_5.setStyle("-fx-background-color: rgb(0, 25, 255)");
        
        //Initialisation
        quadrilatere(earth, terre, 2000);

		//Mise en place du décochage des radio boutons "Histogramme" et "Quadrilatère"
    	ToggleGroup group = new ToggleGroup();
    	quadrilatere.setToggleGroup(group);
    	quadrilatere.setSelected(true);
    	histogramme.setToggleGroup(group);
		
		//RadioBoutons "quadrilatere" et "histogramme"
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	           @Override
	           public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
	               if (quadrilatere.isSelected()) {
	            	   earth.getChildren().remove(1, earth.getChildren().size()); // Suppression des données précédentes
	            	   quadrilatere(earth, terre, (int) sliderAnnee.getValue());
	               } else {
	            	   earth.getChildren().remove(1, earth.getChildren().size()); // Suppression des données précédentes
	            	   histogramme(earth, terre, (int) sliderAnnee.getValue());
	               }
	           }
	       });
		
		/*TODO
		 * Si on modifie la vitesse de l'animation, la nouvelle vitesse n'est pas immédiatement prise en compte
		 * Il faut attendre la fin du changement d'année pour que la nouvelle vitesse soit prise en compte
		 */
		//Animation
		final long startNanoTime = System.nanoTime();
        AnimationTimer animation = new AnimationTimer() {
        	double compteur = speed.getValue(); // Permet d'incrémenter les années au bon moment
        	@Override
        	public void handle(long currentNanotime) {
        		double t = (currentNanotime - startNanoTime) / 1000000000.0; // Correspond au temps écoulé depuis le lancement de l'application (en seconde)
        		if ((t > compteur) ) { // On entre dans cette boucle toutes les "speed.getValue()" secondes
        			if (t < (compteur + 0.5)) {
        				compteur += speed.getValue();
        				sliderAnnee.setValue(sliderAnnee.getValue()+1);
        			} else { // Dans ce cas, t est trop grand par rapport au compteur donc on donne la valeur de t au compteur
        				compteur = t;
        			}
        		}
        	}
        };
        
        start_pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (start_pause.getText().equals("Start") && (sliderAnnee.getValue() < 2020)) {
					start_pause.setText("Pause");
					animation.start();
				} else if (start_pause.getText().equals("Pause")){
					start_pause.setText("Start");
					animation.stop();
				}
			}
		});
        
        //Slider "Années"
    	sliderAnnee.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (oldValue.intValue() != newValue.intValue()) {
					StringBuilder annee = new StringBuilder();
					annee.append("Années (");
					annee.append((int) sliderAnnee.getValue());
					annee.append(")");
					labelAnnee.setText(annee.toString());
					if ((newValue.intValue() == 2020) && start_pause.getText().equals("Pause")) { //Arrêt de l'animation lorsque le slider est à l'année 2020
						animation.stop();
						start_pause.setText("Start");
					} else if (quadrilatere.isSelected()) {
						updateQuadrilatere(earth, terre, newValue.intValue());
					} else { // Dans ce cas, on a (histogramme.isSelected() == true)
						updateHistogramme(earth, terre, newValue.intValue());
					}
				}
			}
		});
    	
        earth.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
            	PickResult pr = me.getPickResult();
        		Point3D p = pr.getIntersectedPoint();
        		//TODO here
        		System.out.print(p);
        		System.out.print(" ; Rayon = ");
        		System.out.print(" ");
        		System.out.println(Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2) + Math.pow(p.getZ(), 2)));
        		int[] coordonnees = Coord3dToGeoCoordZone(p.getX(), p.getY(), p.getZ());
        		
        		//graphique
        		StringBuilder title = new StringBuilder(5);
        		title.append("Evolution des anomalies de température de la zone (");
            	title.append(coordonnees[0]);
            	title.append(", ");
            	title.append(coordonnees[1]);
            	title.append(")");
            	chart.setTitle(title.toString());
            	
            	// Suppression des anciennes données
            	if (chart.getData().size() > 0) {
            		chart.getData().remove(0);
            	}
            	

            	XYChart.Series<String, Float> series = new XYChart.Series<String, Float>();
            	List<Float> anomaliesAnnees = terre.anomaliesAnnees((int) coordonnees[0], (int) coordonnees[1]);
            	int indice = 0;
            	int compteurAnnee = 1880;
            	for (int j = 0 ; j < anomaliesAnnees.size() ; j = j + 1) {
            		float val = anomaliesAnnees.get(j);
            		if (!Float.isNaN(val)) {
            			series.getData().add(new XYChart.Data(String.valueOf(compteurAnnee), val));
            		}
            		indice += 1;
            		compteurAnnee += 1;
            	}
            	
            	chart.getData().add(series);
            	//System.out.println(chart.getData().size());
            }
        });
        
        

        // Draw a line

        // Draw an helix

        // Draw city on the earth
        /*disPlayTown(earth, "Brest", 48.447911f, -4.418539f);
        disPlayTown(earth, "Marseille", 43.435555f, 5.213611f);
        disPlayTown(earth, "New York", 40.639751f, -73.778925f);
        disPlayTown(earth, "Cape Town", -33.964806f, 18.601667f);
        disPlayTown(earth, "Istanbul", 40.976922f, 28.814606f);
        disPlayTown(earth, "Reykjavik", 64.13f, -21.940556f);
        disPlayTown(earth, "Singapore", 1.350189f, 103.994433f);
        disPlayTown(earth, "Seoul", 37.469075f, 126.450517f);*/
        
        // Partie 8
        /*final PhongMaterial greenMaterial = new PhongMaterial();
        Color green = new Color(0, 0.1, 0, 0.1);
        greenMaterial.setDiffuseColor(green);
        
        final PhongMaterial redMaterial = new PhongMaterial();
        Color red = new Color(0.1, 0, 0, 0.1);
        redMaterial.setDiffuseColor(red);
        
		boolean couleur = true;
        for (int lon = -180 ; lon < 180 ; lon = lon + 4) { //longitude
        	for (int lat = -90 ; lat < 90 ; lat = lat + 4) { //latitude
        		Point3D topRight = geoCoordTo3dCoord(lat+4, lon+4, 1.01f);
        		Point3D bottomRight = geoCoordTo3dCoord(lat, lon+4, 1.01f);
        		Point3D bottomLeft = geoCoordTo3dCoord(lat, lon, 1.01f);
        		Point3D topLeft = geoCoordTo3dCoord(lat+4, lon, 1.01f);
        		if (couleur) {
        			AddQuadrilateral(earth, topRight, bottomRight, bottomLeft, topLeft, greenMaterial);
        			couleur = false;
        		} else {
        			AddQuadrilateral(earth, topRight, bottomRight, bottomLeft, topLeft, redMaterial);
        			couleur = true;
        		}
        	}
        }*/
        
        // Ajouter ensuite le Group earth au graphe de la scène 3D
        root3D.getChildren().add(earth);
        
        //Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);
        
        /*TODO
         * Meilleure visibilité sans point light
         */
        /*
        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);
        */
        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        // Create the subscene
        SubScene subscene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY); 
        
        pane3D.getChildren().addAll(subscene);
	}//TODO fin du initialize
	
	// Permet de passer de coordonnées sphériques en coordonnées cartésiennes
	public static Point3D geoCoordTo3dCoord(float lat, float lon, float radius) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius,
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor))*radius,
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius);
    }
	
	// Permet de passer de coordonnées cartésiennes en coordonnées sphériques (pour avoir les coordonnées du centre de la zone la plus proche)
	public static int[] Coord3dToGeoCoordZone(double x, double y, double z) {
		double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)); // en général proche de 1.009
		
		double teta = Math.acos(y/r);
		double lat = Math.toDegrees(teta)-90;
		lat = lat - TEXTURE_LAT_OFFSET;
		int latZone = (((int) (lat+90)/4) * 4) - 88; //Latitude du centre de la zone la plus proche
		if (latZone > 88) {
			System.out.println("Vous avez cliqué sur le pôle nord, la zone la plus proche a été sélectionnée");
			latZone = 88;
		}
		
		double phi = Math.atan2(-x, z);
		double lon = Math.toDegrees(phi);
		lon = lon - TEXTURE_LON_OFFSET;
		int lonZone; //Longitude du centre de la zone la plus proche
		if (lon < -180) { 
			lonZone = 178;
		} else {
			lonZone = (((int) (lon+180)/4) * 4) - 178; 
		}
		
		int[] retour = {latZone, lonZone};
		
	    return retour;
	}
	

	//QUADRILATERE
    private void quadrilatere(Group parent, Terre terre, int annee) {
    	for (int lat = -88 ; lat <= 88 ; lat = lat + 4) { //latitude
        	for (int lon = -178 ; lon <= 178 ; lon = lon + 4) { //longitude
        		float anomalie = terre.anomalie(lat, lon, annee);
        		final PhongMaterial colorMaterial = new PhongMaterial();
        		if (anomalie > 0) {
            		if (anomalie < (float) maxAnomalie/5) {
            			colorMaterial.setDiffuseColor(COLOR_QUAD_1);
               		} else if (anomalie < (float) 2*maxAnomalie/5) {
               			colorMaterial.setDiffuseColor(COLOR_QUAD_2);
               		} else if (anomalie < (float) 3*maxAnomalie/5) {
               			colorMaterial.setDiffuseColor(COLOR_QUAD_3);
               		} else if (anomalie < (float) 4*maxAnomalie/5) {
               			colorMaterial.setDiffuseColor(COLOR_QUAD_4);
               		} else {
               			colorMaterial.setDiffuseColor(COLOR_QUAD_5);
               		}
            	} else if (anomalie < 0) { //On sait ici que anomalie et min sont négatives
            		if (anomalie > minAnomalie/5) {
            			colorMaterial.setDiffuseColor(COLOR_QUAD_n1);
            		} else if (anomalie > 2*minAnomalie/5) {
            			colorMaterial.setDiffuseColor(COLOR_QUAD_n2);
            		} else if (anomalie > 3*minAnomalie/5) {
            			colorMaterial.setDiffuseColor(COLOR_QUAD_n3);
            		} else if (anomalie > 4*minAnomalie/5) {
            			colorMaterial.setDiffuseColor(COLOR_QUAD_n4);
            		} else {
            			colorMaterial.setDiffuseColor(COLOR_QUAD_n5);
            		}
            	} else if (anomalie == 0) {
            		colorMaterial.setDiffuseColor(COLOR_QUAD_0);
            	} else {
            		colorMaterial.setDiffuseColor(Color.TRANSPARENT);
            	}
        		Point3D topRight = geoCoordTo3dCoord(lat+2, lon+2, 1.01f);
            	Point3D bottomRight = geoCoordTo3dCoord(lat-2, lon+2, 1.01f);
            	Point3D bottomLeft = geoCoordTo3dCoord(lat-2, lon-2, 1.01f);
            	Point3D topLeft = geoCoordTo3dCoord(lat+2, lon-2, 1.01f);
                AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, colorMaterial);
        	}
    	}
    }
    
    // Modification des quadrilatères au lieu de les supprimer et les recréer à chaque fois
    private void updateQuadrilatere(Group parent, Terre terre, int annee) {
    	for (int i = 1 ; i < parent.getChildren().size() ; i = i + 1) {
    		float anomalie = terre.getListeZones().get(i-1).getListeAnomalies().get(annee - 1880);
    		final PhongMaterial colorMaterial = new PhongMaterial();
    		if (anomalie > 0) {
        		if (anomalie < (float) maxAnomalie/5) {
        			colorMaterial.setDiffuseColor(COLOR_QUAD_1);
           		} else if (anomalie < (float) 2*maxAnomalie/5) {
           			colorMaterial.setDiffuseColor(COLOR_QUAD_2);
           		} else if (anomalie < (float) 3*maxAnomalie/5) {
           			colorMaterial.setDiffuseColor(COLOR_QUAD_3);
           		} else if (anomalie < (float) 4*maxAnomalie/5) {
           			colorMaterial.setDiffuseColor(COLOR_QUAD_4);
           		} else {
           			colorMaterial.setDiffuseColor(COLOR_QUAD_5);
           		}
        	} else if (anomalie < 0) { //On sait ici que anomalie et min sont négatives
        		if (anomalie > minAnomalie/5) {
        			colorMaterial.setDiffuseColor(COLOR_QUAD_n1);
        		} else if (anomalie > 2*minAnomalie/5) {
        			colorMaterial.setDiffuseColor(COLOR_QUAD_n2);
        		} else if (anomalie > 3*minAnomalie/5) {
        			colorMaterial.setDiffuseColor(COLOR_QUAD_n3);
        		} else if (anomalie > 4*minAnomalie/5) {
        			colorMaterial.setDiffuseColor(COLOR_QUAD_n4);
        		} else {
        			colorMaterial.setDiffuseColor(COLOR_QUAD_n5);
        		}
        	} else if(anomalie == 0) {
        		colorMaterial.setDiffuseColor(COLOR_QUAD_0);
        	} else {
        		colorMaterial.setDiffuseColor(Color.TRANSPARENT);
        	}
            final MeshView meshView = (MeshView) parent.getChildren().get(i);
            meshView.setMaterial(colorMaterial);
    	}
    }
    
    // Pour les fonctions quadrilatere et updateQuadrilatere
    private void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight, Point3D bottomLeft, Point3D topLeft, PhongMaterial material)
    {
        final TriangleMesh triangleMesh = new TriangleMesh();
        final float[] points = {
                (float)topRight.getX(), (float)topRight.getY(), (float)topRight.getZ(),
                (float)topLeft.getX(), (float)topLeft.getY(), (float)topLeft.getZ(),
                (float)bottomLeft.getX(), (float)bottomLeft.getY(), (float)bottomLeft.getZ(),
                (float)bottomRight.getX(), (float)bottomRight.getY(), (float)bottomRight.getZ(),
        };
        
        final float[] texCoords = {
                1, 1,
                1, 0,
                0, 1,
                0, 0
        };
        
        final int[] faces = {
                0, 1, 1, 0, 2, 2,
                0, 1, 2, 2, 3, 3
        };
        
        /*points :
         *  
         *  1   0        texture :
         *  -----      1,1(0)   1,0(1)
         *  |   |      ----------
         *  |   |      |        |
         *  |   |      |        |
         *  -----      ----------
         *  2   3      0,1(2)   0,0(3)
         */
        
        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);
        
        final MeshView meshView = new MeshView(triangleMesh);
        meshView.setMaterial(material);
        parent.getChildren().addAll(meshView);
    }
    
    
    //HISTOGRAMME
    private void histogramme (Group parent, Terre terre, int annee) {
    	//Mise en place de la légende :
       	for (int lat = -88 ; lat <= 88 ; lat = lat + 4) { //latitude
           	for (int lon = -178 ; lon <= 178 ; lon = lon + 4) { //longitude
           		Point3D point1 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.01);
           		Point3D point2;
           		float anomalie = terre.anomalie(lat, lon, annee);
           		final PhongMaterial colorMaterial = new PhongMaterial();
           		if (anomalie > 0) {
              		if (anomalie < (float) maxAnomalie/5) {
              			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.04);
              			colorMaterial.setDiffuseColor(COLOR_QUAD_1);
               		} else if (anomalie < (float) 2*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_2);
               		} else if (anomalie < (float) 3*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_3);
               		} else if (anomalie < (float) 4*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_5);
               		}
               	} else if (anomalie < 0) { //On sait ici que anomalie et min sont négatives
               		if (anomalie > minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.04);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n1);
               		} else if (anomalie > (float) 2*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n2);
               		} else if (anomalie > (float) 3*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n3);
               		} else if (anomalie > (float) 4*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n5);
               		}
               	} else if (anomalie == 0) {
               		point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.02);
               		colorMaterial.setDiffuseColor(COLOR_QUAD_0);
               	} else {
               		point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.02);
               		colorMaterial.setDiffuseColor(Color.TRANSPARENT);
               	}
                Cylinder line = createLine(point1, point2);
                line.setMaterial(colorMaterial);
               	parent.getChildren().add(line);
           	}
       	}
    }
    
    // Modification des histogrammes au lieu de les supprimer et les recréer à chaque fois
    private void updateHistogramme (Group parent, Terre terre, int annee) {
    	int indice = 0;
    	for (int lat = -88 ; lat <= 88 ; lat = lat + 4) { //latitude
           	for (int lon = -178 ; lon <= 178 ; lon = lon + 4) { //longitude
           		Point3D point1 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.01);
           		Point3D point2;
        		float anomalie = terre.getListeZones().get(indice).getListeAnomalies().get(annee - 1880);
        		final PhongMaterial colorMaterial = new PhongMaterial();
        		if (anomalie > 0) {
           			if (anomalie < (float) maxAnomalie/5) {
           				point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.04);
           				colorMaterial.setDiffuseColor(COLOR_QUAD_1);
               		} else if (anomalie < (float) 2*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_2);
               		} else if (anomalie < (float) 3*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_3);
               		} else if (anomalie < (float) 4*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_5);
               		}
        		} else if (anomalie < 0) { //On sait ici que anomalie et min sont négatives
               		if (anomalie > minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.04);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n1);
               		} else if (anomalie > (float) 2*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n2);
               		} else if (anomalie > (float) 3*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n3);
               		} else if (anomalie > (float) 4*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_QUAD_n5);
               		}
               	} else if(anomalie == 0) {
               		point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.02);
               		colorMaterial.setDiffuseColor(COLOR_QUAD_0);
               	} else {
               		point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.02);
               		colorMaterial.setDiffuseColor(Color.TRANSPARENT);
               	}
            	final Cylinder line = (Cylinder) parent.getChildren().get(indice+1); //On prend indice+1 car l'indice 0 correspond aux composants pour l'affichage de la terre
                line.setMaterial(colorMaterial);
                    
                Point3D diff = point2.subtract(point1);
                double height = diff.magnitude();
                line.setHeight(height);
                    
                Point3D mid = point2.midpoint(point1);
                Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());
                line.getTransforms().set(0, moveToMidpoint);
                Point3D yAxis = new Point3D(0, 1, 0);
                Point3D axisOfRotation = diff.crossProduct(yAxis);
                double angle = Math.acos(diff.normalize().dotProduct(yAxis));
                Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
                line.getTransforms().set(1, rotateAroundCenter);
                indice += 1;
        	}
    	}
    }
    
    // From Rahel LÃ¼thy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    // Pour les fonctions histogramme et updateHistogramme
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        
        line.setRadius(0.0075); // Rayon du cylindre

        return line;
    }
    
    
	/*
    public void disPlayTown(Group parent, String name, float latitude, float longitude) {
    	Sphere sphere = new Sphere(0.01);
    	Point3D position = geoCoordTo3dCoord(latitude, longitude, 1.01f);
    	sphere.setTranslateX(position.getX());
    	sphere.setTranslateY(position.getY());
    	sphere.setTranslateZ(position.getZ());
    	
    	final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        
        sphere.setMaterial(greenMaterial);
    	sphere.setId(name);
    	parent.getChildren().add(sphere);
    }*/
}