package com.rope.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class mapa implements Screen {

    private main game;
    private SpriteBatch batch;
    private Texture btnNivel1, btnNivel2, btnNivel3, btnNivel4, btnNivel5;
    private Texture background; // Para el fondo
    private Vector2 posBtnNivel1, posBtnNivel2, posBtnNivel3, posBtnNivel4, posBtnNivel5;
    private float btnWidth, btnHeight;

    public mapa(main game) {
        this.game = game;
        batch = new SpriteBatch();

        // Cargar texturas para los botones
        btnNivel1 = new Texture("boton.png");
        btnNivel2 = new Texture("boton.png");
        btnNivel3 = new Texture("boton.png");
        btnNivel4 = new Texture("boton.png");
        btnNivel5 = new Texture("boton.png");

        // Cargar la textura del fondo
        background = new Texture("mapa.jpg");

        // Posiciones de los botones
        posBtnNivel1 = new Vector2(160, 200);
        posBtnNivel2 = new Vector2(400, 368);
        posBtnNivel3 = new Vector2(620, 500);
        posBtnNivel4 = new Vector2(400, 622);
        posBtnNivel5 = new Vector2(170, 675);

        // Tamaño de los botones
        btnWidth = 70;  // Ancho deseado para los botones
        btnHeight = 70; // Alto deseado para los botones
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        // Dibujar el fondo
        batch.draw(background, 0, 0);

        // Dibujar los botones con el tamaño modificado
        batch.draw(btnNivel1, posBtnNivel1.x, posBtnNivel1.y, btnWidth, btnHeight);
        batch.draw(btnNivel2, posBtnNivel2.x, posBtnNivel2.y, btnWidth, btnHeight);
        batch.draw(btnNivel3, posBtnNivel3.x, posBtnNivel3.y, btnWidth, btnHeight);
        batch.draw(btnNivel4, posBtnNivel4.x, posBtnNivel4.y, btnWidth, btnHeight);
        batch.draw(btnNivel5, posBtnNivel5.x, posBtnNivel5.y, btnWidth, btnHeight);

        batch.end();

        // Detectar si el usuario hace clic en algún botón
        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            // Verificar en qué botón hizo clic
            if (isButtonPressed(touchPos, posBtnNivel1, btnWidth, btnHeight)) {
                game.setScreen(new Nivel1());
            } else if (isButtonPressed(touchPos, posBtnNivel2, btnWidth, btnHeight)) {
                game.setScreen(new Nivel2());
            } else if (isButtonPressed(touchPos, posBtnNivel3, btnWidth, btnHeight)) {
                game.setScreen(new Nivel3());
            } else if (isButtonPressed(touchPos, posBtnNivel4, btnWidth, btnHeight)) {
                game.setScreen(new Nivel4());
            } else if (isButtonPressed(touchPos, posBtnNivel5, btnWidth, btnHeight)) {
                game.setScreen(new Nivel5());
            }
        }
    }

    private boolean isButtonPressed(Vector2 touchPos, Vector2 buttonPos, float buttonWidth, float buttonHeight) {
        return touchPos.x > buttonPos.x && touchPos.x < buttonPos.x + buttonWidth
                && touchPos.y > buttonPos.y && touchPos.y < buttonPos.y + buttonHeight;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        btnNivel1.dispose();
        btnNivel2.dispose();
        btnNivel3.dispose();
        btnNivel4.dispose();
        btnNivel5.dispose();
        background.dispose(); // Disponer el fondo también
    }

    @Override
    public void show() {}
}
    
    
    
     /*public JPanel panel = new JPanel();; //crear panel como atributo 
    JFrame currentFrame = this;
    
    
        
     
     public mapa(){
  
  this.setSize(506,712);//establecer tamaño de la ventana
  setTitle("mapa"); //titulo 
  setLocationRelativeTo(null);      //pone la pantalla centrada
  setDefaultCloseOperation(EXIT_ON_CLOSE);//cerrar el programa con el boton
  iniciarComponentes();
  
  }  

    public void iniciarComponentes(){
    colocarBotones();
    panel.setLayout(null);
    this.getContentPane().add(panel);
    
    } 
     
     public void colocarBotones(){
     
        JButton nivel1 = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/boton/boton.png")).getImage()));
        //nivel1.setBounds(680, 400, 50, 50);
        nivel1.setContentAreaFilled(false); // Elimina el relleno del botón
        nivel1.setBorderPainted(false);     // Elimina el borde del botón
        nivel1.setFocusPainted(false);      // Elimina el foco visual
        nivel1.setOpaque(false); // Asegura que el fondo sea transparente
        nivel1.setBounds(250, 300, 50, 50);
        panel.add(nivel1);
     
     
     }
     */
     
     
   /* private SpriteBatch batch;
   // private Texture backgroundImage;
    private Stage stage;
    private main mainGame; // Para poder cambiar pantallas

    public mapa(main mainGame) {
        this.mainGame = mainGame; // Recibe una referencia del juego principal
    }

    public void create() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Cargar la imagen de fondo
        //backgroundImage = new Texture("mapa.jpg");

        // Crear los botones con listeners
        createButtons();
    }

    private void createButtons() {
        // Cargar la textura del botón
        Texture buttonTexture = new Texture("boton.png");
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        // Botón para Nivel 1
        ImageButton nivel1 = new ImageButton(buttonDrawable);
        nivel1.setSize(70, 70);
        nivel1.setPosition(160, 200);
        nivel1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Cambiando a Nivel 1");
                mainGame.setScreen(new Nivel1(mainGame)); // Cambiar a Nivel 1
            }
        });

        // Botón para Nivel 2
        ImageButton nivel2 = new ImageButton(buttonDrawable);
        nivel2.setSize(70, 70);
        nivel2.setPosition(400, 368);
        nivel2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Cambiando a Nivel 2");
                mainGame.setScreen(new Nivel2()); // Cambiar a Nivel 2
            }
        });

        // Botón para Nivel 3
        ImageButton nivel3 = new ImageButton(buttonDrawable);
        nivel3.setSize(70, 70);
        nivel3.setPosition(620, 500);
        nivel3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Cambiando a Nivel 3");
                mainGame.setScreen(new Nivel3()); // Cambiar a Nivel 3
            }
        });

        // Añadir los botones al escenario
        stage.addActor(nivel1);
        stage.addActor(nivel2);
        stage.addActor(nivel3);
    }

    public void render() {
        // Limpiar la pantalla
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Dibujar el fondo
        batch.begin();
       // batch.draw(backgroundImage, 0, 0);
        batch.end();

        // Actualizar y dibujar el escenario (stage) para los botones
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        batch.dispose();
        //backgroundImage.dispose();
        stage.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float f) {
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
    */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

