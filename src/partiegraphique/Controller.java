package partiegraphique;

//TODO trier les import

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
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Toggle;
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
    
    private float maxAnomalie; // Anomalie maximale de température, pour éviter d'effectuer plusieurs fois l'appel à la méthode terre.getMaxAnomalie()
    private float minAnomalie; // Anomalie minimale de température, pour éviter d'effectuer plusieurs fois l'appel à la méthode terre.getMinAnomalie()
	
    private static final Color COLOR_0 = new Color(0.75, 0.75, 0.75, 0.75);
    
    //Couleurs quadrilatères
	private static final Color COLOR_QUAD_5 = new Color(1, 0, 0, 0.75);
	private static final Color COLOR_QUAD_4 = new Color(1, 0.15, 0, 0.75);
	private static final Color COLOR_QUAD_3 = new Color(1, 0.3, 0, 0.75);
	private static final Color COLOR_QUAD_2 = new Color(1, 0.45, 0, 0.75);
	private static final Color COLOR_QUAD_1 = new Color(1, 0.6, 0, 0.75);
	private static final Color COLOR_QUAD_n1 = new Color(0, 0.29, 1, 0.75);
	private static final Color COLOR_QUAD_n2 = new Color(0, 0.22, 1, 0.75);
	private static final Color COLOR_QUAD_n3 = new Color(0, 0.15, 1, 0.75);
	private static final Color COLOR_QUAD_n4 = new Color(0, 0.07, 1, 0.75);
	private static final Color COLOR_QUAD_n5 = new Color(0, 0, 1, 0.75);
	
	//Couleurs histogrammes
	private static final Color COLOR_HISTO_5 = new Color(1, 0, 0, 0.75);
	private static final Color COLOR_HISTO_4 = new Color(1, 0.1, 0.1, 0.75);
	private static final Color COLOR_HISTO_3 = new Color(1, 0.2, 0.2, 0.75);
	private static final Color COLOR_HISTO_2 = new Color(1, 0.3, 0.3, 0.75);
	private static final Color COLOR_HISTO_1 = new Color(1, 0.4, 0.4, 0.75);
	private static final Color COLOR_HISTO_n1 = new Color(0, 0.29, 1, 0.75);
	private static final Color COLOR_HISTO_n2 = new Color(0, 0.22, 1, 0.75);
	private static final Color COLOR_HISTO_n3 = new Color(0, 0.15, 1, 0.75);
	private static final Color COLOR_HISTO_n4 = new Color(0, 0.07, 1, 0.75);
	private static final Color COLOR_HISTO_n5 = new Color(0, 0, 1, 0.75);
	
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
	private CheckBox checkBoxTerreCouleur;
	
	@FXML
	private CheckBox checkBoxAnomalies;
	
	@FXML
	private RadioButton buttonQuadrilatere;
	
	@FXML
	private RadioButton buttonHistogramme;
	
	@FXML
	private Button start_pause;
	
	@FXML
	private Slider speed;
	
	@FXML
	private LineChart<Integer, Float> chart;
	
	@FXML
	private NumberAxis xAxis;
	
	@FXML
	private NumberAxis yAxis;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        // Mise en place du décochage des radio boutons "buttonHistogramme" et "buttonQuadrilatère"
    	ToggleGroup group = new ToggleGroup();
    	buttonQuadrilatere.setToggleGroup(group);
    	buttonQuadrilatere.setSelected(true);
    	buttonHistogramme.setToggleGroup(group);
    	
		// Données concernant les anomalies de température
		Terre terre = Read_file.getDataFromCSVFile("src/climatechange/tempanomaly_4x4grid.csv");
		maxAnomalie = terre.getMaxAnomalie();
    	minAnomalie = terre.getMinAnomalie();
    	
        // Données concernant la structure de la Terre
        ObjModelImporter objCouleurImporter = new ObjModelImporter();
        ObjModelImporter objGrisImporter = new ObjModelImporter();
        try {
        	URL modelCouleurUrl = this.getClass().getResource("Earth/earth.obj");
        	objCouleurImporter.read(modelCouleurUrl);
        	URL modelGrisUrl = this.getClass().getResource("Earth/earth_nb.obj");
        	objGrisImporter.read(modelGrisUrl);
        } catch (ImportException e) {
        	// handle exception
        	System.out.println(e.getMessage());
        }
        MeshView[] meshViewsCouleur = objCouleurImporter.getImport();
        MeshView[] meshViewsGris = objGrisImporter.getImport();
        
        // Groupe parent de la scène 3D
        Group root3D = new Group();
        
        // Groupes enfants
        Group earthCouleur = new Group(meshViewsCouleur); // contient la structure de la Terre (en couleur)
        Group earthGris = new Group(meshViewsGris); // contient la structure de la Terre (en gris)
        Group quad = new Group(); // Groupe des quadrilatères pour une certaine année
        Group histo = new Group(); // Groupe des histogrammes pour une certaine année
        
        // Initialisation des deux types de visualisation pour l'année 2000
        quadrilatere(quad, terre, 2000);
        histogramme(histo, terre, 2000);
        
        // Initialisation de la terre en couleur
        root3D.getChildren().add(earthCouleur); // Ajout de earth (en couleur) à l'indice 0 de root3D.getChildren()
        checkBoxTerreCouleur.setSelected(true);
        
        // Ajout d'une lumière ambiante
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight); // ajout de ambientLight à l'indice 1 de root3D.getChildren()
        
        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D); // ajout de cameraXform (dans la classe CameraManager) à l'indice 2 de root3D.getChildren()
        
        SubScene subscene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.gray(0.3)); 
        pane3D.getChildren().addAll(subscene);
        
        // Listener lié à la checkBox "checkBoxTerreCouleur"
        checkBoxTerreCouleur.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (checkBoxTerreCouleur.isSelected()) {
					root3D.getChildren().set(0, earthCouleur);
				} else {
					root3D.getChildren().set(0, earthGris);
				}
			}
        });
        
        // Listener lié à la checkBox "CheckBoxAnomalies"
        checkBoxAnomalies.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (checkBoxAnomalies.isSelected()) {
					updateMode(root3D, quad, histo);
				} else {
					emptyLegend();
					root3D.getChildren().remove(3);
				}
			}
        });
    	
    	// Listener lié aux radioBoutons "buttonQuadrilatere" et "buttonHistogramme"
    	group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
    		@Override
	           public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
    			updateMode(root3D, quad, histo);
    		}
    	});
    	
    	
		

		// Animation
		final long startNanoTime = System.nanoTime();
        AnimationTimer animation = new AnimationTimer() {
        	double compteur = speed.getValue(); // Permet d'incrémenter les années au bon moment
        	@Override
        	public void handle(long currentNanotime) {
        		double t = (currentNanotime - startNanoTime) / 1000000000.0; // Correspond au temps écoulé depuis le lancement de l'application (en seconde)
        		if ((t > compteur) ) {
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
        
        // Listener du slider "Années"
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
					}
					updateQuadrilatere(quad, terre, newValue.intValue());
					updateHistogramme(histo, terre, newValue.intValue());
				}
			}
		});
    	
    	// Evenement lié au clic de la souris sur la terre
    	EventHandler<MouseEvent> eventMode = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
            	PickResult pr = me.getPickResult();
            	Point3D p = pr.getIntersectedPoint();
            	
        		System.out.print("Coordonnées cartésiennes du point sélectionné : ");
        		StringBuilder sb1 = new StringBuilder();
        		sb1.append("(x = ");
            	sb1.append(p.getX());
            	sb1.append(", y = ");
            	sb1.append(p.getY());
            	sb1.append(", z = ");
            	sb1.append(p.getZ());
            	sb1.append(")");
            	System.out.println(sb1.toString());
        		
        		System.out.print("Coordonnées géométriques du point sélectionné : ");
        		double[] coordonnees = coord3dToGeoCoord(p.getX(), p.getY(), p.getZ());
        		StringBuilder sb2 = new StringBuilder();
        		sb2.append("(Latitude = ");
            	sb2.append(coordonnees[0]);
            	sb2.append(", Longitude = ");
            	sb2.append(coordonnees[1]);
            	sb2.append(")");
            	System.out.println(sb2.toString());

            	System.out.print("Coordonnées géométriques du centre de la zone la plus proche : ");
        		int[] coordonneesZone = nearestCenterZoneGeoCoord(coordonnees[0], coordonnees[1], coordonnees[2]);
        		StringBuilder sb3 = new StringBuilder();
        		sb3.append("(Latitude = ");
            	sb3.append(coordonneesZone[0]);
            	sb3.append(", Longitude = ");
            	sb3.append(coordonneesZone[1]);
            	sb3.append(")");
            	System.out.println(sb3.toString() + "\n");
        		
            	
        		// Graphique
        		StringBuilder title = new StringBuilder(5);
        		title.append("Evolution des anomalies de température de la zone (");
            	title.append(coordonneesZone[0]);
            	title.append(", ");
            	title.append(coordonneesZone[1]);
            	title.append(")");
            	chart.setTitle(title.toString());
            	
            	// Suppression des anciennes données
            	if (chart.getData().size() > 0) {
            		chart.getData().remove(0);
            	}
            	
            	XYChart.Series<Integer, Float> series = new XYChart.Series<Integer, Float>();
            	List<Float> anomaliesAnnees = terre.anomaliesAnnees((int) coordonneesZone[0], (int) coordonneesZone[1]);
            	
            	int compteurAnnee = 1880;
            	for (int i = 0 ; i < anomaliesAnnees.size() ; i = i + 1) {
            		float val = anomaliesAnnees.get(i);
            		if (!Float.isNaN(val)) {
            			XYChart.Data<Integer, Float> data = new XYChart.Data<Integer, Float>(compteurAnnee, val);
            			series.getData().add(data);
            		}
            		compteurAnnee += 1;
            	}
            	chart.getData().add(series);
            }
        };
        earthCouleur.setOnMousePressed(eventMode);
        earthGris.setOnMousePressed(eventMode);
        quad.setOnMousePressed(eventMode);
        
        //TODO delete here
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
        
        
	}//TODO fin du initialize
	
	/**
	 * Fonction permettant de passer de coordonnées géographiques en coordonnées cartésiennes (adaptée à la texture de la terre fournie)
	 * @param lat
	 * @param lon
	 * @param radius
	 * @return
	 */
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
	
	/**
	 * Fonction permettant de passer de coordonnées cartésiennes en coordonnées géographiques (adaptée à la texture de la terre fournie)
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static double[] coord3dToGeoCoord(double x, double y, double z) {
		double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
		
		double teta = Math.acos(y/r);
		double lat = Math.toDegrees(teta)-90;
		lat -= TEXTURE_LAT_OFFSET;
		if (lat > 90) { // Rééquilibrage
			lat = 89.99;
		}
		
		double phi = Math.atan2(-x, z);
		double lon = Math.toDegrees(phi);
		lon -= TEXTURE_LON_OFFSET;
		if (lon < -180) { // Rééquilibrage
			lon += 360;
		}
		
		double[] retour = {lat, lon, r};
	    return retour;
	}
	
	/**
	 * On fournit les coordonnées géographiques d'un point et cette fonction nous renvoie les coordonnées géographiques du centre de la zone la plus proche de ce point
	 * @param lat
	 * @param lon
	 * @param radius
	 * @return
	 */
	public static int[] nearestCenterZoneGeoCoord(double lat, double lon, double radius) {
		int latZone = (((int) (lat+90)/4) * 4) - 88; //Latitude du centre de la zone la plus proche
		
		int lonZone; //Longitude du centre de la zone la plus proche
		lonZone = (((int) (lon+180)/4) * 4) - 178; 
		
		int[] retour = {latZone, lonZone};
		return retour;
	}
	
	private void emptyLegend() {
		pane5.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane4.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane3.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane2.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane1.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane0.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane_1.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane_2.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane_3.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane_4.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    	pane_5.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
	}

	/**
	 * Fonction permettant d'adapter la légende aux quadrilatères
	 */
	private void putlegendQuadrilatere() {
        pane5.setStyle("-fx-background-color: rgb(0, 25, 255)");
    	pane4.setStyle("-fx-background-color: rgb(0, 50, 255)");
    	pane3.setStyle("-fx-background-color: rgb(0, 75, 255)");
    	pane2.setStyle("-fx-background-color: rgb(0, 100, 255)");
    	pane1.setStyle("-fx-background-color: rgb(0, 125, 255)");
    	pane0.setStyle("-fx-background-color: rgb(255, 255, 255)");
    	pane_1.setStyle("-fx-background-color: rgb(255, 191, 0)");
    	pane_2.setStyle("-fx-background-color: rgb(255, 153, 0)");
    	pane_3.setStyle("-fx-background-color: rgb(255, 115, 0)");
    	pane_4.setStyle("-fx-background-color: rgb(255, 77, 0)");
    	pane_5.setStyle("-fx-background-color: rgb(255, 0, 0)");
	}
	
	/**
	 * Fonction qui ajoute des quadrilatères au niveau de chaque zone dans le groupe parent.
	 * Chacun de ces quadrilatères a une couleur adaptée à sa zone et à son anomalie de température pour une certaine année. 
	 * @param parent
	 * @param terre
	 * @param annee
	 */
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
            	} else if (anomalie < 0) {
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
            		colorMaterial.setDiffuseColor(COLOR_0);
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
    
    /**
     * Fonction permettant la mise à jour de la couleur de chaque quadrilatère en fonction de l'anomalie de température de sa zone pour une année donnée.
     * @param parent
     * @param terre
     * @param annee
     */
    private void updateQuadrilatere(Group parent, Terre terre, int annee) {
    	for (int i = 0 ; i < parent.getChildren().size() ; i = i + 1) {
    		float anomalie = terre.getListeZones().get(i).getListeAnomalies().get(annee - 1880);
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
        	} else if (anomalie < 0) {
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
        		colorMaterial.setDiffuseColor(COLOR_0);
        	} else {
        		colorMaterial.setDiffuseColor(Color.TRANSPARENT);
        	}
            final MeshView meshView = (MeshView) parent.getChildren().get(i);
            meshView.setMaterial(colorMaterial);
    	}
    }
    
    /**
     * Fonction permettant l'ajout d'un quadrilatère dans le groupe parent à partir de 4 positions en coordonnées cartésiennes et d'un material.
     * @param parent
     * @param topRight
     * @param bottomRight
     * @param bottomLeft
     * @param topLeft
     * @param material
     */
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
    
    
    /**
	 * Fonction permettant d'adapter la légende aux histogrammes
	 */
    private void putlegendHistogramme() {
        pane5.setStyle("-fx-background-color: rgb(255, 25, 25)");
    	pane4.setStyle("-fx-background-color: rgb(255, 50, 50)");
    	pane3.setStyle("-fx-background-color: rgb(255, 75, 75)");
    	pane2.setStyle("-fx-background-color: rgb(255, 100, 100)");
    	pane1.setStyle("-fx-background-color: rgb(255, 150, 150)");
    	pane0.setStyle("-fx-background-color: rgb(255, 255, 255)");
    	pane_1.setStyle("-fx-background-color: rgb(0, 125, 255)");
    	pane_2.setStyle("-fx-background-color: rgb(0, 100, 255)");
    	pane_3.setStyle("-fx-background-color: rgb(0, 75, 255)");
    	pane_4.setStyle("-fx-background-color: rgb(0, 50, 255)");
    	pane_5.setStyle("-fx-background-color: rgb(0, 40, 255)");
	}
    
    /**
	 * Fonction qui ajoute des histogrammes au niveau de chaque zone dans le groupe parent.
	 * Chacun de ces histogrammes a une couleur et une taille adaptés à sa zone et à son anomalie de température pour une certaine année. 
	 * @param parent
	 * @param terre
	 * @param annee
	 */
    private void histogramme (Group parent, Terre terre, int annee) {
       	for (int lat = -88 ; lat <= 88 ; lat = lat + 4) { //latitude
           	for (int lon = -178 ; lon <= 178 ; lon = lon + 4) { //longitude
           		Point3D point1 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.01);
           		Point3D point2;
           		float anomalie = terre.anomalie(lat, lon, annee);
           		final PhongMaterial colorMaterial = new PhongMaterial();
           		if (anomalie > 0) {
              		if (anomalie < (float) maxAnomalie/5) {
              			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.04);
              			colorMaterial.setDiffuseColor(COLOR_HISTO_1);
               		} else if (anomalie < (float) 2*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_2);
               		} else if (anomalie < (float) 3*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_3);
               		} else if (anomalie < (float) 4*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_5);
               		}
               	} else if (anomalie < 0) {
               		if (anomalie > minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.04);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n1);
               		} else if (anomalie > (float) 2*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n2);
               		} else if (anomalie > (float) 3*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n3);
               		} else if (anomalie > (float) 4*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n5);
               		}
               	} else if (anomalie == 0) {
               		point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.02);
               		colorMaterial.setDiffuseColor(COLOR_0);
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
    
    /**
     * Fonction permettant la mise à jour de la couleur et de la taille de chaque histogramme en fonction de l'anomalie de température de sa zone pour une année donnée.
     * @param parent
     * @param terre
     * @param annee
     */
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
           				colorMaterial.setDiffuseColor(COLOR_HISTO_1);
               		} else if (anomalie < (float) 2*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_2);
               		} else if (anomalie < (float) 3*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_3);
               		} else if (anomalie < (float) 4*maxAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_5);
               		}
        		} else if (anomalie < 0) {
               		if (anomalie > minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.04);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n1);
               		} else if (anomalie > (float) 2*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.07);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n2);
               		} else if (anomalie > (float) 3*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.1);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n3);
               		} else if (anomalie > (float) 4*minAnomalie/5) {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.13);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n4);
               		} else {
               			point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.16);
               			colorMaterial.setDiffuseColor(COLOR_HISTO_n5);
               		}
               	} else if(anomalie == 0) {
               		point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.02);
               		colorMaterial.setDiffuseColor(COLOR_0);
               	} else {
               		point2 = geoCoordTo3dCoord((float) lat, (float) lon, (float) 1.02);
               		colorMaterial.setDiffuseColor(Color.TRANSPARENT);
               	}
            	final Cylinder line = (Cylinder) parent.getChildren().get(indice);
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
    
    // From Rahel Lathy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    /**
     * Fonction permettant la création d'un cylindre à partir d'un point d'origine et un point d'arrivée (en coordonnées cartésiennes)
     * @param origin
     * @param target
     * @return
     */
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
        
        line.setRadius(0.006);

        return line;
    }
    
    private void updateMode(Group parent, Group quad, Group histo) {
    	if (buttonQuadrilatere.isSelected() && checkBoxAnomalies.isSelected()) {
			// Affichage des quadrilatères
        	putlegendQuadrilatere();
        	if (parent.getChildren().size() == 3) { 
        		parent.getChildren().add(quad); // Ajout des quadrilatères dans le groupe parent de la scène 3D (affichage des quadrilatères)
        	} else {
        		parent.getChildren().set(3, quad); // Remplacement du groupe histo par le groupe quad dans le groupe parent de la scène 3D (affichage des quadrilatères)
        	}
        } else if(buttonHistogramme.isSelected() && checkBoxAnomalies.isSelected()) {
        	// Affichage des histogrammes
           putlegendHistogramme();
           if (parent.getChildren().size() == 3) {
        	   parent.getChildren().add(histo); // Ajout des histogrammes dans le groupe parent de la scène 3D
           } else {
        	   parent.getChildren().set(3, histo); // Remplacement du groupe quad par le groupe histo dans le groupe parent de la scène 3D (affichage des histogrammes)
           }
        }
    }
}