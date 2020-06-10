package partiegraphique;

import climatechange.*;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.SubScene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;

public class Controller implements Initializable {
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
	
	@FXML
	private Pane pane3D;
	
	@FXML
	private RadioButton quadrilatere;
	
	@FXML
	private RadioButton histogramme;
	
	@FXML Slider annees;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Data
		Terre terre = Read_file.getDataFromCSVFile("src/climatechange/tempanomaly_4x4grid.csv");
		
		//Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();
        //Pane pane3D = new Pane(root3D);

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
        
      //Mise en place du décochage des radio boutons "Histogramme" et "Quadrilatère"
    	ToggleGroup group = new ToggleGroup();
    	quadrilatere.setToggleGroup(group);
    	quadrilatere.setSelected(true);
    	histogramme.setToggleGroup(group);
    	
    	//Slider "Années"
    	ChangeListener<Number> listener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (quadrilatere.isSelected()) {
					quadrilatere(earth, terre, newValue.intValue());
					//enlever histogrammes
				} else {
					histogramme(earth, terre, newValue.intValue());
					//enlever quadrilateres
				}
		        
			}
		};
		annees.valueProperty().addListener(listener);

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
        
        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);
        
        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        // Create the subscene
        SubScene subscene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subscene);
        
        // Set the rotation Speed
        //double rotationSpeed = 100.0;
        
        // Add an animation timer
        /*final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
        	public void handle(long currentNanotime) {
        		double t = (currentNanotime - startNanoTime) / 1000000000.0;
        		//Add your code here
        		greenCube.setRotationAxis(new Point3D(0,1,0));
        		greenCube.setRotate(rotationSpeed * t);
        		redCube.setRotationAxis(new Point3D(1,1,1));
        		redCube.setRotate(-rotationSpeed * t);
        	}
        }.start();*/
	}
	
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
    }


    // From Rahel LÃ¼thy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
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

        return line;
    }

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
    
    private void quadrilatere(Group parent, Terre terre, int annee) {
    	float max = terre.getMaxAnomalie();
    	float min = terre.getMinAnomalie();
    	for (int lon = -180 ; lon < 180 ; lon = lon + 4) { //longitude
        	for (int lat = -90 ; lat < 90 ; lat = lat + 4) { //latitude
        		Point3D topRight = geoCoordTo3dCoord(lat+4, lon+4, 1.01f);
        		Point3D bottomRight = geoCoordTo3dCoord(lat, lon+4, 1.01f);
        		Point3D bottomLeft = geoCoordTo3dCoord(lat, lon, 1.01f);
        		Point3D topLeft = geoCoordTo3dCoord(lat+4, lon, 1.01f);
        		final PhongMaterial colorMaterial = new PhongMaterial();
        		Color color = Color.WHITE;
        		float anomalie = terre.anomalie(lat+2, lon+2, annee);
        		if (anomalie > 0) {
        			if (anomalie < max/5) {
        				color = new Color(0.2, 0, 0, 0.1);
        			} else if (anomalie < 2*max/5) {
        				color = new Color(0.4, 0, 0, 0.1);
        			} else if (anomalie < 3*max/5) {
        				color = new Color(0.6, 0, 0, 0.1);
        			} else if (anomalie < 4*max/5) {
        				color = new Color(0.8, 0, 0, 0.1);
        			} else {
        				color = new Color(1, 0, 0, 0.1);
        			}
        		} else if (anomalie < 0) { //On sait ici que anomalie et min sont négatives
        			if (anomalie < min/5) {
        				color = new Color(0, 0, 0.2, 0.1);
        			} else if (anomalie < 2*min/5) {
        				color = new Color(0, 0, 0.4, 0.1);
        			} else if (anomalie < 3*min/5) {
        				color = new Color(0, 0, 0.6, 0.1);
        			} else if (anomalie < 4*min/5) {
        				color = new Color(0, 0, 0.8, 0.1);
        			} else {
        				color = new Color(0, 0, 1, 0.1);
        			}
        		}
                colorMaterial.setDiffuseColor(color);
                AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, colorMaterial);
        	}
    	}
    }
    	
    private void histogramme (Group parent, Terre terre, int annee) {
       	float max = terre.getMaxAnomalie();
       	float min = terre.getMinAnomalie();
       	for (int lon = -180 ; lon < 180 ; lon = lon + 4) { //longitude
           	for (int lat = -90 ; lat < 90 ; lat = lat + 4) { //latitude
           		Point3D point1 = geoCoordTo3dCoord((float) lat+2, (float) lon+2, (float) 1.01);
           		Point3D point2 = geoCoordTo3dCoord((float) lat+2, (float) lon+2, (float) 1.01);
           		final PhongMaterial colorMaterial = new PhongMaterial();
           		Color color = Color.WHITE;
           		float anomalie = terre.anomalie(lat+2, lon+2, annee);
           		if (anomalie > 0) {
          			if (anomalie < max/5) {
           				color = new Color(0.2, 0, 0, 0.1);
           				point2 = geoCoordTo3dCoord((float) lat+2, (float) lon+2, (float) 1.21);
           			} else if (anomalie < 2*max/5) {
           				color = new Color(0.4, 0, 0, 0.1);
           				point2 = geoCoordTo3dCoord((float) lat+2, (float) lon+2, (float) 1.41);
           			} else if (anomalie < 3*max/5) {
           				color = new Color(0.6, 0, 0, 0.1);
           				point2 = geoCoordTo3dCoord((float) lat+2, (float) lon+2, (float) 1.61);
           			} else if (anomalie < 4*max/5) {
           				color = new Color(0.8, 0, 0, 0.1);
           				point2 = geoCoordTo3dCoord((float) lat+2, (float) lon+2, (float) 1.81);
           			} else {
           				color = new Color(1, 0, 0, 0.1);
           				point2 = geoCoordTo3dCoord((float) lat+2, (float) lon+2, (float) 2.01);
           			}
           		} else if (anomalie < 0) { //On sait ici que anomalie et min sont négatives
           			if (anomalie < min/5) {
           				color = new Color(0, 0, 0.2, 0.1);
           			} else if (anomalie < 2*min/5) {
           				color = new Color(0, 0, 0.4, 0.1);
           			} else if (anomalie < 3*min/5) {
           				color = new Color(0, 0, 0.6, 0.1);
           			} else if (anomalie < 4*min/5) {
           				color = new Color(0, 0, 0.8, 0.1);
           			} else {
           				color = new Color(0, 0, 1, 0.1);
           			}
           		}
                   colorMaterial.setDiffuseColor(color);
                   
                   parent.getChildren().addAll(createLine(point1, point2));
           	}
       	}
    }
}